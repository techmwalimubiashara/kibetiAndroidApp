package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.CASHFLOW_BUDGET_REMINDER;
import static com.mb.kibeti.LoginActivity.CUSTOMER_JOURNEY;
import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;
import static com.mb.kibeti.LoginActivity.PACKAGE_TYPE;
import static com.mb.kibeti.LoginActivity.SMSREAD;
import static com.mb.kibeti.LoginActivity.TRIAL;
import static com.mb.kibeti.LoginActivity.USERNAME;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.BuildConfig;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.mb.kibeti.feedback.screens.FeedbackActivity;
import com.mb.kibeti.sms_filter.TopRecipientsActivity;
import com.tapadoo.alerter.Alerter;
import com.tapadoo.alerter.OnHideAlertListener;
import com.tapadoo.alerter.OnShowAlertListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.Manifest;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String notify_title = "notify_title";
    public static String notify_content = "notify_content";
    public static String notify_intro = "notify_intro";
    public static String notify_id = "0";

    private int permissionCount=1;

    BottomSheetDialog bottomSheetDialog;
    DrawerLayout drawerLayout;
    //    public static final String MY_PREFERENCES = "MyPrefs";
    private static final int MY_REQUEST_CODE = 100;
    SharedPreferences sharedPreferences;
    MyViewPagerAdapter myViewPagerAdapter;
    BottomNavigationView bottomNavigationView;
    HomeFragment bottomHomeFragment = new HomeFragment();
    CashFragment cashflowsFragment = new CashFragment();
    ReferFragment referFragment = new ReferFragment();
    Learning learningFragment = new Learning();

    NetFragment netFragment = new NetFragment();
    SpendingFragment spendingFragment = new SpendingFragment();
    boolean isNightModeOn;
    private PipeLines utils = new PipeLines();
    AppUpdateManager appUpdateManager;
    int outflow = 0;
    ImageView imAccount;
    DBHandler dbHandler;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    private static final String PERMISSION_READ_SMS = Manifest.permission.READ_SMS;
    private static final String PERMISSION_RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;
    private static final int PERMISSION_REQ_CODE = 1;
    private static final int READ_SMS_PERMISSION_CODE = 1;

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int SMS_PERMISSION_REQUEST_CODE = 100;
    boolean smsIsRead = false;

    Dialog myDialog;
    SmsReceiver smsReceiver;
    String email = "";
    TrialPopUp trialPopUp;
    ImageButton imgDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        smsReceiver = new SmsReceiver();

        drawerLayout = findViewById(R.id.drawer_layout);
        imgDrawer = findViewById(R.id.imgDrawer);
        imAccount = findViewById(R.id.imAccount);

        myViewPagerAdapter = new MyViewPagerAdapter(this);
        dbHandler = new DBHandler(MainActivity.this);

        trialPopUp = new TrialPopUp(MainActivity.this, email);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        String trial = sharedPreferences.getString(TRIAL, "");
        String package_type = sharedPreferences.getString(PACKAGE_TYPE, "");
        String customer_journey = sharedPreferences.getString(CUSTOMER_JOURNEY, "");
        smsIsRead = sharedPreferences.getBoolean(SMSREAD, false);
        email = sharedPreferences.getString(EMAIL, "");

        myDialog = new Dialog(this);


        boolean isConnected = ConnectivityHelper.isConnectedToNetwork(MainActivity.this);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            isNightModeOn = false;
        } else {
            isNightModeOn = true;
        }

        requestNotificationPermission();
        requestReadSMSPermission();


        if (package_type.equals("Trial") || package_type.equals("basic")) {
            if (customer_journey.equals("not_checked")) {
//                trialPopUp.show_dialog();
//                Intent intent = new Intent(MainActivity.this, Intro_My_Goal.class);
//                startActivity(intent);
            }
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && checkSelfPermission(Manifest.permission.RECEIVE_SMS)
//        != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS},PERMISSION_REQ_CODE);
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("Allow Kibeti to send and view SMS messages to give accurate reports")
//                    .setTitle("Permission Required")
//                    .setCancelable(false)
//                    .setPositiveButton("Ok",  (dialog, which)-> {
//                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{PERMISSION_READ_SMS},
//                                PERMISSION_REQ_CODE);
//                        dialog.dismiss();
//                    }).setNegativeButton("Cancel",((dialog,which)->dialog.dismiss()));
//            builder.show();
//        }else {
//
//        }
//
//
//        if(ActivityCompat.checkSelfPermission(this,PERMISSION_READ_SMS)==PackageManager.PERMISSION_GRANTED){
//            Toast.makeText(this,"Permission Granted",Toast.LENGTH_LONG).show();
//            Log.e("TAG", "Is sms read 2 ? " + smsIsRead);
//            if(!smsIsRead){
//                if(readSms()){
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putBoolean(SMSREAD,true);
//                    editor.apply();
//                }
//            }
//
//        }else if(ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_READ_SMS)){
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("Allow Kibeti to send and view SMS messages to give accurate reports")
//                    .setTitle("Permission Required")
//                    .setCancelable(false)
//                    .setPositiveButton("Ok",  (dialog, which)-> {
//                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{PERMISSION_READ_SMS},
//                                PERMISSION_REQ_CODE);
//                        dialog.dismiss();
//                    }).setNegativeButton("Cancel",((dialog,which)->dialog.dismiss()));
//            builder.show();
//            ActivityCompat.requestPermissions(this,
//                    new String[]{android.Manifest.permission.READ_SMS}, READ_SMS_PERMISSION_CODE);
//        }else{
//            ActivityCompat.requestPermissions(this, new String[]{PERMISSION_READ_SMS},
//                    PERMISSION_REQ_CODE);
//        }
//
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{android.Manifest.permission.READ_SMS}, READ_SMS_PERMISSION_CODE);
//        } else {
//            Log.e("TAG", "Is sms read 1 ? " + smsIsRead);
//            if(!smsIsRead){
//                if(readSms()){
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putBoolean(SMSREAD,true);
//                    editor.apply();
//                }
//            }
//        }


        if (!trial.equals("active")) {

            if (package_type.contains("Subscription")) {
                Intent intent = new Intent(MainActivity.this, MakeSubscriptionalPayment.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this, ExpiredActivty.class);
                startActivity(intent);
            }
        }


