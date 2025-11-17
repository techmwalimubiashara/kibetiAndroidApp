package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tapadoo.alerter.Alerter;
import com.tapadoo.alerter.OnHideAlertListener;
import com.tapadoo.alerter.OnShowAlertListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MyGoalsActivity extends AppCompatActivity {
    PipeLines utils = new PipeLines();


    FloatingActionButton floatingActionButton;
    ListView listView;
    SettingGoalAdapter adapter;
    ProgressDialog pDialog;
    ArrayList<SettingGoalRowItem> rowItems;

    String url_get_goal = utils.BASEURL+"/save_plan.php";
    String get_goal_list_URL= utils.GET_GOAL_LIST_URL;
    TextView tvTtlAmount,tvTtlDaily,tvTtlMonthly;
    private String email = "";
    private SharedPreferences sharedPreferences;
    private NetworkChangeListener networkChangeListener;
    private ShowPopup popup;
    private Dialog myDialog;
    private DecimalFormat formatter;
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_goals);

        pDialog = new ProgressDialog(this);
        myDialog = new Dialog(this);
        rowItems = new ArrayList<>();

        popup = new ShowPopup(this,myDialog);

        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("###,###,###,###");
//        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL, "");


        formatter = new DecimalFormat("#,###,###,###");


        listView = findViewById(R.id.list_view);
        tvTtlAmount = findViewById(R.id.tvTtlAmount);
        tvTtlDaily = findViewById(R.id.tvTtlDaily);
        tvTtlMonthly = findViewById(R.id.tvTtlMonthly);
        linearLayout = findViewById(R.id.linearLayout);
//        clients_download_pdf = findViewById(R.id.clientsPDF);



        boolean isConnected = ConnectivityHelper.isConnectedToNetwork(this);

        if (isConnected) {
//            Toast.makeText(this, "Connected to the Internet", Toast.LENGTH_SHORT).show();
            get_goal_list();
        } else {
            Toast.makeText(MyGoalsActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        adapter = new SettingGoalAdapter(getApplicationContext(), R.layout.list_item_tabulation, rowItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
//                TextView tvClientName = findViewById(R.id.tvBeta);
//                TextView tvValueOfBusiness = findViewById(R.id.tvGamma);
//                TextView tvApproximateDealDate = findViewById(R.id.tvDelta);
//                TextView tvPhoneNumber = findViewById(R.id.tvEpsilon);

//String strGoal, strAmount,strDate,strDaily,strMonthly,strWeekly,strSemiAnnually,strAnnually;
                popup.showPopup(rowItems.get(i).getStrGoal(),rowItems.get(i).getStrAmount(),
                        rowItems.get(i).getStrDate(),rowItems.get(i).getStrDaily(),rowItems.get(i).getStrWeekly(),rowItems.get(i).getStrMonthly(),
                        rowItems.get(i).getStrQuarterly(), rowItems.get(i).getStrSemiAnnually(),rowItems.get(i).getStrAnnually());

            }
        });

        floatingActionButton=findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextInt = new Intent(MyGoalsActivity.this, SettingGoals.class);
                startActivity(nextInt);
//                Toast.makeText(getActivity(), "Business "+strBusinessName+"" +
//                        "\nEmail"+strEmailAddress+"\n"+" Username "+strUsername, Toast.LENGTH_SHORT).show();
            }
        });

//        clients_download_pdf.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                exportClientsPDF();
//            }
//        });



    }


    public void showAlerter(String alertTitle, String alertMsg) {
        Alerter.create(this)
                .setTitle(alertTitle)
                .setText(alertMsg)
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
//                                Intent intent = new Intent(MainActivity.this, CashflowBudget.class);
//                                startActivity(intent);

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

    public void setGoal(View view) {
        Intent nextInt = new Intent(MyGoalsActivity.this, SettingGoals.class);
        startActivity(nextInt);
//                Toast.makeText(getActivity(), "Business "+strBusinessName+"" +
//                        "\nEmail"+strEmailAddress+"\n"+" Username "+strUsername, Toast.LENGTH_SHORT).show();
    }
    private void get_goal_list() {
        pDialog.setMessage("Loading please wait ....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, get_goal_list_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
//                progressBar.setVisibility(View.GONE);
                pDialog.dismiss();
                rowItems.clear();
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    Boolean error = jsonObject.getBoolean("error");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    Log.e("TAG", "DATA IS " + jsonArray);

                    if(!error){

                        int l=0;
                        l=jsonArray.length();

                        Log.e("TAG", "DATA  length is " + jsonArray.length());

                        for(int i=0;i<l;i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            String goal_title = object.getString("goal_name");
                            String goal_date = object.getString("goal_date");
                            int goal_amount = object.getInt("goal_amount");
                            int goal_daily = object.getInt("daily_amount");
                            int goal_weekly = goal_daily*7;
                            int goal_monthly = goal_daily*30;
                            int goal_quarterly = goal_daily*92;
                            int goal_semi_annually = goal_daily*183;
                            int goal_annually = goal_daily*365;

                            Log.e("TAG", "Goal Name " + goal_title);

                            SettingGoalRowItem item = new SettingGoalRowItem(goal_title, "" + formatter.format(goal_amount), "" + goal_date
                                    , "" +formatter.format(goal_daily),"" +formatter.format(goal_weekly), "" +formatter.format(goal_monthly),
                                    "" +formatter.format(goal_quarterly),"" +formatter.format(goal_semi_annually),
                                    "" +formatter.format(goal_annually));

                            rowItems.add(item);

                        }
                        if(l>0){
                            linearLayout.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            showAlerter("","Tap on goal to view more details");
                        }else{
                            linearLayout.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);

                            Intent intent = new Intent(MyGoalsActivity.this,SettingGoals.class);
                            startActivity(intent);
                        }
                    }
                    adapter.notifyDataSetChanged();

                }catch (Exception e){

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                pDialog.dismiss();
                Toast.makeText(MyGoalsActivity.this, "Fail to get response ", Toast.LENGTH_SHORT).show();
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