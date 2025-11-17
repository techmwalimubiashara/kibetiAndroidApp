package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;
import static com.mb.kibeti.LoginActivity.USERNAME;
//import static com.mbbp.businesspartners.LoginActivity.EMAIL;
//import static com.mbbp.businesspartners.LoginActivity.MY_PREFERENCES;
//import static com.mbbp.businesspartners.LoginActivity.USERNAME;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.play.core.appupdate.AppUpdateManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;



public class DashboardAlpha extends AppCompatActivity {
    Cursor cur1, cur2;
    SQLiteDatabase db, db3;
    EditText etJupiter;
    TextView tvIntroductoryBrief;
    String strUsername, strEmailAddress, strSetupStatus, strBusinessStatus;

    PipeLines Genetics = new PipeLines();

    Cursor cur;
    ExpensesCLVA adadpter;
    ListView lvMyEfxpenses;
    DecimalFormat formatter;
    TextView tvCumulativeExpenses;
    ArrayList<FeedbackRowItem> initiwalList;

    String think_big = Genetics.think_big;
    String my_expenses_table = Genetics.my_expenses_table;
    String my_expenses_table_columns = Genetics.my_expenses_table_columns;

    String create_table = Genetics.create_table;
    String select_from = Genetics.select_from;

    String strAppName = Genetics.America;
    String strAppDescription = Genetics.Europe;
    String strUsernamePh = Genetics.Mercury;
    String strEmailAddressPh = Genetics.Venus;
    String strBusinessNamePh = Genetics.Earth;
    String URL_OPERATION_RETRIEVE_TARGETS = Genetics.URL_OPERATION_RETRIEVE_TARGETS;
    String URL_OPERATION_PIPELINES_RETRIEVE = Genetics.URL_OPERATION_PIPELINES_RETRIEVE;
    String strTabColBizNam = Genetics.strTabColBizNam;
    String strTabColExpenseAmount = Genetics.strTabColExpenseAmount;

    String strItemName, strAmount, strMerge;
    String strBusinessName = "";
    String table_in_use = Genetics.table_in_use;
    String table_in_use_column = Genetics.table_in_use_columns;
    private static final int MY_REQUEST_CODE = 100;

    String my_businesses_table = Genetics.my_businesses_table;

    String insert_into = Genetics.insert_into;
    String URL_OPERATION_GET_BUSINESS_UPDATES = Genetics.URL_OPERATION_GET_BUSINESS_UPDATES;
    String my_businesses_table_columns = Genetics.my_businesses_table_columns;
    String strIntroductoryBriefNew = Genetics.strIntroductoryBriefNew;

    String strOrigin = Genetics.strOrigin;
    String strDestinationKeyExpenses = Genetics.strDestinationKeyExpenses;
    String strDestinationRevenueTargets = Genetics.strDestinationRevenueTargets;
    String strDestinationClients = Genetics.strDestinationClients;
    String strDestinationAchievedRevenue = Genetics.strDestinationAchievedRevenue;
    String strDestinationPerformance = Genetics.strDestinationPerformance;

    AlphaAnimation animGenesis, animExodus;
    Button btnMyExpenses, btnMyPipelines;
    SharedPreferences sharedPreferences;
    AppUpdateManager appUpdateManager;
    TextView tvWelcome;
    ProgressDialog pDialog;
    RecyclerView recyclerViewActual, recyclerViewClients;
    private List<MovieModel> movieList = new ArrayList<>();
    private List<MovieModel> clientList = new ArrayList<>();
    private MoviesAdapter mAdapter, clientAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    BPHomeFragment homeFragment = new BPHomeFragment();
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);




        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
//        NavigationView navigationView = findViewById(R.id.nav_view);

//        navigationView.setNavigationItemSelectedListener(this);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        strEmailAddress = sharedPreferences.getString(EMAIL, "");
        strUsername = sharedPreferences.getString(USERNAME, "");


        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);


        strEmailAddress = sharedPreferences.getString(EMAIL, "");
        strUsername = sharedPreferences.getString(USERNAME, "");

