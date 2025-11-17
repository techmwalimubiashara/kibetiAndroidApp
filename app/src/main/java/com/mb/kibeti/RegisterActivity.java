package com.mb.kibeti;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import com.mb.kibeti.LoginActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.mb.kibeti.currency_picker.CurrencyPickerActivity;

public class RegisterActivity extends AppCompatActivity {

    String CASHFLOW_BUDGET_REMINDER = LoginActivity.CASHFLOW_BUDGET_REMINDER;
    String CURRENCY = LoginActivity.CURRENCY;
    String CUSTOMER_JOURNEY = LoginActivity.CUSTOMER_JOURNEY;
    String DAILY_REMINDER = LoginActivity.DAILY_REMINDER;
    String EMAIL = LoginActivity.EMAIL;
    String GOALSET = LoginActivity.GOALSET;
    String MONTHLY_REMINDER = LoginActivity.MONTHLY_REMINDER;
    String MY_PREFERENCES = LoginActivity.MY_PREFERENCES;
    String PACKAGE_TYPE = LoginActivity.PACKAGE_TYPE;
    String SMSREAD = LoginActivity.SMSREAD;
    String STATUS = LoginActivity.STATUS;
    String TRIAL = LoginActivity.TRIAL;
    String USERNAME = LoginActivity.USERNAME;

    //    EditText ed_fName, ed_lName, ed_phone;
    PipeLines utils = new PipeLines();

    String URL_LOGIN_GOOGLE = utils.LOGIN_GOOGLE_URL;
    TextInputEditText ed_fName, ed_lName, ed_phone;
    LinearLayout layout1, layout2, verifyPhoneNumber;
    TextInputEditText ed_email, ed_password;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    public static final String URL_LOGIN = "https://mwalimubiashara.com/app/signup.php";
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    ProgressDialog pDialog;

