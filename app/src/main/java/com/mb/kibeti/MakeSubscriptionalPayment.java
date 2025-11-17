package com.mb.kibeti;

        import static com.mb.kibeti.LoginActivity.CURRENCY;
        import static com.mb.kibeti.LoginActivity.EMAIL;
        import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;
        import static com.mb.kibeti.LoginActivity.TRIAL;

        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.graphics.Color;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.os.Handler;
        import android.text.TextUtils;
        import android.text.method.LinkMovementMethod;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.LinearLayout;
        import android.widget.ProgressBar;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.HashMap;

public class MakeSubscriptionalPayment extends AppCompatActivity {
    Button back;
    EditText phoneNo;
    ProgressBar progressBar, progressBar1;
    SharedPreferences sharedPreferences;
    TextView tvAmount,tvHeader;
    LinearLayout linearLayout1,linearLayout2;
    private int backPressCounter = 0;
    private static final int REQUIRED_BACK_PRESS_COUNT = 2;
    private static final int BACK_PRESS_INTERVAL = 2000; // 2 seconds
    public static final String URL_LOGIN = "https://mwalimubiashara.com/app/payment.php";
    public static final String URL2 = "https://mwalimubiashara.com/app/getpayment.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_subscriptional_payment);
        linearLayout1 = findViewById(R.id.layout);
        linearLayout2 = findViewById(R.id.layout2);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES,MODE_PRIVATE);

        back = findViewById(R.id.backBtn);
        phoneNo = findViewById(R.id.phoneNo);
        progressBar = findViewById(R.id.progressbar);
        progressBar1 = findViewById(R.id.progressbar1);
        tvAmount = findViewById(R.id.tvAmount);
        tvHeader = findViewById(R.id.tvHeader);

//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(MakeSubscriptionalPayment.this, ExpiredActivty.class);
//                startActivity(intent);
//            }
//        });

        String appName = getApplicationInfo().loadLabel(getPackageManager()).toString();
        tvHeader.setText(appName+" Payment");
        String currency = sharedPreferences.getString(CURRENCY,"KES");
        tvAmount.setText(currency+" 300 (Monthly subscription)");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                finish();
                Intent intent = new Intent(MakeSubscriptionalPayment.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
            }
        });
        setupHyperlink();
    }

    public void payment(View view){
        final String phone = phoneNo.getText().toString();
        sharedPreferences = getSharedPreferences(MY_PREFERENCES,MODE_PRIVATE);

        final String email = sharedPreferences.getString(EMAIL,"");

        // validating the text fields if empty or not.
        if (TextUtils.isEmpty(phone)) {
            phoneNo.setError("Phone number is required");
        } else {
//        if (TextUtils.isEmpty(password)) {
//            ed_password.setError("Please enter Amount");
//        }  if (!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){
//            // calling method to add data to Firebase Firestore.

            progressBar.setVisibility(View.VISIBLE);
            class Payment extends AsyncTask<Void, Void, String> {

                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("phone", phone);
                    params.put("email", email);
                    params.put("plan", "subscribe");

                    return requestHandler.sendPostRequest(URL_LOGIN, params);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);

                    progressBar.setVisibility(View.GONE);
                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(s);
                        //if no error in response
                        if (!obj.getBoolean("error")) {

                            linearLayout1.setVisibility(View.GONE);
                            linearLayout2.setVisibility(View.VISIBLE);

//                            Toast.makeText(getApplicationContext(), "email found", Toast.LENGTH_LONG).show();

                        } else {

                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MakeSubscriptionalPayment.this, "Something is wrong ", Toast.LENGTH_LONG).show();
                    }
                }
            }

            Payment payment = new Payment();
            payment.execute();
        }
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

    private void setupHyperlink() {
        TextView tac = findViewById(R.id.tvFeedback);
        tac.setMovementMethod(LinkMovementMethod.getInstance());
        tac.setLinkTextColor(Color.parseColor("#404040"));
//        TextView pp = findViewById(R.id.privacy_policy);
//        pp.setMovementMethod(LinkMovementMethod.getInstance());
//        pp.setLinkTextColor(Color.parseColor("#404040"));
    }

    public void refresh(View view){
        final String phone = phoneNo.getText().toString();
        sharedPreferences = getSharedPreferences(MY_PREFERENCES,MODE_PRIVATE);

        final String email = sharedPreferences.getString(EMAIL,"");


        progressBar1.setVisibility(View.VISIBLE);
        class Refresh extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);

                return requestHandler.sendPostRequest(URL2, params);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                progressBar1.setVisibility(View.GONE);
                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);
                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        String trial = obj.getString("account_status");

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(TRIAL, trial);
                        editor.apply();
                        finish();
                        if(trial.equals("active")){
                            Toast.makeText(getApplicationContext(), "Success: Payment made successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MakeSubscriptionalPayment.this, MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(), "Failed:Payment was not successfully made", Toast.LENGTH_LONG).show();

                        }


//                            linearLayout1.setVisibility(View.GONE);
//                            linearLayout2.setVisibility(View.VISIBLE);

//                            Toast.makeText(getApplicationContext(), "email found", Toast.LENGTH_LONG).show();

                    } else {

                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MakeSubscriptionalPayment.this, "Something is wrong ", Toast.LENGTH_LONG).show();
                }
            }
        }

        Refresh refresh = new Refresh();
        refresh.execute();

    }
}