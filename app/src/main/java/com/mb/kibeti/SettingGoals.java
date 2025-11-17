package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mb.kibeti.tap_tracking.TapTracker;
import com.tapadoo.alerter.Alerter;
import com.tapadoo.alerter.OnHideAlertListener;
import com.tapadoo.alerter.OnShowAlertListener;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SettingGoals extends AppCompatActivity {

    ListView listView;
    SettingGoalAdapter adapter;
    ProgressDialog pDialog;
    ArrayList<SettingGoalRowItem> rowItems;
    PipeLines utils = new PipeLines();
    String url_get_goal = utils.SAVE_PLAN_URL;
    TextView tvTtlAmount, tvTtlDaily, tvTtlMonthly;
    TextView tvToolHeader;
    LinearLayout clients_download_pdf;
    private LinearLayout containerLayout;
    private TextView previousButton;
    private Button nextButton;
    private LinearLayout stepIndicatorsLayout;
    private EditText edAmount1, edAmount2, edAmount3, edAmount4, edAmount5, edAmount6, edAmount7, edAmount8;
    private int days = 0;
    private int currentStep = 0, totalAmount = 0;
    private View[] steps;
    private View step1, step2, step3;
    private TextView[] stepIndicators;
    private NumberFormatOnTyping numberFormatOnTyping;
    private RadioButton radio_daily, radio_weekly, radio_monthly, radio_quarterly, radio_semi_annual, radio_annual;
    private ImageView calendar1, calendar2, calendar3, calendar4, calendar5, calendar6, calendar7, calendar8;
    private ShowCalendar sCalendar;
    private TextView tvPickDate1, tvPickDate2, tvPickDate3, tvPickDate4, tvPickDate5, tvPickDate6, tvPickDate7, tvPickDate8;
    private TextView tvInvest01, tvInvest02, tvInvest03, tvInvest04, tvInvest05, tvInvest06, tvInvest07, tvInvest08;
    private DecimalFormat formatter;
    private SimpleDateFormat simpleDateFormat;
    private Date latest = null;
    private float dailyAmount = 0;
    private float weeklyAmount = 0;
    private float monthlyAmount = 0;
    private float quarterlyAmount = 0;
    private float semiAnnuallyAmount = 0;
    private float annuallyAmount = 0;
    private String email = "";
    private SharedPreferences sharedPreferences;
    private NetworkChangeListener networkChangeListener;
    private ShowPopup popup;
    private Dialog myDialog;
    private TapTracker tapTracker;
    ImageView backButton;
    private boolean isSaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_goals);

        tapTracker = new TapTracker(this);

        myDialog = new Dialog(this);

        popup = new ShowPopup(this, myDialog);
        // Initializing views from activity_main
        containerLayout = findViewById(R.id.container_layout);
        previousButton = findViewById(R.id.previous_button);
        nextButton = findViewById(R.id.next_button);
        backButton = findViewById(R.id.backButton);
        tvToolHeader = findViewById(R.id.tvToolHeader);

        tvToolHeader.setText("What is your financial goal?");

        stepIndicatorsLayout = findViewById(R.id.step_indicators_layout);
        numberFormatOnTyping = new NumberFormatOnTyping(this);
        sCalendar = new ShowCalendar(this);
        step1 = LayoutInflater.from(this).inflate(R.layout.set_goals, containerLayout, false);
        step2 = LayoutInflater.from(this).inflate(R.layout.select_plan, containerLayout, false);
        step3 = LayoutInflater.from(this).inflate(R.layout.save_plan, containerLayout, false);

        pDialog = new ProgressDialog(this);

        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("###,###,###,###");
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL, "");

        latest = getDateToday();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Initializing steps (views)
        steps = new View[]{
                step1,
//                step2,
                step3
        };


        tapTracker.postTrack(email, "Setting Goal");


        intializeStep1();

        initializeStep2();

        initializeStep3();
        // Initializing step indicators (circles and arrows)
        initializeStepIndicators();


        showCurrentStep();

        // Seting click listener for previous button
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStep > 0) {
                    currentStep--;
                    showCurrentStep();
                    if (currentStep == 0) {
                        totalAmount = 0;
                    }
                }
            }
        });

        // Seting click listener for next button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStep < steps.length - 1) {

