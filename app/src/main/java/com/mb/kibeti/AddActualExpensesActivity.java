package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddActualExpensesActivity extends AppCompatActivity {
    Spinner freq1, sortSpn;
    EditText cat, line, amount, valency, freq, date;
    static int budgetAmount = 0;
    int budgetAmountAv = 0;
    int valencyAmount = 0;
    private Button submitBtn, deleteBtn;
    ListView listView;
    ImageView calendar, sortCalendar;
    MyAdapterIncomeHist myAdapter;
    ProgressBar progressBar;
    String email, category, income_line, income_amount, income_frequency, income_id, strDate, freq_string;
    SharedPreferences sharedPreferences;
    DecimalFormat formatter;
    static ArrayList<DataClass> dataClassArrayList = new ArrayList<>();
    DataClass dataClass;
    int actAmount = 0;
    TextView tvBudTotal, tvActTotal, tvVarTotal;
    int budTotal, actTotal, varTotal;
    TextView sortDate;
    LinearLayout layoutNotFound;
    ArrayList<String> dateGot;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        freq = findViewById(R.id.idFrequency);
        freq1 = findViewById(R.id.idFrequencyOld);
        sortSpn = findViewById(R.id.idSpinner);
        sortDate = findViewById(R.id.sortDate);
        cat = findViewById(R.id.idCat);
        line = findViewById(R.id.idLine);
        valency = findViewById(R.id.idValency);
        amount = findViewById(R.id.idAmount);
        date = findViewById(R.id.idDate);
        tvBudTotal = findViewById(R.id.tvBudTotal);
        tvActTotal = findViewById(R.id.tvActTotal);
        tvVarTotal = findViewById(R.id.tvVarTotal);
        submitBtn = findViewById(R.id.idBtnSave);
        listView = findViewById(R.id.lvHistory);
        layoutNotFound = findViewById(R.id.layoutNotFound);
        progressBar = findViewById(R.id.progressbar);
        calendar = findViewById(R.id.calendar);
        sortCalendar = findViewById(R.id.sortCalendar);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL, "");

        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("###,###,###,###");

        dateGot = new ArrayList<String>();
        List<String> categories = new ArrayList<String>();
        categories.add("All");
        categories.add("Date");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpn.setAdapter(dataAdapter);


        String cat1 = "";
        Intent intent = this.getIntent();
        if (intent != null) {

            String s1 = intent.getStringExtra("amount");
            cat1 = intent.getStringExtra("cat");
            String s2 = intent.getStringExtra("line");
            freq_string = intent.getStringExtra("frequency");
//            String valency1 = intent.getStringExtra("frequency");
            income_id = intent.getStringExtra("id");

            Log.e("TAG","Line name "+s2+" line ID "+income_id+" Category "+cat1);
            String no_o = s1.replaceAll(" ", "");
            s1 = no_o.replaceAll("KES", "");
            String no_o2 = s1.replaceAll(",", "");

            no_o = s2.replaceAll(" ", "");
            String no_o1 = no_o.replaceAll("KES", "");
            s2 = no_o1.replaceAll(",", "");

            budgetAmount = Integer.parseInt(s2);

            budgetAmountAv = Integer.parseInt(s2) - Integer.parseInt(no_o2);

            valencyAmount = budgetAmountAv - Integer.parseInt(no_o2);


            cat.setText(cat1);
            line.setText(formatter.format(budgetAmountAv));
//            amount.setText(s1);
            amount.setText(0 + "");
//            valency.setText(valencyAmount + "");
            valency.setText(formatter.format(0));
//            valency.setText(valencyAmount+"");
            freq.setText(freq_string);
//            freq1.setSelection(intent.getIntExtra("frequency"));
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Actual Expense - " + cat1);

        sortSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (position == 0) {
                    getData("all");
                    sortCalendar.setVisibility(View.GONE);
                    sortDate.setVisibility(View.GONE);
                    sortDate.setText("Select Date");
                } else {
                    sortCalendar.setVisibility(View.VISIBLE);
                    sortDate.setVisibility(View.VISIBLE);
                }
//                if (iCurrentSelection != position){
//                    // Your code here
//                }
//                iCurrentSelection = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                amount.clearFocus();

                income_frequency = "XXXX";
                category = cat.getText().toString();
                income_line = line.getText().toString();
                strDate = date.getText().toString();
                income_amount = amount.getText().toString().replaceAll(",", "");
//                income_id = "0";
                // validating the text fields if empty or not.

                if (TextUtils.isEmpty(income_amount) || income_amount.equals("0")) {
                    amount.setError("Please enter amount");
                }
                if (TextUtils.isEmpty(strDate)) {
                    date.setError("Please enter date");
                }
                if (TextUtils.isEmpty(income_frequency)) {
//                    freq1.setError("Please enter Goal Period");
                }
                if (!TextUtils.isEmpty(income_frequency) && !TextUtils.isEmpty(income_amount) && !TextUtils.isEmpty(strDate)) {

                       if(valencyAmount<0){
                           confirm();
                       }else{
                           addDataToDatabase();

//                           Toast.makeText(AddActualExpensesActivity.this, "Outflow Id is "+income_id, Toast.LENGTH_SHORT).show();
                       }


                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        myAdapter = new MyAdapterIncomeHist(AddActualExpensesActivity.this, dataClassArrayList);
        listView.setAdapter(myAdapter);
        listView.setClickable(true);
        getData("all");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(ViewLumpsum.this,ViewLumpsum.class);
//                intent.putExtra("cat", dataClassArrayList.get(i).getCat());
//                intent.putExtra("line",dataClassArrayList.get(i).getLine());
//                intent.putExtra("amount",dataClassArrayList.get(i).getAmount());
//                intent.putExtra("id",dataClassArrayList.get(i).getInflowId());
//                intent.putExtra("frequency",dataClassArrayList.get(i).getFrequency());
//                startActivity(intent);
//                myDialog = new Dialog(getContext());
//                ShowPopup(dataClassArrayList.get(i).getCat(), dataClassArrayList.get(i).getLine(),
//                        dataClassArrayList.get(i).getAmount(), dataClassArrayList.get(i).getFrequency(), dataClassArrayList.get(i).getInflowId());

//                Toast.makeText(ViewLumpsum.this, "User id is:"+dataClassArrayList.get(i).getInflowId(), Toast.LENGTH_SHORT).show();
            }
        });


        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowCalendar(date);
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCalendar(date);
            }
        });
        sortCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        AddActualExpensesActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                String selectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
