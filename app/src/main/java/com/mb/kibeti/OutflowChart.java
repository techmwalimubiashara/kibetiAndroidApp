package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;
import com.mb.kibeti.BuildConfig;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutflowChart extends AppCompatActivity {

    BarChart barChart;

    PieChart pieChart;
    BarData barData;
    BarDataSet barDataSet;
    ImageView yourGoal, lumpsumIV, investmentReturnIV, spendingIV, budgetIV, trackerIV,shoppingIV,investImg;
    ArrayList barEnteriesArrayList, headings, dataList;
    String email = "";
    ProgressDialog pDialog;
    SharedPreferences sharedPreferences;
    String url = "https://mwalimubiashara.com/app/actual_cashflow.php";
    XAxis xAxis;
    //    String[] weekdays = new String[8];
    ArrayList<PieEntry> visitors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outflow_chart);
        barEnteriesArrayList = new ArrayList<>();
        dataList = new ArrayList<>();

        visitors = new ArrayList<>();

        sharedPreferences = this.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
//        SharedPreferences sharedPreferences = getContext().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL, "");

        pDialog = new ProgressDialog(this);

        pDialog.setMessage("Loading your data ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);


        headings = new ArrayList<>();

        headings.add("sun");
        headings.add("mon");
        headings.add("tue");
        headings.add("wed");
        headings.add("thur");
        headings.add("fri");
        headings.add("sat");

        barChart = findViewById(R.id.idBarChart);
        pieChart = findViewById(R.id.idPieChart);
        lumpsumIV =  findViewById(R.id.view1);
        investmentReturnIV =  findViewById(R.id.view2);
        budgetIV =  findViewById(R.id.imBudget);
        trackerIV =  findViewById(R.id.imTracker);
        shoppingIV =  findViewById(R.id.imShopping);
        spendingIV =  findViewById(R.id.view3);
        investImg =  findViewById(R.id.investImg);

        getCashflow();


        lumpsumIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( OutflowChart.this, LumpsumActivity.class);
                 startActivity(intent);
            }
        });
        investImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( OutflowChart.this, InvestmentCalculator.class);
                 startActivity(intent);
            }
        });
        investmentReturnIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( OutflowChart.this, InvestmentReturnActivity.class);
                 startActivity(intent);
            }
        });
        spendingIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( OutflowChart.this, TablayoutWithIconsActivity.class);
                 startActivity(intent);
            }
        });

        budgetIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CashFragment()).commit();
                Intent intent = new Intent( OutflowChart.this, CashflowBudget.class);
                 startActivity(intent);

            }
        });

        shoppingIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CashFragment()).commit();
