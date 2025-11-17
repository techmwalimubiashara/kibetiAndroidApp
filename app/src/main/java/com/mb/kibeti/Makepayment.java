package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;
import static com.mb.kibeti.LoginActivity.PACKAGE_TYPE;
import static com.mb.kibeti.LoginActivity.TRIAL;

import static java.lang.Thread.currentThread;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mb.kibeti.feedback.screens.FeedbackActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Handler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Makepayment extends AppCompatActivity {
    //    TextView back;
    EditText phoneNo, codeTv;
    Button nextBtn, backBtn1;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    ProgressDialog pDialog;
    RadioGroup radioGroup;
    LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout6, linearLayout7, couponLayout;
    String email = "", accountStatus = "";
    TextView back, tvHeader, tvFeedback;
    static PipeLines utils = new PipeLines();
    public static final String make_payment_URL = utils.MAKE_PAYMENT_URL;
    public static final String get_payment__status_URL = utils.GET_PAYMENT_STATUS_URL;
    ImageView backButton;

    ProgressBar progressBar5;
    TextView tvPhoneValidation, tvPlanValidation;
    PaymentRequiredPopup paymentRequiredPopup;
    Dialog myDialog;

    private static final String BASE_URL = "https://your-server-url.com/api/";
    private static final int POLLING_INTERVAL = 3000; // 3 seconds

    private PaymentApi paymentApi;
    private String transactionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makepayment);
        linearLayout1 = findViewById(R.id.layout);
        linearLayout2 = findViewById(R.id.layout1);
        linearLayout3 = findViewById(R.id.layout2);
        linearLayout6 = findViewById(R.id.layout6);
        linearLayout7 = findViewById(R.id.layout7);
        couponLayout = findViewById(R.id.couponLayout);
        nextBtn = findViewById(R.id.nextBtn);
        backBtn1 = findViewById(R.id.backBtn1);
        backButton = findViewById(R.id.backButton);
        tvPhoneValidation = findViewById(R.id.tvPhoneValidation);
        tvPlanValidation = findViewById(R.id.tvPlanValidation);

        back = findViewById(R.id.backBtn);
        phoneNo = findViewById(R.id.phoneNo1);
        codeTv = findViewById(R.id.codeTv);
        progressBar = findViewById(R.id.progressbar);
        progressBar5 = findViewById(R.id.progressbar5);
        tvHeader = findViewById(R.id.tvHeader);

        radioGroup = (RadioGroup) findViewById(R.id.groupradio);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL, "");

        // Uncheck or reset the radio buttons initially
        radioGroup.clearCheck();

        myDialog = new Dialog(this);

        String appName = getApplicationInfo().loadLabel(getPackageManager()).toString();
        tvHeader.setText(appName + " Payment");

        paymentRequiredPopup = new PaymentRequiredPopup(getApplicationContext(),
                myDialog, "Enjoy your unlimited access by upgrading your account");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Add the Listener to the RadioGroup
        radioGroup.setOnCheckedChangeListener(
                new RadioGroup
                        .OnCheckedChangeListener() {
                    @Override

                    // The flow will come here when
                    // any of the radio buttons in the radioGroup
                    // has been clicked

                    // Check which radio button has been clicked
                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId) {

                        // Get the selected Radio Button
                        RadioButton
                                radioButton
                                = (RadioButton) group
                                .findViewById(checkedId);
                        tvPlanValidation.setText("");
                        tvPlanValidation.setVisibility(View.GONE);
                    }
                });
        backBtn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                linearLayout2.setVisibility(View.GONE);
//                    linearLayout2.setVisibility(View.GONE);
                linearLayout1.setVisibility(View.VISIBLE);

            }
        });

//        nextBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                // When submit button is clicked,
//                // Ge the Radio Button which is set
//                // If no Radio Button is set, -1 will be returned
//                int selectedId = radioGroup.getCheckedRadioButtonId();
//                if (selectedId == -1) {
//                    Toast.makeText(Makepayment.this,
//                                    "Payment plan is required",
//                                    Toast.LENGTH_SHORT)
//                            .show();
//
//                } else {
//
//                    checkIfReferred();
//                    linearLayout1.setVisibility(View.GONE);
////                    linearLayout2.setVisibility(View.GONE);
//                    getAccount();
//                    linearLayout2.setVisibility(View.VISIBLE);
//                }
//            }
//        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                getAccountStatus();
//
//                if(accountStatus.equals("expired")){
//                    Intent intent = new Intent(Makepayment.this, ExpiredActivty.class);
//                    startActivity(intent);
//                }else{
//                    Toast.makeText(Makepayment.this,  "Checking "+accountStatus, Toast.LENGTH_SHORT).show();
                finish();
