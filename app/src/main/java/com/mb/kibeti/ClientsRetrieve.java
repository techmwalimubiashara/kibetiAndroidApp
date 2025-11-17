package com.mb.kibeti;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClientsRetrieve extends AppCompatActivity {
    Cursor cur;
    SQLiteDatabase db;
    ListView listView;
    TabulationCLVA adapter;
    ProgressDialog pDialog;
    DecimalFormat formatter;
    ArrayList<FeedbackRowItem> rowItems;
    PipeLines Genetics = new PipeLines();
    String think_big = Genetics.think_big;
    String my_profile_table = Genetics.my_profile_table;
    String my_profile_table_columns = Genetics.my_profile_table_columns;
    String strBusinessNamePh = Genetics.Earth;
    String create_table = Genetics.create_table;
    String select_from = Genetics.select_from;
    String strAppName = Genetics.America;
    String strAppDescription = Genetics.Europe;
    String strUsernamePh = Genetics.Mercury;
    String strEmailAddressPh = Genetics.Venus;
    String strErrorToast = Genetics.strErrorToast;

    String URL_OPERATION_PIPELINES_RETRIEVE = Genetics.URL_OPERATION_PIPELINES_RETRIEVE;
    String strClientName, strValueOfBusiness, strApproximateDealDate, strPhoneNumber;
    String strUsername, strEmailAddress, strBusinessName;
    LinearLayout clients_download_pdf;

    TextView tvCumulativeActual;

    int intCumulativeTargetRevenue = 0;
    int intCumulativeActual = 0;
    int intCumulativeVariance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retrieve_pipelines);

        Intent prevInt = getIntent();
        strUsername = prevInt.getStringExtra(strUsernamePh);
        strEmailAddress = prevInt.getStringExtra(strEmailAddressPh);
        strBusinessName = prevInt.getStringExtra(strBusinessNamePh);


        getSupportActionBar().setTitle(strBusinessName + ": My clients");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        formatter = new DecimalFormat("#,###,###,###");


        //openOrCreateDatabase "globally":
        db = openOrCreateDatabase(think_big, Context.MODE_PRIVATE, null);
        db.execSQL(create_table + my_profile_table + my_profile_table_columns);

        cur = db.rawQuery(select_from + " " + my_profile_table, null);
        //cur = db.rawQuery(select_from + " " + my_profile_table + " WHERE `username` != ''", null);

        while (cur.moveToNext()) {
            strUsername = cur.getString(0);
            strEmailAddress = cur.getString(2);
        }

        listView = findViewById(R.id.list_view);
        tvCumulativeActual = findViewById(R.id.tvCumulativeActual);
        clients_download_pdf = findViewById(R.id.clientsPDF);

        //ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            //All good
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Retrieving the clients...");
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
                                    Toast.makeText(ClientsRetrieve.this, "Kindly relaunch this App after reconfiguring the internet settings...", Toast.LENGTH_LONG).show();
                                    //finish();
                                    //finish();
                                    //finish();
                                }
                            }).create().show();
        }
        clients_download_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportClientsPDF();
            }
        });
        rowItems = new ArrayList<>();
        adapter = new TabulationCLVA(this, R.layout.list_item_tabulation, rowItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvClientName = view.findViewById(R.id.tvBeta);
                TextView tvValueOfBusiness = view.findViewById(R.id.tvGamma);
                TextView tvApproximateDealDate = view.findViewById(R.id.tvDelta);
                TextView tvPhoneNumber = view.findViewById(R.id.tvEpsilon);

                strClientName = tvClientName.getText().toString();
                strValueOfBusiness = tvValueOfBusiness.getText().toString();
                strApproximateDealDate = tvApproximateDealDate.getText().toString();
                strPhoneNumber = tvPhoneNumber.getText().toString();


//                Intent nextInt = new Intent(ClientsRetrieve.this, ClientDVC.class);
//                nextInt.putExtra("1", strClientName);
//                nextInt.putExtra("2", strValueOfBusiness);
//                nextInt.putExtra("3", strApproximateDealDate);
//                nextInt.putExtra("4", strPhoneNumber);
//                nextInt.putExtra("5", strUsername);
//                nextInt.putExtra("6", strEmailAddress);
//                nextInt.putExtra("7", strBusinessName);
//                startActivity(nextInt);

            }
        });
    }

    public void AddClient(View v) {
        //Intent nextInt = new Intent(this, ClientsRetrieve.class);
        Intent nextInt = new Intent(this, ClientNew.class);
        nextInt.putExtra(strUsernamePh, strUsername);
        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
        nextInt.putExtra(strBusinessNamePh, strBusinessName);
        startActivity(nextInt);
    }


    private void operationRetrievePipelines() {

        RequestQueue queue = Volley.newRequestQueue(this);

//        String strFinalUrl = URL_OPERATION_RETRIEVE_TARGETS;

        StringRequest request = new StringRequest(Request.Method.POST, URL_OPERATION_PIPELINES_RETRIEVE, new Response.Listener<String>() {
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

                            int intCounter = i + 1;
                            String strCounter = "" + intCounter;

                            strClientName = object.getString("client_name");
                            strValueOfBusiness = object.getString("value_of_business");
                            strApproximateDealDate = object.getString("deal_date");
                            strPhoneNumber = object.getString("phone_number");


                            String strActual = strValueOfBusiness;
                            if (strValueOfBusiness.equals("")) {
                                strActual = "0";
                            } else {
                                strActual = strValueOfBusiness;
                            }

//
//                            int intTargetRevenue = Integer.parseInt(strTargetRevenueBeta);
                            int intActualRevenue = Integer.parseInt(strActual);
                            int intValueOfBusiness = Integer.parseInt(strValueOfBusiness);
//                            int intTargetRevenue = Integer.parseInt(strTargetRevenue);
//                            int intVariance =  intActualRevenue - intTargetRevenue;
//                            intCumulativeTargetRevenue += intTargetRevenue;
                            intCumulativeActual += intActualRevenue;
//                            intCumulativeVariance = intCumulativeActual - intCumulativeTargetRevenue;
//                            strTargetRevenue = "Kshs. " + jsonObject.getString("target_revenue") + "/=";
//                            String strMatthew = formatter.format(intTargetRevenue);
                            String strMark = formatter.format(intActualRevenue);
                            String strValueOfBusinessFomartted = formatter.format(intValueOfBusiness);
//                            String strLuke = formatter.format(intVarianceRevenue);

                            FeedbackRowItem item = new FeedbackRowItem(strCounter, strClientName, strValueOfBusinessFomartted, strApproximateDealDate, strPhoneNumber, "");

                            rowItems.add(item);

                        }
                        String strCumulativeActual = formatter.format(intCumulativeActual);

                        tvCumulativeActual.setText(strCumulativeActual);

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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                pDialog.dismiss();

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
                params.put("business_name", strBusinessName);

                return params;
            }
        };

        queue.add(request);
    }

    private void exportClientsPDF() {
//        Intent nextInt = new Intent(ClientsRetrieve.this, ClientsDownloadPDF.class);
//        nextInt.putExtra(strUsernamePh, strUsername);
//        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//        nextInt.putExtra(strBusinessNamePh, strBusinessName);
//        startActivity(nextInt);
    }