//        Toast.makeText(this, "Email: "+strEmailAddress+" " +
//                "\nUsername: "+strUsername, Toast.LENGTH_SHORT).show();


        Intent prevInt = getIntent();
        strBusinessName = prevInt.getStringExtra(strBusinessNamePh);

        getSupportActionBar().setTitle(strBusinessName);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle bundle = new Bundle();
        bundle.putString(strBusinessNamePh, strBusinessName );
        bundle.putString(strUsernamePh, strUsername );
        bundle.putString(strEmailAddressPh, strEmailAddress);


        homeFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
//                R.string.close_nav);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                    new HomeFragment()).commit();
//            navigationView.setCheckedItem(R.id.nav_home);
//        }



//        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refreshLayout);
////        TextView textView = (TextView)findViewById(R.id.tv1);
//
//        // Refresh  the layout
//        swipeRefreshLayout.setOnRefreshListener(
//                new SwipeRefreshLayout.OnRefreshListener() {
//                    @Override
//                    public void onRefresh() {
//
//                        checkConnection();
//
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                }
//        );
//
//
//        SimpleDateFormat sdf = new SimpleDateFormat("ss", Locale.getDefault());
//        String currentSecond = sdf.format(new Date());
//


//
//        recyclerViewActual = findViewById(R.id.recyclerViewActual);
//        recyclerViewClients = findViewById(R.id.recyclerViewClients);
//        mAdapter = new MoviesAdapter(movieList);
//        clientAdapter = new MoviesAdapter(clientList);
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        LinearLayoutManager cLayoutManager = new LinearLayoutManager(getApplicationContext());
//        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        cLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recyclerViewActual.setLayoutManager(mLayoutManager);
//        recyclerViewClients.setLayoutManager(cLayoutManager);
////        recyclerViewClients.setItemAnimator(new DefaultItemAnimator());
////        recyclerViewActual.setItemAnimator(new DefaultItemAnimator());
//        recyclerViewClients.setAdapter(clientAdapter);
//        recyclerViewActual.setAdapter(mAdapter);
//        mAdapter.setOnClickListener(new MoviesAdapter.OnClickListener() {
//            @Override
//            public void onClick(int position, MovieModel model) {
//
//                String xx = movieList.get(position).getGenre().toString();
//                Toast.makeText(DashboardAlpha.this, "Item Clicked "+xx, Toast.LENGTH_SHORT).show();
//            }
//        });
//        clientAdapter.setOnClickListener(new MoviesAdapter.OnClickListener() {
//            @Override
//            public void onClick(int position, MovieModel model) {
//
//                String xx = clientList.get(position).getGenre().toString();
//                Toast.makeText(DashboardAlpha.this, "Iltem Clicked "+xx, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        tvWelcome = findViewById(R.id.tvWelcome);
//        tvIntroductoryBrief = findViewById(R.id.tvIntroductoryBrief);
//        tvIntroductoryBrief.setText(strIntroductoryBriefNew);
//
//        etJupiter = findViewById(R.id.etJupiter);
//        CheckForAppUpdate();
//
//        db = openOrCreateDatabase(think_big, Context.MODE_PRIVATE, null);
//        db3 = openOrCreateDatabase(think_big, Context.MODE_PRIVATE, null);
//
//        db.execSQL(create_table + table_in_use + table_in_use_column);
//        db.execSQL(create_table + my_businesses_table + my_businesses_table_columns);
//
//
//
//        cur1 = db.rawQuery(select_from + " " + table_in_use, null);
//
//        if (cur1.getCount() <= 0) {
//            strSetupStatus = "Incomplete";
//        } else {
//            strSetupStatus = "Complete";
//        }
//
//        cur2 = db.rawQuery(select_from + " " + my_businesses_table, null);
//
//        if (cur2.getCount() <= 0) {
//            strBusinessStatus = "Incomplete";
//            InternetConnectionCheck();
//        } else {
//            while (cur2.moveToNext()) {
//                strBusinessName = cur2.getString(1);
//            }
//
//            strBusinessStatus = "Complete";
//        }
//
//
//
//        btnMyExpenses = findViewById(R.id.btnMyExpenses);
//        btnMyPipelines = findViewById(R.id.btnMyPipelines);
//
//        tvWelcome.setText(strBusinessName);
//        if(!strBusinessName.equals("")){
//            checkConnection();
//        }
//
//
//        animGenesis = new AlphaAnimation(0.0f, 1.0f);
//        animGenesis.setDuration(700);
//        animGenesis.setStartOffset(200);
//
//        animExodus = new AlphaAnimation(0.0f, 1.0f);
//        animExodus.setDuration(700);
//        animExodus.setStartOffset(400);
//
//        if (currentSecond.startsWith("1")) {
//            btnMyExpenses.startAnimation(animGenesis);
//            btnMyPipelines.startAnimation(animExodus);
//
//        } else if (currentSecond.startsWith("2")) {
//            btnMyExpenses.startAnimation(animGenesis);
//            btnMyPipelines.startAnimation(animExodus);
//
//        } else if (currentSecond.startsWith("3")) {
//            btnMyExpenses.startAnimation(animGenesis);
//            btnMyPipelines.startAnimation(animExodus);
//
//        } else if (currentSecond.startsWith("4")) {
//            btnMyExpenses.startAnimation(animGenesis);
//            btnMyPipelines.startAnimation(animExodus);
//
//        } else if (currentSecond.startsWith("5")) {
//            btnMyExpenses.startAnimation(animGenesis);
//            btnMyPipelines.startAnimation(animExodus);
//
//        } else {
//            btnMyExpenses.startAnimation(animGenesis);
//            btnMyPipelines.startAnimation(animExodus);
//
//        }
//    }
    }

    //    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//    }