//                    showCurrentStep();

                    currentStep++;
                    showCurrentStep();
                    if (days == 0 || totalAmount == 0) {
                        currentStep = 0;
                        showCurrentStep();
                    }


                } else {
                    // When Last step reached submit the form
                    if (!isSaved) {
                        submitForm();
                    }

                }
            }
        });
    }

    private void showAlerter(String alertTitle, String alertMsg) {
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

    private void initializeStep3() {
        formatter = new DecimalFormat("#,###,###,###");


        listView = step3.findViewById(R.id.list_view);
        tvTtlAmount = step3.findViewById(R.id.tvTtlAmount);
        tvTtlDaily = step3.findViewById(R.id.tvTtlDaily);
        tvTtlMonthly = step3.findViewById(R.id.tvTtlMonthly);
        clients_download_pdf = step3.findViewById(R.id.clientsPDF);
        rowItems = new ArrayList<>();

        networkChangeListener = new NetworkChangeListener();

        adapter = new SettingGoalAdapter(getApplicationContext(), R.layout.list_item_tabulation, rowItems);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
//                TextView tvClientName = step3.findViewById(R.id.tvBeta);
//                TextView tvValueOfBusiness = step3.findViewById(R.id.tvGamma);
//                TextView tvApproximateDealDate = step3.findViewById(R.id.tvDelta);
//                TextView tvPhoneNumber = step3.findViewById(R.id.tvEpsilon);

//String strGoal, strAmount,strDate,strDaily,strMonthly,strWeekly,strSemiAnnually,strAnnually;
                popup.showPopup(rowItems.get(i).getStrGoal(), rowItems.get(i).getStrAmount(),
                        rowItems.get(i).getStrDate(), rowItems.get(i).getStrDaily(), rowItems.get(i).getStrWeekly(), rowItems.get(i).getStrMonthly(),
                        rowItems.get(i).getStrQuarterly(), rowItems.get(i).getStrSemiAnnually(), rowItems.get(i).getStrAnnually());

            }
        });