//                                getDateData(selectedDate);
                                sortDate.setText(selectedDate);
                                getData(selectedDate);

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });

        amount.addTextChangedListener(new TextWatcher() {
            int budgetAmount1 = 0;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

                String originalString = s.toString();


                if (s.length() > 0) {

                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    int number = Integer.parseInt(originalString);
                    valencyAmount = number - budgetAmount1;
                } else {
                    valencyAmount -= budgetAmount1;
                }
//                valency.setText(valencyAmount + "");
                valency.setText(formatter.format(valencyAmount));
            }

            @Override
            public void afterTextChanged(Editable s) {
//                int number = Integer.parseInt(s.toString());
                amount.removeTextChangedListener(this);
//                budgetAmount1 = Integer.parseInt(line.getText().toString());
                budgetAmount1 = budgetAmountAv;

                String amountStr = amount.getText().toString();
                if (!TextUtils.isEmpty(amountStr)) {

                    String originalString = amountStr;

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);


                    String formattedString = formatter.format(longval);

                    amount.setText(formattedString);
                    amount.setSelection(amount.getText().length());

                    int number = Integer.parseInt(originalString);

                    valencyAmount = budgetAmount1 - number;
                } else {
                    valencyAmount = budgetAmount1;
                }
                valency.setText(formatter.format(valencyAmount));

                amount.addTextChangedListener(this);
            }
        });