//                Intent intent = new Intent( OutflowChart.this, ShoppingAssistance.class);
                Intent intent = new Intent( OutflowChart.this, ShoppingBudget.class);
                 startActivity(intent);

            }
        });

    }


    public void addOutflow(View view) {
        Intent intent = new Intent(OutflowChart.this, AddOutflowActivity.class);
        startActivity(intent);
        finish();
    }

    public void backOutflow(View view) {
        finish();
    }

    public void share(View view){
        try {
            final String appPackageName = this.getPackageName();
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, appPackageName);
            String shareMessage = "\nHi, Great app to help make your money work for you!\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }


    private void getCashflow() {

        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(OutflowChart.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);

//                progressBar.setVisibility(View.GONE);
//
                barEnteriesArrayList.clear();
//                dataClassArrayListOutflow.clear();


                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("inflow_success");
                    String success_out = jsonObject.getString("outflow_success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    JSONArray jsonArrayout = jsonObject.getJSONArray("outflow");
//                    totalInflow.setText(jsonObject.getString("total_inflow"));
//                    totalOutflow.setText(jsonObject.getString("total_outflow"));

                    int l = 0;
                    l = jsonArrayout.length();

//                    Log.e("TAG", "Outflow length is " + l);


//                    float title = 0f;
//                    float entry1 = 3, entry2 = 7, entry3 = 8, entry4 = 4, entry5 = 7, entry6 = 10, entry7 = 7;
//
//                    dataList.add(entry1);
//                    dataList.add(entry2);
//                    dataList.add(entry3);
//                    dataList.add(entry4);
//                    dataList.add(entry5);
//                    dataList.add(entry6);
//                    dataList.add(entry7);

                    ArrayList<DataModel> dataModels = new ArrayList<>();

                    for (int k = 0; k < l; k++) {
                        JSONObject objectOut = jsonArrayout.getJSONObject(k);

//                        visitors.add(new PieEntry(entry4,"2019"));
//                        visitors.add(new PieEntry(entry6,"2020"));

                        Log.e("TAG", "Cat " + objectOut.getString("cat_1") + " 1 " + objectOut.getString("cat_1") + "/ 2 " + objectOut.getString("cat_2") + "/ 3 " + objectOut.getString("cat_3"));


                        if (convertToInt(objectOut.getString("amount_1")) != 0) {
                            dataModels.add(new DataModel(objectOut.getString("cat_1"), convertToInt(objectOut.getString("amount_1"))));

                        }
                        if (convertToInt(objectOut.getString("amount_2")) != 0) {

                            dataModels.add(new DataModel(objectOut.getString("cat_2"), convertToInt(objectOut.getString("amount_2"))));
                        }
                        if (convertToInt(objectOut.getString("amount_3")) != 0) {
                            dataModels.add(new DataModel(objectOut.getString("cat_3"), convertToInt(objectOut.getString("amount_3"))));
                        }
                        if (convertToInt(objectOut.getString("amount_4")) != 0) {
                            dataModels.add(new DataModel(objectOut.getString("cat_4"), convertToInt(objectOut.getString("amount_4"))));
                        }
                        if (convertToInt(objectOut.getString("amount_5")) != 0) {
                            dataModels.add(new DataModel(objectOut.getString("cat_5"), convertToInt(objectOut.getString("amount_5"))));
                        }
                        if (convertToInt(objectOut.getString("amount_6")) != 0) {
                            dataModels.add(new DataModel(objectOut.getString("cat_6"), convertToInt(objectOut.getString("amount_6"))));
                        }
                        if (convertToInt(objectOut.getString("amount_7")) != 0) {
                            dataModels.add(new DataModel(objectOut.getString("cat_7"), convertToInt(objectOut.getString("amount_7"))));
                        }
                        if (convertToInt(objectOut.getString("amount_8")) != 0) {
                            dataModels.add(new DataModel(objectOut.getString("cat_8"), convertToInt(objectOut.getString("amount_8"))));
                        }
                        if (convertToInt(objectOut.getString("amount_9")) != 0) {
                            dataModels.add(new DataModel(objectOut.getString("cat_9"), convertToInt(objectOut.getString("amount_9"))));
                        }
                        if (convertToInt(objectOut.getString("amount_10")) != 0) {
                            dataModels.add(new DataModel(objectOut.getString("cat_10"), convertToInt(objectOut.getString("amount_10"))));
                        }

                    }
//                    myAdapter.notifyDataSetChanged();

                    setupPieChart(dataModels);
                    Log.e("TAG", "Data entries length " + barEnteriesArrayList.size());

                } catch (Exception e) {

//                    e.printStackTrace();
                }
                pDialog.dismiss();

//                PieDataSet pieDataSet = new PieDataSet(visitors, "Outflows");
//                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//                pieDataSet.setValueTextColor(Color.BLACK);
//                pieDataSet.setSliceSpace(4f);
//                pieDataSet.setValueTextSize(17f);
//
//                Legend legend = pieChart.getLegend();
////                    legend.setForm(Legend.LegendForm.CIRCLE);
////                    legend.setWordWrapEnabled(true);
////                    legend.setTextSize(11);
////                    legend.setFormSize(10);
////                    legend.setFormToTextSpace(2);
//                legend.setEnabled(false);


//                PieData pieData = new PieData(pieDataSet);
//                pieChart.setData(pieData);
//                pieChart.getDescription().setEnabled(false);
//                pieChart.setHoleRadius(60);
////                pieChart.setNoDataText("Tap to view the graph");
//                pieChart.setNoDataText("Please try again later.");
//                pieChart.setCenterText("Outflows");
//                pieChart.setCenterTextColor(Color.BLACK);
//                pieChart.notifyDataSetChanged();
//                pieChart.animate();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                pDialog.dismiss();
                Toast.makeText(OutflowChart.this, "Fail to get response ", Toast.LENGTH_SHORT).show();
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

                return params;
            }
        };

        queue.add(request);
    }


    private int convertToInt(String str) {
        String amnt1 = str.replaceAll(",", "");

        amnt1 = amnt1.replaceAll(" ", "");
        amnt1 = amnt1.replaceAll("KES.", "");

//        int amount1 = Float.parseFloat(amnt1);
        int amount1 = Integer.parseInt(amnt1);

        Log.e("TAG", "String in " + str + ", Int out " + amount1);

        return amount1;
    }

    private void setupPieChart(List<DataModel> dataModels) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (DataModel dataModel : dataModels) {
            entries.add(new PieEntry(dataModel.getValue(), dataModel.getLabel()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Outflows");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);

        PieData data = new PieData(dataSet);

        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.invalidate(); // refresh
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //do whatever you want here
        finish();
        return true;
    }

}