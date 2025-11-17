package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.CURRENCY;
import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;
import static com.mb.kibeti.LoginActivity.PACKAGE_TYPE;
import static com.mb.kibeti.LoginActivity.TRIAL;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mb.kibeti.landingPageTask.ui.PaymentActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChartActivity extends AppCompatActivity {

    private String email = "";
    private SharedPreferences sharedPreferences;
    private DecimalFormat formatter;
    private String currency = "";
    TextView tVTotalAmountInvested;
    TextView tVTotalIncomeMonthly;
    TextView tvmonthly_return1;
    PieChart pieChart, pieChartInvest;
    Button btnBack,btnSave;
    ProgressDialog pDialog;
    private String chart_url = "https://mwalimubiashara.com/app/invest-numbers.php";
    ArrayList <InvestmentPass> investList;
    ArrayList<DataModel> dataModels = new ArrayList<>();
    ArrayList<DataModel> dataModelsInvest = new ArrayList<>();
    private String url = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        sharedPreferences = this.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL, "");
        currency = sharedPreferences.getString(CURRENCY, "");
        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("###,###,###,###");
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Analysing your investment portfolio...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
//        pDialog.show();
        investList = (ArrayList<InvestmentPass>) getIntent().getSerializableExtra("investmentList");

        btnBack = findViewById(R.id.btnBack);
        tVTotalAmountInvested = findViewById(R.id.tVTotalAmountInvested);
        tVTotalIncomeMonthly = findViewById(R.id.tVTotalIncomeMonthly);
        pieChartInvest = findViewById(R.id.idPieChartInvest);
        pieChart = findViewById(R.id.idPieChart);
        btnSave = findViewById(R.id.btnSave);

