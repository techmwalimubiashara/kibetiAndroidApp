package com.mb.kibeti;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.switchmaterial.SwitchMaterial;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ClientNew extends AppCompatActivity {
    Cursor cur;
    SQLiteDatabase db;
    DecimalFormat formatter;
    String strPipelineValueFormatted;
    TextView tvIntroductoryBrief;
    EditText etClientName, etPhoneNumber, etValueOfBusiness;
    String strClientName, strPhoneNumber, strValueOfBusiness;

    PipeLines Genetics = new PipeLines();
    String think_big = Genetics.think_big;
    String my_pipelines_table = Genetics.my_pipelines_table;
    String my_pipelines_table_columns = Genetics.my_pipelines_table_columns;

    String create_table = Genetics.create_table;
    String insert_into = Genetics.insert_into;
    String select_from = Genetics.select_from;

    String strMyPipelineAlpha = Genetics.strMyPipelineAlpha;
    String strMyPipelineBeta = Genetics.strMyPipelineBeta;
    String strAppName = Genetics.America;
    String strAppDescription = Genetics.Europe;
    String strUsernamePh = Genetics.Mercury;
    String strEmailAddressPh = Genetics.Venus;
    String strBusinessNamePh = Genetics.Earth;
    String strTabColBizNam = Genetics.strTabColBizNam;
    String strSortingPreferencePh = Genetics.strSortingPreferencePh;
    String strAddContactManually = Genetics.strAddContactManually;
    String strUsername, strEmailAddress, strBusinessName;

    ProgressDialog pDialog;
    String URL_OPERATION_PIPELINE_REGISTER = Genetics.URL_OPERATION_PIPELINE_REGISTER;

    String strContactNewOrExisting = "New";
    Button btnApproximateDealDate, btnViewPipelines;
    TextView tvSetDate;
    TextView tvAddContactManually, tvValueOfBusiness, tvStartDateAlpha, tvStartDateBeta;
    String strDayOfMonth, strMonth, strSelectedDate;

    int intNameIndex, intPhoneIndex;
    static final int RESULT_PICK_CONTACT = 1;
    String strContact, strNumberAlpha, strNumberBeta, strNumberGamma;

    SwitchMaterial toggleBtn;

    LinearLayout hidden_view;

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dtPkDlg = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
            int actualMonth = monthOfYear + 1;
            strDayOfMonth = "" + dayOfMonth;

            //Month string:
            if (actualMonth == 1) {
                strMonth = "Jan";
            } else if (actualMonth == 2) {
                strMonth = "Feb";
            } else if (actualMonth == 3) {
                strMonth = "Mar";
            } else if (actualMonth == 4) {
                strMonth = "Apr";
            } else if (actualMonth == 5) {
                strMonth = "May";
            } else if (actualMonth == 6) {
                strMonth = "Jun";
            } else if (actualMonth == 7) {
                strMonth = "Jul";
            } else if (actualMonth == 8) {
                strMonth = "Aug";
            } else if (actualMonth == 9) {
                strMonth = "Sep";
            } else if (actualMonth == 10) {
                strMonth = "Oct";
            } else if (actualMonth == 11) {
                strMonth = "Nov";
            } else {
                strMonth = "Dec";
            }

            SimpleDateFormat sdf = new SimpleDateFormat("d", Locale.getDefault());
            String strDateToday = sdf.format(new Date());
            int intDateToday = Integer.parseInt(strDateToday);

            if (selectedYear == 2022) {
                if (actualMonth == 12) {
                    if (dayOfMonth < intDateToday) {
                        tvStartDateAlpha.setVisibility(View.VISIBLE);
                        strSelectedDate = "Selected date: " + strDayOfMonth + " " + strMonth + " " + selectedYear + "\n\nPlease select a date from today and beyond\n";
                        btnApproximateDealDate.setText("Approximate deal date");
                        tvStartDateBeta.setVisibility(View.VISIBLE);
                        tvStartDateBeta.setText(strSelectedDate);

                    } else {
                        String strSelectedDate = strDayOfMonth + " " + strMonth + " " + selectedYear;
                        btnApproximateDealDate.setText(strSelectedDate);
                        tvStartDateAlpha.setVisibility(View.GONE);
                        tvStartDateBeta.setVisibility(View.GONE);

                    }
                } else {
                    tvStartDateAlpha.setVisibility(View.VISIBLE);
                    strSelectedDate = "Selected date: " + strDayOfMonth + " " + strMonth + " " + selectedYear + "\n\nPlease select a valid month\n";
                    btnApproximateDealDate.setText("Approximate deal date");
                    tvStartDateBeta.setVisibility(View.VISIBLE);
                    tvStartDateBeta.setText(strSelectedDate);

                }
            } else if (selectedYear == 2025) {
                tvStartDateAlpha.setVisibility(View.VISIBLE);
                strSelectedDate = "Selected date: " + strDayOfMonth + " " + strMonth + " " + selectedYear + "\n\nPlease select a month from the year 2023 or 2024\n";
                btnApproximateDealDate.setText("Approximate deal date");
                tvStartDateBeta.setVisibility(View.VISIBLE);
                tvStartDateBeta.setText(strSelectedDate);

            } else {
                strSelectedDate = strDayOfMonth + " " + strMonth + " " + selectedYear;
                btnApproximateDealDate.setText(strSelectedDate);
                tvSetDate.setText("Deal date"+strSelectedDate);
                tvStartDateAlpha.setVisibility(View.GONE);
                tvStartDateBeta.setVisibility(View.GONE);

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_new);

        formatter = new DecimalFormat("#,###,###,###,###,###,###");

        Intent prevInt = getIntent();
        strUsername = prevInt.getStringExtra(strUsernamePh);
        strEmailAddress = prevInt.getStringExtra(strEmailAddressPh);
        strBusinessName = prevInt.getStringExtra(strBusinessNamePh);

        //Toast.makeText(this, "\n" + URL_OPERATION_PIPELINE_REGISTER + "\n", Toast.LENGTH_SHORT).show();

        //getActionBar().hide();
        //getSupportActionBar().hide();
        getSupportActionBar().setTitle(strBusinessName + ": My client");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvIntroductoryBrief = findViewById(R.id.tvIntroductoryBrief);
        tvIntroductoryBrief.setText(strMyPipelineAlpha + strMyPipelineBeta);
        //tvIntroductoryBrief.setText(strMyPipelineAlpha + "(d/w/m/y)" + strMyPipelineBeta);

        etClientName = findViewById(R.id.etClientName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        hidden_view = findViewById(R.id.hidden_view);

        tvValueOfBusiness = findViewById(R.id.tvValueOfBusiness);
        toggleBtn = findViewById(R.id.toggleBtn);
        etValueOfBusiness = findViewById(R.id.etValueOfBusiness);


        etValueOfBusiness.addTextChangedListener(formatterTextWatcher);

        btnApproximateDealDate = findViewById(R.id.btnApproximateDealDate);
        tvSetDate = findViewById(R.id.tvSetDate);
        tvStartDateAlpha = findViewById(R.id.tvStartDateAlpha);
        tvStartDateBeta = findViewById(R.id.tvStartDateBeta);
        tvAddContactManually = findViewById(R.id.tvAddContactManually);
        btnViewPipelines = findViewById(R.id.btnViewPipelines);

        tvAddContactManually.setText(strAddContactManually);

        if (toggleBtn.isChecked()) {
            // if button is checked we are setting
            // text as Toggle Button is On
//            statusTV.setText("Toggle Button is On");
            hidden_view.setVisibility(View.VISIBLE);
        } else {
            hidden_view.setVisibility(View.GONE);
            // if button is unchecked we are setting
            // text as Toggle Button is Off
//            statusTV.setText("Toggle Button is Off");

        }

        // on below line we are adding click listener for our toggle button
        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are checking if
                // toggle button is checked or not.
                if (toggleBtn.isChecked()) {
                    // on below line we are setting message
                    // for status text view if toggle button is checked.
//                    statusTV.setText("Toggle Button is On");
                    hidden_view.setVisibility(View.VISIBLE);
                } else {
                    // on below line we are setting message for
                    // status text view if toggle button is un checked.
//                    statusTV.setText("Toggle Button is Off");
                    hidden_view.setVisibility(View.GONE);
                }
            }
        });

        //openOrCreateDatabase "globally":
        db = openOrCreateDatabase(think_big, Context.MODE_PRIVATE, null);
        db.execSQL(create_table + my_pipelines_table + my_pipelines_table_columns);

        cur = db.rawQuery(select_from + " " + my_pipelines_table + " WHERE " + strTabColBizNam + " == '" + strBusinessName + "'", null);
        //cur = db.rawQuery(select_from + " " + my_expenses_table + " WHERE priority == '1'", null);
        //cur = db.rawQuery(select_from + " " + my_expenses_table + " WHERE business_name == 'Amazon'", null);

        if (cur.getCount() <= 0) {
            btnViewPipelines.setVisibility(View.GONE);
        } else {
            //Relax
        }
    }

    public void SelectContact(View v) {
        /*
         * On the spot notes:
         * 1. startActivityForResult(intent, 1): Deprecated, also for image picker: R&D: Need to research & upgrade
         * 2. Contact functionality working without the inclusion of teh read contacts permission in the manifest file:
         *       <uses-permission android:name="android.permission.READ_CONTACTS" />
         * 3.
         *
         *
         * */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //Now request for the permission
            //Toast.makeText(this, "\nA\n", Toast.LENGTH_SHORT).show();
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
            Intent intContact = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            startActivityForResult(intContact, RESULT_PICK_CONTACT);
        } else {
            //Toast.makeText(this, "\nB\n", Toast.LENGTH_SHORT).show();
            //For versions lower than Marshmallow [23]
            //To get the phone book
            Intent intContact = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            startActivityForResult(intContact, RESULT_PICK_CONTACT);
        }
    }

    @Override
    //With or without: @Nullable: Works either way
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        //super.onActivityResult(requestCode, resultCode, data);
        //super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
            strContactNewOrExisting = "Existing";
            tvAddContactManually.setVisibility(View.GONE);
        } else {
            //Toast.makeText(this, "Failed to pick contact", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Contact not picked", Toast.LENGTH_SHORT).show();
        }
    }

    private void contactPicked(Intent data) {
        try {
            Uri uri = data.getData();
            cur = getContentResolver().query(uri, null, null, null, null);
            //cur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

            intNameIndex = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            intPhoneIndex = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            cur.moveToFirst();
            strContact = cur.getString(intNameIndex);
            strNumberAlpha = cur.getString(intPhoneIndex);

            if (strNumberAlpha.startsWith("+254")) {
                strNumberBeta = strNumberAlpha.replace("+254", "0");
                strNumberGamma = strNumberBeta.replace(" ", "");

            } else if (strNumberAlpha.startsWith("254")) {
                strNumberBeta = strNumberAlpha.replace("254", "0");
                strNumberGamma = strNumberBeta.replace(" ", "");

            } else {
                strNumberBeta = strNumberAlpha;
                strNumberGamma = strNumberBeta.replace(" ", "");
            }
            etClientName.setText(strContact);
            etPhoneNumber.setText(strNumberGamma);

            etClientName.setEnabled(false);
            etPhoneNumber.setEnabled(false);

            etClientName.setTypeface(null, Typeface.BOLD);
            etPhoneNumber.setTypeface(null, Typeface.BOLD);

            etClientName.setTextColor(Color.parseColor("#F38118"));
            etPhoneNumber.setTextColor(Color.parseColor("#F38118"));
        } catch (Exception e) {
            //???
        }
    }

    TextWatcher formatterTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //Toast.makeText(AlternativeDaily.this, s, Toast.LENGTH_SHORT).show();
            //if (etDaily.equals("")) {
            if (etValueOfBusiness.getText().toString().equals("")) {
                //Relax: No point of trying to attempt to number format nulls
            } else {
                int intCharLen = etValueOfBusiness.getText().toString().length();
                String strCharLen = "" + intCharLen;
                if (strCharLen.equals("19")) {
                    Toast.makeText(ClientNew.this, "Character limit reached", Toast.LENGTH_SHORT).show();
                } else {
                    Long lngDaily = Long.parseLong(s.toString());
                    //Integer intDaily = Integer.parseInt(s.toString());
                    strPipelineValueFormatted = formatter.format(lngDaily);
                    //Toast.makeText(AlternativeDaily.this, strDaily, Toast.LENGTH_SHORT).show();
                    //Causing an infinite loop:
                    //etDaily.setText(strDaily);
                    tvValueOfBusiness.setText(strPipelineValueFormatted);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            //etDaily.setText(s);
            //etDaily.setText(strDaily7);
        }
    };

    public void ApproximateDealDate(View v) {
        final Calendar mCalendar = Calendar.getInstance();

        final DatePickerDialog ddate =  new DatePickerDialog(this, dtPkDlg,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        ddate.getDatePicker().setMinDate(
                mCalendar.getTimeInMillis());
        ddate.show();

    }

    public void ViewClients(View v) {
        //Intent nextInt = new Intent(this, ClientsRetrieve.class);
        Intent nextInt = new Intent(this, ClientsRetrieve.class);
        nextInt.putExtra(strUsernamePh, strUsername);
        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
        nextInt.putExtra(strBusinessNamePh, strBusinessName);
        nextInt.putExtra(strSortingPreferencePh, "1");
        startActivity(nextInt);
    }
    public void SavePipeline(View v) {
        /*Le struggle:
        strClientName = etClientName.getText().toString().toUpperCase().trim();

        String strTempClientName = etClientName.getText().toString().trim();
        StringBuilder sb = new StringBuilder(strTempClientName);
        strClientName = "" + sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));*/

        strClientName = etClientName.getText().toString().trim();
        strValueOfBusiness = etValueOfBusiness.getText().toString().trim();
        strPhoneNumber = etPhoneNumber.getText().toString().trim();
        strSelectedDate = btnApproximateDealDate.getText().toString().trim();

        if (strClientName.equals("")) {
            Toast.makeText(this, "Please enter the client name", Toast.LENGTH_SHORT).show();

        } else if (strValueOfBusiness.equals("")) {
            Toast.makeText(this, "Please enter the value of the anticipated business", Toast.LENGTH_SHORT).show();

        } else if (strPhoneNumber.equals("")) {
            Toast.makeText(this, "Please enter the client's phone number", Toast.LENGTH_SHORT).show();

        } else if (strSelectedDate.equals("Approximate deal date")) {
            Toast.makeText(this, "Please set the approximate deal date", Toast.LENGTH_SHORT).show();

        } else {
            if (strContactNewOrExisting.equals("New")) {
                //Write contact permission: App also works without it
                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                intent.putExtra(ContactsContract.Intents.Insert.NAME, strClientName)
                        .putExtra(ContactsContract.Intents.Insert.PHONE, strPhoneNumber);
                startActivity(intent);
                Toast.makeText(this, "You'll be prompted to save the new contact: " + strClientName + " to your phone book", Toast.LENGTH_LONG).show();
            }

            InternetConnectionCheck();
//            db.execSQL(insert_into + my_pipelines_table + " ( business_name ,business_date ,ValueOfBusiness ,clientName ,phoneNumber)  VALUES (" +
//                    "'" + strBusinessName + "'," +
//                    "'" + strSelectedDate + "'," +
//                    "'" + strValueOfBusiness + "'," +
//                    "'" + strClientName + "'," +
//                    "'" + strPhoneNumber + "');");
//
//            Intent nextInt = new Intent(ClientNew.this, ClientsOffline.class);
//            nextInt.putExtra(strUsernamePh, strUsername);
//            nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//            nextInt.putExtra(strBusinessNamePh, strBusinessName);
//            nextInt.putExtra(strSortingPreferencePh, "1");
//            startActivity(nextInt);
//            overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_btm);
//            finish();
        }
    }

    public void InternetConnectionCheck() {
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
                                    Toast.makeText(ClientNew.this, "\nKindly relaunch this App after reconfiguring the internet settings...\n", Toast.LENGTH_SHORT).show();
                                }
                            }).create().show();
        }
    }

    private void OperationSendDetails() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Saving " + strClientName + "...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_OPERATION_PIPELINE_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        pDialog.dismiss();