//        requestRuntimePermission();

        if (networkChangeListener.isNetwork(getApplicationContext())) {
            updateTransaction(getApplicationContext());
//            Toast.makeText(this, "Fully updated", Toast.LENGTH_SHORT).show();
        }


        if (isConnected) {
            CheckForAppUpdate();
            getWorth();

            getNotification(email);
        } else {
            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


//        if (trial==string1) {
//
//        }else{
//            finish();
//            Intent intent;
//            if (package_type.equals("Subscription")) {
//
//                intent = new Intent(MainActivity.this, MakeSubscriptionalPayment.class);
//
//            } else {
//
//                intent = new Intent(MainActivity.this, ExpiredActivty.class);
//            }
//            startActivity(intent);
//        }

        NavigationView navigationView = findViewById(R.id.nav_view);

        String username = sharedPreferences.getString(USERNAME, "");
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = (TextView) headerView.findViewById(R.id.nameTvDrawer);
        TextView navUserEmail = (TextView) headerView.findViewById(R.id.emailTvDrawer);
        navUserEmail.setText(email);
        navUserName.setText(username);


        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }


        bottomNavigationView = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, bottomHomeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.home) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, bottomHomeFragment).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                }else if(id == R.id.refer) {

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, referFragment).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }else if(id == R.id.spending) {
//                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, spendingFragment).commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, learningFragment).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }else if(id == R.id.setting) {
//                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                                new SettingsFragment()).commit();
                         getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new AccountFragment()).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                }else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return false;
                }
            }
        });

        imgDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    drawerLayout.openDrawer(navigationView);
                }
            }
        });
        imAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AccountFragment()).commit();

            }
        });


    }

    private void requestReadSMSPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                            != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, PERMISSION_REQUEST_CODE);
//                showPermissionExplanationDialog();
                createReadSMSBottomSheetDialog(permissionCount);

            } else {
//                if (!smsIsRead) {
//                    if (readSms() > 0) {
//                        Intent intent = new Intent(MainActivity.this, TopRecipientsActivity.class);
//                        startActivity(intent);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putBoolean(SMSREAD, true);
//                        editor.apply();
//
//                        Log.e("E/Checking SMS","Read SMS is allowed");

//                    }
//                }
            }
        } else {
            if (!smsIsRead) {
                readSms();
            }

        }
    }

    private boolean readSms() {
        ContentResolver contentResolver = getContentResolver();
        int count = 0;
        Cursor cursor = contentResolver.query(
                Telephony.Sms.CONTENT_URI,
                null,
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {

            do {
                String address = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                String body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY));
                if (address.equals("MPESA")) {
//                smsList.add("Sender: " + address + "\nMessage: " + body);
                    if (smsReceiver.mpesasorting(this, body).equals("outflow")) {
//                        smsList.add("Sender: " + address + "\nMessage: " + body);
                        count++;
                    }

                }
            } while (cursor.moveToNext() && count <= 5);

        }

        if (cursor != null) {
            cursor.close();
        }
