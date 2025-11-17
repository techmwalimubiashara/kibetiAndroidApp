package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.CASHFLOW_BUDGET_REMINDER;
import static com.mb.kibeti.LoginActivity.CURRENCY;
import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;
import static com.mb.kibeti.LoginActivity.USERNAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mb.kibeti.tap_tracking.TapTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Angle;
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.Spread;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class CashflowBudget extends AppCompatActivity {
    LinearLayout layoutLanding, layoutStep1, layoutStep2, layoutStep3;
    ScrollView layoutOutflowBudget;
    RadioGroup radioGroup;
    EditText amount;
    TextView tvAmount1, tvAmount2, tvAmount3, tvAmount4, tvAmount5, tvAmount6, tvAmount7, tvAmount8, tvAmount9, tvAmount10;
    DecimalFormat formatter;
    ProgressDialog pDialog;
    String ageSelected = "0";

    String email = "";
    SharedPreferences sharedPreferences;
    TapTracker tapTracker;
    private KonfettiView konfettiView = null;
    private Shape.DrawableShape drawableShape = null;
    RadioButton radioButton;
    Button btnNextStep2, btnNextStep3;
    String currency = "";
    TextView tvEnterAmount;
    BasicProfile basicProfile;

    BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashflow_budget);


        layoutLanding = findViewById(R.id.layoutLanding);
        layoutStep1 = findViewById(R.id.layoutStep1);
        layoutStep2 = findViewById(R.id.layoutStep2);
        layoutStep3 = findViewById(R.id.layoutStep3);
        btnNextStep2 = findViewById(R.id.btnNextStep2);
        btnNextStep3 = findViewById(R.id.btnNextStep3);
        layoutOutflowBudget = findViewById(R.id.layoutOutflowBudget);
        radioGroup = findViewById(R.id.groupradio);
        amount = findViewById(R.id.idAmount);
        tvAmount1 = findViewById(R.id.tvAmount1);
        tvAmount2 = findViewById(R.id.tvAmount2);
        tvAmount3 = findViewById(R.id.tvAmount3);
        tvAmount4 = findViewById(R.id.tvAmount4);
        tvAmount5 = findViewById(R.id.tvAmount5);
        tvAmount6 = findViewById(R.id.tvAmount6);
        tvAmount7 = findViewById(R.id.tvAmount7);
        tvAmount8 = findViewById(R.id.tvAmount8);
        tvAmount9 = findViewById(R.id.tvAmount9);
        tvAmount10 = findViewById(R.id.tvAmount10);
        tvEnterAmount = findViewById(R.id.tvAmount);


        tapTracker = new TapTracker(this);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL, "");
        currency = sharedPreferences.getString(CURRENCY, "");
        basicProfile = new BasicProfile(this, email);

        tvEnterAmount.setText(currency);

        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("###,###,###,###");

        tapTracker.postTrack(email, "Setting Goal");

        setNumberFormatOnTyping(amount, btnNextStep3);


        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Drawable drawable =
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_heart);

        drawableShape = new Shape.DrawableShape(drawable, true, true);

        konfettiView = findViewById(R.id.konfettiView);
        EmitterConfig emitterConfig = new Emitter(5L, TimeUnit.SECONDS).perSecond(50);
        Party party =
                new PartyFactory(emitterConfig)
                        .angle(270)
                        .spread(90)
                        .setSpeedBetween(1f, 5f)
                        .timeToLive(2000L)
                        .shapes(new Shape.Rectangle(0.2f), drawableShape)
                        .sizes(new Size(12, 5f, 0.2f))
                        .position(0.0, 0.0, 1.0, 0.0)
                        .build();
        konfettiView.setOnClickListener(view -> konfettiView.start(party));
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                btnNextStep2.setEnabled(true);
                btnNextStep2.setBackground(ContextCompat.getDrawable(CashflowBudget.this, R.drawable.button_rectangle_line));
            }
        });

    }

    private void rain() {
        EmitterConfig emitterConfig = new Emitter(5, TimeUnit.SECONDS).perSecond(100);
        konfettiView.start(
                new PartyFactory(emitterConfig)
                        .angle(Angle.BOTTOM)
                        .spread(Spread.ROUND)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(0f, 15f)
                        .position(new Position.Relative(0.0, 0.0).between(new Position.Relative(1.0, 0.0)))
                        .build());
    }

    public void manualBudget(View view) {
        Intent intent = new Intent(CashflowBudget.this, AddOutflowActivity.class);
        startActivity(intent);
        finish();
    }

    public void setNumberFormatOnTyping(EditText edAmount, Button btn) {

        edAmount.addTextChangedListener(new TextWatcher() {
            //            int budgetAmount1=0;
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edAmount.removeTextChangedListener(this);

                String amountStr = edAmount.getText().toString();
                if (!TextUtils.isEmpty(amountStr)) {

                    String originalString = amountStr;

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    String formattedString = formatter.format(longval);
                    edAmount.setText(formattedString);
                    edAmount.setSelection(edAmount.getText().length());

                    btn.setEnabled(true);
                    btn.setBackground(ContextCompat.getDrawable(CashflowBudget.this, R.drawable.button_rectangle_line));

                } else {
                    btn.setEnabled(false);
                    btn.setBackground(ContextCompat.getDrawable(CashflowBudget.this, R.drawable.button_greyed_out));

                }
                edAmount.addTextChangedListener(this);
            }
        });
    }

    public void setNumberFormatOnTyping(EditText edAmount) {

        edAmount.addTextChangedListener(new TextWatcher() {
            //            int budgetAmount1=0;
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edAmount.removeTextChangedListener(this);

                String amountStr = edAmount.getText().toString();
                if (!TextUtils.isEmpty(amountStr)) {

                    String originalString = amountStr;

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    String formattedString = formatter.format(longval);
                    edAmount.setText(formattedString);
                    edAmount.setSelection(edAmount.getText().length());

                } else {

                }
                edAmount.addTextChangedListener(this);
            }
        });
    }


    private void outflowBudget(String age, String amount) {

        String url = "https://mwalimubiashara.com/app/generate_outflow_budget.php";

        RequestQueue queue = Volley.newRequestQueue(this);


        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                String amnt1 = "0", amnt2 = "0", amnt3 = "0", amnt4 = "0", amnt5 = "0", amnt6 = "0", amnt7 = "0", amnt8 = "0", amnt9 = "0", amnt10 = "0";

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        int l = 0;
                        l = jsonArray.length();
                        for (int i = 0; i < l; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            amnt1 = object.getString("rec_amt_1");
                            amnt2 = object.getString("rec_amt_2");
                            amnt3 = object.getString("rec_amt_3");
                            amnt4 = object.getString("rec_amt_4");
                            amnt5 = object.getString("rec_amt_5");
                            amnt6 = object.getString("rec_amt_6");
                            amnt7 = object.getString("rec_amt_7");
                            amnt8 = object.getString("rec_amt_8");
                            amnt9 = object.getString("rec_amt_9");
                            amnt10 = object.getString("rec_amt_10");

                        }

                    }


                } catch (Exception e) {

                }
                tvAmount1.setText(currency + " " + amnt1.replaceAll("KES. ", ""));
                tvAmount2.setText(currency + " " + amnt2.replaceAll("KES. ", ""));
                tvAmount3.setText(currency + " " + amnt3.replaceAll("KES. ", ""));
                tvAmount4.setText(currency + " " + amnt4.replaceAll("KES. ", ""));
                tvAmount5.setText(currency + " " + amnt5.replaceAll("KES. ", ""));
                tvAmount6.setText(currency + " " + amnt6.replaceAll("KES. ", ""));
                tvAmount7.setText(currency + " " + amnt7.replaceAll("KES. ", ""));
                tvAmount8.setText(currency + " " + amnt8.replaceAll("KES. ", ""));
                tvAmount9.setText(currency + " " + amnt9.replaceAll("KES. ", ""));
                tvAmount10.setText(currency + " " + amnt10.replaceAll("KES. ", ""));

                pDialog.dismiss();
                layoutStep3.setVisibility(View.GONE);
                layoutOutflowBudget.setVisibility(View.VISIBLE);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                // method to handle errors.
