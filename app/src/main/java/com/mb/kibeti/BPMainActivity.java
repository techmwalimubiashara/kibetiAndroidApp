package com.mb.kibeti;

//import static com.smg.mh.MinimalistAndroidNotifications.CHANNEL_2_ID;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;
import static com.mb.kibeti.LoginActivity.USERNAME;
//import static com.mbbp.businesspartners.LoginActivity.MY_PREFERENCES;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.widget.Toast;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.InstallStatus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BPMainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {
    Cursor cur1, cur2;
    DecimalFormat formatter;
    SQLiteDatabase db1, db2;
    PipeLines Genetics = new PipeLines();
    String think_big = Genetics.think_big;
    String my_businesses_table = Genetics.my_businesses_table;
    String my_businesses_table_columns = Genetics.my_businesses_table_columns;
    String my_expenses_table = Genetics.my_expenses_table;
    String my_expenses_table_columns = Genetics.my_expenses_table_columns;
    String create_table = Genetics.create_table;
    String select_from = Genetics.select_from;
    String strAppName = Genetics.America;
    String strAppDescription = Genetics.Europe;
    String strUsernamePh = Genetics.Mercury;
    String strEmailAddressPh = Genetics.Venus;
    String strBusinessNamePh = Genetics.Earth;
    String strTabColBizNam = Genetics.strTabColBizNam;
    String strTabColExpenseAmount = Genetics.strTabColExpenseAmount;
    SharedPreferences sharedPreferences;
    CLVAPentagon adapter;
    ListView lvMyBusinesses;
    ArrayList<FeedbackRowItem> initialList, filteredList;
    TextView tvBusinessName, tvKeyExpenses, tvRevenueTargets, tvPipelines, tvAchievedRevenue;
    String strBusinessName, strKeyExpenses, strRevenueTargets, strRevenueTargetAlternative, strPipelines, strAchievedRevenue, strMerge;
    String strUsername, strEmailAddress;
    DrawerLayout drawerLayout;
    ProgressDialog pDialog;
    ArrayList<FeedbackRowItem> rowItems;

    AppUpdateManager appUpdateManager;
//    HomeFragment homeFragment = new HomeFragment();

    private static final int MY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bp_activity_main);

//        Intent prevInt = getIntent();
//        strUsername = prevInt.getStringExtra(strUsernamePh);
//        strEmailAddress = prevInt.getStringExtra(strEmailAddressPh);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        strEmailAddress = sharedPreferences.getString(EMAIL, "");
        strUsername = sharedPreferences.getString(USERNAME, "");

        formatter = new DecimalFormat("#,###,###,###");
        rowItems = new ArrayList<>();

//        getSupportActionBar().setTitle("My businesses");

//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

