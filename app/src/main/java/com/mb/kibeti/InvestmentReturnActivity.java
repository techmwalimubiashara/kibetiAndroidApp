package com.mb.kibeti;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class InvestmentReturnActivity extends AppCompatActivity {
    private EditText edtGoalName,edtGoalAmount,edtGoalPeriod;
    private EditText edtCapital,edtMonthlyInflow,edtYears,edtPeriod,edtRRRMargin,edtGrowth;
    ImageView ivNPV,ivProfitability,ivPBP,ivIRR;
    private Button submitBtn;
    private ProgressBar progressBar;
    TextView tvNPV,tvIRR,tvPBP,tvProfitability,tvTipsName;
    private String capital, monthlyInflow,years,payPackPeriod,RRRMargin,growth;
    DecimalFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment_return);

        edtCapital = findViewById(R.id.idEdtCapital);
        edtMonthlyInflow = findViewById(R.id.idEdtMonthlyInflow);
        edtYears = findViewById(R.id.idEdtYears);
        edtPeriod = findViewById(R.id.idEdtPeriod);
        edtRRRMargin = findViewById(R.id.idEdtRRRMargin);
        edtGrowth = findViewById(R.id.idEdtGrowth);

        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("###,###,###,###");

        ivNPV = findViewById(R.id.ivNPV);
        ivIRR = findViewById(R.id.ivIRR);
        ivPBP = findViewById(R.id.ivPBP);
        ivProfitability = findViewById(R.id.ivProfitability);

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        tvNPV = findViewById(R.id.tvNPV);
        tvIRR =findViewById(R.id.tvIRR);
        tvPBP =findViewById(R.id.tvPBP);
        tvProfitability =findViewById(R.id.tvProfitability);

        tvTipsName = findViewById(R.id.idTipsName);

        submitBtn = findViewById(R.id.idBtnSubmit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                capital=edtCapital.getText().toString().replaceAll(",", "");
                monthlyInflow=edtMonthlyInflow.getText().toString().replaceAll(",", "");
                years=edtYears.getText().toString();
                payPackPeriod=edtPeriod.getText().toString();
                RRRMargin=edtRRRMargin.getText().toString();
                growth=edtGrowth.getText().toString();

                // validating the text fields if empty or not.
                if (TextUtils.isEmpty(capital)) {
                    edtCapital.setError("Please enter Capital");
                }  if (TextUtils.isEmpty(monthlyInflow)) {
                    edtMonthlyInflow.setError("Please enter Amount");
                } if (TextUtils.isEmpty(years)) {
                    edtYears.setError("Please enter Goal Period");
                }if (TextUtils.isEmpty(payPackPeriod)) {
                    edtPeriod.setError("Please enter Capital");
                }  if (TextUtils.isEmpty(RRRMargin)) {
                    edtRRRMargin.setError("Please enter Amount");
                } if (TextUtils.isEmpty(growth)) {
                    edtGrowth.setError("Please enter Goal Period");
                }
                if (!TextUtils.isEmpty(capital)&&!TextUtils.isEmpty(monthlyInflow)
                        &&!TextUtils.isEmpty(years)&&!TextUtils.isEmpty(payPackPeriod)&&!TextUtils.isEmpty(RRRMargin)&&!TextUtils.isEmpty(growth)){
                    // calling method to add data to Firebase Firestore.
                    addDataToDatabase(capital, monthlyInflow,years,payPackPeriod,RRRMargin,growth);
                }else{
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        edtMonthlyInflow.addTextChangedListener(new TextWatcher() {
            //            int budgetAmount1=0;
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                int number = Integer.parseInt(s.toString());
                edtMonthlyInflow.removeTextChangedListener(this);

                String amountStr = edtMonthlyInflow.getText().toString();
                if (!TextUtils.isEmpty(amountStr)) {

                    String originalString = amountStr;

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);


                    String formattedString = formatter.format(longval);

                    edtMonthlyInflow.setText(formattedString);
                    edtMonthlyInflow.setSelection(edtMonthlyInflow.getText().length());

                    int number = Integer.parseInt(originalString);

//                    valencyAmount =  number - budgetAmount1;
                } else {
//                    valencyAmount = 0 - budgetAmount1;
                }
//                valency.setText(formatter.format(valencyAmount));

                edtMonthlyInflow.addTextChangedListener(this);
            }
        });

        edtCapital.addTextChangedListener(new TextWatcher() {
            //            int budgetAmount1=0;
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                int number = Integer.parseInt(s.toString());
                edtCapital.removeTextChangedListener(this);
//                budgetAmount1 = Integer.parseInt(line.getText().toString());
//                budgetAmount1 = budgetAmount;



                String amountStr = edtCapital.getText().toString();
                if (!TextUtils.isEmpty(amountStr)) {

                    String originalString = amountStr;

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);


                    String formattedString = formatter.format(longval);

                    edtCapital.setText(formattedString);
                    edtCapital.setSelection(edtCapital.getText().length());

                    int number = Integer.parseInt(originalString);

//                    valencyAmount =  number - budgetAmount1;
                } else {
//                    valencyAmount = 0 - budgetAmount1;
                }
//                valency.setText(formatter.format(valencyAmount));

                edtCapital.addTextChangedListener(this);
            }