//                Toast.makeText(this, "Fail to get response ", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {

                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("age", age);
                params.put("amount", amount);

                return params;
            }
        };

        queue.add(request);
    }

    private void outflowBudgetSave(String age, String amount) {

        String url = "https://mwalimubiashara.com/app/generate_outflow_budget.php";

        RequestQueue queue = Volley.newRequestQueue(this);


        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {


                    }


                } catch (Exception e) {

                }
                pDialog.dismiss();
                layoutLanding.setVisibility(View.VISIBLE);
                layoutOutflowBudget.setVisibility(View.GONE);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                // method to handle errors.
//                Toast.makeText(this, "Fail to get response ", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {

                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("age", age);
                params.put("amount", amount);
                params.put("action", "save");

                return params;
            }
        };

        queue.add(request);
    }

    public void startLayoutStep1(View view) {

        layoutLanding.setVisibility(View.GONE);
        layoutStep1.setVisibility(View.VISIBLE);
        layoutStep3.setVisibility(View.GONE);
        layoutStep2.setVisibility(View.GONE);
        layoutOutflowBudget.setVisibility(View.GONE);

    }

    public void backStep1(View view) {

        layoutLanding.setVisibility(View.VISIBLE);
        layoutStep1.setVisibility(View.GONE);
        layoutStep3.setVisibility(View.GONE);
        layoutStep2.setVisibility(View.GONE);
        layoutOutflowBudget.setVisibility(View.GONE);

    }

    public void startLayoutStep2(View view) {
        int checkedId = radioGroup.getCheckedRadioButtonId();


        radioButton = (RadioButton) radioGroup
                .findViewById(checkedId);

        if (checkedId == -1) {
            Toast.makeText(CashflowBudget.this,
                            "Select your age bracket",
                            Toast.LENGTH_SHORT)
                    .show();
        } else {

            if (checkedId == R.id.radio_18_22) {
                ageSelected = "22";
            } else if (checkedId == R.id.radio_23_29) {

                ageSelected = "27";
            } else if (checkedId == R.id.radio_30_34) {

                ageSelected = "32";
            } else if (checkedId == R.id.radio_35_39) {

                ageSelected = "37";
            } else if (checkedId == R.id.radio_40_44) {

                ageSelected = "42";
            } else if (checkedId == R.id.radio_45_49) {

                ageSelected = "47";
            } else if (checkedId == R.id.radio_50_54) {

                ageSelected = "52";
            } else if (checkedId == R.id.radio_55_59) {

                ageSelected = "57";
            } else if (checkedId == R.id.radio_60_64) {

                ageSelected = "62";
            } else if (checkedId == R.id.radio_over65) {

                ageSelected = "67";
            }


//            String text = radioButton.getText().toString();
//            if (text.equals("18 - 24 years")) {
//                ageSelected = "22";
//
//            } else if (text.equals("18 - 29 years")) {
//                ageSelected = "27";
//
//            } else if (text.equals("30 - 39 years")) {
//                ageSelected = "36";
//            } else if (text.equals("40 - 49 years")) {
//                ageSelected = "46";
//            } else if (text.equals("50 - 59 years")) {
//                ageSelected = "54";
//            } else {
//                ageSelected = "65";
//            }

            layoutLanding.setVisibility(View.GONE);
            layoutStep1.setVisibility(View.GONE);
            layoutStep3.setVisibility(View.GONE);
            layoutOutflowBudget.setVisibility(View.GONE);
            layoutStep2.setVisibility(View.VISIBLE);
        }
    }

    public void backStep2(View view) {


        layoutLanding.setVisibility(View.GONE);
        layoutStep1.setVisibility(View.VISIBLE);
        layoutStep3.setVisibility(View.GONE);
        layoutStep2.setVisibility(View.GONE);
        layoutOutflowBudget.setVisibility(View.GONE);

    }

    public void backStep3(View view) {

        layoutLanding.setVisibility(View.GONE);
        layoutStep1.setVisibility(View.GONE);
        layoutStep3.setVisibility(View.GONE);
        layoutOutflowBudget.setVisibility(View.GONE);
        layoutStep2.setVisibility(View.VISIBLE);
    }

    public void startLayoutStep3(View view) {
        if (amount.getText().length() == 0) {
            amount.setError("Amount is required");
        } else {
            TextView tvWellDone, tvAgeGroup, tvEarning;
            tvWellDone = findViewById(R.id.tvWellDone);
            tvAgeGroup = findViewById(R.id.tvAgeGroup);
            tvEarning = findViewById(R.id.tvEarning);
            String name = sharedPreferences.getString(USERNAME, "");
            layoutLanding.setVisibility(View.GONE);
            layoutStep1.setVisibility(View.GONE);
            layoutStep3.setVisibility(View.VISIBLE);
            layoutStep2.setVisibility(View.GONE);
            layoutOutflowBudget.setVisibility(View.GONE);
            tvWellDone.setText("Well done " + name + "!! All is set to generate your budget.");
            tvAgeGroup.setText("✅Your age bracket " + radioButton.getText().toString());
            tvEarning.setText("✅Your monthly income " + currency + ". " + amount.getText().toString());
            rain();
        }

    }

    public void generateOutflowBudget(View view) {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Generating budget ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        outflowBudget(ageSelected, amount.getText().toString());
    }

    public void editBudget(View view) {
        createBottomSheetDialog();

//        EditBudgetPopup bottomSheet = new EditBudgetPopup();
//
//
//        String str_amnt1 = tvAmount1.getText().toString();
//        String str_amnt2 = tvAmount2.getText().toString();
//        String str_amnt3 = tvAmount3.getText().toString();
//        String str_amnt4 = tvAmount4.getText().toString();
//        String str_amnt5 = tvAmount5.getText().toString();
//        String str_amnt6 = tvAmount6.getText().toString();
//        String str_amnt7 = tvAmount7.getText().toString();
//        String str_amnt8 = tvAmount8.getText().toString();
//        String str_amnt9 = tvAmount9.getText().toString();
//        String str_amnt10 = tvAmount10.getText().toString();
//
//        Bundle bundle = new Bundle();
//        bundle.putString("str_amnt1", str_amnt1.replaceAll( "KES. ", "" ));
//        bundle.putString("str_amnt2", str_amnt2.replaceAll( "KES. ", "" ));
//        bundle.putString("str_amnt3", str_amnt3.replaceAll( "KES. ", "" ));
//        bundle.putString("str_amnt4", str_amnt4.replaceAll( "KES. ", "" ));
//        bundle.putString("str_amnt5", str_amnt5.replaceAll( "KES. ", "" ));
//        bundle.putString("str_amnt6", str_amnt6.replaceAll( "KES. ", "" ));
//        bundle.putString("str_amnt7", str_amnt7.replaceAll( "KES. ", "" ));
//        bundle.putString("str_amnt8", str_amnt8.replaceAll( "KES. ", "" ));
//        bundle.putString("str_amnt9", str_amnt9.replaceAll( "KES. ", "" ));
//        bundle.putString("str_amnt10", str_amnt10.replaceAll( "KES. ", "" ));
//        bottomSheet.setArguments(bundle);
//
//        bottomSheet.show(getSupportFragmentManager(),
//                "Edit budget");
    }


    public void saveOutflowBudget(View view) {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Saving spending budget ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
//        outflowBudgetSave(ageSelected, amount.getText().toString());
        Log.e("TAG", "Networth is " + basicProfile.getNetworth());
        if (basicProfile.getNetworth() == 0) {
            addOutBudgetOutflow();
//            pDialog.dismiss();

        } else {
            pDialog.dismiss();
            // Create the object of AlertDialog Builder class
            AlertDialog.Builder builder = new AlertDialog.Builder(CashflowBudget.this);

            // Set the message show for the Alert time
            builder.setMessage("You are about to overwrite your existing budget.\n Do you want to continue?");

            // Set Alert Title
            builder.setTitle("Budget Overwrite");

            // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
            builder.setCancelable(false);

            // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
            builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                // When the user click yes button then app will close

                addOutBudgetOutflow();
            });

            // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
            builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                // If user click no then dialog box is canceled.
                dialog.cancel();
            });

            // Create the Alert dialog
            AlertDialog alertDialog = builder.create();
            // Show the Alert Dialog box
            alertDialog.show();
        }

    }

    private void addOutBudgetOutflow() {

        // url to post our data
        String url = "https://mwalimubiashara.com/app/post_outflow.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(CashflowBudget.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // on below line we are displaying a success toast message.
//                    Toast.makeText(CashflowBudget.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    finish();
                    Intent intent = new Intent(CashflowBudget.this, CashflowPdfActivity.class);
                    intent.putExtra("url", "cashflow_download.php");
                    intent.putExtra("activity_name", "Outflow budget pdf");
                    startActivity(intent);
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    String currentDate = sdf.format(new Date());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(CASHFLOW_BUDGET_REMINDER, currentDate);
                    editor.commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(CashflowBudget.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
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

                params.put("email", email);
                params.put("outflow_cat1", "A. Day to Day");
                params.put("outflow_line1", "Other day-to-day ");
                params.put("amount1", tvAmount1.getText().toString().replaceAll(",", "").substring(4));
                params.put("outflow_frequency1", "Monthly");
                params.put("outflow_cat2", "B. Faith and Giving");
                params.put("outflow_line2", "Other givings");
                params.put("amount2", tvAmount2.getText().toString().replaceAll(",", "").substring(4));
                params.put("outflow_frequency2", "Monthly");
                params.put("outflow_cat3", "C. Savings");
                params.put("outflow_line3", "Other Savings (Pay yourself first)");
                params.put("amount3", tvAmount3.getText().toString().replaceAll(",", "").substring(4));
                params.put("outflow_frequency3", "Monthly");
                params.put("outflow_cat4", "D. Self");
                params.put("outflow_line4", "Others Self");
                params.put("amount4", tvAmount4.getText().toString().replaceAll(",", "").substring(4));
                params.put("outflow_frequency4", "Monthly");
                params.put("outflow_cat5", "E. Investments");
                params.put("outflow_line5", "Other Investments");
                params.put("amount5", tvAmount5.getText().toString().replaceAll(",", "").substring(4));
                params.put("outflow_frequency5", "Monthly");
                params.put("outflow_cat6", "F. Dependents");
                params.put("outflow_line6", "Other dependents expenses");
                params.put("amount6", tvAmount6.getText().toString().replaceAll(",", "").substring(4));
                params.put("outflow_frequency6", "Monthly");
                params.put("outflow_cat7", "G. Mobility");
                params.put("outflow_line7", "Other Mobility");
                params.put("amount7", tvAmount7.getText().toString().replaceAll(",", "").substring(4));
                params.put("outflow_frequency7", "Monthly");
                params.put("outflow_cat8", "H. Protection");
                params.put("outflow_line8", "Other protection");
                params.put("amount8", tvAmount8.getText().toString().replaceAll(",", "").substring(4));
                params.put("outflow_frequency8", "Monthly");
                params.put("outflow_cat9", "Loan Serving");
                params.put("outflow_line9", "loan");
                params.put("amount9", tvAmount9.getText().toString().replaceAll(",", "").substring(4));
                params.put("outflow_frequency9", "Monthly");
                params.put("outflow_cat10", "J. Other outflows");
                params.put("outflow_line10", "Others");
                params.put("amount10", tvAmount10.getText().toString().replaceAll(",", "").substring(4));
                params.put("outflow_frequency10", "Monthly");
                params.put("total_income", amount.getText().toString().replaceAll(",", ""));

                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    private void createBottomSheetDialog() {
        if (bottomSheetDialog == null) {
            View v = LayoutInflater.from(this).inflate(R.layout.edit_budget_popup, null);

            Button btnSave = v.findViewById(R.id.idBtnSave);

//            String str_amnt1 = this.getArguments().getString("str_amnt1");
//            String str_amnt2 = this.getArguments().getString("str_amnt2");
//            String str_amnt3 = this.getArguments().getString("str_amnt3");
//            String str_amnt4 = this.getArguments().getString("str_amnt4");
//            String str_amnt5 = this.getArguments().getString("str_amnt5");
//            String str_amnt6 = this.getArguments().getString("str_amnt6");
//            String str_amnt7 = this.getArguments().getString("str_amnt7");
//            String str_amnt8 = this.getArguments().getString("str_amnt8");
//            String str_amnt9 = this.getArguments().getString("str_amnt9");
//            String str_amnt10 = this.getArguments().getString(" str_amnt10");


            String str_amnt1 = tvAmount1.getText().toString();
            String str_amnt2 = tvAmount2.getText().toString();
            String str_amnt3 = tvAmount3.getText().toString();
            String str_amnt4 = tvAmount4.getText().toString();
            String str_amnt5 = tvAmount5.getText().toString();
            String str_amnt6 = tvAmount6.getText().toString();
            String str_amnt7 = tvAmount7.getText().toString();
            String str_amnt8 = tvAmount8.getText().toString();
            String str_amnt9 = tvAmount9.getText().toString();
            String str_amnt10 = tvAmount10.getText().toString();
//
            NumberFormatOnTyping numberFormatOnTyping = new NumberFormatOnTyping(this);

            EditText edItem1 = v.findViewById(R.id.edItem1);
            EditText edItem2 = v.findViewById(R.id.edItem2);
            EditText edItem3 = v.findViewById(R.id.edItem3);
            EditText edItem4 = v.findViewById(R.id.edItem4);
            EditText edItem5 = v.findViewById(R.id.edItem5);
            EditText edItem6 = v.findViewById(R.id.edItem6);
            EditText edItem7 = v.findViewById(R.id.edItem7);
            EditText edItem8 = v.findViewById(R.id.edItem8);
            EditText edItem9 = v.findViewById(R.id.edItem9);
            EditText edItem10 = v.findViewById(R.id.edItem10);


            edItem1.setText(str_amnt1.substring(3));
            edItem2.setText(str_amnt2.substring(3));
            edItem3.setText(str_amnt3.substring(3));
            edItem4.setText(str_amnt4.substring(3));
            edItem5.setText(str_amnt5.substring(3));
            edItem6.setText(str_amnt6.substring(3));
            edItem7.setText(str_amnt7.substring(3));
            edItem8.setText(str_amnt8.substring(3));
            edItem9.setText(str_amnt9.substring(3));
            edItem10.setText(str_amnt10.substring(3));

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvAmount1.setText(currency + " " + edItem1.getText().toString());
                    tvAmount2.setText(currency + " " + edItem2.getText().toString());
                    tvAmount3.setText(currency + " " + edItem3.getText().toString());
                    tvAmount4.setText(currency + " " + edItem4.getText().toString());
                    tvAmount5.setText(currency + " " + edItem5.getText().toString());
                    tvAmount6.setText(currency + " " + edItem6.getText().toString());
                    tvAmount7.setText(currency + " " + edItem7.getText().toString());
                    tvAmount8.setText(currency + " " + edItem8.getText().toString());
                    tvAmount9.setText(currency + " " + edItem9.getText().toString());
                    tvAmount10.setText(currency + " " + edItem10.getText().toString());

                    bottomSheetDialog.dismiss();
                }
            });

            numberFormatOnTyping.setNumberFormatOnTyping(edItem1);
            numberFormatOnTyping.setNumberFormatOnTyping(edItem2);
            numberFormatOnTyping.setNumberFormatOnTyping(edItem3);
            numberFormatOnTyping.setNumberFormatOnTyping(edItem4);
            numberFormatOnTyping.setNumberFormatOnTyping(edItem5);
            numberFormatOnTyping.setNumberFormatOnTyping(edItem6);
            numberFormatOnTyping.setNumberFormatOnTyping(edItem7);
            numberFormatOnTyping.setNumberFormatOnTyping(edItem8);
            numberFormatOnTyping.setNumberFormatOnTyping(edItem9);
            numberFormatOnTyping.setNumberFormatOnTyping(edItem10);

            bottomSheetDialog = new BottomSheetDialog(this);

            bottomSheetDialog.setContentView(v);

            bottomSheetDialog.show();
        } else {
            bottomSheetDialog.show();
        }
    }


}