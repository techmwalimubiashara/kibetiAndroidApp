package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditInflow extends AppCompatActivity {
    Spinner freq;

    EditText cat, line, amount;

    private Button submitBtn,deleteBtn;

    ProgressBar progressBar;
    String email, category, income_line, income_amount, income_frequency, income_id;
    SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_inflow);
        freq = findViewById(R.id.idFrequency);
        cat = findViewById(R.id.idCat);
        line = findViewById(R.id.idLine);
        amount = findViewById(R.id.idAmount);
        submitBtn = findViewById(R.id.idBtnSave);
        deleteBtn = findViewById(R.id.idBtnDelete);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL, "");

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

        Intent intent = this.getIntent();
        if (intent != null) {

            String s1 = intent.getStringExtra("amount");
            income_id = intent.getStringExtra("id");

            String no_o = s1.replaceAll(" ", "");
            String no_o1 = no_o.replaceAll("KES", "");
            String no_o2 = no_o1.replaceAll(",", "");

            cat.setText(intent.getStringExtra("cat"));
            line.setText(intent.getStringExtra("line"));
            amount.setText(no_o2);
//            freq.setSelection(intent.getIntExtra("frequency"));
        }

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progressBar.setVisibility(View.VISIBLE);

                income_frequency = freq.getSelectedItem().toString();
                category = cat.getText().toString();
                income_line = line.getText().toString();
                income_amount = amount.getText().toString();
//                income_id = "0";

                // validating the text fields if empty or not.

                if (TextUtils.isEmpty(income_amount)) {
                    amount.setError("Please enter amount");
                }
                if (TextUtils.isEmpty(income_frequency)) {
//                    freq.setError("Please enter Goal Period");
                }
                if (!TextUtils.isEmpty(income_frequency) && !TextUtils.isEmpty(income_amount)&&!income_frequency.equals("select Frequency")) {
                    // calling method to add data to Firebase Firestore.

                    addDataToDatabase();
                } else {
//                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }});

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //do whatever you want here
        finish();
        return true;
    }

    private void delete() {

        // url to post our data
        String url = "https://mwalimubiashara.com/app/delete_inflow.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(EditInflow.this);


        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                progressBar.setVisibility(View.GONE);
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("error");

                    if (!success) {
                        Toast.makeText(EditInflow.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(EditInflow.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                params.put("id", income_id);

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

    private void addDataToDatabase() {

        // url to post our data
        String url = "https://mwalimubiashara.com/app/update_inflow.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(EditInflow.this);


        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                progressBar.setVisibility(View.GONE);
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
//                progressBar.setVisibility(View.GONE);
                Toast.makeText(EditInflow.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                params.put("id", income_id);

                return params;
            }
        };

        queue.add(request);

    }
}