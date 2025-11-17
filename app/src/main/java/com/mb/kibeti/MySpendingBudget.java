package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.CURRENCY;
import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mb.kibeti.landingPageTask.ui.PaymentActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MySpendingBudget extends AppCompatActivity {

    ProgressBar progressBar;
//    ListView listView,listViewOutflow;
    MyAdapter myAdapter;
    //    MyAdapterCashflow myAdapterOut;
    DataClass dataClass;
    DataClassOutflow dataClassOut;
    PipeLines utils = new PipeLines();


    TextView totalInflow,totalOutflow;
    TextView amntTv1,amntTv2,amntTv3,amntTv4,amntTv5,amntTv6,amntTv7,amntTv8,amntTv9,amntTv10;
    TextView recomTv1,recomTv2,recomTv3,recomTv4,recomTv5,recomTv6,recomTv7,recomTv8,recomTv9,recomTv10;
    String currency;
    TextView percentTv1,percentTv2,percentTv3,percentTv4,percentTv5,percentTv6,percentTv7,percentTv8,percentTv9,percentTv10;

    ImageView imgArrow1,imgArrow2,imgArrow3,imgArrow4,imgArrow5,imgArrow6,imgArrow7,imgArrow8,imgArrow9,imgArrow10;
    String url = utils.GET_CASHFLOW_DISPLAY;

    String email = "";

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 3000;

    public static ArrayList<DataClass> dataClassArrayList = new ArrayList<>();
    public static ArrayList<DataClassOutflow> dataClassArrayListOutflow = new ArrayList<>();
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_spending_budget);

        // Inflate the layout for this fragment

//        listView =  findViewById(R.id.listview);
//        listViewOutflow =  findViewById(R.id.listviewOut);
        SharedPreferences sharedPreferences =   getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL,"");
        currency = sharedPreferences.getString(CURRENCY,"KES");

        progressBar =  findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        totalInflow =  findViewById(R.id.totalInflow);
        totalOutflow =  findViewById(R.id.totalOutflow);



        amntTv1 =  findViewById(R.id.amntTv1);
        amntTv2 =  findViewById(R.id.amntTv2);
        amntTv3 =  findViewById(R.id.amntTv3);
        amntTv4 =  findViewById(R.id.amntTv4);
        amntTv5 =  findViewById(R.id.amntTv5);
        amntTv6 =  findViewById(R.id.amntTv6);
        amntTv7 =  findViewById(R.id.amntTv7);
        amntTv8 =  findViewById(R.id.amntTv8);
        amntTv9 =  findViewById(R.id.amntTv9);
        amntTv10 =  findViewById(R.id.amntTv10);

        recomTv1 =  findViewById(R.id.recomTv1);
        recomTv2 =  findViewById(R.id.recomTv2);
        recomTv3 =  findViewById(R.id.recomTv3);
        recomTv4 =  findViewById(R.id.recomTv4);
        recomTv5 =  findViewById(R.id.recomTv5);
        recomTv6 =  findViewById(R.id.recomTv6);
        recomTv7 =  findViewById(R.id.recomTv7);
        recomTv8 =  findViewById(R.id.recomTv8);
        recomTv9 =  findViewById(R.id.recomTv9);
        recomTv10 =  findViewById(R.id.recomTv10);

        imgArrow1 =  findViewById(R.id.imgArrow1);
        imgArrow2 =  findViewById(R.id.imgArrow2);
        imgArrow3 =  findViewById(R.id.imgArrow3);
        imgArrow4 =  findViewById(R.id.imgArrow4);
        imgArrow5 =  findViewById(R.id.imgArrow5);
        imgArrow6 =  findViewById(R.id.imgArrow6);
        imgArrow7 =  findViewById(R.id.imgArrow7);
        imgArrow8 =  findViewById(R.id.imgArrow8);
        imgArrow9 =  findViewById(R.id.imgArrow9);
        imgArrow10 =  findViewById(R.id.imgArrow10);

        percentTv1 =  findViewById(R.id.percentTv1);
        percentTv2 =  findViewById(R.id.percentTv2);
        percentTv3 =  findViewById(R.id.percentTv3);
        percentTv4 =  findViewById(R.id.percentTv4);
        percentTv5 =  findViewById(R.id.percentTv5);
        percentTv6 =  findViewById(R.id.percentTv6);
        percentTv7 =  findViewById(R.id.percentTv7);
        percentTv8 =  findViewById(R.id.percentTv8);
        percentTv9 =  findViewById(R.id.percentTv9);
        percentTv10 =  findViewById(R.id.percentTv10);

        myAdapter = new MyAdapter(  this,dataClassArrayList);