//                }


            }
        });

        setupHyperlink();

        phoneNo.addTextChangedListener(new TextWatcher() {
            //            int budgetAmount1=0;
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phoneNo.removeTextChangedListener(this);

                String phoneNumber = phoneNo.getText().toString();
//                String phoneNumber = s.toString();
                if (isValid(phoneNumber)) {
                    tvPhoneValidation.setText("");
                    tvPhoneValidation.setVisibility(View.GONE);
//                    tvPhoneValidation.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    nextBtn.setEnabled(true);
                    nextBtn.setBackground(ContextCompat.getDrawable(Makepayment.this, R.drawable.button_rectangle_line));
                    codeTv.setEnabled(true);

                } else {
                    tvPhoneValidation.setVisibility(View.VISIBLE);
                    tvPhoneValidation.setText("Invalid phone number");
                    tvPhoneValidation.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    nextBtn.setEnabled(false);
                    nextBtn.setBackground(ContextCompat.getDrawable(Makepayment.this, R.drawable.button_greyed_out));
                    codeTv.setEnabled(false);
                }

                phoneNo.addTextChangedListener(this);
            }

        });
//        phoneNo.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // Before text changes
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // While text is being changed
//                String phoneNumber = s.toString();
//                if (isValid(phoneNumber)) {
//                    tvPhoneValidation.setText("Valid phone number");
//                    tvPhoneValidation.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
//                } else {
//                    tvPhoneValidation.setText("Invalid phone number");
//                    tvPhoneValidation.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // After text changes
//            }
//        });
    }

    private void setupHyperlink() {
        TextView feedback1 = findViewById(R.id.tvFeedback);
        feedback1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent feedback = new Intent(Makepayment.this, FeedbackActivity.class);
                startActivity(feedback);
            }
        });
