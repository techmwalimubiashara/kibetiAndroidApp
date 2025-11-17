package com.mb.kibeti;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mb.kibeti.currency_picker.CurrencyPickerActivity;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.mb.kibeti.forgot_password.ResetPasswordActivity;
import com.mb.kibeti.forgot_password.data.ForgotPasswordRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private PipeLines utils = new PipeLines();
    private String URL_LOGIN = utils.LOGIN_URL;//"https://mwalimubiashara.com/app/applogin.php";
    private String URL_LOGIN_GOOGLE = utils.LOGIN_GOOGLE_URL;//"https://mwalimubiashara.com/app/applogin_with_google.php";
//    EditText ed_email, ed_password;

    //    ImageView loginGoogle;
    Button loginGoogle;

    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    SignInClient oneTapClient;
    BeginSignInRequest signUpRequest;

    private static final int RC_SIGN_IN = 100; // Request code for sign-in
    private GoogleSignInClient mGoogleSignInClient;

    private int backPressCounter = 0;
    private static final int REQUIRED_BACK_PRESS_COUNT = 2;
    private static final int BACK_PRESS_INTERVAL = 2000;

    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;

    public static final String MY_PREFERENCES = "MyPrefs";
    public static final String EMAIL = "email";
    public static final String INTRO = "isIntroOpnend";
    public static final String TRIAL = "trial";
    public static final String PACKAGE_TYPE = "package";
    public static final String STATUS = "status";
    public static final String PASSCODE = "passcode";
    public static final String USERNAME = "username";
    public static final String GOALSET = "goalSet";
    public static final String CURRENCY = "currency";
    public static final String PHONENUMBER = "phoneNumber";

    public static final String SMSREAD = "smsread";
    public static final String CUSTOMER_JOURNEY = "customer_journey";
    public static final String CASHFLOW_BUDGET_REMINDER = "cashflow_budget_reminder";
    public static String DAILY_REMINDER = "daily_reminder";
    public static String MONTHLY_REMINDER = "monthly_remainder";
    private boolean status;
    private String passcode;
    TextInputEditText ed_email, ed_password;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    private CardView cardView;
    private LinearLayout hiddenView;
    private ImageButton arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ed_email = findViewById(R.id.loginEmailAddress1);
        ed_password = findViewById(R.id.loginPassword1);
        loginGoogle = findViewById(R.id.ivGoogle);
        progressBar = findViewById(R.id.progressbar);
        TextView versionTV = findViewById(R.id.tvVersion);

        cardView = findViewById(R.id.base_cardview);
        hiddenView = findViewById(R.id.hidden_view);
        arrow = findViewById(R.id.arrow_button);
//        progressBar.setVisibility(View.GONE);
//
//        String version =
//                "Version : " + BuildConfig.VERSION_NAME;

