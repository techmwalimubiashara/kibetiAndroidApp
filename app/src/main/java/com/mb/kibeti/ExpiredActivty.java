package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;
import static com.mb.kibeti.LoginActivity.PACKAGE_TYPE;
import static com.mb.kibeti.LoginActivity.TRIAL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ExpiredActivty extends AppCompatActivity {

    public static final String URL_LOGIN = "https://mwalimubiashara.com/app/payment.php";
    TextView logout,tvHeader;
    SharedPreferences sharedPreferences;
    Button makePaymentBtn;
    private int backPressCounter = 0;
    private static final int REQUIRED_BACK_PRESS_COUNT = 2;
    private static final int BACK_PRESS_INTERVAL = 2000; // 2 seconds
    String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expired_activty);
        logout = findViewById(R.id.tvLogOut);
        tvHeader = findViewById(R.id.tvHeader);
        makePaymentBtn = findViewById(R.id.makePaymentBtn);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);

        email = sharedPreferences.getString(EMAIL, "");

        String appName = getApplicationInfo().loadLabel(getPackageManager()).toString();
        tvHeader.setText("Your "+appName+"account has expired");

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.clear();
//                editor.apply();
//                finish();
//                Intent intent = new Intent(ExpiredActivty.this, LoginActivity.class);
//                startActivity(intent);
//                Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
                getAccount();
//                finish();
                String trial = sharedPreferences.getString(TRIAL, "");
                Log.e("TAG", "RESPONSE ON STATUS " + trial+" PACKAGE "+sharedPreferences.getString(PACKAGE_TYPE, ""));
                if(trial.equals("active")){
                    Intent intent = new Intent(ExpiredActivty.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        });

        makePaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ExpiredActivty.this, Makepayment.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        backPressCounter++;

        if (backPressCounter == REQUIRED_BACK_PRESS_COUNT) {
//            super.onBackPressed(); // Exit the app
            this.finish();
            System.exit(0);
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

    private void getAccount() {

        String url = "https://mwalimubiashara.com/app/basic.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
//                progressBar.setVisibility(View.GONE);

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    String package_type = jsonObject.getString("package").toString();
                    String status = jsonObject.getString("status").toString();
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString(TRIAL, status);
                    editor.putString(PACKAGE_TYPE, package_type);
                    editor.commit();

//                    if(success.equals("1")){
//
//                        for(int i=0;i<jsonArray.length();i++){
//                            JSONObject object = jsonArray.getJSONObject(i);
//
////                            username.setText(object.getString("first_name")+"  "+object.getString("other_name"));
////                            tvStatus.setText(object.getString("trialExpired"));
////                            tvTariff.setText(object.getString("package"));//
////                            tvExpiry.setText(object.getString("expiry"));
//
//                            String tariff = object.getString("package").toString();
//                            String status = object.getString("package").toString();
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//
//                            editor.putString(TRIAL, status);
//                            editor.putString(PACKAGE_TYPE, tariff);
//
//
//                        }
//                    }

                }catch (Exception e){

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

                return params;
            }
        };

        queue.add(request);
    }

}