//    private void checkConnection(){
//        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
//
//        if (isConnected) {
//            //All good
//            pDialog = new ProgressDialog(this);
//            pDialog.setMessage("Retrieving the revenue targets...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
//            operationRetrieveRevenueTargets();
//            operationRetrievePipelines();
//        } else {
//            new AlertDialog.Builder(this)
//                    .setMessage("Internet connection is required to proceed...")
//                    .setCancelable(false)
//                    .setPositiveButton("Check settings",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
//                                    Toast.makeText(DashboardAlpha.this, "Kindly relaunch this App after reconfiguring the internet settings...", Toast.LENGTH_LONG).show();
//                                }
//                            }).create().show();
//        }
//    }
//
////    @Override
////    protected void onResume() {
////        super.onResume(
////        new SwipeRefreshLayout.OnRefreshListener() {
////            @Override
////            public void onRefresh() {
////
////                checkConnection();
////
////                swipeRefreshLayout.setRefreshing(false);
////            }
////        };
////    }
//
//    private void operationRetrieveRevenueTargets() {
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        String strFinalUrl = URL_OPERATION_RETRIEVE_TARGETS;
//
//        StringRequest request = new StringRequest(Request.Method.POST, strFinalUrl, new com.android.volley.Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.e("TAG", "RESPONSE IS " + response);
//                pDialog.dismiss();
//
////                dataClassArrayList.clear();
//                try{
//                    JSONObject jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("success");
//                    String message = jsonObject.getString("message");
//                    Boolean error = jsonObject.getBoolean("error");
//                    JSONArray jsonArray = jsonObject.getJSONArray("data");
//
//
//
//                    if(!error) {
////
//                        int l = 0;
//                        l = jsonArray.length();
//
//                        for (int i = 0; i < l; i++) {
//                            JSONObject object = jsonArray.getJSONObject(i);
//
//
//                            int intCounter = i + 1;
//                            String strCounter = "" + intCounter;
//
////                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            String strTargetDate = object.getString("tb_date");
//                            String strTargetRevenueBeta = object.getString("target_revenue");
//                            String strActualRevenue = object.getString("tb_actual");
//                            String strVarianceAlpha = "(0)" ;
////                            String strVarianceAlpha = object.getString("tb_variance");
//                            String strVarianceReason = object.getString("tb_reason");
//
//
//
//                            String strActual = strActualRevenue;
////                            if (strActualRevenue.equals("")) {
////                                strActual = "0";
////                            } else {
////                                strActual = strActualRevenue;
////                            }
////
//                            String strTargetRevenue = strTargetRevenueBeta;
////                            if (strVarianceAlpha.equals("")) {
////                                strVarianceBeta = strTargetRevenueBeta;
////
////                            } else {
////                                strVarianceBeta = strVarianceAlpha;
////                            }
////
//////                            int intTargetRevenue = Integer.parseInt(strTargetRevenueBeta);
//                            int intActualRevenue = Integer.parseInt(strActual);
////                            int intTargetRevenue = Integer.parseInt(strTargetRevenue);
////                            int intVariance =  intActualRevenue - intTargetRevenue;
////                            intCumulativeTargetRevenue += intTargetRevenue;
////                            intCumulativeActual += intActualRevenue;
////                            intCumulativeVariance = intCumulativeActual - intCumulativeTargetRevenue;
//////                            strTargetRevenue = "Kshs. " + jsonObject.getString("target_revenue") + "/=";
//////                            String strMatthew = formatter.format(intTargetRevenue);
//                            String strActualRevenueFormat = formatter.format(intActualRevenue);
//////                            String strLuke = formatter.format(intVarianceRevenue);
////                            if(intVariance<0){
////                                intVariance = 0-intVariance;
////                                strVarianceAlpha = "("+intVariance+")";
////                            }else{
////                                strVarianceAlpha =  intVariance+"";
////                            }
//
////                            FeedbackRowItem item = new FeedbackRowItem(strCounter, strTargetDate, strTargetRevenueBeta, strActualRevenue,strVarianceAlpha, strVarianceReason);
////                            initialList.add(item);
//
//                            MovieModel movie= new MovieModel(strVarianceAlpha, strActual, strTargetDate);
//                            movieList.add(movie);
//
//                        }
//
//                        mAdapter.notifyDataSetChanged();
////
//////                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                    }else{
//                        pDialog.dismiss();
////                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//
//                    }
//                    Log.e("checking ",message);
//                }catch (Exception e){
//
//                }
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // method to handle errors.
//                pDialog.dismiss();
//                Toast.makeText(getApplicationContext(), "Fail to get response ", Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            public String getBodyContentType() {
//
//                return "application/x-www-form-urlencoded; charset=UTF-8";
//            }
//
//            @Override
//            protected Map<String, String> getParams() {
//
//                Map<String, String> params = new HashMap<String, String>();
//
//                params.put("email", strEmailAddress);
//                params.put("business_name", strBusinessName);
//
////                Log.e("Email ",strEmailAddress);
//
//                return params;
//            }
//        };
//
//        queue.add(request);
//    }
//    private void operationRetrievePipelines() {
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        StringRequest request = new StringRequest(Request.Method.POST, URL_OPERATION_PIPELINES_RETRIEVE, new com.android.volley.Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.e("TAG", "RESPONSE IS " + response);
//                pDialog.dismiss();
//
//                String strClientName,strValueOfBusiness,strApproximateDealDate,strPhoneNumber;
//
////                dataClassArrayList.clear();
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("success");
//                    String message = jsonObject.getString("message");
//                    Boolean error = jsonObject.getBoolean("error");
//                    JSONArray jsonArray = jsonObject.getJSONArray("data");
//
//                    if (!error) {
////
//                        int l = 0;
//                        l = jsonArray.length();
//
//                        for (int i = 0; i < l; i++) {
//                            JSONObject object = jsonArray.getJSONObject(i);
//
//                            int intCounter = i + 1;
//                            String strCounter = "" + intCounter;
//
//                            strClientName = object.getString("client_name");
//                            strValueOfBusiness = object.getString("value_of_business");
//                            strApproximateDealDate = object.getString("deal_date");
//                            strPhoneNumber = object.getString("phone_number");
//
//
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
////                            intCumulativeActual += intActualRevenue;
////                            intCumulativeVariance = intCumulativeActual - intCumulativeTargetRevenue;
////                            strTargetRevenue = "Kshs. " + jsonObject.getString("target_revenue") + "/=";
////                            String strMatthew = formatter.format(intTargetRevenue);
////                            String strMark = formatter.format(intActualRevenue);
//                            String strValueOfBusinessFomartted = formatter.format(intValueOfBusiness);
////                            String strLuke = formatter.format(intVarianceRevenue);
////
////                            FeedbackRowItem item = new FeedbackRowItem(strCounter, strClientName, strValueOfBusinessFomartted, strApproximateDealDate, strPhoneNumber, "");
////
////                            rowItems.add(item);
//
//                            MovieModel movie= new MovieModel(strClientName, strValueOfBusiness, strApproximateDealDate);
//                            clientList.add(movie);
//
//                        }
////                        String strCumulativeActual = formatter.format(intCumulativeActual);
//
////                        tvCumulativeActual.setText(strCumulativeActual);
//
//                        clientAdapter.notifyDataSetChanged();
//
////                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//
//                    } else {
//
//                        pDialog.dismiss();
//
//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//
//                    }
////                    Log.e("checking ",message);
//                } catch (Exception e) {
//                    pDialog.dismiss();
//                }
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // method to handle errors.
//                pDialog.dismiss();
//
//                Toast.makeText(getApplicationContext(), "Fail to get response ", Toast.LENGTH_SHORT).show();
//
//            }
//
//        }) {
//            @Override
//            public String getBodyContentType() {
//
//                return "application/x-www-form-urlencoded; charset=UTF-8";
//            }
//
//            @Override
//            protected Map<String, String> getParams() {
//
//                Map<String, String> params = new HashMap<String, String>();
//
//                params.put("email", strEmailAddress);
//                params.put("business_name", strBusinessName);
//
//                return params;
//            }
//        };
//
//        queue.add(request);
//    }
//
//    public void HowItWorks(View v) {
//        Intent nextInt = new Intent(this, HowItWorks.class);
//        nextInt.putExtra(strUsernamePh, strUsername);
//        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//        startActivity(nextInt);
//    }
//
//    public void NewBusiness(View v) {
//        //Intent nextInt = new Intent(this, NewBusiness.class);
//        Intent nextInt = new Intent(this, MyBusinesses.class);
//        nextInt.putExtra(strUsernamePh, strUsername);
//        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//        startActivity(nextInt);
//    }
//
//    public void MyExpenses (View v) {
//
//        Intent nextInt = new Intent(this, MyExpensesBeta.class);
//        nextInt.putExtra(strOrigin, strDestinationKeyExpenses);
//        nextInt.putExtra(strUsernamePh, strUsername);
//        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//        nextInt.putExtra(strBusinessNamePh, strBusinessName);
//        startActivity(nextInt);
//        startActivity(nextInt);
//    }
//    public void AddExpenses (View v) {
//
//        Intent nextInt = new Intent(this, MyExpensesAlpha.class);
//        nextInt.putExtra(strOrigin, strDestinationKeyExpenses);
//        nextInt.putExtra(strUsernamePh, strUsername);
//        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//        nextInt.putExtra(strBusinessNamePh, strBusinessName);
//        startActivity(nextInt);
//        startActivity(nextInt);
//    }
////    public void MyExpenses(View v) {
////        //Intent nextInt = new Intent(this, MyExpensesAlpha.class);
////        Intent nextInt = new Intent(this, SelectBusiness.class);
////        nextInt.putExtra(strOrigin, strDestinationKeyExpenses);
////        nextInt.putExtra(strUsernamePh, strUsername);
////        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
////        startActivity(nextInt);
////    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        TextView tvItemName = view.findViewById(R.id.tvBeta);
//        TextView tvAmount = view.findViewById(R.id.tvGamma);
//
//        strItemName = tvItemName.getText().toString();
//        strAmount = tvAmount.getText().toString();
//
//        //strMerge = "\n" + strItemName + "\n\n" + strAmount + "\n";
//        strMerge = "\nDelete or edit:\n\nWork in progress\n";
//        //Toast.makeText(this, strMerge, Toast.LENGTH_SHORT).show();
//
//        //Intent nextInt = new Intent(this, TurifayKanana.class);
//        Intent nextInt = new Intent(this, EditExpense.class);
//        nextInt.putExtra(strUsernamePh, strUsername);
//        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//        nextInt.putExtra(strBusinessNamePh, strBusinessName);
//        nextInt.putExtra("1", strItemName);
//        nextInt.putExtra("2", strAmount);
//        startActivity(nextInt);
//        finish();
//    }
//    public void Advanced(View v) {
//        Intent nextInt = new Intent(this, DashboardBeta.class);
//        nextInt.putExtra(strUsernamePh, strUsername);
//        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//        startActivity(nextInt);
//    }
//
//    public void SetRevenueTargets(View v) {
//        if (strBusinessStatus.equals("Complete")) {
//            etJupiter.setText("");
//            Intent nextInt = new Intent(this, RevenueTargetAlternatives.class);
//            nextInt.putExtra(strOrigin, strDestinationRevenueTargets);
//            nextInt.putExtra(strUsernamePh, strUsername);
//            nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//            nextInt.putExtra(strBusinessNamePh, strBusinessName);
//            startActivity(nextInt);
//        } else {
//            Toast.makeText(this, "Please save/ type in a business name using the [My businesses] button", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    public void ViewRevenueTargets(View v) {
//        etJupiter.setText("");
//        if (strSetupStatus.equals("Complete")) {
//            //Intent nextInt = new Intent(this, RevenueTargetView.class);
//            Intent nextInt = new Intent(this, SelectBusiness.class);
//            nextInt.putExtra(strOrigin, strDestinationAchievedRevenue);
//            nextInt.putExtra(strUsernamePh, strUsername);
//            nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//            startActivity(nextInt);
//        } else {
//            Toast.makeText(this, "Please set the revenue targets first", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public void MyClients(View v) {
//        //Intent nextInt = new Intent(this, ClientNew.class);
//        Intent nextInt = new Intent(this, ClientsRetrieve.class);
////        nextInt.putExtra(strOrigin, strDestinationClients);
//        nextInt.putExtra(strUsernamePh, strUsername);
//        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//        nextInt.putExtra(strBusinessNamePh, strBusinessName);
//        startActivity(nextInt);
//    }
//    public void AddClients(View v) {
//        //Intent nextInt = new Intent(this, ClientNew.class);
//        Intent nextInt = new Intent(this, ClientNew.class);
////        nextInt.putExtra(strOrigin, strDestinationClients);
//        nextInt.putExtra(strUsernamePh, strUsername);
//        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//        nextInt.putExtra(strBusinessNamePh, strBusinessName);
//        startActivity(nextInt);
//    }
//    public void TheTabulation (View v) {
//
//        //Toast.makeText(this, "\nWork in progress\n", Toast.LENGTH_SHORT).show();
//        //Toast.makeText(this, "\nWork in progress\n", Toast.LENGTH_SHORT).show();
//        //Toast.makeText(this, "\nWork in progress\n", Toast.LENGTH_SHORT).show();
//
//        etJupiter.setText("");
////        if (strSetupStatus.equals("Complete")) {
//        //Intent nextInt = new Intent(this, Tabulation365.class);
//        Intent nextInt = new Intent(this, Tabulation365.class);
//        nextInt.putExtra(strOrigin, strDestinationPerformance);
//        nextInt.putExtra(strUsernamePh, strUsername);
//        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//        nextInt.putExtra(strBusinessNamePh, strBusinessName);
//        startActivity(nextInt);
////        } else {
////            Toast.makeText(this, "Please set the revenue targets first", Toast.LENGTH_SHORT).show();
////        }
//    }
//
//    public void MyAchievedRevenue(View v) {
//        etJupiter.setText("");
//        if (strSetupStatus.equals("Complete")) {
//            //Intent nextInt = new Intent(this, CalendarDatesBeta.class);
//            Intent nextInt = new Intent(this, CalendarActivity.class);
//            nextInt.putExtra(strOrigin, strDestinationAchievedRevenue);
//            nextInt.putExtra(strUsernamePh, strUsername);
//            nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//            nextInt.putExtra(strBusinessNamePh, strBusinessName);
//            startActivity(nextInt);
//        } else {
//            Toast.makeText(this, "Please set the revenue targets first", Toast.LENGTH_SHORT).show();
//        }
//    }
//
////    public void TheTabulation(View v) {
////        etJupiter.setText("");
////        if (strSetupStatus.equals("Complete")) {
////            //Intent nextInt = new Intent(this, Tabulation365.class);
////            Intent nextInt = new Intent(this, SelectBusiness.class);
////            nextInt.putExtra(strOrigin, strDestinationPerformance);
////            nextInt.putExtra(strUsernamePh, strUsername);
////            nextInt.putExtra(strEmailAddressPh, strEmailAddress);
////            startActivity(nextInt);
////        } else {
////            Toast.makeText(this, "Please set the revenue targets first", Toast.LENGTH_SHORT).show();
////        }
////    }
//
//    public void MyProfile(View v) {
//        etJupiter.setText("");
//        Intent nextInt = new Intent(this, MyProfile.class);
//        nextInt.putExtra(strUsernamePh, strUsername);
//        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//        startActivity(nextInt);
//    }
//
//    public void Settings(View v) {
//        Intent nextInt = new Intent(this, Settings.class);
//        nextInt.putExtra(strUsernamePh, strUsername);
//        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//        startActivity(nextInt);
//    }
//
//    public void Feedback(View v) {
//        Intent nextInt = new Intent(this, Feedback.class);
//        nextInt.putExtra(strUsernamePh, strUsername);
//        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//        startActivity(nextInt);
//    }
//
//    public void KeyNotes(View v) {
//        Intent nextInt = new Intent(this, KeyNotesBeta.class);
//        nextInt.putExtra(strUsernamePh, strUsername);
//        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//        startActivity(nextInt);
//    }
//    private void CheckForAppUpdate() {
//        appUpdateManager = AppUpdateManagerFactory.create(this);
//
//// Returns an intent object that you use to check for an update.
//        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
//
//// Checks that the platform will allow the specified type of update.
//        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                    // This example applies an immediate update. To apply a flexible update
//                    // instead, pass in AppUpdateType.FLEXIBLE
//                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
//                try {
//                    appUpdateManager.startUpdateFlowForResult(
//                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
//                            appUpdateInfo,
//                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
//                            AppUpdateType.FLEXIBLE,
//                            // The current activity making the update request.
//                            this,
//                            // Include a request code to later monitor this update request.
//                            MY_REQUEST_CODE);
//                } catch (IntentSender.SendIntentException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
//        });
//
//        appUpdateManager.registerListener(listener);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == MY_REQUEST_CODE) {
//            if (resultCode != RESULT_OK) {
//                Log.w("Business Partner", "Update flow failed! Result code: " + resultCode);
//                // If the update is cancelled or fails,
//                // you can request to start the update again.
//            }
//        }
//    }
//
//    InstallStateUpdatedListener listener = state -> {
//        if (state.installStatus() == InstallStatus.DOWNLOADED) {
//            // After the update is downloaded, show a notification
//            // and request user confirmation to restart the app.
//            popupSnackbarForCompleteUpdate();
//        }
//
//    };
//
//    private void InternetConnectionCheck(){
//        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
//
//        if (isConnected) {
//            //All good
//            OperationSendDetails();
//        } else {
//            new AlertDialog.Builder(this)
//                    .setMessage("Internet connection is required to proceed...")
//                    .setCancelable(false)
//                    .setPositiveButton("Check settings",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
//                                    Toast.makeText(DashboardAlpha.this, "Kindly relaunch this App after reconfiguring the internet settings...", Toast.LENGTH_SHORT).show();
//                                }
//                            }).create().show();
//        }
//    }
//    private void OperationSendDetails() {
//        pDialog = new ProgressDialog(this);
//        pDialog.setMessage("Working on your account " + strUsername + " ...");
//        pDialog.setIndeterminate(false);
//        pDialog.setCancelable(false);
//        pDialog.show();
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_OPERATION_GET_BUSINESS_UPDATES,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        pDialog.dismiss();
//
//                        //Intent nextInt = new Intent(MainActivity.this, DashboardAlpha.class);
//                        //Intent nextInt = new Intent(MainActivity.this, DashboardAlpha.class);
//                        //Intent nextInt = new Intent(MainActivity.this, DashboardAlpha.class);
//
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String success = jsonObject.getString("success");
//                            String message = jsonObject.getString("message");
//                            Boolean error = jsonObject.getBoolean("error");
//                            JSONArray jsonArray = jsonObject.getJSONArray("data");
//
//                            if(!error) {
////
//                                int l = 0;
//                                l = jsonArray.length();
//
//                                for (int i = 0; i < l; i++) {
//                                    JSONObject object = jsonArray.getJSONObject(i);
//
//
//                                    int intCounter = i + 1;
//                                    String strCounter = "" + intCounter;
//
//                                    String business_name = object.getString("business_name");
////                                    String business_date = object.getString("business_date");
////                                    String ValueOfBusiness = object.getString("ValueOfBusiness");
////                                    String clientName = object.getString("clientName");
////                                    String phoneNumber = object.getString("phoneNumber");
//
////                                    db1.execSQL(insert_into + my_profile_table + "(username, PhoneNumber ,EmailAddress,password) VALUES (" +
////                                            "'" + strUsername + "'," +
////                                            "'" + strPhoneNumber + "'," +
////                                            "'" + strEmailAddress + "',"+
////                                            "'" + strPassword + "');");
//
//                                    db3.execSQL(insert_into + my_businesses_table + " (business_name ,business_date,ValueOfBusiness,clientName ,phoneNumber)VALUES " +
//                                            "('" + business_name + "','','','','');");
//
//                                }
//                            }
//                            else{
//
//
//                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(DashboardAlpha.this, "Something is wrong "+e, Toast.LENGTH_LONG).show();
//                        }
//
//
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
//                Toast.makeText(DashboardAlpha.this, "No response", Toast.LENGTH_SHORT).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams () {
//                Map<String, String> dataToSend = new HashMap<>();
//
//                dataToSend.put("email", strEmailAddress);
//
//                return dataToSend;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }
//
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        finish();
        Intent backInt=new Intent(DashboardAlpha.this, BPMainActivity.class);
        startActivity(backInt);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_alpha_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
//    private void popupSnackbarForCompleteUpdate() {
//        Snackbar snackbar =
//                Snackbar.make(
//                        findViewById(android.R.id.content),
//                        "An update has just been downloaded.",
//                        Snackbar.LENGTH_INDEFINITE);
//        snackbar.setAction("INSTALL", view -> appUpdateManager.completeUpdate());
//        snackbar.setActionTextColor(
//                getResources().getColor(R.color.colorAccent));
//        snackbar.show();
//    }
//
//@Override
//public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//    switch (item.getItemId()) {
//        case R.id.nav_home:
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                    new MyPerformanceFragment()).commit();
//            break;
//
//        case R.id.nav_settings:
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                    new MyExpensesFragment()).commit();
//            break;
//
//        case R.id.nav_share:
//            try {
//                final String appPackageName = this.getPackageName();
//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setType("text/plain");
//                shareIntent.putExtra(Intent.EXTRA_SUBJECT, appPackageName);
//                String shareMessage= "\nHi, Great app to help make your money work for you!\n\n";
//                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
//                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
//                startActivity(Intent.createChooser(shareIntent, "choose one"));
//            } catch(Exception e) {
//                //e.toString();
//            }
//
////                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
////                        new ShareFragment()).commit();
//            break;
//
//        case R.id.nav_about:
//
////            Intent intent1 = new Intent(this, AboutUs.class);
////            this.startActivity(intent1);
//            break;
//
//        case R.id.nav_logout:
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.clear();
//            editor.apply();
//            finish();
//            Intent intent = new Intent(DashboardAlpha.this, LoginActivity.class);
//            startActivity(intent);
//            Toast.makeText(this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
//            break;
//    }
//    drawerLayout.closeDrawer(GravityCompat.START);
//    return true;
//}


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.setTarget) {
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
//        switch (item.getItemId()) {
//
//            case R.id.setTarget:
////                Intent nextInt = new Intent(this, RevenueTargetAlternatives.class);
////                nextInt.putExtra(strOrigin, strDestinationRevenueTargets);
////                nextInt.putExtra(strUsernamePh, strUsername);
////                nextInt.putExtra(strEmailAddressPh, strEmailAddress);
////                nextInt.putExtra(strBusinessNamePh, strBusinessName);
////                startActivity(nextInt);
////                Toast.makeText(this, "Revenue Target Set", Toast.LENGTH_SHORT).show();
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
    }
}