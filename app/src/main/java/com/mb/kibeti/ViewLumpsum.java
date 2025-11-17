package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ViewLumpsum extends AppCompatActivity {
    Spinner freq;

    EditText cat, line, amount;
    ListView listView;

    private Button submitBtn, deleteBtn;

    MyAdapterLumpsum myAdapter;
    DataClass dataClass;

    ProgressBar progressBar;
    String email, category, income_line, income_amount, income_frequency;
    SharedPreferences sharedPreferences;
    EditText endDateEt;
    Dialog myDialog;
    DecimalFormat formatter;
    String lumpsum_name,lump_id;
    LinearLayout addLayout;
    private static ArrayList<DataClass> dataClassArrayList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_lumpsum);
        freq = findViewById(R.id.idFrequency);
        cat = findViewById(R.id.idCat);
        line = findViewById(R.id.idLine);
        amount = findViewById(R.id.idAmount);
        submitBtn = findViewById(R.id.idBtnSave);
        deleteBtn = findViewById(R.id.idBtnDelete);
        endDateEt = findViewById(R.id.idEndDate);
        listView = findViewById(R.id.lvSpending);
        addLayout = findViewById(R.id.addLayout);
        progressBar = findViewById(R.id.progressbar);
        myDialog = new Dialog(ViewLumpsum.this);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL, "");

        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("###,###,###,###");

        Intent intent = this.getIntent();
        if (intent != null) {

            String s1 = intent.getStringExtra("amount");
            lump_id = intent.getStringExtra("id");

            lumpsum_name = intent.getStringExtra("line");
//
//            String no_o = s1.replaceAll(" ", "");
//            String no_o1 = no_o.replaceAll("KES", "");
//            String no_o2 = no_o1.replaceAll(",", "");

            cat.setText(intent.getStringExtra("cat"));
            line.setText(lumpsum_name);
            amount.setText(s1);
            endDateEt.setText(intent.getStringExtra("frequency"));
//            freq.setSelection(intent.getIntExtra("frequency"));
        }

        getSupportActionBar().setTitle(lumpsum_name);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myAdapter = new MyAdapterLumpsum(ViewLumpsum.this, dataClassArrayList);
        listView.setAdapter(myAdapter);
        listView.setClickable(true);
        getData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(ViewLumpsum.this,ViewLumpsum.class);
//                intent.putExtra("cat", dataClassArrayList.get(i).getCat());
//                intent.putExtra("line",dataClassArrayList.get(i).getLine());
//                intent.putExtra("amount",dataClassArrayList.get(i).getAmount());
//                intent.putExtra("id",dataClassArrayList.get(i).getInflowId());
//                intent.putExtra("frequency",dataClassArrayList.get(i).getFrequency());
//                startActivity(intent);
//                myDialog = new Dialog(getContext());
                ShowPopup(dataClassArrayList.get(i).getCat(), dataClassArrayList.get(i).getLine(),
                        dataClassArrayList.get(i).getAmount(), dataClassArrayList.get(i).getFrequency(), dataClassArrayList.get(i).getInflowId());

//                Toast.makeText(ViewLumpsum.this, "User id is:"+dataClassArrayList.get(i).getInflowId(), Toast.LENGTH_SHORT).show();
            }
        });

        addLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddSpendingPopup();
            }
        });

        List<String> categories = new ArrayList<String>();
        categories.add("select Frequency");
        categories.add("Daily");
        categories.add("Weekly");
        categories.add("Monthly");
        categories.add("Quarterly");
        categories.add("Semi Annually");
        categories.add("Annually");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        freq.setAdapter(dataAdapter);


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                income_frequency = freq.getSelectedItem().toString();
                category = cat.getText().toString();
                income_line = line.getText().toString();
                income_amount = amount.getText().toString();
//                lump_id = "0";

                // validating the text fields if empty or not.

                if (TextUtils.isEmpty(income_amount)) {
                    amount.setError("Please enter amount");
                }
                if (TextUtils.isEmpty(income_frequency)) {
//                    freq.setError("Please enter Goal Period");
                }
                if (!TextUtils.isEmpty(income_frequency) && !TextUtils.isEmpty(income_amount) && !income_frequency.equals("select Frequency")) {
                    // calling method to add data to Firebase Firestore.

                    addDataToDatabase();
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
//        deleteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                delete();
//            }
//        });

    }

    //    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        //do whatever you want here
