package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mb.kibeti.eod_dashboard.EODFeelingActivity;
import com.mb.kibeti.goal_tracker.GoalTrackerActivity;
import com.mb.kibeti.invest_guide.InvestmentModelActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MpesaSpending extends AppCompatActivity {

    ProgressBar progressBar;
    public ListView listView;
    public MpesaSpendingAdapter myAdapter;
    PipeLines utils = new PipeLines();
    String GET_MPESA_ALLOCATED_STATUS = utils.GET_MPESA_ALLOCATED_STATUS;
    String UPDATE_ALLOCATED_TRANSCACTION = utils.UPDATE_ALLOCATED_TRANSCACTION;
    DataClass dataClass;
    List<String> categories = new ArrayList<String>();
    String email = "";
    SharedPreferences sharedPreferences;
    String cashflow = "";
    LinearLayout linearLayout;
    Dialog myDialog;
    DecimalFormat formatter;
    Button btnNext;

    private static ArrayList<DataClass> dataClassArrayList = new ArrayList<>();

    //@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesa_spending);

        myDialog = new Dialog(this);

        sharedPreferences = this.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL, "");
        // Inflate the layout for this fragment
        Intent prevInt = getIntent();
        cashflow = prevInt.getStringExtra("cashflow");

//        getBudgetedLine();

//      view = inflater.inflate(R.layout.fragment_inflow, container, false);
        listView = findViewById(R.id.listview);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        linearLayout = findViewById(R.id.linearLayout);
        btnNext = findViewById(R.id.idBtnNextStep);


        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("###,###,###,###");


        myAdapter = new MpesaSpendingAdapter(this, dataClassArrayList);

        this.setTitle("My Mpesa Spending");


        UpdateMpesaPop pop = new UpdateMpesaPop(this,email);

        listView.setAdapter(myAdapter);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                pop.showPopup(dataClassArrayList.get(i).getCat(), dataClassArrayList.get(i).getLine(),
                        dataClassArrayList.get(i).getAmount(), dataClassArrayList.get(i).getFrequency(),
                        dataClassArrayList.get(i).getInflowId());
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmAll();

            }
        });
//        showMpesaIntegrationPopup();

        getMpesaStatus();

    }



    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        getMpesaStatus();
    }
    private void confirmAll(){
        updateAllocatedTransaction();
        Intent intent = new Intent(MpesaSpending.this, EODFeelingActivity.class);
        startActivity(intent);
//        finish();
    }

    private void showMpesaIntegrationPopup(){
        Button submitBtn, btnDetailedBudgetting;
        TextView messageTv, headerTv;

        myDialog.setContentView(R.layout.allocating_mpesa_transactions_popups);
//        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        btnDetailedBudgetting = (Button) myDialog.findViewById(R.id.idBtnDetailedBudgetting);
        submitBtn = (Button) myDialog.findViewById(R.id.idBtnReset);
        messageTv = (TextView) myDialog.findViewById(R.id.monthlyAmount);
        headerTv = (TextView) myDialog.findViewById(R.id.textView2);

//        txtclose.setText("x");

        messageTv.setText("Kibeti has successfully interfaced your recent transactions!");
        headerTv.setText(" ");
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myDialog.dismiss();

            }
        });
        btnDetailedBudgetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myDialog.dismiss();
                Intent intent = new Intent(MpesaSpending.this,AddOutflowActivity.class);

                startActivity(intent);

            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
    public void getMpesaStatus() {

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, GET_MPESA_ALLOCATED_STATUS, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                progressBar.setVisibility(View.GONE);
                dataClassArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {

                        int l = 0;
                        l = jsonArray.length();


                        for (int i = 0; i < l; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String cat = object.getString("income_cat");
                            String line = object.getString("income_line");
                            String freq = object.getString("income_freq");
                            String amount = object.getString("income_amount");
                            String id = object.getString("income_id");
                            String time = object.getString("income_actual_amount");
                            String type  = object.getString("type");
                            String outflow_line = object.getString("outflow_line");

                            dataClass = new DataClass(id, cat, line, amount, freq, time,type,outflow_line);
                            dataClassArrayList.add(dataClass);

                        }

                        if (l > 0) {
                            linearLayout.setVisibility(View.GONE);
                        } else {
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                    }
                    myAdapter.notifyDataSetChanged();

                } catch (Exception e) {

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(getApplicationContext(), "Fail to get response ", Toast.LENGTH_SHORT).show();
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
                params.put("cashflow", cashflow);

                return params;
            }
        };

        queue.add(request);
    }
    public void updateAllocatedTransaction() {

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, UPDATE_ALLOCATED_TRANSCACTION, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG Confirming transactions", "RESPONSE IS " + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
//                    Toast.makeText(MpesaSpending.this, "", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
//                Toast.makeText(getApplicationContext(), "Fail to get response ", Toast.LENGTH_SHORT).show();
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


    private void delete(String id) {



        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(this);


        StringRequest request = new StringRequest(Request.Method.POST, utils.DELETE_INFLOW_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                progressBar.setVisibility(View.GONE);
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("error");

                    if (!success) {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
//                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                params.put("id", id);
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //do whatever you want here
        finish();
        return true;
    }
}