//        amount.addTextChangedListener(new TextWatcher() {
//            int budgetAmount1=0;
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//
//            }
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//                String originalString = s.toString();
////
////                int tempAmnt=0;
//////                if(actAmount>0){
//////                    tempAmnt = actAmount;
//////                }else{
////                    tempAmnt = budgetAmount1;
//////                }
//                if (s.length() > 0) {
//
//                    if (originalString.contains(",")) {
//                        originalString = originalString.replaceAll(",", "");
//                    }
//                    int number = Integer.parseInt(originalString);
//                    valencyAmount = number - budgetAmount1;
//                } else {
//                    valencyAmount = budgetAmount1;
//                }
////                valency.setText(valencyAmount + "");
//                valency.setText(formatter.format(valencyAmount));
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
////                int number = Integer.parseInt(s.toString());
//                amount.removeTextChangedListener(this);
////                budgetAmount1 = Integer.parseInt(line.getText().toString());
////                int tempAmnt=0;
//                if(actAmount>0){
//                    budgetAmount1 = budgetAmountAv-actAmount;
//                }else{
//                    budgetAmount1 = budgetAmountAv;
//                }
//
//
//                String amountStr = amount.getText().toString();
//                if (!TextUtils.isEmpty(amountStr)) {
//
//                    String originalString = amountStr;
//
//                    Long longval;
//                    if (originalString.contains(",")) {
//                        originalString = originalString.replaceAll(",", "");
//                    }
//                    longval = Long.parseLong(originalString);
//
//
//                    String formattedString = formatter.format(longval);
//
//                    amount.setText(formattedString);
//                    amount.setSelection(amount.getText().length());
//
//                    int number = Integer.parseInt(originalString);
//
//                    valencyAmount =  budgetAmount1 - number;
//                } else {
//                    valencyAmount = budgetAmount1;
//                }
//                valency.setText(formatter.format(valencyAmount));
//                amount.setText("");
//
//                amount.addTextChangedListener(this);
//            }
//
//        });

    }

    public void confirm(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddActualExpensesActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("You have spent more than your budget. Do you want to continue?");

        // Set Alert Title
        builder.setTitle("Over spending");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            addDataToDatabase();
            getData("all");
        });

        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
            progressBar.setVisibility(View.GONE);
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //do whatever you want here
        finish();
        return true;
    }