//        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                    new HomeFragment()).commit();
//            navigationView.setCheckedItem(R.id.nav_home);
        }


        db1 = openOrCreateDatabase(think_big, Context.MODE_PRIVATE, null);
        db2 = openOrCreateDatabase(think_big, Context.MODE_PRIVATE, null);

        db1.execSQL(create_table + my_businesses_table + my_businesses_table_columns);
        db2.execSQL(create_table + my_expenses_table + my_expenses_table_columns);


        adapter = new CLVAPentagon(this, R.layout.list_item_pentagon, rowItems);

        checkConnectionAndRetrieveData();

        lvMyBusinesses = findViewById(R.id.lvMyBusinesses);
        lvMyBusinesses.setAdapter(adapter);

        lvMyBusinesses.setOnItemClickListener(this);
    }

    private void checkConnectionAndRetrieveData() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            //All good
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Retrieving your businesses ");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            operationRetrievePipelines();
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("Internet connection is required to proceed...")
                    .setCancelable(false)
                    .setPositiveButton("Check settings",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                                    Toast.makeText(getApplicationContext(), "Kindly relaunch this App after reconfiguring the internet settings...", Toast.LENGTH_LONG).show();

                                }
                            }).create().show();
        }

    }

    private void operationRetrievePipelines() {

        RequestQueue queue = Volley.newRequestQueue(this);

//        String strFinalUrl = URL_OPERATION_RETRIEVE_TARGETS;

        StringRequest request = new StringRequest(Request.Method.POST, Genetics.URL_ALL_BUSINESSES_RETRIEVE, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                pDialog.dismiss();

//                dataClassArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    Boolean error = jsonObject.getBoolean("error");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (!error) {
//
                        int l = 0;
                        l = jsonArray.length();

                        for (int i = 0; i < l; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

//                            int intCounter = i + 1;
//                            String strCounter = "" + intCounter;

//                            strClientName = object.getString("client_name");
//                            strValueOfBusiness = object.getString("value_of_business");
//                            strApproximateDealDate = object.getString("deal_date");
//                            strPhoneNumber = object.getString("phone_number");
                            String bName = object.getString("business_name");


//                            String strActual = strValueOfBusiness;
//                            if (strValueOfBusiness.equals("")) {
//                                strActual = "0";
//                            } else {
//                                strActual = strValueOfBusiness;
//                            }
//
////
////                            int intTargetRevenue = Integer.parseInt(strTargetRevenueBeta);
//                            int intActualRevenue = Integer.parseInt(strActual);
//                            int intValueOfBusiness = Integer.parseInt(strValueOfBusiness);
////                            int intTargetRevenue = Integer.parseInt(strTargetRevenue);
////                            int intVariance =  intActualRevenue - intTargetRevenue;
////                            intCumulativeTargetRevenue += intTargetRevenue;
//                            intCumulativeActual += intActualRevenue;
////                            intCumulativeVariance = intCumulativeActual - intCumulativeTargetRevenue;
////                            strTargetRevenue = "Kshs. " + jsonObject.getString("target_revenue") + "/=";
////                            String strMatthew = formatter.format(intTargetRevenue);
//                            String strMark = formatter.format(intActualRevenue);
//                            String strValueOfBusinessFomartted = formatter.format(intValueOfBusiness);
////                            String strLuke = formatter.format(intVarianceRevenue);

//                            FeedbackRowItem item = new FeedbackRowItem(strCounter, strClientName, strValueOfBusinessFomartted, strApproximateDealDate, strPhoneNumber, "");

                            FeedbackRowItem item = new FeedbackRowItem(bName, "strKeyExpenses", "", "strPipelines", "strAchievedRevenue", "");

                            rowItems.add(item);

                        }
//                        String strCumulativeActual = formatter.format(intCumulativeActual);
//
//                        tvCumulativeActual.setText(strCumulativeActual);

                        adapter.notifyDataSetChanged();

//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                    } else {

                        pDialog.dismiss();

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                    }
//                    Log.e("checking ",message);
                } catch (Exception e) {

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                pDialog.dismiss();

                System.out.println("VolleyError " + error.getMessage());

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

                params.put("email", strEmailAddress);

                return params;
            }
        };

        queue.add(request);
    }

    public void AddNewBusiness(View v) {
        Intent nextInt = new Intent(this, NewBusiness.class);
        nextInt.putExtra(strUsernamePh, strUsername);
        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
        startActivity(nextInt);
    }

    private ArrayList<FeedbackRowItem> retrieveBusinesses() {
        initialList = new ArrayList<FeedbackRowItem>();
        cur1 = db1.rawQuery(select_from + " " + my_businesses_table, null);

        while (cur1.moveToNext()) {

            String strExpensesTally = "";
            strBusinessName = cur1.getString(1);
            cur2 = db2.rawQuery(select_from + " " + my_expenses_table + " WHERE " + strTabColBizNam + " = '" + strBusinessName + "' AND " + strTabColExpenseAmount + " != '0'", null);

            if (cur1.getCount() <= 0) {
                strKeyExpenses = "Key expenses: ";

            } else {
                strKeyExpenses = "Key expenses: " + strExpensesTally;
            }

            strRevenueTargets = "Revenue targets: " + cur1.getString(2);
            strRevenueTargetAlternative = cur1.getString(3);
            strPipelines = "ClientNew: " + cur1.getString(4);
            strAchievedRevenue = "Achieved revenue: " + cur1.getString(5);

            initialList.add(new FeedbackRowItem(strBusinessName, strKeyExpenses, strRevenueTargets + " [" + strRevenueTargetAlternative + "] ", strPipelines, strAchievedRevenue, ""));
        }
        return initialList;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Log.w("Business Partner", "Update flow failed! Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }
    }

    InstallStateUpdatedListener listener = state -> {
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            // After the update is downloaded, show a notification
            // and request user confirmation to restart the app.
            popupSnackbarForCompleteUpdate();
        }

    };

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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        tvBusinessName = view.findViewById(R.id.tvAlpha);
        tvKeyExpenses = view.findViewById(R.id.tvBeta);
        tvRevenueTargets = view.findViewById(R.id.tvGamma);
        tvPipelines = view.findViewById(R.id.tvDelta);
        tvAchievedRevenue = view.findViewById(R.id.tvEpsilon);

        strBusinessName = tvBusinessName.getText().toString();
        strKeyExpenses = tvKeyExpenses.getText().toString();
        strRevenueTargets = tvRevenueTargets.getText().toString();
        strPipelines = tvPipelines.getText().toString();
        strAchievedRevenue = tvAchievedRevenue.getText().toString();

        strMerge = "\n\n" + strBusinessName + "\n\n" + strKeyExpenses + "\n\n" + strRevenueTargets + "\n\n" + strRevenueTargetAlternative + "\n\n" + strPipelines + "\n\n" + strAchievedRevenue + "\n\n";

        Intent nextInt = new Intent(this, DashboardAlpha.class);
        nextInt.putExtra(strUsernamePh, strUsername);
        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
        nextInt.putExtra(strBusinessNamePh, strBusinessName);
        nextInt.putExtra("strSource", "Business");
        startActivity(nextInt);