//        if (count > 0) {
//            Intent intent = new Intent(MainActivity.this, Read5SMS.class);
//            startActivity(intent);
////            return true;
//        }

        return true;
    }

    private void getAlert() {

        Log.e("TAG", "Total outflow amount " + outflow);
        if (outflow < 1000) {

//            String dt = "2012-01-04";  // Start date
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();


            try {
//            String CurrentDate= "09/24/2018";
//                String FinalDate = "05/03/2024";
                Date date1;
                Date date2;
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

                String CurrentDate = sdf.format(new Date());
                String lastDate = sharedPreferences.getString(CASHFLOW_BUDGET_REMINDER, "01/01/2000");

                c.setTime(sdf.parse(lastDate));

                c.add(Calendar.DATE, 3);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
//                SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
//                FinalDate = sdf.format(c.getTime());

                date1 = sdf.parse(lastDate);
                date2 = sdf.parse(CurrentDate);
                long difference = Math.abs(date1.getTime() - date2.getTime());
                long differenceDates = difference / (24 * 60 * 60 * 1000);
//                if (differenceDates > 3) {
//                    showAlerter(CurrentDate);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString(CASHFLOW_BUDGET_REMINDER, CurrentDate);
//                    editor.apply();
//
//                }
                String dayDifference = Long.toString(differenceDates);
                Log.e("TAG", "The difference between two dates is " + dayDifference + " days");
            } catch (Exception exception) {
                Log.e("TAG", "Unable to find difference");
                exception.printStackTrace();
            }
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.menu, menu);
//        MenuItem menuItem = menu.findItem(R.id.action_search);
//        MenuItem profileMenu = menu.findItem(R.id.profile);
//        MenuItem notificationMenu = menu.findItem(R.id.notification);
//        SearchView searchView = (SearchView) menuItem.getActionView();
//        searchView.setQueryHint("Type here to search");
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//
////                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, cashflowsFragment).commit();
//                return false;
//            }
//        });
//        profileMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new AccountFragment()).commit();
//                return true;
//            }
//        });
//        notificationMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new NotificationFragment()).commit();
//                return true;
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_home){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AccountFragment()).commit();

                }else if(id == R.id.nav_settings){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SettingsFragment()).commit();

                }else if(id == R.id.nav_share){
                try {
                    final String appPackageName = this.getPackageName();
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, appPackageName);
                    String shareMessage = "\nHi, Great app to help make your money work for you!\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }

//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new ShareFragment()).commit();

                }else if(id == R.id.nav_about){

                Intent intent1 = new Intent(this, AboutUs.class);
                this.startActivity(intent1);

                }else if(id == R.id.nav_feedback){

                Intent feedback = new Intent(this, FeedbackActivity.class);
                this.startActivity(feedback);
                }else if(id == R.id.nav_logout){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                finish();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Logged out Successfully", Toast.LENGTH_SHORT).show();

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateTransaction(Context context) {
        // on below line we are creating a
        // database for reading our database.
        DBHandler dbHandler = new DBHandler(context);
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        SmsReceiver updateMpesa = new SmsReceiver();
        String TABLE_NAME = "mpesa_records";

        // on below line we are creating a cursor with query to
        // read data from database.
        Cursor cursorTrans
                = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);


        // moving our cursor to first position.
        if (cursorTrans.moveToFirst()) {
            do {


                updateMpesa.addDataToDatabaseOutflow(context,
                        cursorTrans.getString(1),
                        cursorTrans.getString(3),
                        cursorTrans.getString(2),
                        cursorTrans.getString(4),
                        cursorTrans.getString(5),
                        cursorTrans.getString(6),
                        cursorTrans.getString(7)
                );

                Log.e("TAG", "Data fetched 1" + cursorTrans.getString(1) + " 2 " +
                        cursorTrans.getString(3) + " 3 " +
                        cursorTrans.getString(2) + " 4 " +
                        cursorTrans.getString(4));
//                + AMOUNT_COL + " TEXT,"
//                        + TRANS_CODE_COL + " TEXT,"
//                        + DATE_COL + " TEXT,"
//                        + CASHFLOW_COL + " TEXT)";


            } while (cursorTrans.moveToNext());
        }


        db.execSQL("delete from " + TABLE_NAME);
        db.close();

        cursorTrans.close();
    }

    private void CheckForAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this);

