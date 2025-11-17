package com.mb.kibeti;

import androidx.appcompat.app.AppCompatActivity;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

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

public class MpesaUpdates extends AppCompatActivity {

    ProgressBar progressBar;
    public ListView listView;
    public MpesaCheckingAdapter myAdapter;
    PipeLines util =  new PipeLines();
    DataClass dataClass;
    List<String> categories = new ArrayList<String>();
    String email = "";
    SharedPreferences sharedPreferences;
    String cashflow = "";
    LinearLayout linearLayout;
//    ActivityMainBinding binding;
    Dialog myDialog;
    DecimalFormat formatter;

    String DELETE_INFLOW_URL=util.DELETE_INFLOW_URL;
    String GET_MPESA_STATUS = util.GET_MPESA_STATUS;

    private static ArrayList<DataClass> dataClassArrayList = new ArrayList<>();

    //    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesa_updates);

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


        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("###,###,###,###");


        myAdapter = new MpesaCheckingAdapter(this, dataClassArrayList);

        this.setTitle("Allocating Mpesa - " + cashflow);


        UpdateMpesaPop pop = new UpdateMpesaPop(this,email);

        listView.setAdapter(myAdapter);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                pop.showPopup(dataClassArrayList.get(i).getCat(), dataClassArrayList.get(i).getLine(),
                        dataClassArrayList.get(i).getAmount(), dataClassArrayList.get(i).getFrequency(),
                        dataClassArrayList.get(i).getInflowId());

                getMpesaStatus();
            }
        });

//        showMpesaIntegrationPopup();

        getMpesaStatus();

    }

//    public void ShowPopup(String cat, String line, String am, String f, String id) {
//        TextView txtclose;
//        Button submitBtn, deleteBtn;
//        EditText edCat, edLine, edAmount, edDate;
//        Spinner freq;
//        myDialog.setContentView(R.layout.custom_mpesa_popup);
//        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
//        deleteBtn = (Button) myDialog.findViewById(R.id.idBtnDelete);
//        submitBtn = (Button) myDialog.findViewById(R.id.idBtnSave);
//        edCat = (EditText) myDialog.findViewById(R.id.idCat);
//        edLine = (EditText) myDialog.findViewById(R.id.idSent);
//        edDate = (EditText) myDialog.findViewById(R.id.idDate);
//        edAmount = (EditText) myDialog.findViewById(R.id.idAmount);
//        freq = (Spinner) myDialog.findViewById(R.id.idLine);
//
//
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
//
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        freq.setAdapter(dataAdapter);
//        txtclose.setText("x");
//
//
//        String s1 = am;
////        String mpesa_trans_id = id;
//
//        String no_o = s1.replaceAll(" ", "");
//        String no_o1 = no_o.replaceAll("KES", "");
//        String no_o2 = no_o1.replaceAll(",", "");
//
//        edCat.setText(cat);
//        edLine.setText(line);
//        edDate.setText(f);
//
//        Long longval = Long.parseLong(no_o2);
//
//        String formattedString = formatter.format(longval);
//
//        edAmount.setText(formattedString);
//
//        edAmount.addTextChangedListener(new TextWatcher() {
//            //            int budgetAmount1=0;
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
////                int number = Integer.parseInt(s.toString());
//                edAmount.removeTextChangedListener(this);
//
//                String amountStr = edAmount.getText().toString();
//                if (!TextUtils.isEmpty(amountStr)) {
//
//                    String originalString = amountStr;
//
//                    Long longval;
//                    if (originalString.contains(",")) {
//                        originalString = originalString.replaceAll(",", "");
//                    }
//                    longval = Long.parseLong(originalString);
//
//
//                    String formattedString = formatter.format(longval);
//
//                    edAmount.setText(formattedString);
//                    edAmount.setSelection(edAmount.getText().length());
//
//                    int number = Integer.parseInt(originalString);
//
////                    valencyAmount =  number - budgetAmount1;
//                } else {
////                    valencyAmount = 0 - budgetAmount1;
//                }
////                valency.setText(formatter.format(valencyAmount));
//
//                edAmount.addTextChangedListener(this);
//            }
//        });
//        submitBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                progressBar.setVisibility(View.VISIBLE);
////                income_id = "0";
//                String income_frequency, category, income_line, mpesa_trans,income_amount, strDate;
//                income_frequency = freq.getSelectedItem().toString();
//                mpesa_trans  = edCat.getText().toString();
//                income_line = edLine.getText().toString();
//                strDate = edDate.getText().toString();
//                String[] datesp1 = strDate.split("-");
//
//                String date = datesp1[2] + "-" + datesp1[1] + "-" + datesp1[0];
//
//
//                income_amount = edAmount.getText().toString().replaceAll(",", "");
////                income_id = "0";
//                // validating the text fields if empty or not.
//
//                if (TextUtils.isEmpty(income_amount)) {
//                    edAmount.setError("Please enter amount");
//                }
////                if (income_frequency.equals("select Frequency")) {
//////                    freq.setError("Please enter Goal Period");
////                }
//                if (!TextUtils.isEmpty(income_frequency) && !TextUtils.isEmpty(income_amount) && !income_frequency.equals("select line")) {
//                    // calling method to add data to Firebase Firestore.
//                    String[] sp = income_frequency.split("-");
//                    String id = sp[0];
//
//                    addDataToDatabase(income_amount, id, strDate,mpesa_trans,email);
////                    addDataToDatabase(id, category, income_line, income_amount, income_frequency);
////                    progressBar.setVisibility(View.VISIBLE);
//                    getMpesaStatus();
//                } else {
////                    progressBar.setVisibility(View.GONE);
//                }
//            }
//        });
//        deleteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                delete(id);
//            }
//        });
//
////        btnFollow = (Button) myDialog.findViewById(R.id.btnfollow);
//        txtclose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                delete();
//                myDialog.dismiss();
//                progressBar.setVisibility(View.VISIBLE);
//                getMpesaStatus();
//            }
//        });
//        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        myDialog.show();
//    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        getMpesaStatus();
    }