//        finish();
//        return true;
//    }

    private void ShowPopup(String cat, String line, String am, String f, String id) {
        TextView txtclose;
        Button submitBtn, deleteBtn;
        EditText fromDate,toDate, edLine, edAmount;

        ImageView calendar1,calendar2;

        myDialog.setContentView(R.layout.custom_lumpsum_spending_popup);
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        deleteBtn = (Button) myDialog.findViewById(R.id.idBtnDelete);
        submitBtn = (Button) myDialog.findViewById(R.id.idBtnSave);
        fromDate = (EditText) myDialog.findViewById(R.id.fromDate);
        toDate = (EditText) myDialog.findViewById(R.id.toDate);
        edLine = (EditText) myDialog.findViewById(R.id.idLine);
        edAmount = (EditText) myDialog.findViewById(R.id.idAmount);


        calendar1 = (ImageView) myDialog.findViewById(R.id.calendar1);
        calendar2 = (ImageView) myDialog.findViewById(R.id.calendar2);
        txtclose.setText("x");

        calendar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowCalendar(fromDate);
            }
        });
        calendar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowCalendar(toDate);
            }
        });

        String s1 = am;
        String lump_id = id;

        String no_o = s1.replaceAll(" ", "");
        String no_o1 = no_o.replaceAll("KES", "");
        String no_o2 = no_o1.replaceAll(",", "");

        Long longval;
        longval = Long.parseLong(no_o2);

        fromDate.setText(cat);
        toDate.setText(f);
        edLine.setText(line);
        edAmount.setText(formatter.format(longval));
//            freq.setSelection(f);
//        }

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
//                lump_id = "0";
                String income_frequency, category, income_line, income_amount;
                income_frequency = freq.getSelectedItem().toString();
                category = fromDate.getText().toString();
                income_line = edLine.getText().toString();
                income_amount = edAmount.getText().toString().replaceAll(",", "");
//                lump_id = "0";
                // validating the text fields if empty or not.

                if (TextUtils.isEmpty(income_amount)) {
                    edAmount.setError("Please enter amount");
                }
//                if (income_frequency.equals("select Frequency")) {
////                    freq.setError("Please enter Goal Period");
//                }
                if (!TextUtils.isEmpty(income_frequency) && !TextUtils.isEmpty(income_amount) && !income_frequency.equals("select Frequency")) {
                    // calling method to add data to Firebase Firestore.
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        edAmount.addTextChangedListener(new TextWatcher() {
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
                edAmount.removeTextChangedListener(this);

                String amountStr = edAmount.getText().toString();
                if (!TextUtils.isEmpty(amountStr)) {

                    String originalString = amountStr;

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);


                    String formattedString = formatter.format(longval);

                    edAmount.setText(formattedString);
                    edAmount.setSelection(edAmount.getText().length());

                    int number = Integer.parseInt(originalString);

//                    valencyAmount =  number - budgetAmount1;
                } else {
//                    valencyAmount = 0 - budgetAmount1;
                }
//                valency.setText(formatter.format(valencyAmount));

                edAmount.addTextChangedListener(this);
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(id);
                getData();
            }
        });

//        btnFollow = (Button) myDialog.findViewById(R.id.btnfollow);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myDialog.dismiss();
                progressBar.setVisibility(View.VISIBLE);
                getData();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void AddSpendingPopup() {
        TextView txtclose;
        Button submitBtn, deleteBtn;
        EditText fromDate,toDate, edLine, edAmount;
        ImageView calendar1,calendar2;


        myDialog.setContentView(R.layout.custom_lumpsum_spending_popup);
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        deleteBtn = (Button) myDialog.findViewById(R.id.idBtnDelete);
        submitBtn = (Button) myDialog.findViewById(R.id.idBtnSave);
        fromDate = (EditText) myDialog.findViewById(R.id.fromDate);
        toDate = (EditText) myDialog.findViewById(R.id.toDate);
        edLine = (EditText) myDialog.findViewById(R.id.idLine);
        edAmount = (EditText) myDialog.findViewById(R.id.idAmount);
        calendar1 = (ImageView) myDialog.findViewById(R.id.calendar1);
        calendar2 = (ImageView) myDialog.findViewById(R.id.calendar2);
        txtclose.setText("x");

        calendar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowCalendar(fromDate);
            }
        });
        calendar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowCalendar(toDate);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
//                lump_id = "0";
                String edate, sdate, income_line, income_amount;
                sdate = fromDate.getText().toString();
                edate = toDate.getText().toString();
                income_line = edLine.getText().toString();
                income_amount = edAmount.getText().toString().replaceAll(",", "");
//                lump_id = "0";
                // validating the text fields if empty or not.

                if (TextUtils.isEmpty(income_amount)) {
                    edAmount.setError("Please enter amount");
                }
                if (TextUtils.isEmpty(income_line)) {
                    edLine.setError("Please enter name");
                }
                if (TextUtils.isEmpty(edate)) {
                    toDate.setError("Please select start date");
                }
                if (TextUtils.isEmpty(sdate)) {
                    fromDate.setError("Please select end date");
                }
//                if (income_frequency.equals("select Frequency")) {
//                    freq.setError("Please enter Goal Period");
//                }
                if (!TextUtils.isEmpty(income_line) && !TextUtils.isEmpty(income_amount) && !TextUtils.isEmpty(sdate) && !TextUtils.isEmpty(edate)) {
                    // calling method to add data to Firebase Firestore.
                    addSpending(income_line,income_amount,sdate,edate);

                    getData();

                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        edAmount.addTextChangedListener(new TextWatcher() {
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
                edAmount.removeTextChangedListener(this);

                String amountStr = edAmount.getText().toString();
                if (!TextUtils.isEmpty(amountStr)) {

                    String originalString = amountStr;

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);


                    String formattedString = formatter.format(longval);

                    edAmount.setText(formattedString);
                    edAmount.setSelection(edAmount.getText().length());

                    int number = Integer.parseInt(originalString);

//                    valencyAmount =  number - budgetAmount1;
                } else {
//                    valencyAmount = 0 - budgetAmount1;
                }
//                valency.setText(formatter.format(valencyAmount));

                edAmount.addTextChangedListener(this);
            }
        });