// Returns an intent object that you use to check for an update.

Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            AppUpdateType.FLEXIBLE,
                            // The current activity making the update request.
                            this,
                            // Include a request code to later monitor this update request.
                            MY_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        appUpdateManager.registerListener(listener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Log.w("Kibeti", "Update flow failed! Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }
    }

    InstallStateUpdatedListener listener = state -> {
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            // After the update is downloaded, show a notification
            // and request user confirmation to restart the app.
//            popupSnackbarForCompleteUpdate();

            appUpdateManager.completeUpdate(); // auto restart
        }

    };

    private void getWorth() {

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, "https://mwalimubiashara.com/app/get_worth.php", new com.android.volley.Response.Listener<String>() {
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

                            outflow = object.getInt("outflow");


                        }
                    }

                } catch (Exception e) {

                }
                getAlert();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.

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
    }private void smsNotAllwed() {

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, utils.READ_SMS_NOT_ALLOWED_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
//                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("1")) {
                        Log.e("TAG", "USER Details saved");
                    }

                } catch (Exception e) {

                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.

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

    private void getNotification(String email) {

        String url = "https://mwalimubiashara.com/app/get_notification.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
//                progressBar.setVisibility(View.GONE);
//                dataClassArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {

                        int l = 0;
                        l = jsonArray.length();

                        for (int i = 0; i < l; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String id = object.getString("noti_id");
                            String title = object.getString("noti_title");
                            String msg = object.getString("noti_msg");
                            String intro = object.getString("noti_intro");

//                            createNotification(
//                                    title,
//                                    msg,
//                                    id,
//                                    intro,
//                                    NotificationCompat.PRIORITY_HIGH,
//                                    100
//                            );

//                            dataClass = new DataClass(cat,line,freq,amount);
//                            dataClassArrayList.add(dataClass);

                        }
//                        if(l>0){
//                            linearLayout.setVisibility(View.GONE);
//                        }else{
//                            linearLayout.setVisibility(View.VISIBLE);
//                        }
                    }
//                    myAdapter.notifyDataSetChanged();

                } catch (Exception e) {

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
//                Toast.makeText(this, "Fail to get response ", Toast.LENGTH_SHORT).show();
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
    // Displays the snackbar notification and call to action.

    private void createReadSMSBottomSheetDialog(int permissionCount) {
        if (bottomSheetDialog == null) {
            View v = LayoutInflater.from(this).inflate(R.layout.read_sms_permission_request, null);

            Button btnSave = v.findViewById(R.id.idBtnSave);
            TextView tvNotNow = v.findViewById(R.id.tvNotNow);
            TextView tvExplanation = v.findViewById(R.id.tvExplanation);

            if(permissionCount==1){
                tvExplanation.setText("Track and manage your money automatically from SMS — simple, smart, and secure with Kibeti.");
            }else if(permissionCount==2){
                tvExplanation.setText("All your personal data is secured and encrypted. It is only accessible by you to help you manage your finances.");
            }else if(permissionCount==3){
                tvExplanation.setText("Remind me later – I’ll skip the automatic features in the Kibeti App for now.");
            }

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialog.dismiss();
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
                            SMS_PERMISSION_REQUEST_CODE);
                }
            });

            tvNotNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    permissionCount = ++;

                    bottomSheetDialog.dismiss();
                }
            });



            bottomSheetDialog = new BottomSheetDialog(this);

            bottomSheetDialog.setContentView(v);

            bottomSheetDialog.show();
        } else {
            bottomSheetDialog.show();
        }
    }

    private void createBottomSheetDialog() {
        if (bottomSheetDialog == null) {
            View v = LayoutInflater.from(this).inflate(R.layout.notification_permission_request, null);

            Button btnSave = v.findViewById(R.id.idBtnSave);
            TextView tvNotNow = v.findViewById(R.id.tvNotNow);

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialog.dismiss();
                    requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
                    requestNotificationPermission();
                }
            });

            tvNotNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialog.dismiss();
                }
            });



            bottomSheetDialog = new BottomSheetDialog(this);

            bottomSheetDialog.setContentView(v);

            bottomSheetDialog.show();
        } else {
            bottomSheetDialog.show();
        }
    }
    public void showAlerter(String currentDate) {
        Alerter.create(this)
                .setTitle("Budget your cash outflow")
                .setText("Tap to budget your cashflow according to your actuals")
                .setIcon(
                        R.drawable.logo)
                .setBackgroundColorRes(
                        R.color.primary_green)
                .setDuration(10000)
                .setOnClickListener(
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // do something when
                                // Alerter message was clicked
                                Intent intent = new Intent(MainActivity.this, CashflowBudget.class);
                                startActivity(intent);

//                                finish();
                            }
                        })

                .setOnShowListener(
                        new OnShowAlertListener() {

                            @Override
                            public void onShow() {
                                // do something when
                                // Alerter message shows
                            }
                        })

                .setOnHideListener(
                        new OnHideAlertListener() {

                            @Override
                            public void onHide() {
                                // do something when
                                // Alerter message hides
                            }
                        })
                .show();
    }

    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(
                        findViewById(android.R.id.content),
                        "An update has just been downloaded.",
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("INSTALL", view -> appUpdateManager.completeUpdate());
        snackbar.setActionTextColor(
                getResources().getColor(R.color.primary_orange));
        snackbar.show();
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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
//        this.finishAffinity();
    }

    private void createNotification(String title, String content,
                                    String channedId, String intro, int priorty, int notificationID) {

        Intent intent = new Intent(this, NotifyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Bundle extras = new Bundle();
        extras.putString(NotifyActivity.notify_title, title);
        extras.putString(NotifyActivity.notify_content, content);
        extras.putString(NotifyActivity.notify_intro, intro);
        extras.putString(NotifyActivity.notify_id, channedId);
        intent.putExtras(extras);
        intent.setAction(Intent.ACTION_VIEW);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), notificationID, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getApplicationContext(), channedId)
                        .setSmallIcon(R.drawable.logo)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                        .setAutoCancel(true)
                        .setLights(Color.BLUE, 500, 500)
                        .setVibrate(new long[]{500, 500, 500})
                        .setPriority(priorty)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setContentIntent(pendingIntent)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);


        // Since android Oreo notification channel is needed.
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channedId,
                    channedId,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(channel);
        }
        Notification notification = notificationBuilder.build();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(notificationID, notification);

        playNotificationSound();
    }

    //    @Override
