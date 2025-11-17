package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class CashflowAnalysis extends AppCompatActivity {

    BarChart barChart;
    Button btnNextManangeNumber;
    PipeLines utils = new PipeLines();
    NumberFormat format = NumberFormat.getInstance(Locale.US);
    private String GET_CASHFLOW_DISPLAY_URL = utils.GET_CASHFLOW_DISPLAY;
    private double decimalTotalSpending = 0f;
    private double decimalTotalOutflow = 0f;
    private double decimalTotalInflow = 0f;
    private String freqSelected = "today";
    Button btnMonth, btnWeek, btnYear, btnToday;
    private String email = "";
    private SharedPreferences sharedPreferences;
    private TextView tvOutflowBudget, tvOutflowActual;
    ArrayList<BarEntry> entries;
    float[] animatedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cashflow_analysis);

        btnToday = findViewById(R.id.btnToday);
        btnWeek = findViewById(R.id.btnWeek);
        btnMonth = findViewById(R.id.btnMonth);
        btnYear = findViewById(R.id.btnQuarter);
        barChart = findViewById(R.id.idBarChartCashflow);
        btnNextManangeNumber = findViewById(R.id.btnSave);

        tvOutflowBudget = findViewById(R.id.tvOutflowBudget);
        tvOutflowActual = findViewById(R.id.tvOutflowActual);


        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL, "");

        entries = new ArrayList<>();

        getCashflow();

        btnToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFreqSelected("today");
                setOthersInactive();
                btnToday.setBackgroundResource(R.drawable.button_rectangle_line);
                entries.clear();
                getCashflow();
            }
        });
        btnMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFreqSelected("month");
                setOthersInactive();
                btnMonth.setBackgroundResource(R.drawable.button_rectangle_line);
                entries.clear();
                getCashflow();
            }
        });
        btnWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFreqSelected("week");
                setOthersInactive();
                btnWeek.setBackgroundResource(R.drawable.button_rectangle_line);
                entries.clear();
                getCashflow();
            }
        });
        btnYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFreqSelected("year");
                setOthersInactive();
                btnYear.setBackgroundResource(R.drawable.button_rectangle_line);
                entries.clear();
                getCashflow();
            }
        });
        btnNextManangeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CashflowAnalysis.this, JourneyMpesaAllocation.class);
                intent.putExtra("cashflow", "outflow");
                startActivity(intent);
            }
        });

//        ValueAnimator animator = ValueAnimator.ofInt(0, (int) decimalTotalOutflow);
//        animator.setDuration(3000); // 2 seconds
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                tvOutflowBudget.setText(valueAnimator.getAnimatedValue().toString());
//            }
//        });
//        animator.start();

//        ValueAnimator animator = ValueAnimator.ofFloat(0f, (float) decimalTotalOutflow);
//        animator.setDuration(2000); // 3 seconds
//        animator.addUpdateListener(valueAnimator -> {
//            float value = (float) valueAnimator.getAnimatedValue();
//            tvOutflowBudget.setText(String.format("%.0f", value)); // 2 decimal places
//        });
//        animator.start();
    }

    private void setFreqSelected(String f) {
        freqSelected = f;
    }

    private void animateBarValues() {
        for (int i = 0; i < entries.size(); i++) {
            final int index = i;
            float targetValue = entries.get(i).getY();

            ValueAnimator animator = ValueAnimator.ofFloat(0, targetValue);
            animator.setDuration(2000); // 2 seconds
            animator.addUpdateListener(animation -> {
                animatedValues[index] = (float) animation.getAnimatedValue();
                barChart.invalidate(); // refresh chart with new label values
            });
            animator.start();
        }

        // Animate bars (optional)
        barChart.animateY(3000);
    }

    public void setOthersInactive() {

        btnToday.setBackgroundResource(R.drawable.button_greyed_out);
        btnMonth.setBackgroundResource(R.drawable.button_greyed_out);
        btnWeek.setBackgroundResource(R.drawable.button_greyed_out);
        btnYear.setBackgroundResource(R.drawable.button_greyed_out);
    }

    private void setGraph() {
        // 1. Prepare data entries

        Log.e("Tag Data fetched", "Total outflow: " + decimalTotalOutflow + "\n Total inflow " + decimalTotalSpending);

        entries.add(new BarEntry(0, (float) decimalTotalOutflow));   // x=0, y=4
        entries.add(new BarEntry(1, (float) decimalTotalSpending));

        animatedValues = new float[entries.size()];

        // 2. Create dataset
        BarDataSet dataSet = new BarDataSet(entries, "");

        if (decimalTotalSpending > decimalTotalOutflow) {
            dataSet.setColors(new int[]{
//                    Color.rgb(243, 129, 24),
                    Color.rgb(37, 150, 190),
                    Color.RED,
            });
        } else {
            dataSet.setColors(new int[]{
                    Color.rgb(37, 150, 190),
                    Color.GREEN
            });
        }
//        dataSet.setColors(new int[]{
//                Color.GREEN,
//                Color.RED,
//        });


        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        // 3. Create BarData
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f); // set custom bar width

        final String[] cashflowItems = new String[]{
                "Budget", "Actuals"
        };

        // 4. Setup chart
        barChart.setData(barData);
        barChart.setFitBars(true); // make x-axis fit exactly all bars
        barChart.getDescription().setEnabled(false);


        // X-axis styling
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(cashflowItems));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f); // step of 1
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(cashflowItems.length);


//        barChart.animateY(3000); // animation
//        animateBarValues
        animateBarValues();

        // CLICK LISTENER HERE
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int index = (int) e.getX();
                float amount = e.getY();
                String name = cashflowItems[index];
                Intent intent = new Intent(getApplicationContext(), CashflowPdfActivity.class);
                intent.putExtra("url", "actuals_download.php");
                intent.putExtra("activity_name", "Spending Budget Plan Pdf");
                startActivity(intent);
                Toast.makeText(getApplicationContext(),
                        name + ": KES " + amount,
                        Toast.LENGTH_SHORT
                ).show();
            }

            @Override
            public void onNothingSelected() { }
        });
    }


    private void getCashflow() {

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, GET_CASHFLOW_DISPLAY_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE  FROM " + GET_CASHFLOW_DISPLAY_URL + " IS " + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("inflow_success");
                    String success_out = jsonObject.getString("outflow_success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    JSONArray jsonArrayout = jsonObject.getJSONArray("outflow");

                    String outflow_total = jsonObject.getString("total_outflow");
                    String inflow_total = jsonObject.getString("total_inflow");
                    String spending_total = jsonObject.getString("total_spending");

                    Number numberTotalInflow = format.parse(inflow_total);
                    decimalTotalInflow = numberTotalInflow.doubleValue();

                    Number numberTotalOutflow = format.parse(outflow_total);
                    decimalTotalOutflow = numberTotalOutflow.doubleValue();

                    Number numberTotalSpending = format.parse(spending_total);
                    decimalTotalSpending = numberTotalSpending.doubleValue();

                    tvOutflowBudget.setText(outflow_total);
                    tvOutflowActual.setText(spending_total);

                    int l = 0;
                    l = jsonArrayout.length();


                } catch (Exception e) {

                }
                setGraph();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.

                Toast.makeText(CashflowAnalysis.this, "Fail to get response", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {

                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("email", email);
                params.put("freq", freqSelected);

                return params;
            }
        };

        queue.add(request);
    }
}