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
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewBusiness extends AppCompatActivity {
    Cursor cur;
    SQLiteDatabase db;
    PipeLines Genetics = new PipeLines();
    String think_big = Genetics.think_big;
    String my_businesses_table = Genetics.my_businesses_table;
    String my_businesses_table_columns = Genetics.my_businesses_table_columns;

    String create_table = Genetics.create_table;
    String insert_into = Genetics.insert_into;
    String select_from = Genetics.select_from;

    String strAppName = Genetics.America;
    String strAppDescription = Genetics.Europe;
    String strErrorToast = Genetics.strErrorToast;
    String strUsernamePh = Genetics.Mercury;
    String strEmailAddressPh = Genetics.Venus;

    String URL_OPERATION_REGISTER_BUSINESS = Genetics.URL_OPERATION_REGISTER_BUSINESS;
    EditText etBusinessName;
    String strBusinessName, strUsername, strEmailAddress;

    //Passion on steroids
    Spinner spinnerMyBusinesses;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_business);

        Intent prevInt = getIntent();
        strUsername = prevInt.getStringExtra(strUsernamePh);
        strEmailAddress = prevInt.getStringExtra(strEmailAddressPh);

        getSupportActionBar().setTitle("Add Business");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etBusinessName = findViewById(R.id.etBusinessName);

        db = openOrCreateDatabase(think_big, Context.MODE_PRIVATE, null);
        db.execSQL(create_table + my_businesses_table + my_businesses_table_columns);

        ArrayList<String> arrayMyBusinesses = new ArrayList<String>();
        arrayMyBusinesses.add("Select business");

        cur = db.rawQuery(select_from + " " + my_businesses_table, null);
        while (cur.moveToNext()) {
            strBusinessName = cur.getString(0);
            arrayMyBusinesses.add(strBusinessName);
        }

        //My businesses
        spinnerMyBusinesses = findViewById(R.id.spinnerMyBusinesses);
        ArrayAdapter arrayAdapterMyBusinesses = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayMyBusinesses);
        spinnerMyBusinesses.setAdapter(arrayAdapterMyBusinesses);
        spinnerMyBusinesses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strBusinessName = "" + parent.getItemAtPosition(position);
                Toast.makeText(NewBusiness.this, strBusinessName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void SaveBusiness(View v) {
        strBusinessName = etBusinessName.getText().toString().trim();

        if (strBusinessName.equals("")) {
            Toast.makeText(this, "Please enter the name of your business", Toast.LENGTH_SHORT).show();
        } else {
            //Detect & prevent duplicates:
            //For detection & prevention of duplicates:
            InternetConnectionCheck();
        }
    }

    private void InternetConnectionCheck() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            //All good
            OperationSendDetails();
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("Internet connection is required to proceed...")
                    .setCancelable(false)
                    .setPositiveButton("Check settings",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivityForResult(new Intent(Settings.ACTION_SETTINGS), 0);
                                    Toast.makeText(NewBusiness.this, "Kindly relaunch this App after reconfiguring the internet settings...", Toast.LENGTH_SHORT).show();
                                }
                            }).create().show();
        }
    }

    private void OperationSendDetails() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Registering " + strBusinessName + " ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_OPERATION_REGISTER_BUSINESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();


                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            //if no error in response
                            if (!obj.getBoolean("error")) {

//                            String username = obj.getString("username");
//                            String email = obj.getString("email");
                                String trial = obj.getString("status");
                                String message = obj.getString("message");


                                if (trial.equals("success")) {
//                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    cur = db.rawQuery(select_from + " " + my_businesses_table + " WHERE business_name = '" + strBusinessName + "'", null);
                                    if (cur.getCount() <= 0) {
                                        db.execSQL(insert_into + my_businesses_table + " (business_name ,business_date,ValueOfBusiness,clientName ,phoneNumber) VALUES ('" + strBusinessName + "','','','','');");

                                        Intent nextInt = new Intent(getApplicationContext(), BPMainActivity.class);
                                        nextInt.putExtra(strUsernamePh, strUsername);
                                        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
                                        startActivity(nextInt);
                                        finish();
                                    } else {
                                        //String strMerge = "\n" + strBusinessName + " has already been saved\n\nPlease register a business with another name\n";
//                                        String strMerge = strBusinessName + " has already been saved, please register a business with a different name";
//                                        Toast.makeText(this, strMerge, Toast.LENGTH_LONG).show();
                                    }

                                    Intent nextInt = new Intent(NewBusiness.this, BPMainActivity.class);
                                    nextInt.putExtra(strUsernamePh, strUsername);
                                    nextInt.putExtra(strEmailAddressPh, strEmailAddress);
                                    startActivity(nextInt);
                                    overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_btm);
                                    finish();

                                } else {
//                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                                startActivity(intent);
//                                ed_email.setError(obj.getString("message"));
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                                }

                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
//                                ed_email.setError(obj.getString("emailErro"));
//                                ed_password.setError(obj.getString("passErro"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(NewBusiness.this, "Something is wrong " + e, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(NewBusiness.this, strErrorToast, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> dataToSend = new HashMap<>();

                dataToSend.put("email", strEmailAddress);
                dataToSend.put("business_name", strBusinessName);
//                dataToSend.put("app", "My Hustle");
//                dataToSend.put("category", strUsername);
                return dataToSend;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
        return;

    }
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
}