//            public void afterTextChanged(Editable s) {
////                int number = Integer.parseInt(s.toString());
//                budgetAmount1 = Integer.parseInt(line.getText().toString());
//                if(!TextUtils.isEmpty(amount.getText().toString())){
//                    int number = Integer.parseInt(amount.getText().toString());
//
//
//                    valencyAmount =  budgetAmount1 - number;
//                }
//                else{
//                    valencyAmount = budgetAmount1;
//                }
//                valency.setText(valencyAmount+"");
//            }
        });

    }

    private void addDataToDatabase(String capital, String monthlyInflow,String years,String payPackPeriod,String RRRMargin, String growth) {

        // url to post our data
        String url = "https://mwalimubiashara.com/app/get_investmentreturn.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(InvestmentReturnActivity.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray =jsonObject.getJSONArray("data");
                    if(success.equals("1")){

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject object=jsonArray.getJSONObject(i);

                            tvNPV.setText(object.getString("NPVresult"));
                            tvIRR.setText(object.getString("IRRresult"));
                            tvPBP.setText(object.getString("paybackresult"));
                            tvProfitability.setText(object.getString("profitindexresult"));


                            if(object.getString("NPVreac").equals("YES")){
                                ivNPV.setImageResource(R.drawable.ic_check_circle);
                            }else{
                                ivNPV.setImageResource(R.drawable.ic_cancel);
                            }
                            if(object.getString("IRRreac").equals("YES")){
                                ivIRR.setImageResource(R.drawable.ic_check_circle);
                            }else{
                                ivIRR.setImageResource(R.drawable.ic_cancel);
                            }
                            if(object.getString("paybackreac").equals("YES")){
                                ivPBP.setImageResource(R.drawable.ic_check_circle);
                            }else{
                                ivPBP.setImageResource(R.drawable.ic_cancel);
                            }
                            if(object.getString("profitreac").equals("YES")){
                                ivProfitability.setImageResource(R.drawable.ic_check_circle);
                            }else{
                                ivProfitability.setImageResource(R.drawable.ic_cancel);
                            }

//                            tvTipsName.setText("You fund!");

                        }
                    }

//                    Toast.makeText(InvestmentReturnActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // and setting data to edit text as empty
//                lumpsumNameEdt.setText("");
//                amountEdt.setText("");
//                startDateEdt.setText("");
//                endDateEdt.setText("");
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(InvestmentReturnActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                // as we are passing data in the form of url encoded
                // so we are passing the content type below
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                // below line we are creating a map for storing
                // our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();
                // on below line we are passing our
                // key and value pair to our parameters.
                params.put("capital", capital);
                params.put("monthlyInflow", monthlyInflow);
                params.put("years", years);
                params.put("paybackperiod", payPackPeriod);
                params.put("RRRMargin", RRRMargin);
                params.put("growth", growth);

                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}