//        versionTV.setText(version);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail() // Request email
                .build();

        // Initialize GoogleSignInClient
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Set a click listener on the Sign-In button
//        findViewById(R.id.ivGoogle).setOnClickListener(view -> signIn());


        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        status = sharedPreferences.getBoolean(STATUS, false);
        passcode = sharedPreferences.getString(PASSCODE, "");

        if (status) {

            Intent intent;
            if (passcode != "") {
                intent = new Intent(LoginActivity.this,
                        Passcode.class);
            } else {
                intent = new Intent(LoginActivity.this,
                        MainActivity.class);
            }

            startActivity(intent);
            finish();
        }

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

        oneTapClient = Identity.getSignInClient(this);
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.web_client_id))
                        // Show all accounts on the device.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();
        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 signIn();
            }
        });
    }

    private void signIn() {
        // Start the Google Sign-In activity
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // Handle the sign-in result
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
                loginWithGoogle(email, fullname[0],fullname[1]);


            }
        } catch (ApiException e) {
            // Handle sign-in failure
            Log.e("GoogleSignIn", "Sign-In failed: " + e.getStatusCode());
            Toast.makeText(this, "Sign-In Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if the user is already signed in
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            Log.d("GoogleSignIn", "User already signed in: " + account.getDisplayName());
        }
    }

    private void switchAccount() {
        // Sign out to allow account switching
        mGoogleSignInClient.signOut();
//                .addOnCompleteListener(this, task -> {
//                    // Now start the sign-in process again
//
//                });
    }
    private void loginWithGoogle(String email, String firstName,String lastName) {
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
                        String phone_number = obj.getString("phone_number");//issues
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(USERNAME, username);
                        editor.putString(DAILY_REMINDER, "Tap to set time");
                        editor.putString(MONTHLY_REMINDER, monthly_reminder);
                        editor.putString(CASHFLOW_BUDGET_REMINDER,"01/01/2000");
                        editor.putString(PHONENUMBER,phone_number);
                        editor.putBoolean(SMSREAD,false);
                        editor.putBoolean(GOALSET,false);
                        editor.putString(EMAIL, email);
                        editor.putString(CURRENCY, currency);
                        editor.putString(CUSTOMER_JOURNEY, "not_checked");
                        editor.putString(TRIAL, trial);
                        editor.putString(PACKAGE_TYPE, package_type);
                        editor.putBoolean(STATUS, true);
                        editor.putBoolean("isIntroOpnend",true);
                        editor.commit();
                        finish();

                        if (trial.equals("active")) {

                            if(currency.equals("not_set")){

                                Intent intent = new Intent(LoginActivity.this, CurrencyPickerActivity.class);
                                startActivity(intent);
                            }else{
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }

//                                } else {
//
//                                }

                        } else {
                            if (package_type.equals("Subscription")) {
                                Intent intent = new Intent(LoginActivity.this, MakeSubscriptionalPayment.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(LoginActivity.this, ExpiredActivty.class);
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
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Something is wrong " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }

        LoginWithGoogle login = new LoginWithGoogle();
        login.execute();
    }

    public void verify(View view) {
        startActivity(new Intent(LoginActivity.this,PhoneAuthActivity.class));
    }
    public void login(View view) {
        final String email = ed_email.getText().toString();
        final String password = ed_password.getText().toString();

        // validating the text fields if empty or not.
        if (TextUtils.isEmpty(email)) {
            ed_email.setError("Please enter email");
        }
        if (TextUtils.isEmpty(password)) {
            ed_password.setError("Please enter password");
        }
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            // calling method to add data to Firebase Firestore.

            progressBar.setVisibility(View.VISIBLE);
            class Login extends AsyncTask<Void, Void, String> {

                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);

                    //returning the response
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

                            String username = obj.getString("username");
                            String email = obj.getString("email");
                            String currency = obj.getString("currency");
                            String trial = obj.getString("account_status");
                            String daily_reminder = obj.getString("daily_reminder");
                            String monthly_reminder = obj.getString("monthly_reminder");
                            String package_type = obj.getString("package_type");
                            String phone_number = obj.getString("phone_number");
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(USERNAME, username);
                            editor.putString(DAILY_REMINDER, "Tap to set time");
                            editor.putString(MONTHLY_REMINDER, monthly_reminder);
                            editor.putString(EMAIL, email);
                            editor.putString(CURRENCY, currency);
                            editor.putString(CUSTOMER_JOURNEY, "not_checked");
                            editor.putString(CASHFLOW_BUDGET_REMINDER,"01/01/2000");
                            editor.putString(PHONENUMBER,phone_number);
                            editor.putBoolean(SMSREAD,false);
                            editor.putBoolean(GOALSET,false);
                            editor.putString(TRIAL, trial);
                            editor.putString(PACKAGE_TYPE, package_type);
                            editor.putBoolean(STATUS, true);
                            editor.putBoolean("isIntroOpnend",true);
                            editor.commit();
                            finish();

//                            String string1 = new String("app@dev.com");

//                            Toast.makeText(getApplicationContext(), email, Toast.LENGTH_LONG).show();
//                            if(package_type.equals("Subscription")) {
                            if (trial.equals("active")) {
                                if(currency.equals("not_set")){

                                    Intent intent = new Intent(LoginActivity.this, CurrencyPickerActivity.class);
                                    startActivity(intent);
                                }else{
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }

                            } else {
//                                if (package_type.equals("Subscription")) {
//                                    Intent intent = new Intent(LoginActivity.this, MakeSubscriptionalPayment.class);
//                                    startActivity(intent);
//                                } else {
                                    Intent intent = new Intent(LoginActivity.this, ExpiredActivty.class);
                                    startActivity(intent);
//                                }
                            }

                        } else {

//                            ed_email.setError(obj.getString("message"));
//                            ed_password.setError(obj.getString("message"));
                            Toast.makeText(LoginActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                        }
//                        Toast.makeText(getApplicationContext(), email, Toast.LENGTH_LONG).show();
//                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Something is wrong ", Toast.LENGTH_LONG).show();
                    }
                }
            }

            Login login = new Login();
            login.execute();
        }
    }
//    private void signIn(){
//        Intent signInIntent = gsc.getSignInIntent();
//        startActivityForResult(signInIntent,1000);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1000) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                task.getResult(ApiException.class);
//                navigateToSecondActivity();
//
//                Log.e("TAG", "Moving to second screen ");
//            } catch (ApiException e) {
//                Toast.makeText(getApplicationContext(), "Signin with Google cannot be completed at the moment ", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

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

    void navigateToSecondActivity() {
//        finish();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

//        updateUI(acc);

        if (acct != null) {

            String username = acct.getDisplayName();
            String email = acct.getDisplayName();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(USERNAME, username);
            editor.putString(EMAIL, email);
            editor.putString(CURRENCY, "");
            editor.putString(CASHFLOW_BUDGET_REMINDER,"01/01/2000");
            editor.putString(PHONENUMBER,"");
            editor.putBoolean(SMSREAD,false);
            editor.putBoolean(GOALSET,false);
            editor.putBoolean(STATUS, true);
            editor.putBoolean(INTRO, true);
            editor.apply();
            finish();
//            Toast.makeText(getApplicationContext(), "Logged in successfully", Toast.LENGTH_LONG).show();
            Log.e("TAG", "Email Address " + email+" /n Username "+username);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        try {
//            // Get the signed-in account
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//
//            // Display the user's information
//            if (account != null) {
//                String name = account.getDisplayName();
//                String email = account.getEmail();
//                String photoUrl = (account.getPhotoUrl() != null) ? account.getPhotoUrl().toString() : "No Photo";
//
//                Toast.makeText(this, "Welcome, " + name, Toast.LENGTH_SHORT).show();
//                Log.d("GoogleSignIn", "Name: " + name + ", Email: " + email + ", Photo: " + photoUrl);
//            }
//        } catch (ApiException e) {
//            // Handle sign-in failure
//            Log.e("GoogleSignIn", "Sign-In failed: " + e.getStatusCode());
//            Toast.makeText(this, "Sign-In Failed", Toast.LENGTH_SHORT).show();
//        }
//    }
//    @Override
//    protected void onStart() {
//        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(networkChangeListener, filter);
//        super.onStart();
//
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        if (account != null) {
//            Log.d("GoogleSignIn", "User already signed in: " + account.getDisplayName());
//        }
//    }

    @Override
    protected void onStop() {
//        unregisterReceiver(networkChangeListener);
        super.onStop();
//        appUpdateManager.unregisterListener(listener);

    }

    public void register(View view) {
        finish();
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
    public void forgotPass(View view) {

        Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
    }
}