//    public void onBackPressed() {
//        // this method is used to finish the activity
//        // when user enters the correct password
//
//    }
    private boolean hasPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void showOutflowChart(View view) {
        Intent intent = new Intent(this, OutflowChart.class);
        startActivity(intent);
    }

    public void showInflowChart(View view) {
        Intent intent = new Intent(this, OutflowChart.class);
        startActivity(intent);
    }

    public void playNotificationSound() {
        try {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(MainActivity.this, defaultSoundUri);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                // Request permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
            }
//            else {
//                showToast("Notification permission already granted.");
//            }
        } else {
//            showToast("Notification permission not required for this Android version.");
        }
    }


    // New method using ActivityResultLauncher for Android 13+.
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
//                    showToast("Notification permission granted!");
//                } else {
//                    showToast("Notification permission denied!");
                createBottomSheetDialog();
//
                }
            });

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void requestRuntimePermission() {
        if (ActivityCompat.checkSelfPermission(this, PERMISSION_READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
            Log.e("TAG", "Is sms read 2 ? " + smsIsRead);
            if (!smsIsRead) {
                if (readSms()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(SMSREAD, true);
                    editor.apply();
                }
            }

        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_READ_SMS)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Allow Kibeti to send and view SMS messages xxxxxxx")
                    .setTitle("Permission Required")
                    .setCancelable(false)
                    .setPositiveButton("Ok", (dialog, which) -> {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{PERMISSION_READ_SMS},
                                PERMISSION_REQ_CODE);
                        dialog.dismiss();
                    }).setNegativeButton("Cancel", ((dialog, which) -> dialog.dismiss()));
            builder.show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{PERMISSION_READ_SMS},
                    PERMISSION_REQ_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(!smsIsRead){
                    if(readSms()){


//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putBoolean(SMSREAD,true);
//                        editor.apply();
                    }
                }
                Intent intent = new Intent(MainActivity.this, TopRecipientsActivity.class);
                startActivity(intent);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SMSREAD, true);
                editor.apply();

                Log.e("E/Checking SMS","Read SMS is allowed");