//
//        tvmonthly_return1 = findViewById(R.id.tvmonthly_return1);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInvestment();
            }
        });

        if(investList!=null) {

            for (InvestmentPass investLine : investList) {

                Log.e("TAG", "PIE chart  email: " +email+ " Investment line "+investLine.getInvest_name()+" Amount "+investLine.getAmount()+" Freq "+investLine.getFreq());

            }
            getInvest();
        }

    }
    private  void saveInvestment(){

//        getInvest("https://mwalimubiashara.com/app/save_invest_numbers.php");
//        Intent intent = new Intent(InvestmentCalculator.this, InvestmentRecommendationCalculator.class);
////        intent.putExtra("url", "investment_plan_download.php");
//        intent.putExtra("activity_name", "Investment Plan Pdf");

        String trial = sharedPreferences.getString(TRIAL, "");
        String package_type = sharedPreferences.getString(PACKAGE_TYPE,"");

        Intent intent = new Intent(ChartActivity.this, PaymentActivity.class);

        intent.putExtra("from_activity","investment");
        intent.putExtra("next_activity","InvestmentRecommendationCalculator");

        Log.e("TAG","Account status is "+trial+" Package is "+package_type);
        if(!package_type.equals("basic")||!package_type.equals("Trial")){
            if(trial.equals("active")){

                Log.e("TAG","Account status is "+trial+" Package is "+package_type);
                intent = new Intent(ChartActivity.this, InvestmentRecommendationCalculator.class);
                intent.putExtra("url", "investment_plan_download.php");
                intent.putExtra("activity_name", "Investment Plan Pdf");

            }
        }

        startActivity(intent);

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
//    private void getInvest() {
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.e("TAG", "RESPONSE IS " + response);
//                String monthly_amnt1 = "", ror1 = "", monthly_return_adjusted1 = "",
//                        adjusted_ror1 = "", comment1 = "",invest = "", monthly_amnt2 = "", ror2 = "", monthly_return_adjusted2 = "", adjusted_ror2 = "", comment2 = "", monthly_amnt3 = "", ror3 = "", monthly_return_adjusted3 = "", adjusted_ror3 = "", comment3 = "";
//
//                int totalAmountInvested = 0;
//                int totalAmountMonthly = 0;
//                dataModelsInvest.clear();
//                dataModels.clear();
//
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
////                    String success = jsonObject.getString("success");
//                    JSONArray jsonArray = jsonObject.getJSONArray("data");
//
//
//                    int l = 0;
//                    l = jsonArray.length();
//
//                    for (int i = 0; i < l; i++) {
//
//                        JSONObject object = jsonArray.getJSONObject(i);
//                        monthly_amnt1 = object.getString("monthly_return");
//                        ror1 = object.getString("ror");
//                        monthly_return_adjusted1 = object.getString("monthly_return_adjusted");
//                        adjusted_ror1 = object.getString("adjusted_ror");
//                        comment1 = object.getString("comment");
//                        invest = object.getString("invest");
//
//                        int amount = object.getInt("amount");
//
//                        totalAmountMonthly = totalAmountMonthly+convertToInt(monthly_amnt1);
//                        totalAmountInvested =totalAmountInvested+ amount;
//
////                        monthly_amnt2 = object.getString("monthly_return2");
////                        ror2 = object.getString("ror2");
////                        monthly_return_adjusted2 = object.getString("monthly_return_adjusted2");
////                        adjusted_ror2 = object.getString("adjusted_ror2");
////                        comment2 = object.getString("comment2");
////
////                        monthly_amnt3 = object.getString("monthly_return3");
////                        ror3 = object.getString("ror3");
////                        monthly_return_adjusted3 = object.getString("monthly_return_adjusted3");
////                        adjusted_ror3 = object.getString("adjusted_ror3");
////                        comment3 = object.getString("comment3");
//
//                        Log.e("TAG", "PIE chart  invest name: " + invest+" invest amount "+monthly_amnt1);
//
////                        int monthly_return =
////                        if (monthly_amnt != 0) {
//                        dataModels.add(new DataModel(invest, convertToInt(monthly_amnt1)));
////                        }
//
////                        if(amount!=0){
//                        dataModelsInvest.add(new DataModel(invest, amount));
////                        }
//
//
//
//                    }
//
//
//                } catch (Exception e) {
//
//                }
//
//                if(!dataModels.isEmpty()) {
////                    layoutInvest1.setVisibility(View.GONE);
////                    layoutInvest2.setVisibility(View.GONE);
////
////                    layoutInvest3.setVisibility(View.GONE);
//
//                    setupPieChart(dataModels);
//                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//                    formatter.applyPattern("###,###,###,###");
//
//                    tVTotalIncomeMonthly.setText(currency+" " + formatter.format(totalAmountMonthly));
//                    tVTotalAmountInvested.setText(currency+" " + formatter.format(totalAmountInvested));
////                    layoutInput.setVisibility(View.GONE);
////                    otherLayout.setVisibility(View.GONE);
////                    layoutAllInvest.setVisibility(View.VISIBLE);
//                }
//
//                pDialog.dismiss();
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
//                Toast.makeText(ChartActivity.this, "Fail to get response ", Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            public String getBodyContentType() {
//
//                return "application/x-www-form-urlencoded; charset=UTF-8";
//            }
//
//            @Override
//            protected Map<String, String> getParams() {
//
//                Map<String, String> params = new HashMap<String, String>();
//
//
//                params.put("email", email);
////                params.put("invest1", tvInvest01.getText().toString());
////                params.put("invest2", tvInvest02.getText().toString());
////                params.put("invest3", tvInvest03.getText().toString());
////                params.put("invest4", tvInvest04.getText().toString());
////                params.put("invest5", tvInvest05.getText().toString());
////                params.put("invest6", tvInvest06.getText().toString());
////                params.put("invest7", tvInvest07.getText().toString());
////                params.put("invest8", tvInvest08.getText().toString());
////                params.put("invest9", tvInvest09.getText().toString());
////                params.put("invest10", tvInvest010.getText().toString());
////
////                params.put("amount1", getAmountValue(ed_amount1));
////                params.put("amount2", getAmountValue(ed_amount2));
////                params.put("amount3", getAmountValue(ed_amount3));
////                params.put("amount4", getAmountValue(ed_amount4));
////                params.put("amount5", getAmountValue(ed_amount5));
////                params.put("amount6", getAmountValue(ed_amount6));
////                params.put("amount7", getAmountValue(ed_amount7));
////                params.put("amount8", getAmountValue(ed_amount8));
////                params.put("amount9", getAmountValue(ed_amount9));
////                params.put("amount10", getAmountValue(ed_amount10));
////
////                params.put("freq1",tvFreq1.getText().toString());
////                params.put("freq2",tvFreq2.getText().toString());
////                params.put("freq3",tvFreq3.getText().toString());
////                params.put("freq4",tvFreq4.getText().toString());
////                params.put("freq5",tvFreq5.getText().toString());
////                params.put("freq6",tvFreq6.getText().toString());
////                params.put("freq7",tvFreq7.getText().toString());
////                params.put("freq8",tvFreq8.getText().toString());
////                params.put("freq9",tvFreq9.getText().toString());
////                params.put("freq10",tvFreq10.getText().toString());
//
//                return params;
//            }
//
//        };
//
//        queue.add(request);
//    }
    private void setupPieChart(List<DataModel> dataModels) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (DataModel dataModel : dataModels) {
            entries.add(new PieEntry(dataModel.getValue(), dataModel.getLabel()));
        }
        ArrayList<PieEntry> entriesInvest = new ArrayList<>();
        for (DataModel dataModel : dataModelsInvest) {
            entriesInvest.add(new PieEntry(dataModel.getValue(), dataModel.getLabel()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(20f); // Set size for value text (e.g., percentages)
        dataSet.setValueTextColor(Color.BLACK); // Set color for value text


        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextColor(getResources().getColor(R.color.text_color));
        legend.setWordWrapEnabled(true);
        legend.setTextSize(11);
        legend.setFormSize(10);
        legend.setFormToTextSpace(2);
        legend.setEnabled(true);

        PieData data = new PieData(dataSet);
//        data.setDrawValues(false);

        PieDataSet dataSetInvest = new PieDataSet(entriesInvest, "");
        dataSetInvest.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSetInvest.setValueTextSize(16f); // Set size for value text (e.g., percentages)
        dataSetInvest.setValueTextColor(Color.BLACK); // Set color for value text

        Legend legendInvest = pieChartInvest.getLegend();
        legendInvest.setEnabled(false);



        PieData dataInvest = new PieData(dataSetInvest);
//        dataInvest.setHighlightEnabled(true);

        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.invalidate(); // refresh

        pieChartInvest.setData(dataInvest);
        pieChartInvest.setDrawEntryLabels(false);
        pieChartInvest.getDescription().setEnabled(false);
        pieChartInvest.invalidate();
    }
    private void getInvest() {

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, chart_url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                String monthly_amnt1 = "", ror1 = "", monthly_return_adjusted1 = "",
                        adjusted_ror1 = "", comment1 = "", invest = "", monthly_amnt2 = "", ror2 = "", monthly_return_adjusted2 = "", adjusted_ror2 = "", comment2 = "", monthly_amnt3 = "", ror3 = "", monthly_return_adjusted3 = "", adjusted_ror3 = "", comment3 = "";

                int totalAmountInvested = 0;
                int totalAmountMonthly = 0;
                dataModelsInvest.clear();
                dataModels.clear();

                try {
                    JSONObject jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");


                    int l = 0;
                    l = jsonArray.length();

                    for (int i = 0; i < l; i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        monthly_amnt1 = object.getString("monthly_return");
                        ror1 = object.getString("ror");
                        monthly_return_adjusted1 = object.getString("monthly_return_adjusted");
                        adjusted_ror1 = object.getString("adjusted_ror");
                        comment1 = object.getString("comment");
                        invest = object.getString("invest");

                        int amount = object.getInt("amount");

                        totalAmountMonthly = totalAmountMonthly + convertToInt(monthly_amnt1);
                        totalAmountInvested = totalAmountInvested + amount;


                        Log.e("TAG", "PIE cahrt data from API: " +email+ " Amount "+monthly_amnt1);

//                        int monthly_return =
//                        if (monthly_amnt != 0) {
                        dataModels.add(new DataModel(invest, convertToInt(monthly_amnt1)));
//                        }

//                        if(amount!=0){
                        dataModelsInvest.add(new DataModel(invest, amount));



                    }


                } catch (Exception e) {

                }

//
//                int totalAmount = 0;
//                tvmonthly_return1.setText("Monthly Return: Ksh." + monthly_amnt1);
//                tvRor1.setText("Rate of Return: " + ror1);
//                tvmonthly_return_adjusted1.setText("Monthly Return (Risk Adjusted): Ksh." + monthly_return_adjusted1);
//                tvAdjustedRor1.setText("Adjusted Rate of Return: " + adjusted_ror1);
//
//                dataModels.add(new DataModel(investLine1, convertToInt(monthly_amnt1)));
//                dataModelsInvest.add(new DataModel(investLine1, amount1));
//
////                dataModels.add(new DataModel("investLine2", convertToInt("2,400")));
//
//                String amt1 = monthly_amnt1.replaceAll(",", "");
//                int amount1 = Integer.parseInt(amt1.toString()); //monthly_amnt3.replaceAll(",", "");
//                totalAmount = totalAmount + amount1;
//
//                if (!monthly_amnt2.equals("")) {
//                    tvmonthly_return2.setText("Monthly Return: Ksh." + monthly_amnt2);
//                    tvRor2.setText("Rate of Return: " + ror2);
//                    tvmonthly_return_adjusted2.setText("Monthly Return (Risk Adjusted): Ksh." + monthly_return_adjusted2);
//                    tvAdjustedRor2.setText("Adjusted Rate of Return: " + adjusted_ror2);
//                    layoutInvest2.setVisibility(View.VISIBLE);
//
//                    amt1 = monthly_amnt2.replaceAll(",", "");
//                    amount1 = Integer.parseInt(amt1.toString()); //monthly_amnt3.replaceAll(",", "");
//                    totalAmount = totalAmount + amount1;
//
//                    dataModels.add(new DataModel(investLine2, convertToInt(monthly_amnt2)));
//                    dataModelsInvest.add(new DataModel(investLine2, amount2));
//                } else {
//                if(!dataModels.isEmpty()) {
//                    layoutInvest1.setVisibility(View.GONE);
//                    layoutInvest2.setVisibility(View.GONE);
////                }
//                if (!monthly_amnt3.equals("")) {
//                    tvmonthly_return3.setText("Monthly Return: Ksh." + monthly_amnt3);
//                    tvRor3.setText("Rate of Return: " + ror3);
//                    tvmonthly_return_adjusted3.setText("Monthly Return (Risk Adjusted): Ksh." + monthly_return_adjusted3);
//                    tvAdjustedRor3.setText("Adjusted Rate of Return: " + adjusted_ror3);
//                    layoutInvest3.setVisibility(View.VISIBLE);
//                    amt1 = monthly_amnt3.replaceAll(",", "");
//                    amount1 = Integer.parseInt(amt1.toString()); //monthly_amnt3.replaceAll(",", "");
//                    totalAmount = totalAmount + amount1;
//                    dataModels.add(new DataModel(investLine3, convertToInt(monthly_amnt3)));
//                    dataModelsInvest.add(new DataModel(investLine3, amount3));
//                } else {
//                    layoutInvest3.setVisibility(View.GONE);
//                }

                setupPieChart(dataModels);
                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("###,###,###,###");

                tVTotalIncomeMonthly.setText(currency + " " + formatter.format(totalAmountMonthly));
                tVTotalAmountInvested.setText(currency + " " + formatter.format(totalAmountInvested));
//                    layoutInput.setVisibility(View.GONE);
//                    otherLayout.setVisibility(View.GONE);
//                    layoutAllInvest.setVisibility(View.VISIBLE);

//                pDialog.dismiss();
        }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(ChartActivity.this, "Fail to get response ", Toast.LENGTH_SHORT).show();
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

                if(investList!=null) {

                    for(int i=1;i<investList.size();i++){
                        params.put("invest" + i, investList.get(i).getInvest_name());
                        params.put("amount" + i, investList.get(i).getAmount());
                        params.put("freq"+i, investList.get(i).getFreq());
                    }
//                    for (InvestmentPass investLine : investList) {
//
////                        int i = 1;
//                        params.put("invest" + i, investLine.getInvest_name());
//                        params.put("amount" + i, investLine.getAmount());
//                        params.put("freq"+i, investLine.getFreq());
//                        i++;
//                    }
                }

                return params;
            }

        };

        queue.add(request);
    }
}