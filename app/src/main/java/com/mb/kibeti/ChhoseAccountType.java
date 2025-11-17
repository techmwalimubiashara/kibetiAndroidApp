package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChhoseAccountType extends AppCompatActivity {
    private int backPressCounter = 0;
    private static final int REQUIRED_BACK_PRESS_COUNT = 2;
    private static final int BACK_PRESS_INTERVAL = 2000;
    SharedPreferences sharedPreferences;
    String email = "";
    ProgressDialog pDialog;
    Button btnEmployed, btnNonEmployed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chhose_account_type);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL, "");

        btnEmployed = findViewById(R.id.btnEmployed);
        btnNonEmployed = findViewById(R.id.btnNonEmployed);

        btnEmployed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIncomeType("employed");

                Intent intent = new Intent(ChhoseAccountType.this, Read5SMS.class);
                startActivity(intent);


            }

        });
        btnNonEmployed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIncomeType("non-employed");

                    Intent intent = new Intent(ChhoseAccountType.this, DashboardAlpha.class);
                    startActivity(intent);

            }
        });
    }


    @Override
    public void onBackPressed() {

        backPressCounter++;

        if (backPressCounter == REQUIRED_BACK_PRESS_COUNT) {
            finish(); // Exit the app
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressCounter = 0;
                }
            }, BACK_PRESS_INTERVAL);
        }
    }

    private void setIncomeType(String income) {

        pDialog = new ProgressDialog(ChhoseAccountType.this);
        pDialog.setMessage("We are setting your account. Please wait ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(ChhoseAccountType.this);
        String url = "https://mwalimubiashara.com/app/update_income.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {




//                        finish();

                    }


                } catch (Exception e) {

                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                // method to handle errors.
                Toast.makeText(ChhoseAccountType.this, "Fail to get response ", Toast.LENGTH_SHORT).show();
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
                params.put("income_type", income);

                return params;
            }
        };

        queue.add(request);


    }

}