//        feedback1.setMovementMethod(LinkMovementMethod.getInstance());
//        feedback1.setLinkTextColor(Color.parseColor("#404040"));
//
//        TextView feedback2 = findViewById(R.id.tvFeedback2);
//        feedback2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent feedback = new Intent(Makepayment.this, FeedbackActivity.class);
//                startActivity(feedback);
//            }
//        });
////        feedback2.setMovementMethod(LinkMovementMethod.getInstance());
////        feedback2.setLinkTextColor(Color.parseColor("#404040"));
//
//        TextView feedback3 = findViewById(R.id.tvFeedback3);
//        feedback3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent feedback = new Intent(Makepayment.this, FeedbackActivity.class);
//                startActivity(feedback);
//            }
//        });
////        feedback3.setMovementMethod(LinkMovementMethod.getInstance());
////        feedback3.setLinkTextColor(Color.parseColor("#404040"));
////        TextView pp = findViewById(R.id.privacy_policy);
////        pp.setMovementMethod(LinkMovementMethod.getInstance());
////        pp.setLinkTextColor(Color.parseColor("#404040"));
    }


    public void view_free(View view) {
        LinearLayout hiddenView = findViewById(R.id.hidden_view);
        LinearLayout hiddenView2 = findViewById(R.id.hidden_view2);
        CardView cardView = findViewById(R.id.base_cardview);
        ImageView freeIm = findViewById(R.id.freeIm);
        ImageView premiumIm = findViewById(R.id.premiumIm);
        if (hiddenView.getVisibility() == View.VISIBLE) {
            TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
            hiddenView.setVisibility(View.GONE);
            freeIm.setImageResource(R.drawable.baseline_expand_right);
        } else {
            TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
            hiddenView.setVisibility(View.VISIBLE);
            hiddenView2.setVisibility(View.GONE);
            freeIm.setImageResource(R.drawable.baseline_expand_less_24);
            premiumIm.setImageResource(R.drawable.baseline_expand_right);
        }
    }

    public void view_premium(View view) {
        LinearLayout hiddenView = findViewById(R.id.hidden_view2);
        LinearLayout hiddenView2 = findViewById(R.id.hidden_view);
        CardView cardView = findViewById(R.id.base_cardview2);
        ImageView premiumIm = findViewById(R.id.premiumIm);
        ImageView freeIm = findViewById(R.id.freeIm);
        if (hiddenView.getVisibility() == View.VISIBLE) {
            TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
            hiddenView.setVisibility(View.GONE);
            premiumIm.setImageResource(R.drawable.baseline_expand_right);
        } else {
            TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
            hiddenView.setVisibility(View.VISIBLE);
            hiddenView2.setVisibility(View.GONE);
            freeIm.setImageResource(R.drawable.baseline_expand_right);
            premiumIm.setImageResource(R.drawable.baseline_expand_less_24);
        }
    }

    public void payment(View view) {

//        progressBar2.setVisibility(View.VISIBLE);
        String phone = phoneNo.getText().toString();
        String coupon = codeTv.getText().toString();

        pDialog = new ProgressDialog(this);


        int selectedId = radioGroup.getCheckedRadioButtonId();

        String plan = "";

        RadioButton radioButton
                = (RadioButton) radioGroup
                .findViewById(selectedId);

        if (selectedId == -1) {

//            Toast.makeText(Makepayment.this,
//                            "Payment plan is required",
//                            Toast.LENGTH_SHORT)
//                    .show();

            tvPlanValidation.setVisibility(View.VISIBLE);
            tvPlanValidation.setText("Payment plan is required");
            tvPlanValidation.setTextColor(getResources().getColor(android.R.color.holo_red_dark));

        } else if (TextUtils.isEmpty(phone)) {
            phoneNo.setError("");
            tvPhoneValidation.setVisibility(View.VISIBLE);
            tvPhoneValidation.setError("Phone number is required");
            tvPhoneValidation.setTextColor(getResources().getColor(android.R.color.holo_red_dark));

        } else {
            String text = radioButton.getText().toString();
            if (text.equals("Ksh.300 (Monthly Subscription)")) {
                plan = "subscribe";

            } else if (text.equals("Ksh.3,000 (Annual Subscription) Save 16%!")) {
                plan = "annual";
            } else if (text.equals("Ksh.5,000 (Lifetime Subscription)")) {
                plan = "lifetime";
            } else if (text.equals("Ksh.1,000 (Monthly Subs + Wealth Insights)")) {
                plan = "insights";
            }


            Log.e("TAG", "Plan selected is " + selectedId);
            final String planEnd = plan;

            pDialog.setMessage("Initiating payment to  " + phone + " ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
//        if (TextUtils.isEmpty(password)) {
//            ed_password.setError("Please enter Amount");
//        }  if (!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){
//            // calling method to add data to Firebase Firestore.

            if (phone.substring(0, 1).equals("0")) {
                phone = "254" + phone.substring(1);

            }
//            else
//            {
//                phone = "254"+phone;
//            }

            final String strPhone = phone;

//            progressBar2.setVisibility(View.VISIBLE);

            class Payment extends AsyncTask<Void, Void, String> {

                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("phone", strPhone);
                    params.put("email", email);
                    params.put("plan", planEnd);
                    params.put("coupon", coupon);

                    Log.e("TAG", "Data sent \n Phone " + strPhone
                            + "\n email " + email + "\n plan " + planEnd + "\n coupon " + coupon);
                    return requestHandler.sendPostRequest(make_payment_URL, params);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    Log.e("TAG", "RESPONSE IS " + s);
//                    progressBar2.setVisibility(View.GONE);
                    pDialog.dismiss();
                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(s);
                        String message = obj.getString("message").toString();
//                        if no error in response
//                        String success = obj.getString("success");
//                        JSONArray jsonArray = obj.getJSONArray("data");
//                        if (success.equals("1")) {
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject object = jsonArray.getJSONObject(i);
//
//                                int worth = object.getInt("money");
//                                monthlyAmount.setText(object.getString("moneyformat"));
//                                if (worth > 0) {
//                                    monthlyAmount.setTextColor(Color.parseColor("#0F9D58"));
//                                } else {
//                                    monthlyAmount.setTextColor(Color.parseColor("#FF0000"));
//                                }
//                            }
//                        }

                        if (!obj.getBoolean("error")) {

                            if (message.equals("Code not recognized")) {
//                                codeTv.setError("Coupon code not recognized");

                                codeTv.setError("");
                                tvPhoneValidation.setVisibility(View.VISIBLE);
                                tvPhoneValidation.setError("Promo code not recognized");
                                tvPhoneValidation.setTextColor(getResources().getColor(android.R.color.holo_red_dark));

                                linearLayout1.setVisibility(View.GONE);
                                linearLayout2.setVisibility(View.VISIBLE);
                                linearLayout3.setVisibility(View.GONE);

                            } else {
                                linearLayout1.setVisibility(View.GONE);
                                linearLayout2.setVisibility(View.GONE);
                                linearLayout3.setVisibility(View.VISIBLE);

//                                pollPaymentStatus();
                            }

//                            Toast.makeText(getApplicationContext(), "email found", Toast.LENGTH_LONG).show();

                        } else {
                            tvPhoneValidation.setVisibility(View.VISIBLE);
                            tvPhoneValidation.setError("Promo code not recognized");
                            tvPhoneValidation.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                            codeTv.setError("");

//                            Toast.makeText(getApplicationContext(), "Error detected at  "+obj.getString("message"), Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Makepayment.this, "Something is wrong ", Toast.LENGTH_LONG).show();
                    }
                }
            }

            Payment payment = new Payment();
            payment.execute();
        }
    }

    private void checkIfReferred() {

        String url = "https://mwalimubiashara.com/app/get_partner.php";

//        String email = sharedPreferences.getString(EMAIL, "");

        RequestQueue queue = Volley.newRequestQueue(this.getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
//                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    Boolean isFound = jsonObject.getBoolean("isfound");
//                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (isFound) {

                        couponLayout.setVisibility(View.GONE);
                    } else {
                        couponLayout.setVisibility(View.VISIBLE);
                    }

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

                return params;
            }
        };

        queue.add(request);
    }

    private void getAccount() {

        String url = "https://mwalimubiashara.com/app/get_account.php";

//        String email = sharedPreferences.getString(EMAIL, "");

        RequestQueue queue = Volley.newRequestQueue(this.getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
//                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String tariff = object.getString("package").toString();

//                            if(!tariff.equals("Subscription")){
//                                couponLayout.setVisibility(View.GONE);
//                            }
                        }
                    }

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

                return params;
            }
        };

        queue.add(request);
    }

    public void retry(View view) {

        linearLayout1.setVisibility(View.VISIBLE);
        linearLayout2.setVisibility(View.GONE);
        linearLayout3.setVisibility(View.GONE);
        linearLayout6.setVisibility(View.GONE);
        linearLayout7.setVisibility(View.GONE);
    }

    public void refresh(View view) {
        final String phone = phoneNo.getText().toString();
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);