//                readSms();
            } else {
//                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                if(permissionCount<3){
//                    showPermissionExplanationDialog();
                    createReadSMSBottomSheetDialog(permissionCount);
                }else{
                    smsNotAllwed();
                }

                permissionCount++;
//                requestNotificationPermission();

            }
        }
    }

    private void showPermissionExplanationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Record your spending automatically")
                .setMessage("Kibeti is designed with you in mind: it automates reporting for your money!")
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
                                SMS_PERMISSION_REQUEST_CODE);
                    }
                })
                .create()
                .show();
//        AddSpendingPopup();
    }

//    private void AddSpendingPopup() {
////        TextView txtclose;
//        Button submitBtn, cancelBtn;
//
//        myDialog.setContentView(R.layout.allow_permission_pop_up);
////        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
//        cancelBtn = (Button) myDialog.findViewById(R.id.idBtnCancel);
//        submitBtn = (Button) myDialog.findViewById(R.id.idBtnReset);
//
////        txtclose.setText("x");
//
//        submitBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                resetting();
//                ActivityCompat.requestPermissions(MainActivity.this,
//                        new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
//                        PERMISSION_REQUEST_CODE);
//                myDialog.dismiss();
//            }
//        });
//
//        cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myDialog.dismiss();
//            }
//        });
//
////        txtclose.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////                myDialog.dismiss();
////            }
////        });
//
//        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        myDialog.show();
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if(requestCode == READ_SMS_PERMISSION_CODE){
//            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//
////                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
//                Log.e("TAG", "Is sms read 3 ? " + smsIsRead);
//                if(!smsIsRead){
//                    if(readSms()){
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putBoolean(SMSREAD,true);
//                        editor.apply();
//                    }
//                }
//
//            }
//            else {
//
////            else if(!ActivityCompat.shouldShowRequestPermissionRationale(this,PERMISSION_READ_SMS)){
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setMessage("Allow Kibeti to send and view SMS messages to give accurate reports")
//                        .setTitle("Permission Required")
//                        .setCancelable(false)
//                        .setNegativeButton("Cancel",((dialog,which)->dialog.dismiss()))
//                        .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                Uri uri = Uri.fromParts("package",getPackageName(),null);
//                                intent.setData(uri);
//                                startActivity(intent);
//                                dialog.dismiss();
//                            }
//                        });
//                builder.show();
//            }
////            else{
////                requestRuntimePermission();
////            }
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                if (!smsIsRead) {
//
//                    readSms();
////                    Intent intent = new Intent(MainActivity.this, Read5SMS.class);
////                    startActivity(intent);
////                        SharedPreferences.Editor editor = sharedPreferences.edit();
////                        editor.putBoolean(SMSREAD, true);
////                        editor.apply();
//
////                    }
//                }
////                readSms();
//            } else {
////                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
//                showPermissionExplanationDialog();
//            }
//        }
//    }
//
//    public void showPermissionExplanationDialog() {
//        new AlertDialog.Builder(this)
//                .setTitle("Record your spending automatically")
//                .setMessage("With your permission, this app can read your SMS message to help you manage your spending.")
//                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        ActivityCompat.requestPermissions(MainActivity.this,
//                                new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
//                                PERMISSION_REQUEST_CODE);
//                    }
//                })
//                .create()
//                .show();
////        AddSpendingPopup();
//    }


//    public int readSms() {
//        ContentResolver contentResolver = getContentResolver();
//        int count = 0;
//        Cursor cursor = contentResolver.query(
//                Telephony.Sms.CONTENT_URI,
//                null,
//                null,
//                null,
//                null);
//
//        if (cursor != null && cursor.moveToFirst()) {
//
//            do {
//                String address = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
//                String body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY));
//                if (address.equals("MPESA")) {
////                smsList.add("Sender: " + address + "\nMessage: " + body);
//                    if (smsReceiver.mpesasorting(this, body).equals("outflow")) {
////                        smsList.add("Sender: " + address + "\nMessage: " + body);
//                        count++;
//                    }
//
//                }
//            } while (cursor.moveToNext() && count <= 5);
//
//        }
//
//        if (cursor != null) {
//            cursor.close();
//        }
//
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean(SMSREAD, true);
//        editor.apply();
//
////        if (count > 0) {
////            Intent intent = new Intent(MainActivity.this, Read5SMS.class);
////            startActivity(intent);
//////            return true;
////        }
//
//        return count;
//    }
}