//        btnFollow = (Button) myDialog.findViewById(R.id.btnfollow);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myDialog.dismiss();
                progressBar.setVisibility(View.VISIBLE);
                getData();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
    private void addSpending(String name, String amnt, String sdate,String edate) {

        // url to post our data
        String url = "https://mwalimubiashara.com/app/post_spending.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(ViewLumpsum.this);


        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("error");

                    if (!success) {
                        Toast.makeText(ViewLumpsum.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        popupSnackbar(jsonObject.getString("message"));
//                        finish();
                        myDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ViewLumpsum.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                params.put("id", lump_id);
                params.put("spend_name",  name);
                params.put("spend_amnt",  amnt);
                params.put("spend_sdate",  sdate);
                params.put("spend_edate",  edate);

                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
    private void delete() {

        // url to post our data
        String url = "https://mwalimubiashara.com/app/delete_lump.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(ViewLumpsum.this);


        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("error");

                    if (!success) {
                        Toast.makeText(ViewLumpsum.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        popupSnackbar(jsonObject.getString("message"));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ViewLumpsum.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                params.put("id", lump_id);

                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
    private void delete(String id) {

        // url to post our data
        String url = "https://mwalimubiashara.com/app/delete_lump_sp.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(ViewLumpsum.this);


        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("error");

                    if (!success) {
                        popupSnackbar(jsonObject.getString("message"));
                        myDialog.dismiss();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ViewLumpsum.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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

    private void popupSnackbar(String msg) {
        Snackbar snackbar =
                Snackbar.make(
                        findViewById(android.R.id.content),
                        msg,
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        snackbar.setActionTextColor(
                getResources().getColor(R.color.primary_green));
        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_lumpsum_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){

            onBackPressed();
            //super.onBackPressed();
            return true;

        }else if(id==android.R.id.home){
            return true;
        }else if(id==R.id.action_edit){
            return true;
        }else if(id==R.id.action_delete){
            delete();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
//        switch (item.getItemId()) {
//            //case R.id.home:
//            case android.R.id.home:
//                onBackPressed();
//                //super.onBackPressed();
//                return true;
//            case R.id.action_edit:
////                Intent nextInt = new Intent(this, EditLumpsum.class);
////                startActivity(nextInt);
////                finish();
//                return true;
//            case R.id.action_delete:
//                    delete();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
    }

    private void addDataToDatabase() {

        // url to post our data
        String url = "https://mwalimubiashara.com/app/update_inflow.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(ViewLumpsum.this);


        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("error");
//                    JSONArray jsonArray =jsonObject.getJSONArray("data");
                    if (!success) {
//                        Toast.makeText(EditInflow.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        popupSnackbar(jsonObject.getString("message"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ViewLumpsum.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                params.put("category", category);
                params.put("income_line", income_line);
                params.put("income_amount", income_amount);
                params.put("income_frequency", income_frequency);
                params.put("id", lump_id);

                return params;
            }
        };

        queue.add(request);

    }

    private void getData() {
        String url = "https://mwalimubiashara.com/app/get_lumpsum_spending.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                dataClassArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

//                    if(success.equals("1")){
                    int l = 0;
                    l = jsonArray.length();

                    for (int i = 0; i < l; i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String start = object.getString("start");
                        String name = object.getString("name");
                        String end = object.getString("end");
                        String amount = object.getString("amount");
                        String id = object.getString("id");

                        dataClass = new DataClass(id, start, name, amount, end, "");

//                            dataClass = new DataClass(id,name,age,gender);
                        dataClassArrayList.add(dataClass);

                    }
//                    if(l>0){
//                        linearLayout.setVisibility(View.GONE);
//                    }else{
//                        linearLayout.setVisibility(View.VISIBLE);
//                    }
//                    }
                    myAdapter.notifyDataSetChanged();

                } catch (Exception e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getCont, error.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(ViewLumpsum.this, "Something is wrong. Please check your internet connection", Toast.LENGTH_SHORT).show();
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
                params.put("lump_id", lump_id);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ViewLumpsum.this);
        requestQueue.add(request);
    }
    public void ShowCalendar(EditText ed){
        final Calendar c = Calendar.getInstance();

        // on below line we are getting
        // our day, month and year.
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // on below line we are creating a variable for date picker dialog.
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                // on below line we are passing context.
                ViewLumpsum.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // on below line we are setting date to our text view.
                        ed.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                },
                // on below line we are passing year,
                // month and day for selected date in our date picker.
                year, month, day);
        // at last we are calling show to
        // display our date picker dialog.
        datePickerDialog.show();
    }
}