//        myAdapterOut = new MyAdapterCashflow(  this,dataClassArrayListOutflow);
//        listView.setAdapter(myAdapter);
//        listViewOutflow.setAdapter(myAdapterOut);


//            handler.postDelayed(runnable = new Runnable() {
//                public void run() {
//                    handler.postDelayed(runnable, delay);
//        getData();
        getCashflow(email,url);
//        getData();
//                }
//            }, delay);

//        if (internet_connection()){
//            // Execute DownloadJSON AsyncTask
////            new DownloadJSON().execute();
//        }else{
//            //create a snackbar telling the user there is no internet connection and issuing a chance to reconnect
//            final Snackbar snackbar = Snackbar.make( findViewById(android.R.id.content),
//                    "No internet connection.",
//                    Snackbar.LENGTH_SHORT);
//            snackbar.setActionTextColor(ContextCompat.getColor(this,
//                    R.color.red));
//            snackbar.setAction(R.string.try_again, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //recheck internet connection and call DownloadJson if there is internet
//                }
//            }).show();
//        }

        ImageView imBack = findViewById(R.id.imBack);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        floatingActionButton= findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( MySpendingBudget.this, CashflowPdfActivity.class);
                intent.putExtra("url", "cashflow_download.php");
                intent.putExtra("activity_name", "Spending Budget Plan Pdf");

                   startActivity(intent);
            }
        });

        Button btnTrackBudget =  findViewById(R.id.btnTrackBudget);
        btnTrackBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MySpendingBudget.this, "Coming Soon!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MySpendingBudget.this, BudgetTracker.class);
                intent.putExtra("from_activity","budget");
                intent.putExtra("next_activity","actuals");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getCashflow(email,url);
    }

