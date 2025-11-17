package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.CURRENCY;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class YourGoalActivity extends AppCompatActivity {
    private EditText edtGoalName,edtGoalAmount,edtGoalPeriod;
    private Button submitBtn,cancelBtn;
    PipeLines utils = new PipeLines();
    String GET_YOUR_GOAL_URL = utils.GET_YOUR_GOAL_URL;
    Spinner spGoalName;
    ProgressBar progressBar;
    TextView tvAnnually,tvMonthly,tvWeekly,tvDaily,tvTipsName,tvErrGoalName;
    private String goalName, goalAmount, goalPeriod;
    SharedPreferences sharedPreferences;
    DecimalFormat formatter;
    String currency;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_goal);
//        edtGoalName=findViewById(R.id.idEdtGoalName);
        spGoalName=findViewById(R.id.idEdtGoalName);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES,MODE_PRIVATE);
        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("###,###,###,###");
        currency = sharedPreferences.getString(CURRENCY,"KES");
        List<String> categories = new ArrayList<String>();
        categories.add("Select Goal");
        categories.add("Buy a car");
        categories.add("Buy a house");
        categories.add("Take a Vacation");
        categories.add("Start a business");
        categories.add("Pay college fees");
        categories.add("Clear loan");
        categories.add("Other");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGoalName.setAdapter(dataAdapter);

        edtGoalAmount=findViewById(R.id.idEdtGoalAmount);
        edtGoalPeriod=findViewById(R.id.idEdtGoalPeroid);
        tvErrGoalName=findViewById(R.id.tvErrGoalName);

        tvAnnually = findViewById(R.id.annuallyamountTv);
        tvMonthly = findViewById(R.id.monthlyamountTv);
        tvWeekly = findViewById(R.id.weeklyamountTv);
        tvDaily = findViewById(R.id.dailyamountTv);
        tvTipsName = findViewById(R.id.idTipsName);

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        submitBtn = findViewById(R.id.idBtnSubmit);
        cancelBtn = findViewById(R.id.idBtnCancel);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                // getting data from edittext fields.
//                goalName = edtGoalName.getText().toString();
                goalName = spGoalName.getSelectedItem().toString();
                goalAmount = edtGoalAmount.getText().toString().replaceAll(",", "");
                goalPeriod = edtGoalPeriod.getText().toString();

                // validating the text fields if empty or not.
//                if (TextUtils.isEmpty(goalName)) {
//                    edtGoalName.setError("Please enter Your Goal");
//                }
                if (goalName.equals("Select Goal")){
                    tvErrGoalName.setText("Please select your goal");
                }
                if (TextUtils.isEmpty(goalAmount)) {
                    edtGoalAmount.setError("Please enter Amount");
                } if (TextUtils.isEmpty(goalPeriod)) {
                    edtGoalPeriod.setError("Please enter Goal Period");
                } if (!goalName.equals("Select Goal")&&!TextUtils.isEmpty(goalAmount)&&!TextUtils.isEmpty(goalPeriod)){
                    // calling method to add data to Firebase Firestore.
                    addDataToDatabase(goalName, goalAmount, goalPeriod);
                    tvErrGoalName.setText("");
                }else{
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               spGoalName.setSelection(0);
                edtGoalAmount.setText("");
                edtGoalPeriod.setText("");

                tvAnnually.setText("KES 0");
                tvMonthly.setText("KES 0");
                tvWeekly.setText("KES 0");
                tvDaily.setText("KES 0");
                tvTipsName.setText("");

            }
        });

        edtGoalAmount.addTextChangedListener(new TextWatcher() {
//            int budgetAmount1=0;
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
//                    int number = Integer.parseInt(originalString);
//                    valencyAmount = budgetAmount1 - number;
                } else {
//                    valencyAmount -= budgetAmount1;
                }
//                valency.setText(valencyAmount + "");
//                valency.setText(formatter.format(valencyAmount));
            }

            @Override
            public void afterTextChanged(Editable s) {
//                int number = Integer.parseInt(s.toString());
                edtGoalAmount.removeTextChangedListener(this);
//                budgetAmount1 = Integer.parseInt(line.getText().toString());
//                budgetAmount1 = budgetAmount;



                String amountStr = edtGoalAmount.getText().toString();
                if (!TextUtils.isEmpty(amountStr)) {

                    String originalString = amountStr;

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);


                    String formattedString = formatter.format(longval);

                    edtGoalAmount.setText(formattedString);
                    edtGoalAmount.setSelection(edtGoalAmount.getText().length());

                    int number = Integer.parseInt(originalString);

//                    valencyAmount =  number - budgetAmount1;
                } else {
//                    valencyAmount = 0 - budgetAmount1;
                }
//                valency.setText(formatter.format(valencyAmount));

                edtGoalAmount.addTextChangedListener(this);
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
    public void ResetGoal(View v){
        edtGoalAmount.setText("");
    }
    private void addDataToDatabase(String goalName, String goalAmount, String goalPeriod) {

        // url to post our data
//        String url = "https://mwalimubiashara.com/app/get_yourgoal.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(YourGoalActivity.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, GET_YOUR_GOAL_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray =jsonObject.getJSONArray("data");
                    if(success.equals("1")){

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject object=jsonArray.getJSONObject(i);

                            tvAnnually.setText(currency+" "+object.getString("annually"));
                            tvMonthly.setText(currency+" "+object.getString("presentmonthly"));
                            tvWeekly.setText(currency+" "+object.getString("presentweekly"));
                            tvDaily.setText(currency+" "+object.getString("presentdaily"));
                            tvTipsName.setText("You stand to gain approximately "+currency+" "+object.getString("discounted")+" " +
                                    "by placing this money in a money market fund as you wait for "+goalPeriod+" years.");

                        }
                    }

//                    Toast.makeText(YourGoalActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // and setting data to edit text as empty
//                lumpsumNameEdt.setText("");
//                amountEdt.setText("");
//                startDateEdt.setText("");
//                endDateEdt.setText("");
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                progressBar.setVisibility(View.GONE);
                Toast.makeText(YourGoalActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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

                // below line we are creating a map for storing
                // our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our
                // key and value pair to our parameters.
                params.put("goalName", goalName);
                params.put("goalAmount", goalAmount);
                params.put("goalPeriod", goalPeriod);

                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}