//                        Intent nextInt = new Intent(ClientNew.this, ClientsRetrieve.class);
//                        nextInt.putExtra(strUsernamePh, strUsername);
//                        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//                        startActivity(nextInt);
//                        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_btm);
//                        finish();
                        //finish();
                        //finish();
                        pDialog.dismiss();

                        //Intent nextInt = new Intent(MainActivity.this, DashboardAlpha.class);
                        //Intent nextInt = new Intent(MainActivity.this, DashboardAlpha.class);
                        //Intent nextInt = new Intent(MainActivity.this, DashboardAlpha.class);

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

                                    db.execSQL(insert_into + my_pipelines_table + " ( business_name ,business_date ,ValueOfBusiness ,clientName ,phoneNumber)  VALUES (" +
                                            "'" + strBusinessName + "'," +
                                            "'" + strSelectedDate + "'," +
                                            "'" + strValueOfBusiness + "'," +
                                            "'" + strClientName + "'," +
                                            "'" + strPhoneNumber + "');");

                                    Intent nextInt = new Intent(ClientNew.this, ClientsRetrieve.class);
                                    nextInt.putExtra(strUsernamePh, strUsername);
                                    nextInt.putExtra(strEmailAddressPh, strEmailAddress);
                                    nextInt.putExtra(strBusinessNamePh, strBusinessName);
                                    nextInt.putExtra(strSortingPreferencePh, "1");
                                    startActivity(nextInt);
                                    overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_btm);

//                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                                    startActivity(intent);

                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                }
                            } else {
                                pDialog.dismiss();

                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ClientNew.this, "Something is wrong " + e, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(ClientNew.this, "\nOops! An error occurred...\nPlease try again...\n", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> dataToSend = new HashMap<>();
                dataToSend.put("email", strEmailAddress);
                dataToSend.put("client_name", strClientName);
                dataToSend.put("value_of_business", strValueOfBusiness);
                dataToSend.put("phone_number", strPhoneNumber);
                dataToSend.put("deal_date", strSelectedDate);
                dataToSend.put("business_name", strBusinessName);
                //dataToSend.put("platform", "Android");
                //dataToSend.put("device_make", strDeviceMake);
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