    //    TextInputEditText ed_email, ed_password;
    EditText phone, otp;
    Button btngenOTP, btnverifyOTP;
    FirebaseAuth mAuth;
    String verificationID;
    private static final int RC_SIGN_IN = 9001;
    private CardView cardView;
    private LinearLayout hiddenView;
    private ImageButton arrow;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        ed_email = findViewById(R.id.emailAddress);
        ed_password = findViewById(R.id.loginPassword1);
        ed_fName = findViewById(R.id.edFName);
        ed_lName = findViewById(R.id.edLName);
        ed_phone = findViewById(R.id.edPhone);
        otp = findViewById(R.id.otp);
        progressBar = findViewById(R.id.progressbar);
        layout1 = findViewById(R.id.step1Layout);
        layout2 = findViewById(R.id.step2Layout);
        verifyPhoneNumber = findViewById(R.id.verifyPhoneNumber);
        btnverifyOTP = findViewById(R.id.btnverifyOTP);
        cardView = findViewById(R.id.base_cardview);
        hiddenView = findViewById(R.id.hidden_view);
        arrow = findViewById(R.id.arrow_button);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        setupHyperlink();
        btnverifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(otp.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Enter Valid OTP code", Toast.LENGTH_SHORT).show();
                } else {

                    verifycode(otp.getText().toString());
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Button signInButton = findViewById(R.id.ivGoogle);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        cardView.setOnClickListener(view -> {
            if (hiddenView.getVisibility() == View.VISIBLE) {

                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                hiddenView.setVisibility(View.GONE);
                arrow.setImageResource(R.drawable.baseline_expand_right);
            } else {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                hiddenView.setVisibility(View.VISIBLE);
                arrow.setImageResource(R.drawable.baseline_expand_less_24);
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            // Get the signed-in account
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Display the user's information
            if (account != null) {
                String name = account.getDisplayName();
                String email = account.getEmail();
                String photoUrl = (account.getPhotoUrl() != null) ? account.getPhotoUrl().toString() : "No Photo";

                String[] fullname = name.split(" ");


                mGoogleSignInClient.signOut();
//                Toast.makeText(this, "Welcome, " + fullname[0], Toast.LENGTH_SHORT).show();
                Log.d("GoogleSignIn", "Name: " + name + ", Email: " + email + ", Photo: " + photoUrl);
                loginWithGoogle(email, fullname[0], fullname[1]);


            }
        } catch (ApiException e) {
            // Handle sign-in failure
            Log.e("GoogleSignIn", "Sign-In failed: " + e.getStatusCode());
            Toast.makeText(this, "Sign-In Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginWithGoogle(String email, String firstName, String lastName) {
        progressBar.setVisibility(View.VISIBLE);


        class LoginWithGoogle extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("firstname", firstName);
                params.put("lastname", lastName);

                //returning the response
                return requestHandler.sendPostRequest(URL_LOGIN_GOOGLE, params);
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

                        String username = obj.getString("username");
                        String email = obj.getString("email");
                        String currency = obj.getString("currency");
                        String trial = obj.getString("account_status");
                        String daily_reminder = obj.getString("daily_reminder");
                        String monthly_reminder = obj.getString("monthly_reminder");
                        String package_type = obj.getString("package_type");
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(USERNAME, username);
                        editor.putString(DAILY_REMINDER, "Tap to set time");
                        editor.putString(MONTHLY_REMINDER, monthly_reminder);
                        editor.putString(CASHFLOW_BUDGET_REMINDER, "01/01/2000");
                        editor.putBoolean(SMSREAD, false);
                        editor.putBoolean(GOALSET, false);
                        editor.putString(EMAIL, email);
                        editor.putString(CURRENCY, currency);
                        editor.putString(CUSTOMER_JOURNEY, "not_checked");
                        editor.putString(TRIAL, trial);
                        editor.putString(PACKAGE_TYPE, package_type);
                        editor.putBoolean(STATUS, true);
                        editor.putBoolean("isIntroOpnend", true);
                        editor.commit();
                        finish();

//                        Toast.makeText(getApplicationContext(),"Currency: "+currency,Toast.LENGTH_LONG).show();

//                            String string1 = new String("app@dev.com");
//                            Toast.makeText(getApplicationContext(), email, Toast.LENGTH_LONG).show();
//                            if(package_type.equals("Subscription")) {
                        if (trial.equals("active")) {

                            if (currency.equals("not_set")) {

                                Intent intent = new Intent(RegisterActivity.this, CurrencyPickerActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                            }

//                                } else {
//
//                                }

                        } else {
                            if (package_type.equals("Subscription")) {
                                Intent intent = new Intent(RegisterActivity.this, MakeSubscriptionalPayment.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(RegisterActivity.this, ExpiredActivty.class);
                                startActivity(intent);
                            }

                        }

                    } else {

                        ed_email.setError(obj.getString("message"));
//                        ed_password.setError(obj.getString("message"));
                    }
//                        Toast.makeText(getApplicationContext(), email, Toast.LENGTH_LONG).show();
//                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
//                    e.printStackTrace();
                    Toast.makeText(RegisterActivity.this, "Something is wrong ", Toast.LENGTH_LONG).show();
                }
            }
        }

        LoginWithGoogle login = new LoginWithGoogle();
        login.execute();
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
//            String welcomeMessage = "Signed in as: " + account.getDisplayName();
//            String welcomeMessage = "Signed in as: " + account.getDisplayName();
            String welcomeMessage = "Signed in as: " + account.getDisplayName() + "" + account.getFamilyName() + " email " + account.getEmail();
            ;

//            String firstName = account.getGivenName();
//            String lastName = account.getFamilyName();
//            String email = account.getEmail();

            Toast.makeText(this, welcomeMessage, Toast.LENGTH_SHORT).show();
            // Navigate to the next activity or update UI to show signed-in state
        } else {
            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void login(View view) {
        finish();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void setupHyperlink() {
        TextView linkTextView = findViewById(R.id.tvTac);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void back2Step1(View view) {
        verifyPhoneNumber.setVisibility(View.GONE);
        layout1.setVisibility(View.VISIBLE);
    }

    public void step1(View view) {
        final String phoneNumber = ed_phone.getText().toString();
        final String password = ed_fName.getText().toString();
        final String password2 = ed_lName.getText().toString();

        // validating the text fields if empty or not.
//        if (TextUtils.isEmpty(phoneNumber)) {
//            ed_phone.setError("Phone is required");
//        }
        if (TextUtils.isEmpty(password)) {
            ed_fName.setError("First name is required");
        }
        if (TextUtils.isEmpty(password2)) {
            ed_lName.setError("Last name is required");
        }
        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(password2)) {
            layout1.setVisibility(View.GONE);
//            verifyPhoneNumber.setVisibility(View.VISIBLE);
//            sendverificationcode(phoneNumber);
            layout2.setVisibility(View.VISIBLE);

        }
    }

    public void step2(View view) {

        verifycode(otp.getText().toString());
        layout1.setVisibility(View.VISIBLE);
        layout2.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
//        appUpdateManager.unregisterListener(listener);

    }

    private void verifycode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
        signinbyCredentials(credential);
    }

    private void signinbyCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Toast.makeText(RegisterActivity.this, "Login Successful ", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(PhoneAuthActivity.this,Ma));
                        verifyPhoneNumber.setVisibility(View.GONE);
                        layout2.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void sendverificationcode(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+254" + phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:" + credential);

//            signInWithPhoneAuthCredential(credential);

            final String code = credential.getSmsCode();
            if (code != null) {
                verifycode(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e);

            Toast.makeText(RegisterActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
            // Show a message and update the UI
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:" + verificationId);

            // Save verification ID and resending token so we can use them later
//            mVerificationId = verificationId;
//            mResendToken = token;
            super.onCodeSent(verificationId, token);
            verificationID = verificationId;
        }
    };

    public void register(View view) {


        final String email = ed_email.getText().toString();
        final String password = ed_password.getText().toString();
//        final String password2 = ed_password2.getText().toString();
        final String phone = ed_phone.getText().toString();
        final String fname = ed_fName.getText().toString();
        final String lname = ed_lName.getText().toString();

        // validating the text fields if empty or not.
        if (TextUtils.isEmpty(email)) {
            ed_email.setError("Email is required");
        }
        if (TextUtils.isEmpty(fname)) {
            ed_fName.setError("First name is required");
        }
        if (TextUtils.isEmpty(phone)) {
            ed_phone.setError("Phone number is required");
        }
        if (TextUtils.isEmpty(password)) {
            ed_password.setError("Password is required");
        }
//        if(TextUtils.isEmpty(password2)) {
//            ed_password2.setError("Confirm password is required");
//        }
//        if(!password.equals(password2)){
//            ed_password.setError("Password not matching");
//            ed_password2.setError("Password not matching");
//        }

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            // calling method to add data to Firebase Firestore.

//            progressBar.setVisibility(View.VISIBLE);

            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Registering ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            class Register extends AsyncTask<Void, Void, String> {

                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);
                    params.put("fname", fname);
                    params.put("lname", lname);
                    params.put("phone", phone);

                    //returning the response
                    return requestHandler.sendPostRequest(URL_LOGIN, params);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);

//                    progressBar.setVisibility(View.GONE);
                    pDialog.dismiss();

                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(s);
                        //if no error in response
                        if (!obj.getBoolean("error")) {

//                            String username = obj.getString("username");
//                            String email = obj.getString("email");
                            String message = obj.getString("message");


                            if (message.equals("success")) {


                                String username = obj.getString("username");
                                String email = obj.getString("email");
                                String trial = obj.getString("account_status");
                                String daily_reminder = obj.getString("daily_reminder");
                                String monthly_reminder = obj.getString("monthly_reminder");
                                String package_type = obj.getString("package_type");

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(USERNAME, username);
                                editor.putString(DAILY_REMINDER, "Tap to set time");
                                editor.putString(MONTHLY_REMINDER, monthly_reminder);
                                editor.putString(EMAIL, email);
                                editor.putString(CUSTOMER_JOURNEY, "not_checked");
                                editor.putString(CASHFLOW_BUDGET_REMINDER, "01/01/2000");
                                editor.putBoolean(SMSREAD, false);
                                editor.putString(TRIAL, trial);
                                editor.putString(PACKAGE_TYPE, package_type);
                                editor.putBoolean(STATUS, true);
                                editor.putBoolean("isIntroOpnend", true);
                                editor.commit();
//                                finish();
                                Toast.makeText(getApplicationContext(), "Account created successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
//                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                                startActivity(intent);
//                                ed_email.setError(obj.getString("message"));

                            }

                        } else {
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                            ed_email.setError(obj.getString("emailErro"));
                            ed_password.setError(obj.getString("passErro"));
                        }
                    } catch (JSONException e) {

                        Log.d(TAG, e.getLocalizedMessage());
//                        Toast.makeText(RegisterActivity.this, "Something is wrong ", Toast.LENGTH_LONG).show();
                    }
                }
            }

            Register register = new Register();
            register.execute();
        }
    }
}