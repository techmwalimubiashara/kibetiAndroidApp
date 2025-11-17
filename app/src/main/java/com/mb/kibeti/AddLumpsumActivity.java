package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddLumpsumActivity extends AppCompatActivity {

    private EditText lumpsumNameEdt, startDateEdt, amountEdt,endDateEdt;
    private EditText spNameEdt1, spstartDateEdt1, spamountEdt1,spendDateEdt1;
    private EditText spNameEdt2, spstartDateEdt2, spamountEdt2,spendDateEdt2;
    private EditText spNameEdt3, spstartDateEdt3, spamountEdt3,spendDateEdt3;
    String email="";
    SharedPreferences sharedPreferences;
    LinearLayout layouSDate1;
    // creating variable for button
    DecimalFormat formatter;
    ImageView calendar1,calendar2,calendar3,calendar4,calendar5,calendar6,calendar7,calendar8;
    private Button submitCourseBtn;

    // creating a strings for storing our values from edittext fields.
    private String lumpsumName, startDate, amount,endDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lumpsum);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL,"");

        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("###,###,###,###");

        lumpsumNameEdt = findViewById(R.id.idEdtLumpsumName);
        amountEdt = findViewById(R.id.idEdtAmount);
        startDateEdt = findViewById(R.id.idEdtStartDate);
        endDateEdt = findViewById(R.id.idEdtEndDate);
        calendar1 = findViewById(R.id.calendar1);
        calendar2 = findViewById(R.id.calendar2);
        calendar3 = findViewById(R.id.calendar3);
        calendar4 = findViewById(R.id.calendar4);
        calendar5 = findViewById(R.id.calendar5);
        calendar6 = findViewById(R.id.calendar6);
        calendar7 = findViewById(R.id.calendar7);
        calendar8 = findViewById(R.id.calendar8);

        spNameEdt1 = findViewById(R.id.idSpendingOn1);
        spamountEdt1 = findViewById(R.id.idSpendingAmount1);
        spstartDateEdt1 = findViewById(R.id.idSpendingStartDate1);
        spendDateEdt1 = findViewById(R.id.idSpendingEndDate1);

        layouSDate1 = findViewById(R.id.layouSDate1);

        spNameEdt2 = findViewById(R.id.idSpendingOn2);
        spamountEdt2 = findViewById(R.id.idSpendingAmount2);
        spstartDateEdt2 = findViewById(R.id.idSpendingStartDate2);
        spendDateEdt2 = findViewById(R.id.idSpendingEndDate2);

        spNameEdt3 = findViewById(R.id.idSpendingOn3);
        spamountEdt3 = findViewById(R.id.idSpendingAmount3);
        spstartDateEdt3 = findViewById(R.id.idSpendingStartDate3);
        spendDateEdt3 = findViewById(R.id.idSpendingEndDate3);

        submitCourseBtn = findViewById(R.id.idBtnSubmitCourse);


        submitCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getting data from edittext fields.
                lumpsumName = lumpsumNameEdt.getText().toString();
                amount = amountEdt.getText().toString().replaceAll(",", "");
                startDate = startDateEdt.getText().toString();
                endDate = endDateEdt.getText().toString();

                String spName1 = spNameEdt1.getText().toString();
                String spstartDate1 = spstartDateEdt1.getText().toString();
                String spAmount1 = spamountEdt1.getText().toString().replaceAll(",", "");
                String spendDate1 = spendDateEdt1.getText().toString();

                // validating the text fields if empty or not.
                if (TextUtils.isEmpty(lumpsumName)) {
                    lumpsumNameEdt.setError("Lumpsum name is required");
                } if (TextUtils.isEmpty(amount)) {
                    amountEdt.setError("Lumpsum amount is required");
                }  if (TextUtils.isEmpty(startDate)) {
                    startDateEdt.setError("Start date is required");
                } if (TextUtils.isEmpty(endDate)) {
                    endDateEdt.setError("End date is required");
                }
                if(!TextUtils.isEmpty(lumpsumName)&&!TextUtils.isEmpty(amount)&&!TextUtils.isEmpty(startDate)
                        &&!TextUtils.isEmpty(endDate)&&!TextUtils.isEmpty(spName1)&&!TextUtils.isEmpty(spAmount1)&&!TextUtils.isEmpty(spstartDate1)
                        &&!TextUtils.isEmpty(spendDate1)){

                    // calling method to add data to Firebase Firestore.
                    addDataToDatabase(lumpsumName, amount, startDate, endDate,spName1,spAmount1,spstartDate1,spendDate1);
                }
            }
        });

        calendar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowCalendar(startDateEdt);
            }
        });
        calendar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowCalendar(endDateEdt);
            }
        });
        calendar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowCalendar(spstartDateEdt1);
            }
        });
        calendar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowCalendar(spendDateEdt1);
            }
        });
        calendar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowCalendar(spstartDateEdt2);
            }
        });
        calendar6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowCalendar(spendDateEdt2);
            }
        });
        calendar7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowCalendar(spstartDateEdt3);
            }
        });
        calendar8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowCalendar(spendDateEdt3);
            }
        });

        spamountEdt1.addTextChangedListener(new TextWatcher() {
            //            int budgetAmount1=0;
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
//                String originalString = s.toString();


//                if (s.length() > 0) {
//
//                    if (originalString.contains(",")) {
//                        originalString = originalString.replaceAll(",", "");
//                    }
////                    int number = Integer.parseInt(originalString);
////                    valencyAmount = budgetAmount1 - number;
//                } else {
////                    valencyAmount -= budgetAmount1;
//                }
////                valency.setText(valencyAmount + "");
////                valency.setText(formatter.format(valencyAmount));
            }

            @Override
            public void afterTextChanged(Editable s) {
//                int number = Integer.parseInt(s.toString());
                spamountEdt1.removeTextChangedListener(this);
//                budgetAmount1 = Integer.parseInt(line.getText().toString());
//                budgetAmount1 = budgetAmount;



                String amountStr = spamountEdt1.getText().toString();
                if (!TextUtils.isEmpty(amountStr)) {

                    String originalString = amountStr;

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);


                    String formattedString = formatter.format(longval);

                    spamountEdt1.setText(formattedString);
                    spamountEdt1.setSelection(spamountEdt1.getText().length());

                    int number = Integer.parseInt(originalString);

//                    valencyAmount =  number - budgetAmount1;
                } else {
//                    valencyAmount = 0 - budgetAmount1;
                }
//                valency.setText(formatter.format(valencyAmount));

                spamountEdt1.addTextChangedListener(this);
            }