//        finish();
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.nav_home:
//                Toast.makeText(this, "Home Item cliacked", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.nav_share:
//                Toast.makeText(this, "Share Item Clicked", Toast.LENGTH_SHORT).show();
//                break;
//            default:
//                Toast.makeText(this, "No Item clicked", Toast.LENGTH_SHORT).show();
//
////             case R.id.nav_home:
////                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
////                        new MyPerformanceFragment()).commit();
////                break;
////
////            case R.id.nav_settings:
////                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
////                        new MyExpensesFragment()).commit();
////                break;
////
////            case R.id.nav_share:
//////                try {
//////                    final String appPackageName = this.getPackageName();
//////                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
//////                    shareIntent.setType("text/plain");
//////                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, appPackageName);
//////                    String shareMessage= "\nHi, Great app to help make your money work for you!\n\n";
//////                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
//////                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
//////                    startActivity(Intent.createChooser(shareIntent, "choose one"));
//////                } catch(Exception e) {
//////                    //e.toString();
//////                }
////                Toast.makeText(this, "Shared Item", Toast.LENGTH_SHORT).show();
////
//////                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//////                        new ShareFragment()).commit();
////                break;
////
////            case R.id.nav_about:
////
//////            Intent intent1 = new Intent(this, AboutUs.class);
//////            this.startActivity(intent1);
////                Toast.makeText(this, "About app", Toast.LENGTH_SHORT).show();
////                break;
////
////            case R.id.nav_logout:
//////                SharedPreferences.Editor editor = sharedPreferences.edit();
//////                editor.clear();
//////                editor.apply();
//////                finish();
//////                Intent intent = new Intent(MAinHomeActivity.this, LoginActivity.class);
//////                startActivity(intent);
////                Toast.makeText(this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
////                break;
////        }
////        drawerLayout.closeDrawer(GravityCompat.START);
//        }
//        return true;
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_beta, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            //case R.id.home:
//            case android.R.id.home:
//                onBackPressed();
//                //super.onBackPressed();
//                return true;
//            case R.id.action_share:
//                Intent share = new Intent(Intent.ACTION_SEND);
//                share.setType("text/plain");
//                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//                share.putExtra(Intent.EXTRA_SUBJECT, strAppName);
//                share.putExtra(Intent.EXTRA_TEXT, strAppDescription + strUsername);
//                startActivity(Intent.createChooser(share, "Share link!"));
//                return true;
//            case R.id.action_home:
//                Intent intHome = new Intent(this, DashboardAlpha.class);
//                intHome.putExtra(strUsernamePh, strUsername);
//                intHome.putExtra(strEmailAddressPh, strEmailAddress);
//                startActivity(intHome);
//                finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

    /// /        switch (item.getItemId()) {
    /// /            case R.id.nav_home:
    /// /                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
    /// /                        new MyPerformanceFragment()).commit();
    /// /                break;
    /// /
    /// /            case R.id.nav_settings:
    /// /                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
    /// /                        new MyExpensesFragment()).commit();
    /// /                break;
    /// /
    /// /            case R.id.nav_share:
    /// ///                try {
    /// ///                    final String appPackageName = this.getPackageName();
    /// ///                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
    /// ///                    shareIntent.setType("text/plain");
    /// ///                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, appPackageName);
    /// ///                    String shareMessage= "\nHi, Great app to help make your money work for you!\n\n";
    /// ///                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
    /// ///                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
    /// ///                    startActivity(Intent.createChooser(shareIntent, "choose one"));
    /// ///                } catch(Exception e) {
    /// ///                    //e.toString();
    /// ///                }
    /// /                Toast.makeText(this, "Shared Item", Toast.LENGTH_SHORT).show();
    /// /
    /// ///                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
    /// ///                        new ShareFragment()).commit();
    /// /                break;
    /// /
    /// /            case R.id.nav_about:
    /// /
    /// ///            Intent intent1 = new Intent(this, AboutUs.class);
    /// ///            this.startActivity(intent1);
    /// /                Toast.makeText(this, "About app", Toast.LENGTH_SHORT).show();
    /// /                break;
    /// /
    /// /            case R.id.nav_logout:
    /// ///                SharedPreferences.Editor editor = sharedPreferences.edit();
    /// ///                editor.clear();
    /// ///                editor.apply();
    /// ///                finish();
    /// ///                Intent intent = new Intent(MAinHomeActivity.this, LoginActivity.class);
    /// ///                startActivity(intent);
    /// /                Toast.makeText(this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
    /// /                break;
    /// /        }
    /// /        drawerLayout.closeDrawer(GravityCompat.START);