//    private void addDataToDatabase(String income_amount, String income_id, String strDate, String mpesa_trans_id) {
//
//        // url to post our data
//        String url = "https://mwalimubiashara.com/app/add_actual_expenses.php";
//
//        if (cashflow.equals("inflow")) {
//            url = "https://mwalimubiashara.com/app/add_actual_inflow.php";
//        }
//
//        // creating a new variable for our request queue
//        RequestQueue queue = Volley.newRequestQueue(MpesaUpdates.this);
//
//
//        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                progressBar.setVisibility(View.GONE);
//                Log.e("TAG", "RESPONSE IS " + response);
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    Boolean success = jsonObject.getBoolean("error");
////                    JSONArray jsonArray =jsonObject.getJSONArray("data");
//                    if (!success) {
//
//                        Toast.makeText(MpesaUpdates.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//
//                    }
//
//                    //              Toast.makeText(YourGoalActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // method to handle errors.
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(MpesaUpdates.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            public String getBodyContentType() {
//                return "application/x-www-form-urlencoded; charset=UTF-8";
//            }
//
//            @Override
//            protected Map<String, String> getParams() {
//
//                Map<String, String> params = new HashMap<String, String>();
//
//                params.put("email", email);
//                params.put("actual_amount", income_amount);
//                params.put("id", income_id);
//                params.put("date", strDate);
//                params.put("mpesa_trans_id", mpesa_trans_id);
//
//                return params;
//            }
//        };
//        queue.add(request);
////        Toast.makeText(AddActualActivity.this, "Id "+income_id+". Freq "+income_frequency+". Amnt "+income_amount, Toast.LENGTH_SHORT).show();
//    }

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
                Intent intent = new Intent(MpesaUpdates.this,AddOutflowActivity.class);

                startActivity(intent);

            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
    public void getMpesaStatus() {

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, GET_MPESA_STATUS, new com.android.volley.Response.Listener<String>() {
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

                            dataClass = new DataClass(id, cat, line, amount, freq, time,type);
                            dataClassArrayList.add(dataClass);

                        }
//                       for (int i = 0; i < l; i++) {
//                            JSONObject object = jsonArray.getJSONObject(i);
//
//                            String cat = object.getString("income_cat");
//                            String line = object.getString("income_line");
//                            String freq = object.getString("income_freq");
//                            String amount = object.getString("income_amount");
//                            String id = object.getString("income_id");
//                            String time = object.getString("income_actual_amount");
//
//                            dataClass = new DataClass(id, cat, line, amount, freq, time);
//                            dataClassArrayList.add(dataClass);
//
//                        }
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

//    private void getBudgetedLine() {
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url = "https://mwalimubiashara.com/app/get_actual_expenses.php";
//
//        if (cashflow.equals("inflow")) {
//            url = "https://mwalimubiashara.com/app/get_actual_inflow.php";
//        }
//
//        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.e("TAG", "RESPONSE IS " + response);
//
//                categories.clear();
//
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("success");
//                    JSONArray jsonArray = jsonObject.getJSONArray("data");
//
//                    if (success.equals("1")) {
//
//                        int l = 0;
//                        l = jsonArray.length();
//
//                        for (int i = 0; i < l; i++) {
//                            JSONObject object = jsonArray.getJSONObject(i);
//                            String line = object.getString("income_line");
//
//                            String id = object.getString("income_id");
//
//
//                            categories.add(id + "-" + line);
//
//                        }
//
//                        categories.add("Others");
//
//                    }
//
//
//                } catch (Exception e) {
//
//                }
//
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // method to handle errors.
//                Toast.makeText(getApplicationContext(), "Fail to get response ", Toast.LENGTH_SHORT).show();
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
//                params.put("email", email);
//                params.put("date", "");
//
//                return params;
//            }
//        };
//
//        queue.add(request);
//    }

    private void delete(String id) {

        // url to post our data
//        String url = "https://mwalimubiashara.com/app";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(this);


        StringRequest request = new StringRequest(Request.Method.POST, DELETE_INFLOW_URL, new com.android.volley.Response.Listener<String>() {
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


//    private void addDataToDatabase(String id, String cat, String line, String amount, String frequency) {
//
//        // url to post our data
//        String url = "https://mwalimubiashara.com/app/update_inflow.php";
//
//        // creating a new variable for our request queue
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
////                progressBar.setVisibility(View.GONE);
//                Log.e("TAG", "RESPONSE IS " + response);
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    Boolean success = jsonObject.getBoolean("error");
////                    JSONArray jsonArray =jsonObject.getJSONArray("data");
//                    if (!success) {
//                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
////                        popupSnackbar(jsonObject.getString("message"));
//                        myDialog.dismiss();
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // method to handle errors.
////                progressBar.setVisibility(View.GONE);
//                Toast.makeText(getApplicationContext(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            public String getBodyContentType() {
//                return "application/x-www-form-urlencoded; charset=UTF-8";
//            }
//
//            @Override
//            protected Map<String, String> getParams() {
//
//                Map<String, String> params = new HashMap<String, String>();
//
//                params.put("email", email);
//                params.put("category", cat);
//                params.put("income_line", line);
//                params.put("income_amount", amount);
//                params.put("income_frequency", frequency);
//                params.put("id", id);
//
//                return params;
//            }
//        };
//
//        getMpesaStatus();
////        myDialog.dismiss();
//
//        queue.add(request);
//
//    }



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