//        floatingActionButton=step3.findViewById(R.id.floating_action_button);
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent nextInt = new Intent(step3.getContext(), ClientNew.class);
//                nextInt.putExtra(strUsernamePh, strUsername);
//                nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//                nextInt.putExtra(strBusinessNamePh, strBusinessName);
//                step3.getContext().startActivity(nextInt);
////                Toast.makeText(getActivity(), "Business "+strBusinessName+"" +
////                        "\nEmail"+strEmailAddress+"\n"+" Username "+strUsername, Toast.LENGTH_SHORT).show();
//            }
//        });

        clients_download_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportClientsPDF();
            }
        });


    }


    private void intializeStep1() {
        edAmount1 = step1.findViewById(R.id.amount1);
        edAmount2 = step1.findViewById(R.id.amount2);
        edAmount3 = step1.findViewById(R.id.amount3);
        edAmount4 = step1.findViewById(R.id.amount4);
        edAmount5 = step1.findViewById(R.id.amount5);
        edAmount6 = step1.findViewById(R.id.amount6);
        edAmount7 = step1.findViewById(R.id.amount7);
        edAmount8 = step1.findViewById(R.id.amount8);

        tvPickDate1 = step1.findViewById(R.id.tvPickDate1);
        tvPickDate2 = step1.findViewById(R.id.tvPickDate2);
        tvPickDate3 = step1.findViewById(R.id.tvPickDate3);
        tvPickDate4 = step1.findViewById(R.id.tvPickDate4);
        tvPickDate5 = step1.findViewById(R.id.tvPickDate5);
        tvPickDate6 = step1.findViewById(R.id.tvPickDate6);
        tvPickDate7 = step1.findViewById(R.id.tvPickDate7);
        tvPickDate8 = step1.findViewById(R.id.tvPickDate8);

        calendar1 = step1.findViewById(R.id.calendar1);
        calendar2 = step1.findViewById(R.id.calendar2);
        calendar3 = step1.findViewById(R.id.calendar3);
        calendar4 = step1.findViewById(R.id.calendar4);
        calendar5 = step1.findViewById(R.id.calendar5);
        calendar6 = step1.findViewById(R.id.calendar6);
        calendar7 = step1.findViewById(R.id.calendar7);
        calendar8 = step1.findViewById(R.id.calendar8);

        tvInvest01 = step1.findViewById(R.id.tvInvest01);
        tvInvest02 = step1.findViewById(R.id.tvInvest02);
        tvInvest03 = step1.findViewById(R.id.tvInvest03);
        tvInvest04 = step1.findViewById(R.id.tvInvest04);
        tvInvest05 = step1.findViewById(R.id.tvInvest05);
        tvInvest06 = step1.findViewById(R.id.tvInvest06);
        tvInvest07 = step1.findViewById(R.id.tvInvest07);
        tvInvest08 = step1.findViewById(R.id.tvInvest08);

        numberFormatOnTyping.setNumberFormatOnTyping(edAmount1, tvPickDate1, "Pick target date", "Target date is required");
        numberFormatOnTyping.setNumberFormatOnTyping(edAmount2, tvPickDate2, "Pick target date", "Target date is required");
        numberFormatOnTyping.setNumberFormatOnTyping(edAmount3, tvPickDate3, "Pick target date", "Target date is required");
        numberFormatOnTyping.setNumberFormatOnTyping(edAmount4, tvPickDate4, "Pick target date", "Target date is required");
        numberFormatOnTyping.setNumberFormatOnTyping(edAmount5, tvPickDate5, "Pick target date", "Target date is required");
        numberFormatOnTyping.setNumberFormatOnTyping(edAmount6, tvPickDate6, "Pick target date", "Target date is required");
        numberFormatOnTyping.setNumberFormatOnTyping(edAmount7, tvPickDate7, "Pick target date", "Target date is required");
        numberFormatOnTyping.setNumberFormatOnTyping(edAmount8, tvPickDate8, "Pick target date", "Target date is required");


        calendar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sCalendar.showCalendar(tvPickDate1);
            }
        });
        calendar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sCalendar.showCalendar(tvPickDate2);
            }
        });
        calendar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sCalendar.showCalendar(tvPickDate3);
            }
        });
        calendar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sCalendar.showCalendar(tvPickDate4);
            }
        });
        calendar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sCalendar.showCalendar(tvPickDate5);
            }
        });
        calendar6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sCalendar.showCalendar(tvPickDate6);
            }
        });
        calendar7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sCalendar.showCalendar(tvPickDate7);
            }
        });
        calendar8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sCalendar.showCalendar(tvPickDate8);
            }
        });
    }


    private int getNoOfDays(String date) {

        int no_days = 0;

        long difference = Math.abs(checkDate(date).getTime() - getDateToday().getTime());
        no_days = (int) (difference / (24 * 60 * 60 * 1000));

        return no_days;
    }

    private Date checkDate(String dt) {

        Date date1 = getDateToday();
        if (dt.equals("Pick target date")) {
            return latest;
        } else {

            try {
                date1 = simpleDateFormat.parse(dt);
                if (date1.after(getDateToday())) {

                    return date1;
                }
            } catch (Exception e) {

                Log.e("TAG", "Date err" + e);
            }
        }

        return date1;
    }

    private Date getDateToday() {
        String currentDate = simpleDateFormat
                .format(new Date());

        try {

            latest = simpleDateFormat.parse(currentDate);

            return latest;

        } catch (Exception e) {
//            return "1900-12-30";
        }
        return null;
    }

    private void initializeStep2() {
        radio_daily = step2.findViewById(R.id.radia_daily);
        radio_weekly = step2.findViewById(R.id.radia_weekly);
        radio_monthly = step2.findViewById(R.id.radia_monthly);
        radio_quarterly = step2.findViewById(R.id.radia_quarterly);
        radio_semi_annual = step2.findViewById(R.id.radio_semi_annually);
        radio_annual = step2.findViewById(R.id.radio_annually);
    }

    // Initializing step indicators with circles and arrows
    private void initializeStepIndicators() {
        stepIndicators = new TextView[steps.length];
        for (int i = 0; i < steps.length; i++) {
            TextView stepIndicator = new TextView(this);
            stepIndicator.setText(String.valueOf(i + 1));
            stepIndicator.setTextColor(Color.WHITE);
            stepIndicator.setTextSize(18);
            stepIndicator.setBackgroundResource(R.drawable.circle_gray);
            stepIndicator.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(10, 0, 10, 0);
            stepIndicator.setLayoutParams(params);
            stepIndicatorsLayout.addView(stepIndicator);
            stepIndicators[i] = stepIndicator;

            if (i < steps.length - 1) {
                addArrowIndicator(stepIndicatorsLayout);
            }
        }
    }

    // Adding arrow indicator between step indicators
    private void addArrowIndicator(LinearLayout stepIndicatorsLayout) {
        ImageView arrow = new ImageView(this);
        // to add this create a new drawable resource file in res->drawable
        arrow.setImageResource(R.drawable.ic_arrow_forward_black_24dp);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER_VERTICAL;
        arrow.setLayoutParams(params);
        stepIndicatorsLayout.addView(arrow);
    }

    // Showing the current step
    private void showCurrentStep() {
        containerLayout.removeAllViews();
        containerLayout.addView(steps[currentStep]);
        // If Current Step is greater then 0 then making Previous Button Visible
        previousButton.setVisibility(currentStep > 0 ? View.VISIBLE : View.GONE);
        nextButton.setText(currentStep < steps.length - 1 ? "Get Plan" : "Save Plan");

        if (currentStep == 1) {
//            getLatestDate();
            calculateSavingPlan();
        }
        updateStepIndicators();
    }

    // Updating the step indicators to highlight the current step
    private void updateStepIndicators() {
        for (int i = 0; i < stepIndicators.length; i++) {
            if (i == currentStep) {
                stepIndicators[i].setBackgroundResource(R.drawable.circle_green);

            } else {
                stepIndicators[i].setBackgroundResource(R.drawable.circle_gray);
            }
        }
    }

    private void calculateSavingPlan() {
        totalAmount = 0;
        days = 0;
        dailyAmount = 0;
        weeklyAmount = 0;
        monthlyAmount = 0;
        quarterlyAmount = 0;
        semiAnnuallyAmount = 0;
        annuallyAmount = 0;
        rowItems.clear();


        getAmount(tvInvest01, edAmount1, tvPickDate1);
        getAmount(tvInvest02, edAmount2, tvPickDate2);
        getAmount(tvInvest03, edAmount3, tvPickDate3);
        getAmount(tvInvest04, edAmount4, tvPickDate4);
        getAmount(tvInvest05, edAmount5, tvPickDate5);
        getAmount(tvInvest06, edAmount6, tvPickDate6);
        getAmount(tvInvest07, edAmount7, tvPickDate7);
        getAmount(tvInvest08, edAmount8, tvPickDate8);

        tvTtlDaily.setText(formatter.format(dailyAmount));
//        radio_weekly.setText("Weekly saving plan is Ksh." + formatter.format(weeklyAmount));
        tvTtlMonthly.setText(formatter.format(monthlyAmount));
//        radio_quarterly.setText("Quarterly saving plan is Ksh." + formatter.format(quarterlyAmount));
//        radio_semi_annual.setText("Semi annually saving plan is Ksh." + formatter.format(semiAnnuallyAmount));
//        radio_annual.setText("Annually saving plan is Ksh." + formatter.format(annuallyAmount));
        tvTtlAmount.setText(formatter.format(totalAmount));

        adapter.notifyDataSetChanged();
        showAlerter("", "Tap on goal to view more details");

    }

    private void getAmount(TextView tvInvest, EditText edAmount, TextView tvPickDate) {
        String dt1 = tvPickDate.getText().toString();
//        latest = 0;
        latest = checkDate(dt1);

        int no_days = 0;
        no_days = getNoOfDays(dt1);
        float ttlAmnt = convertToInt(edAmount);

        float daily = no_days > 0 ? ttlAmnt / no_days : 0;
        float weekly = (daily * 7) > ttlAmnt ? ttlAmnt : (daily * 7);
        float monthly = (daily * 30) > ttlAmnt ? ttlAmnt : (daily * 30);
        float quarterly = (daily * 90) > ttlAmnt ? ttlAmnt : (daily * 90);
        float semiAnnually = (daily * 183) > ttlAmnt ? ttlAmnt : (daily * 183);
        float annually = (daily * 365) > ttlAmnt ? ttlAmnt : (daily * 365);


        if (!dt1.equals("Pick target date") && daily > 0) {

            updateAmount(daily, weekly, monthly, quarterly, semiAnnually, annually, no_days);
            SettingGoalRowItem item = new SettingGoalRowItem(tvInvest.getText().toString(), "" + formatter.format(ttlAmnt), "" + dt1, "" + formatter.format(daily),
                    "" + formatter.format(weekly), "" + formatter.format(monthly), "" + formatter.format(quarterly), "" + formatter.format(semiAnnually), "" + formatter.format(annually));

            rowItems.add(item);
        }

        totalAmount = (int) (totalAmount + ttlAmnt);
        days += no_days;

        Log.e("TAG", "Total amount is " + totalAmount);
    }

    private void updateAmount(float daily, float weekly, float monthly, float quarterly, float semiAnnually, float annually, int ndays) {
        dailyAmount += daily;
        weeklyAmount += weekly;
        monthlyAmount += monthly;
        quarterlyAmount += quarterly;
        semiAnnuallyAmount += semiAnnually;
        annuallyAmount += annually;

    }

    public int convertToInt(String str) {
        String amnt1 = str.replaceAll(",", "");

        amnt1 = amnt1.replaceAll("KES.", "");


//        int amount1 = Float.parseFloat(amnt1);
        int amount1 = Integer.parseInt(amnt1);

        return amount1;
    }

    public int convertToInt(EditText editText) {
        String str = editText.getText().toString();
        if (str.isEmpty()) {
            return 0;
        }
        String amnt1 = str.replaceAll(",", "");

        amnt1 = amnt1.replaceAll(" ", "");
        amnt1 = amnt1.replaceAll("KES.", "");

//        int amount1 = Float.parseFloat(amnt1);
        int amount1 = Integer.parseInt(amnt1);

        return amount1;
    }

    // When clicked on submit button at last form/activity
    private void submitForm() {
//        Toast.makeText(SettingGoals.this, "Saved", Toast.LENGTH_SHORT).show();

        if (networkChangeListener.isNetwork(getApplicationContext())) {
            //All good
            savePlan();
        }
//        finish();
    }


    private void savePlan() {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        pDialog.setMessage("Saving your goal....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest request = new StringRequest(Request.Method.POST, url_get_goal, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE FOR SAVING GOAL IS " + response);
                pDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    Boolean error = jsonObject.getBoolean("error");
//                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (!error) {

                        exportClientsPDF();
                        isSaved = true;

                    } else {

//                        pDialog.dismiss();
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

                int i = 0;

                for (SettingGoalRowItem goalRowItem : rowItems) {

//                    rowItems.get(i).
                    // Print all elements of ArrayList

                    Log.e("TAG", "Goal title saved is " + rowItems.get(i).getStrGoal());
                    params.put("goal" + i, rowItems.get(i).getStrGoal());
                    params.put("amount" + i, rowItems.get(i).getStrAmount());
                    params.put("date" + i, rowItems.get(i).getStrDate());
                    params.put("daily" + i, rowItems.get(i).getStrDaily());
                    i++;
                }

                params.put("email", email);

                return params;
            }
        };

        queue.add(request);
    }

    public void exportClientsPDF() {
        PaymentRequiredPopup paymentRequiredPopup = new PaymentRequiredPopup(this, myDialog,
                "To view your savings plan please upgrade your account by tapping Upgrade Account");

        if (paymentRequiredPopup.checkPayment()) {
            Intent intent = new Intent(SettingGoals.this, CashflowPdfActivity.class);
            intent.putExtra("url", "goal_plan_download.php");
            intent.putExtra("activity_name", "Goal Plan Pdf");
            startActivity(intent);
        }

    }
}