//        return false;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            //case R.id.home:
//            case android.R.id.home:
//                onBackPressed();
//                //super.onBackPressed();
//                return true;
//            case R.id.action_share:
//                Intent share = new Intent(Intent.ACTION_SEND);
//                share.setType("text/plain");
//                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//                share.putExtra(Intent.EXTRA_SUBJECT, strAppName);
//                share.putExtra(Intent.EXTRA_TEXT, strAppDescription + strUsername);
//                startActivity(Intent.createChooser(share, "Share link!"));
//                return true;
//            case R.id.logout:
//
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.clear();
//                editor.apply();
//                finish();
//                Intent intent = new Intent(this, LoginActivity.class);
//                startActivity(intent);
//                Toast.makeText(this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
//                return true;
//

//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_alpha, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        switch (item.getItemId()) {

        if (id == R.id.settingReset) {
            //                onBackPressed();
//                Intent intentReset = new Intent(this, MAinHomeActivity.class);
//                startActivity(intentReset);
//                Intent nextInt = new Intent(this, Settings.class);
//                nextInt.putExtra(strUsernamePh, strUsername);
//                nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//                nextInt.putExtra(strBusinessNamePh, strBusinessName);
//                startActivity(nextInt);
//                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.about) {
            //                Intent nextIntAbout = new Intent(this, HowItWorks.class);
//                nextIntAbout.putExtra(strUsernamePh, strUsername);
//                nextIntAbout.putExtra(strEmailAddressPh, strEmailAddress);
//                nextIntAbout.putExtra(strBusinessNamePh, strBusinessName);
//                startActivity(nextIntAbout);
//                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.feedback) {
//                Intent nextIntFb = new Intent(this, Feedback.class);
//                nextIntFb.putExtra(strUsernamePh, strUsername);
//                nextIntFb.putExtra(strEmailAddressPh, strEmailAddress);
//                nextIntFb.putExtra(strBusinessNamePh, strBusinessName);
//                startActivity(nextIntFb);
//                Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.action_share) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            share.putExtra(Intent.EXTRA_SUBJECT, strAppName);
            share.putExtra(Intent.EXTRA_TEXT, strAppDescription + strUsername);
            startActivity(Intent.createChooser(share, "Share link!"));
//                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.logout) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
//                Toast.makeText(this, "Logged out Successfully", Toast.LENGTH_SHORT).show();


        }


        return true;
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

@Override
public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
//
//        case R.id.nav_settings:
////                onBackPressed();
////                Intent intentReset = new Intent(this, MAinHomeActivity.class);
////                startActivity(intentReset);
////                Intent nextInt = new Intent(this, Settings.class);
////                nextInt.putExtra(strUsernamePh, strUsername);
////                nextInt.putExtra(strEmailAddressPh, strEmailAddress);
////                nextInt.putExtra(strBusinessNamePh, strBusinessName);
////                startActivity(nextInt);
//            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
//            break;
//        case R.id.nav_about:
////                Intent nextIntAbout = new Intent(this, HowItWorks.class);
////                nextIntAbout.putExtra(strUsernamePh, strUsername);
////                nextIntAbout.putExtra(strEmailAddressPh, strEmailAddress);
////                nextIntAbout.putExtra(strBusinessNamePh, strBusinessName);
////                startActivity(nextIntAbout);
//            Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
//            break;
////            case R.id.feedback:
//////                Intent nextIntFb = new Intent(this, Feedback.class);
//////                nextIntFb.putExtra(strUsernamePh, strUsername);
//////                nextIntFb.putExtra(strEmailAddressPh, strEmailAddress);
//////                nextIntFb.putExtra(strBusinessNamePh, strBusinessName);
//////                startActivity(nextIntFb);
////                Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
////                break;
//        case R.id.nav_share:
////                Intent share = new Intent(Intent.ACTION_SEND);
////                share.setType("text/plain");
////                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
////                share.putExtra(Intent.EXTRA_SUBJECT, strAppName);
////                share.putExtra(Intent.EXTRA_TEXT, strAppDescription + strUsername);
////                startActivity(Intent.createChooser(share, "Share link!"));
//            Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
//            break;
//        case R.id.nav_logout:
////                SharedPreferences.Editor editor = sharedPreferences.edit();
////                editor.clear();
////                editor.apply();
////                finish();
////                Intent intent = new Intent(this, LoginActivity.class);
////                startActivity(intent);
//            Toast.makeText(this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
//            break;

    }
    drawerLayout.closeDrawer(GravityCompat.START);
    return true;
}
}