//    boolean internet_connection(){
//        //Check if connected to internet, output accordingly
//        ConnectivityManager cm =
//                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        boolean isConnected = activeNetwork != null &&
//                activeNetwork.isConnectedOrConnecting();
//        return isConnected;
//    }

    private void getCashflow(String email,String url) {

        RequestQueue queue = Volley.newRequestQueue(  this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                progressBar.setVisibility(View.GONE);

                dataClassArrayList.clear();
                dataClassArrayListOutflow.clear();
                try{

                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("inflow_success");
                    String success_out = jsonObject.getString("outflow_success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    JSONArray jsonArrayout = jsonObject.getJSONArray("outflow");
                    totalInflow.setText(currency+" "+jsonObject.getString("total_inflow"));
                    totalOutflow.setText(currency+" "+jsonObject.getString("total_outflow"));



                    int l=0;
                    l=jsonArrayout.length();

//                    Log.e("TAG", "Outflow length is " + l);

                    for(int k=0;k<l;k++) {
                        JSONObject objectOut = jsonArrayout.getJSONObject(k);



                        amntTv1.setText(currency+" "+objectOut.getString("amount_1"));
                        amntTv2.setText(currency+" "+objectOut.getString("amount_2"));
                        amntTv3.setText(currency+" "+objectOut.getString("amount_3"));
                        amntTv4.setText(currency+" "+objectOut.getString("amount_4"));
                        amntTv5.setText(currency+" "+objectOut.getString("amount_5"));
                        amntTv6.setText(currency+" "+objectOut.getString("amount_6"));
                        amntTv7.setText(currency+" "+objectOut.getString("amount_7"));
                        amntTv8.setText(currency+" "+objectOut.getString("amount_8"));
                        amntTv9.setText(currency+" "+objectOut.getString("amount_9"));
                        amntTv10.setText(currency+" "+objectOut.getString("amount_10"));

                        percentTv1.setText(objectOut.getString("percent_1"));
                        percentTv2.setText(objectOut.getString("percent_2"));
                        percentTv3.setText(objectOut.getString("percent_3"));
                        percentTv4.setText(objectOut.getString("percent_4"));
                        percentTv5.setText(objectOut.getString("percent_5"));
                        percentTv6.setText(objectOut.getString("percent_6"));
                        percentTv7.setText(objectOut.getString("percent_7"));
                        percentTv8.setText(objectOut.getString("percent_8"));
                        percentTv9.setText(objectOut.getString("percent_9"));
                        percentTv10.setText(objectOut.getString("percent_10"));


                        recomTv1.setText(currency+" "+objectOut.getString("rec_amt_1"));
                        recomTv2.setText(currency+" "+objectOut.getString("rec_amt_2"));
                        recomTv3.setText(currency+" "+objectOut.getString("rec_amt_3"));
                        recomTv4.setText(currency+" "+objectOut.getString("rec_amt_4"));
                        recomTv5.setText(currency+" "+objectOut.getString("rec_amt_5"));
                        recomTv6.setText(currency+" "+objectOut.getString("rec_amt_6"));
                        recomTv7.setText(currency+" "+objectOut.getString("rec_amt_7"));
                        recomTv8.setText(currency+" "+objectOut.getString("rec_amt_8"));
                        recomTv9.setText(currency+" "+objectOut.getString("rec_amt_9"));
                        recomTv10.setText(currency+" "+objectOut.getString("rec_amt_10"));

                        if (objectOut.getString("v_1").equals("l")) {
                            imgArrow1.setImageResource(R.drawable.arrow_up);
                        }
                        if (objectOut.getString("v_2").equals("l")) {
                            imgArrow2.setImageResource(R.drawable.arrow_up);
                        }
                        if (objectOut.getString("v_3").equals("l")) {
                            imgArrow3.setImageResource(R.drawable.arrow_up);
                        }
                        if (objectOut.getString("v_4").equals("l")) {
                            imgArrow4.setImageResource(R.drawable.arrow_up);
                        }
                        if (objectOut.getString("v_5").equals("l")) {
                            imgArrow5.setImageResource(R.drawable.arrow_up);
                        }
                        if (objectOut.getString("v_6").equals("l")) {
                            imgArrow6.setImageResource(R.drawable.arrow_up);
                        }
                        if (objectOut.getString("v_7").equals("l")) {
                            imgArrow7.setImageResource(R.drawable.arrow_up);
                        }
                        if (objectOut.getString("v_8").equals("l")) {
                            imgArrow8.setImageResource(R.drawable.arrow_up);
                        }
                        if (objectOut.getString("v_9").equals("l")) {
                            imgArrow9.setImageResource(R.drawable.arrow_up);
                        }
                        if (objectOut.getString("v_10").equals("l")) {
                            imgArrow10.setImageResource(R.drawable.arrow_up);
                        }

                    }

                    if(jsonObject.getString("total_outflow").equals("KES. 0")){
                        Intent intent = new Intent(MySpendingBudget.this,CashflowBudget.class);
                        startActivity(intent);
                    }
                    Log.e("TAG", "Total outflow " + jsonObject.getString("total_outflow"));
                }catch (Exception e){

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(MySpendingBudget.this, "Fail to get response ", Toast.LENGTH_SHORT).show();
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

    private  void getData() {
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){

                progressBar.setVisibility(View.GONE);

                dataClassArrayList.clear();
                dataClassArrayListOutflow.clear();
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("inflow_success");
                    String success_out = jsonObject.getString("outflow_success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    JSONArray jsonArrayout = jsonObject.getJSONArray("outflow");
                    totalInflow.setText(currency+" "+jsonObject.getString("total_inflow"));
                    totalOutflow.setText(currency+" "+jsonObject.getString("total_outflow"));

                    int l = 0;
                    l = jsonArrayout.length();

                    for (int i = 0; i < l; i++) {
//                        JSONObject object = jsonArray.getJSONObject(i);
                        Toast.makeText(MySpendingBudget.this, "Outflow Length "+i, Toast.LENGTH_SHORT).show();

                    }

                    JSONObject objectOut = jsonArrayout.getJSONObject(1);
                    amntTv1.setText(currency+" "+objectOut.getString("amount_1"));
                    amntTv2.setText(currency+" "+objectOut.getString("amount_2"));
                    amntTv3.setText(currency+" "+objectOut.getString("amount_3"));
                    amntTv4.setText(currency+" "+objectOut.getString("amount_4"));
                    amntTv5.setText(currency+" "+objectOut.getString("amount_5"));
                    amntTv6.setText(currency+" "+objectOut.getString("amount_6"));
                    amntTv7.setText(currency+" "+objectOut.getString("amount_7"));
                    amntTv8.setText(currency+" "+objectOut.getString("amount_8"));
                    amntTv9.setText(currency+" "+objectOut.getString("amount_9"));
                    amntTv10.setText(currency+" "+objectOut.getString("amount_10"));

                    percentTv1.setText(objectOut.getString("percent_1"));
                    percentTv2.setText(objectOut.getString("percent_2"));
                    percentTv3.setText(objectOut.getString("percent_3"));
                    percentTv4.setText(objectOut.getString("percent_4"));
                    percentTv5.setText(objectOut.getString("percent_5"));
                    percentTv6.setText(objectOut.getString("percent_6"));
                    percentTv7.setText(objectOut.getString("percent_7"));
                    percentTv8.setText(objectOut.getString("percent_8"));
                    percentTv9.setText(objectOut.getString("percent_9"));
                    percentTv10.setText(objectOut.getString("percent_10"));


                    recomTv1.setText(currency+" "+objectOut.getString("rec_amt_1"));
                    recomTv2.setText(currency+" "+objectOut.getString("rec_amt_2"));
                    recomTv3.setText(currency+" "+objectOut.getString("rec_amt_3"));
                    recomTv4.setText(currency+" "+objectOut.getString("rec_amt_4"));
                    recomTv5.setText(currency+" "+objectOut.getString("rec_amt_5"));
                    recomTv6.setText(currency+" "+objectOut.getString("rec_amt_6"));
                    recomTv7.setText(currency+" "+objectOut.getString("rec_amt_7"));
                    recomTv8.setText(currency+" "+objectOut.getString("rec_amt_8"));
                    recomTv9.setText(currency+" "+objectOut.getString("rec_amt_9"));
                    recomTv10.setText(currency+" "+objectOut.getString("rec_amt_10"));

                    if(objectOut.getString("v_1").equals("l")){
                        imgArrow1.setImageResource(R.drawable.arrow_up);
                    }
                    if(objectOut.getString("v_2").equals("l")){
                        imgArrow2.setImageResource(R.drawable.arrow_up);
                    }
                    if(objectOut.getString("v_3").equals("l")){
                        imgArrow3.setImageResource(R.drawable.arrow_up);
                    }
                    if(objectOut.getString("v_4").equals("l")){
                        imgArrow4.setImageResource(R.drawable.arrow_up);
                    }
                    if(objectOut.getString("v_5").equals("l")){
                        imgArrow5.setImageResource(R.drawable.arrow_up);
                    }
                    if(objectOut.getString("v_6").equals("l")){
                        imgArrow6.setImageResource(R.drawable.arrow_up);
                    }
                    if(objectOut.getString("v_7").equals("l")){
                        imgArrow7.setImageResource(R.drawable.arrow_up);
                    }
                    if(objectOut.getString("v_8").equals("l")){
                        imgArrow8.setImageResource(R.drawable.arrow_up);
                    }
                    if(objectOut.getString("v_9").equals("l")){
                        imgArrow9.setImageResource(R.drawable.arrow_up);
                    }
                    if(objectOut.getString("v_10").equals("l")){
                        imgArrow10.setImageResource(R.drawable.arrow_up);
                    }
//                    if(success.equals("1")){
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);

                        String cat = object.getString("income_category");
                        String line = "income_line";
                        String freq = object.getString("income_percent");
                        String amount = object.getString("income_amount");
                        String id = object.getString("income_id");

                        dataClass = new DataClass(id,cat, line, amount, freq,"");

                        dataClassArrayList.add(dataClass);
                        myAdapter.notifyDataSetChanged();
                    }

                }catch (Exception e){

                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(MySpendingBudget.this, "Something is going wrong, please check your Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(  this);
        requestQueue.add(request);
    }
}