//            public void afterTextChanged(Editable s) {
////                int number = Integer.parseInt(s.toString());
//                budgetAmount1 = Integer.parseInt(line.getText().toString());
//                if(!TextUtils.isEmpty(amount.getText().toString())){
//                    int number = Integer.parseInt(amount.getText().toString());
//
//
//                    valencyAmount =  budgetAmount1 - number;
//                }
//                else{
//                    valencyAmount = budgetAmount1;
//                }
//                valency.setText(valencyAmount+"");
//            }
        });
        amountEdt.addTextChangedListener(new TextWatcher() {
            //            int budgetAmount1=0;
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
//                String originalString = s.toString();


//                if (s.length() > 0) {
//
//                    if (originalString.contains(",")) {
//                        originalString = originalString.replaceAll(",", "");
//                    }
////                    int number = Integer.parseInt(originalString);
////                    valencyAmount = budgetAmount1 - number;
//                } else {
////                    valencyAmount -= budgetAmount1;
//                }
////                valency.setText(valencyAmount + "");
////                valency.setText(formatter.format(valencyAmount));
            }

            @Override
            public void afterTextChanged(Editable s) {
//                int number = Integer.parseInt(s.toString());
                amountEdt.removeTextChangedListener(this);
//                budgetAmount1 = Integer.parseInt(line.getText().toString());
//                budgetAmount1 = budgetAmount;



                String amountStr = amountEdt.getText().toString();
                if (!TextUtils.isEmpty(amountStr)) {

                    String originalString = amountStr;

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);


                    String formattedString = formatter.format(longval);

                    amountEdt.setText(formattedString);
                    amountEdt.setSelection(amountEdt.getText().length());

                    int number = Integer.parseInt(originalString);

//                    valencyAmount =  number - budgetAmount1;
                } else {
//                    valencyAmount = 0 - budgetAmount1;
                }
//                valency.setText(formatter.format(valencyAmount));

                amountEdt.addTextChangedListener(this);
            }
//            public void afterTextChanged(Editable s) {
////                int number = Integer.parseInt(s.toString());
//                budgetAmount1 = Integer.parseInt(line.getText().toString());
//                if(!TextUtils.isEmpty(amount.getText().toString())){
//                    int number = Integer.parseInt(amount.getText().toString());
//
//
//                    valencyAmount =  budgetAmount1 - number;
//                }
//                else{
//                    valencyAmount = budgetAmount1;
//                }
//                valency.setText(valencyAmount+"");
//            }
        });

    }
public void ShowCalendar(EditText ed){
    final Calendar c = Calendar.getInstance();

    // on below line we are getting
    // our day, month and year.
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    // on below line we are creating a variable for date picker dialog.
    DatePickerDialog datePickerDialog = new DatePickerDialog(
            // on below line we are passing context.
            AddLumpsumActivity.this,
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    // on below line we are setting date to our text view.
                    ed.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                }
            },
            // on below line we are passing year,
            // month and day for selected date in our date picker.
            year, month, day);
    // at last we are calling show to
    // display our date picker dialog.
    datePickerDialog.show();
}

    public void ShowCalendar(TextView ed){
        final Calendar c = Calendar.getInstance();

        // on below line we are getting
        // our day, month and year.
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // on below line we are creating a variable for date picker dialog.
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                // on below line we are passing context.
                AddLumpsumActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // on below line we are setting date to our text view.
                        ed.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                },
                // on below line we are passing year,
                // month and day for selected date in our date picker.
                year, month, day);
        // at last we are calling show to
        // display our date picker dialog.
        datePickerDialog.show();
    }

    private void addDataToDatabase(String lumpsumName, String amount, String startDate, String endDate,String spName1,String spAmount1,String spstartDate1,String spendDate1) {

        // url to post our data
        String url = "https://mwalimubiashara.com/app/post_lumpsum.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(AddLumpsumActivity.this);


        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // on below line we are displaying a success toast message.
                    Toast.makeText(AddLumpsumActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // and setting data to edit text as empty
                finish();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(AddLumpsumActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                // as we are passing data in the form of url encoded
                // so we are passing the content type below
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("email", email);
                params.put("lumpsumName", lumpsumName);
                params.put("amount", amount);
                params.put("startDate", startDate);
                params.put("endDate", endDate);

                params.put("spName", spName1);
                params.put("spAmount", spAmount1);
                params.put("spStartDate", spstartDate1);
                params.put("spEndDate", spendDate1);

                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}