//    public void operationRetrievePipelines() {
//        super.onStart();
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_OPERATION_PIPELINES_RETRIEVE, new Response.Listener<JSONArray>() {
//            public void onResponse(JSONArray jsonArray) {
//                pDialog.dismiss();
//                //Toast.makeText(Tabulation365.this, "AAA", Toast.LENGTH_SHORT).show();
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    try {
//                        int intCounter = i + 1;
//                        String strCounter = "" + intCounter;
//
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        strClientName = jsonObject.getString("client_name");
//                        strValueOfBusiness = jsonObject.getString("value_of_business");
//                        strApproximateDealDate = jsonObject.getString("deal_date");
//                        strPhoneNumber = jsonObject.getString("phone_number");
//                        FeedbackRowItem item = new FeedbackRowItem(strCounter, strClientName, strValueOfBusiness, strApproximateDealDate, strPhoneNumber, "");
//                        rowItems.add(item);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                adapter.notifyDataSetChanged();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Toast.makeText(ClientsRetrieve.this, strErrorToast, Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> dataToSend = new HashMap<>();
//                dataToSend.put("email", strEmailAddress);
//                //dataToSend.put("platform", "Android");
//                //dataToSend.put("device_make", strDeviceMake);
//                return dataToSend;
//            }
//        };
//
//        requestQueue.add(stringRequest);
////        requestQueue.add(jsonArrayRequest);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.client_retrieve_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();

            return true;

        }else if (id == R.id.bookmark) {
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
//        switch (item.getItemId()) {
//            //case R.id.home:
//            case android.R.id.home:
//                onBackPressed();
//                //super.onBackPressed();
//                return true;
//
//            case R.id.bookmark:
////                Intent intHome = new Intent(this, ClientsToday.class);
////                intHome.putExtra(strBusinessNamePh, strBusinessName);
////                intHome.putExtra(strUsernamePh, strUsername);
////                intHome.putExtra(strEmailAddressPh, strEmailAddress);
////                startActivity(intHome);
////                finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//        return true;
    }
}