//    private void popupSnackbar(String msg) {
//        Snackbar snackbar =
//                Snackbar.make(
//                        findViewById(android.R.id.content),
//                        msg,
//                        Snackbar.LENGTH_INDEFINITE);
//        snackbar.setAction("OK", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        snackbar.setActionTextColor(
//                getResources().getColor(R.color.primary_green));
//        snackbar.show();
//    }

    private void addDataToDatabase() {

        // url to post our data
        String url = "https://mwalimubiashara.com/app/add_actual_expenses.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(AddActualExpensesActivity.this);


        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("error");
//                    JSONArray jsonArray =jsonObject.getJSONArray("data");
                    if (!success) {

//                        popupSnackbar("Income Actual has been updated successfully");
                        Toast.makeText(AddActualExpensesActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        finish();
                        line.setText(valency.getText().toString());
                        amount.setText("0");
                        date.setText("");
                        valency.setText("0");
//                        line.setText(formatter.format(budgetAmount));


                    }else{

                        Toast.makeText(AddActualExpensesActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                    //              Toast.makeText(YourGoalActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getData("all");
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AddActualExpensesActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                params.put("actual_amount", income_amount);
                params.put("id", income_id);
                params.put("date", strDate);
                params.put("line",category);

                return params;
            }
        };
        queue.add(request);
//        Toast.makeText(AddActualActivity.this, "Id "+income_id+". Freq "+income_frequency+". Amnt "+income_amount, Toast.LENGTH_SHORT).show();
    }

    private void getData(String crt) {
        String url = "https://mwalimubiashara.com/app/get_actual_expenses_history.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                varTotal = 0;
                budTotal = 0;
                actTotal = 0;

                ArrayList dateList = new ArrayList();
                if (!crt.equals("all")) {
                    budTotal = budgetAmount;
                }
                String display = "";
                progressBar.setVisibility(View.GONE);
                dataClassArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

//                    if(success.equals("1")){
                    int l = 0;
                    l = jsonArray.length();

                    for (int i = 0; i < l; i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String start = object.getString("date");
                        String name = object.getString("name");
                        String end = object.getString("end");
                        String amount = object.getString("amount");
                        String id = object.getString("id");


                        String no_o = amount.replaceAll(" ", "");
                        String no_o1 = no_o.replaceAll("KES", "");
                        String no_o2 = no_o1.replaceAll(",", "");

//                        valencyAmount = budgetAmount - Integer.parseInt(no_o2);

//                        int number = Integer.parseInt(no_o2) - Integer.parseInt(no_o);
                        int actAmt = Integer.parseInt(no_o2);


                        actTotal = actTotal + actAmt;

                        if (crt.equals("all")) {
                            if (!dateList.isEmpty()) {

                                int dateCount = 0;
                                for (int x = 0; x < dateList.size(); x++) {

//                                    String s = (String) dateList.get(x);
                                    String dateDisplayed = dateList.get(x).toString();

                                    if(freq_string.equals("Daily")){
                                        if (dateDisplayed.equals(start)) {
                                            dateCount++;
                                        }
                                    }
                                    if(freq_string.equals("Monthly")){
//                                        Log.e("Tag","Date is "+dateDisplayed+" Month is "+findDateMonth(dateDisplayed)+" Frequency is "+freq_string+" Week is "+findDateWeek(dateDisplayed));

                                        if (findDateMonth(start).equals(findDateMonth(dateDisplayed))) {
                                            dateCount++;
                                        }
                                    }
                                    if(freq_string.equals("Weekly")){

                                        if (findDateWeek(start)==findDateWeek(dateDisplayed)) {
                                            dateCount++;
                                        }
                                    }

                                }
                                if(dateCount==0){
                                    dateList.add(start);
                                }
//                                if () {
//
//                                }
                            } else {
                                dateList.add(start);
                            }
//                            budTotal = budTotal + budgetAmount;

//                            number = budgetAmount - actAmt;
                            budTotal = dateList.size()*budgetAmount;
                        }

//                        else{
//
////                            number = budTotal - actAmt;
////                            budTotal = budTotal + budgetAmount;
//                        }
//                        varTotal = varTotal + number;

                        dataClass = new DataClass(id, name, start, amount, end, "");

//                            dataClass = new DataClass(id,name,age,gender);
                        dataClassArrayList.add(dataClass);

                    }
//                    if(l>0){
//                        linearLayout.setVisibility(View.GONE);
//                    }else{
//                        linearLayout.setVisibility(View.VISIBLE);
//                    }
//                    }
                    myAdapter.notifyDataSetChanged();

                } catch (Exception e) {

                }

                tvVarTotal.setText("" + formatter.format(budTotal - actTotal));
                tvBudTotal.setText("" + formatter.format(budTotal));
                tvActTotal.setText("" + formatter.format(actTotal));

                if (dataClassArrayList.isEmpty()) {
                    layoutNotFound.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                } else {
                    layoutNotFound.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getCont, error.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(AddActualExpensesActivity.this, "Something is wrong. Please check your internet connection", Toast.LENGTH_SHORT).show();
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
                params.put("income_id", income_id);
                params.put("sort", crt);
                params.put("frequency", freq_string);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AddActualExpensesActivity.this);
        requestQueue.add(request);
    }

    public static String findDateMonth(String date)
    {
        // Splitting the given date by '-'
        String dateParts[] = date.split("-");

        String day = dateParts[0];
        String month = dateParts[1];
        String year = dateParts[2];

        return month;
    }

    public static int getWeekNumberFromDate(String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        // Determine the week of the year
        return date.get(weekFields.weekOfWeekBasedYear());
    }

    public static int findDateWeek(String date)
    {

        String dateParts[] = date.split("-");

        // Getting day, month, and year
        // from date
        int day = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);

        // Printing the day, month, and year

        Calendar sDateCalendar = new GregorianCalendar(year,month,day);
        int week = sDateCalendar.get(Calendar.WEEK_OF_YEAR);

        return week;
    }


    public void ShowCalendar(EditText ed) {
        final Calendar c = Calendar.getInstance();

        // on below line we are getting
        // our day, month and year.
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // on below line we are creating a variable for date picker dialog.
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                // on below line we are passing context.
                AddActualExpensesActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // on below line we are setting date to our text view.
                        String selectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        getDateData(selectedDate);
                        ed.setText(selectedDate);

                    }
                },
                // on below line we are passing year,
                // month and day for selected date in our date picker.
                year, month, day);
        // at last we are calling show to
        // display our date picker dialog.
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void getDateData(String d) {
        String url = "https://mwalimubiashara.com/app/get_dateData.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                progressBar.setVisibility(View.GONE);
//                dataClassArrayList.clear();
                actAmount = 0;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

//                    if(success.equals("1")){
                    int l = 0;
                    l = jsonArray.length();

                    for (int i = 0; i < l; i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        int tempAmnt = object.getInt("amount");
                        if (tempAmnt > 0) {
                            actAmount = tempAmnt;
                            budgetAmountAv = budgetAmount - actAmount;
//                            valency.setText(formatter.format(budgetAmountAv));

                        } else {
                            budgetAmountAv = budgetAmount;

                        }

                        line.setText(formatter.format(budgetAmountAv));
//                        valency.setText(formatter.format(budgetAmountAv));
                        actAmount = 0;
                        amount.setText(formatter.format(actAmount));
                    }


                } catch (Exception e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getCont, error.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(AddActualExpensesActivity.this, "Something is wrong. Please check your internet connection", Toast.LENGTH_SHORT).show();
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
                params.put("income_id", income_id);
                params.put("date", d);
                params.put("frequency", freq_string);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AddActualExpensesActivity.this);
        requestQueue.add(request);
    }
}