//        final String email = sharedPreferences.getString(EMAIL, "");


        progressBar.setVisibility(View.VISIBLE);
        class Refresh extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);

                return requestHandler.sendPostRequest(get_payment__status_URL, params);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                progressBar.setVisibility(View.GONE);
                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);
                    //if no error in response
                    Log.e("TAG", "RESPONSE for payment" + s);
                    if (!obj.getBoolean("error")) {
                        String trial = obj.getString("account_status");
                        String account_type = obj.getString("account_type");
                        String payment_status = obj.getString("payment_status");

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(TRIAL, trial);
                        editor.putString(PACKAGE_TYPE, account_type);
                        editor.apply();
//                        finish();
//                        if (!account_type.equals("Trial") || !account_type.equals("basic")) {
//                            Toast.makeText(getApplicationContext(), "Payment made successfully", Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(Makepayment.this, MainActivity.class);
//                            startActivity(intent);
//                            if (trial.equals("active") && payment_status.equals("PAID")) {
                                if (paymentRequiredPopup.checkPayment()) {
                                    Intent intent = new Intent(Makepayment.this, CelebrationPopActivity.class);
                                    startActivity(intent);

//                                    Toast.makeText(getApplicationContext(), "Payment done", Toast.LENGTH_LONG).show();
                                    finish();
                                }
//                            } else {
//
//                                linearLayout1.setVisibility(View.GONE);
//                                linearLayout2.setVisibility(View.GONE);
//                                linearLayout3.setVisibility(View.GONE);
//                                linearLayout6.setVisibility(View.GONE);
//                                linearLayout7.setVisibility(View.VISIBLE);
//                            }


//                        } else {
//
//                            linearLayout1.setVisibility(View.GONE);
//                            linearLayout2.setVisibility(View.GONE);
//                            linearLayout3.setVisibility(View.GONE);
//                            linearLayout6.setVisibility(View.GONE);
//                            linearLayout7.setVisibility(View.VISIBLE);
//                        }


                    } else {

                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
//                    e.printStackTrace();
                    Toast.makeText(Makepayment.this, "Something is wrong ", Toast.LENGTH_LONG).show();
                }
            }
        }
        Refresh refresh = new Refresh();
        refresh.execute();
    }

    private void pollPaymentStatus() {

//        new Thread(() -> {
//            while (true) {
//                try {
//                    Thread.sleep(POLLING_INTERVAL);
                    checkPaymentStatus();
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                    break;
//                }
//            }

int j=0;
        while (j<=50) {

            checkPaymentStatus();
            int finalJ = j;
            new Handler().postDelayed(() -> {

                String trial = sharedPreferences.getString(TRIAL,"");
                String payment_status = sharedPreferences.getString(PACKAGE_TYPE,"");

                if (trial.equals("active") && payment_status.equals("PAID")) {
//                                progressBar5.setVisibility(View.GONE);
//                                Toast.makeText(getApplicationContext(), "Payment made successful", Toast.LENGTH_LONG).show();
                    linearLayout1.setVisibility(View.GONE);
                    linearLayout2.setVisibility(View.GONE);
                    linearLayout3.setVisibility(View.GONE);
                    linearLayout6.setVisibility(View.VISIBLE);
//                                runOnUiThread(() -> showToast("Payment Completed: "));
                    // Stop polling
//                                currentThread().interrupt();
                } else

                if((trial.equals("active") || !payment_status.equals("PAID"))&& finalJ ==50){

                    linearLayout1.setVisibility(View.GONE);
                    linearLayout2.setVisibility(View.GONE);
                    linearLayout3.setVisibility(View.GONE);
                    linearLayout6.setVisibility(View.GONE);
                    linearLayout7.setVisibility(View.VISIBLE);

                }

            }, 20000);


            j++;
//        }).start();
        }
    }

    private void checkPaymentStatus() {


        String url = "https://mwalimubiashara.com/app/getpayment.php";

//        String email = sharedPreferences.getString(EMAIL, "");

        RequestQueue queue = Volley.newRequestQueue(this.getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE for checking payment" + response);
//                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject obj = new JSONObject(response);
                    if (!obj.getBoolean("error")) {
                        String trial = obj.getString("account_status");
                        String account_type = obj.getString("account_type");
                        String payment_status = obj.getString("payment_status");

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(TRIAL, trial);
                        editor.putString(PACKAGE_TYPE, account_type);
                        editor.apply();
//                        finish();
                        if (!account_type.equals("Trial") || !account_type.equals("basic")) {
//                            Toast.makeText(getApplicationContext(), "Payment made successfully", Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(Makepayment.this, MainActivity.class);
//                            startActivity(intent);

                            if (trial.equals("active") && payment_status.equals("PAID")) {
//                                progressBar5.setVisibility(View.GONE);
//                                Toast.makeText(getApplicationContext(), "Payment made successful", Toast.LENGTH_LONG).show();
                                linearLayout3.setVisibility(View.GONE);
                                linearLayout6.setVisibility(View.VISIBLE);
//                                runOnUiThread(() -> showToast("Payment Completed: "));
                                // Stop polling
//                                currentThread().interrupt();
                            } else {

//                                Toast.makeText(getApplicationContext(), "Payment not successful", Toast.LENGTH_LONG).show();
                            }


                        } else {

//                            Toast.makeText(getApplicationContext(), "Payment not successful", Toast.LENGTH_LONG).show();
                        }


                    } else {

//                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();

                        currentThread().interrupt();

                    }

                } catch (Exception e) {

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
//                Toast.makeText(getApplicationContext(), "Fail to get response ", Toast.LENGTH_SHORT).show();
                currentThread().interrupt();
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


//
//        paymentApi.checkPaymentStatus(transactionId).enqueue(new Callback<PaymentStatusResponse>() {
//            @Override
//            public void onResponse(Call<PaymentStatusResponse> call, Response<PaymentStatusResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    if (response.body().isCompleted) {
//                        runOnUiThread(() -> showToast("Payment Completed: " + response.body().message));
//                        // Stop polling
//                        Thread.currentThread().interrupt();
//                    }
//                } else {
//                    Log.d("PaymentStatus", "Still pending...");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PaymentStatusResponse> call, Throwable t) {
//                Log.e("PaymentStatus", "Error: " + t.getMessage());
//            }
//        });


    }


    public static boolean isValid(String s) {

        // Regular expression to validate
        // global mobile numbers
//        Pattern p = Pattern.compile("^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$");
        Pattern p = Pattern.compile("^[0]{1}?[7,1]{1}?[0,1,2,5,6,4]{1}?[0-9]{7}$");

        // Matcher to check if the string
        // matches the regular expression
        Matcher m = p.matcher(s);

        // Returns true if valid, otherwise false
        return (m.matches());
    }

    private void showToast(String msg) {

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }
}


class PaymentResponse {
    public String transactionId;
    public String paymentUrl;
    // Add other fields as needed
}

class PaymentStatusResponse {
    public boolean isCompleted;
    public String message;
    // Add other fields as needed
}