package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.CURRENCY;
import static com.mb.kibeti.LoginActivity.EMAIL;
//import static com.mb.treasurechest.OutflowChart.
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;
import static com.mb.kibeti.LoginActivity.PACKAGE_TYPE;
import static com.mb.kibeti.LoginActivity.TRIAL;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mb.kibeti.landingPageTask.ui.PaymentActivity;
import com.mb.kibeti.screens.GoalPlannerActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class InvestmentCalculator extends AppCompatActivity {
    EditText edInvestmentAmount1, edInvestmentAmount2, edInvestmentAmount3;
    TextView tvmonthly_return1, tvRor1, tvmonthly_return_adjusted1, tvAdjustedRor1;
    TextView tvAbout1, tvAbout2, tvAbout3, tvAbout4, tvAbout5, tvAbout6, tvAbout7, tvAbout8, tvAbout9, tvAbout10;
    TextView tvInvest01, tvInvest02, tvInvest03, tvInvest04, tvInvest05, tvInvest06, tvInvest07, tvInvest08, tvInvest09, tvInvest010;
    TextView tvmonthly_return2, tvRor2, tvmonthly_return_adjusted2, tvAdjustedRor2;
    TextView tvmonthly_return3, tvRor3, tvmonthly_return_adjusted3, tvAdjustedRor3;
    TextView tvInvestLine1, tvInvestLine2, tvInvestLine3, tVTotalAmountInvested, tVTotalIncomeMonthly, tvInvest1, tvInvest2, tvInvest3;
    ImageView imInvest1, imInvest2, imInvest3, imInvest4, imInvest5, imInvest6, imInvest7, imInvest8, imInvest9, imInvest10;
    EditText ed_amount1, ed_amount2, ed_amount3, ed_amount4, ed_amount5, ed_amount6, ed_amount7, ed_amount8, ed_amount9, ed_amount10;
    TextView tvFreq1, tvFreq2, tvFreq3, tvFreq4, tvFreq5, tvFreq6, tvFreq7, tvFreq8, tvFreq9, tvFreq10;
    TextView tvAmount1, tvAmount2, tvAmount3;
    DecimalFormat formatter;
    Button btnCalculateInvest, btnBack, btnCompareInvestment,btnCalculateInvest1,btnInvestmentJourney,btnSave;
    LinearLayout layoutInput, layoutAllInvest, layoutInvest1, layoutInvest2, layoutInvest3,otherLayout;
    NumberFormatOnTyping numberFormatOnTyping;
    String email = "";
    SharedPreferences sharedPreferences;
    PieChart pieChart, pieChartInvest;
    ProgressDialog pDialog;
    ArrayList<DataModel> dataModels = new ArrayList<>();
    ArrayList<DataModel> dataModelsInvest = new ArrayList<>();
    InvestmentItem investmentItem;
    String investLine1 = "";
    String investLine2 = "";
    String investLine3 = "";
    String currency = "";
    int amount1 = 0;
    int amount2 = 0;
    int amount3 = 0;
    Dialog myDialog;
    InvestAdapter myAdapter;
    Frequency outflowFreq;
    public static ArrayList<InvestmentItem> dataClassArrayList = new ArrayList<>();
    ArrayList<InvestmentPass> investmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment_calculator);

        sharedPreferences = this.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL, "");
        currency = sharedPreferences.getString(CURRENCY, "");
        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("###,###,###,###");

        outflowFreq = new Frequency(getApplicationContext());
        assign();


        myAdapter = new InvestAdapter(this, dataClassArrayList);


        myDialog = new Dialog(this);

        numberFormatOnTyping = new NumberFormatOnTyping(this);

        numberFormatOnTyping.setNumberFormatOnTyping(edInvestmentAmount1);
        numberFormatOnTyping.setNumberFormatOnTyping(edInvestmentAmount2);
        numberFormatOnTyping.setNumberFormatOnTyping(edInvestmentAmount3);
        pDialog = new ProgressDialog(this);


        boolean isConnected = ConnectivityHelper.isConnectedToNetwork(this);

        if (isConnected) {
//            Toast.makeText(this, "Connected to the Internet", Toast.LENGTH_SHORT).show();
            getInvestList();
        }
        addTextListener();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInvestment();

            }
        });
        tvInvest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInvestList(tvInvest1);
            }
        });
        tvInvest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInvestList(tvInvest2);
            }
        });
        tvInvest3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInvestList(tvInvest3);
            }
        });

        ImageButton imBack = findViewById(R.id.imBack);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnCompareInvestment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentRequiredPopup paymentRequiredPopup = new PaymentRequiredPopup(getApplicationContext(),myDialog,
                        "To compare your investments in the market, please upgrade your account by tapping Upgrade Account");

                if(paymentRequiredPopup.checkPayment()){
                    Intent intent = new Intent(InvestmentCalculator.this, CompareInvestment.class);
                    startActivity(intent);
                }

            }
        });

        btnInvestmentJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PaymentRequiredPopup paymentRequiredPopup = new PaymentRequiredPopup(getApplicationContext(),myDialog,
                        "To start your investment journey plan please upgrade your account by tapping Upgrade Account");

                if(paymentRequiredPopup.checkPayment()){
                    Intent intent = new Intent(InvestmentCalculator.this, GoalPlannerActivity.class);
                    startActivity(intent);
                }

            }
        });



        btnCalculateInvest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                investmentList.add(new InvestmentPass(tvInvest01.getText().toString(),getAmountValue(ed_amount1),tvFreq1.getText().toString()));
                investmentList.add(new InvestmentPass(tvInvest02.getText().toString(),getAmountValue(ed_amount2),tvFreq2.getText().toString()));
                investmentList.add(new InvestmentPass(tvInvest03.getText().toString(),getAmountValue(ed_amount3),tvFreq3.getText().toString()));
                investmentList.add(new InvestmentPass(tvInvest04.getText().toString(),getAmountValue(ed_amount4),tvFreq4.getText().toString()));
                investmentList.add(new InvestmentPass(tvInvest05.getText().toString(),getAmountValue(ed_amount5),tvFreq5.getText().toString()));
                investmentList.add(new InvestmentPass(tvInvest06.getText().toString(),getAmountValue(ed_amount6),tvFreq6.getText().toString()));
                investmentList.add(new InvestmentPass(tvInvest07.getText().toString(),getAmountValue(ed_amount7),tvFreq7.getText().toString()));
                investmentList.add(new InvestmentPass(tvInvest08.getText().toString(),getAmountValue(ed_amount8),tvFreq8.getText().toString()));
                investmentList.add(new InvestmentPass(tvInvest09.getText().toString(),getAmountValue(ed_amount9),tvFreq9.getText().toString()));
                investmentList.add(new InvestmentPass(tvInvest010.getText().toString(),getAmountValue(ed_amount10),tvFreq10.getText().toString()));


//                if (isConnected) {
//                    pDialog.setMessage("Calculating  ...");
//                    pDialog.setIndeterminate(false);
//                    pDialog.setCancelable(false);
//                    pDialog.show();
//                    getInvest("https://mwalimubiashara.com/app/invest-numbers.php");
                    Intent intent = new Intent(InvestmentCalculator.this, ChartActivity.class);
                    intent.putExtra("investmentList", investmentList);
                    startActivity(intent);
//                } else {
//                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                }
            }
        });
        btnCalculateInvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isConnected) {
//            Toast.makeText(this, "Connected to the Internet", Toast.LENGTH_SHORT).show()

                    String amt1 = edInvestmentAmount1.getText().toString();
                    String amt2 = edInvestmentAmount2.getText().toString();
                    String amt3 = edInvestmentAmount3.getText().toString();

                    investLine1 = tvInvest1.getText().toString();
                    investLine2 = tvInvest2.getText().toString();
                    investLine3 = tvInvest3.getText().toString();
                    Boolean inv1 = true, inv2 = true, inv3 = true;
                    if (investLine1.equals("Select Investment") && !amt1.isEmpty() || investLine1.equals("Select Investment") && amt1.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please Select Investment", Toast.LENGTH_SHORT).show();
                        inv1 = false;
                    } else if (!investLine1.equals("Select Investment") && amt1.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_SHORT).show();
                        edInvestmentAmount1.setError("Please enter amount");
                        inv1 = false;
                    }
                    if (investLine2.equals("Select Investment") && !amt2.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please Select Investment", Toast.LENGTH_SHORT).show();
                        inv2 = false;
                    } else if (!investLine2.equals("Select Investment") && amt2.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_SHORT).show();
                        edInvestmentAmount2.setError("Please enter amount");
                        inv2 = false;
                    }
                    if (investLine3.equals("Select Investment") && !amt3.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please Select Investment", Toast.LENGTH_SHORT).show();
                        inv3 = false;
                    } else if (!investLine3.equals("Select Investment") && amt3.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_SHORT).show();
                        edInvestmentAmount3.setError("Please enter amount");
                        inv3 = false;
                    }
                    if (inv1 && inv2 && inv3 && !amt1.isEmpty()) {
                        tvInvestLine1.setText("Investment: " + investLine1);
                        tvInvestLine2.setText("Investment: " + investLine2);
                        tvInvestLine3.setText("Investment: " + investLine3);
                        tvAmount1.setText("Amount: "+currency+" " + amt1);
                        tvAmount2.setText("Amount: "+currency+" " + amt2);
                        tvAmount3.setText("Amount: "+currency+" " + amt3);
                        pDialog.setMessage("Calculating  ...");
                        pDialog.setIndeterminate(false);
                        pDialog.setCancelable(false);
                        pDialog.show();
                        amt1 = amt1.replaceAll(",", "");
                        amt2 = amt2.replaceAll(",", "");
                        amt3 = amt3.replaceAll(",", "");
                        Log.e("TAG", "Amount 1 is " + amt1 + " Amount 2 is " + amt2 + "Amount 3 is " + amt3);

                        if (!amt1.isEmpty()) {
                            amount1 = Integer.parseInt(amt1.toString());
                        }
                        if (!amt2.isEmpty()) {
                            amount2 = Integer.parseInt(amt2.toString());
                        }
                        if (!amt3.isEmpty()) {
                            amount3 = Integer.parseInt(amt3.toString());
                        }
                        tVTotalAmountInvested.setText(currency+" " + formatter.format(amount1 + amount2 + amount3));
//                        getInvest("https://mwalimubiashara.com/app/invest-numbers.php");
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backToInput();

            }
        });

    }

    private void assignFreq() {

        tvFreq1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq1);
            }
        });
        tvFreq2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq2);
            }
        });
        tvFreq3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq3);
            }
        });
        tvFreq4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq4);
            }
        });
        tvFreq5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq5);
            }
        });
        tvFreq6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq6);
            }
        });
        tvFreq7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq7);
            }
        });
        tvFreq8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq8);
            }
        });
        tvFreq9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq9);
            }
        });
        tvFreq10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq10);
            }
        });
    }

    private void assign() {
        ed_amount1 = findViewById(R.id.amount1);
        ed_amount2 = findViewById(R.id.amount2);
        ed_amount3 = findViewById(R.id.amount3);
        ed_amount4 = findViewById(R.id.amount4);
        ed_amount5 = findViewById(R.id.amount5);
        ed_amount6 = findViewById(R.id.amount6);
        ed_amount7 = findViewById(R.id.amount7);
        ed_amount8 = findViewById(R.id.amount8);
        ed_amount9 = findViewById(R.id.amount9);
        ed_amount10 = findViewById(R.id.amount10);

        tvFreq1 = findViewById(R.id.tvFreq1);
        tvFreq2 = findViewById(R.id.tvFreq2);
        tvFreq3 = findViewById(R.id.tvFreq3);
        tvFreq4 = findViewById(R.id.tvFreq4);
        tvFreq5 = findViewById(R.id.tvFreq5);
        tvFreq6 = findViewById(R.id.tvFreq6);
        tvFreq7 = findViewById(R.id.tvFreq7);
        tvFreq8 = findViewById(R.id.tvFreq8);
        tvFreq9 = findViewById(R.id.tvFreq9);
        tvFreq10 = findViewById(R.id.tvFreq10);

        pieChart = findViewById(R.id.idPieChart);
        pieChartInvest = findViewById(R.id.idPieChartInvest);
        edInvestmentAmount1 = findViewById(R.id.idInvestmentAmount1);
        edInvestmentAmount2 = findViewById(R.id.idInvestmentAmount2);
        edInvestmentAmount3 = findViewById(R.id.idInvestmentAmount3);
        tvInvestLine1 = findViewById(R.id.tvInvestLine1);
        tvInvestLine2 = findViewById(R.id.tvInvestLine2);
        tvInvestLine3 = findViewById(R.id.tvInvestLine3);
        tvAmount1 = findViewById(R.id.tvAmount1);
        tvAmount2 = findViewById(R.id.tvAmount2);
        tvAmount3 = findViewById(R.id.tvAmount3);
        tvInvest1 = findViewById(R.id.tvInvest1);
        tvInvest2 = findViewById(R.id.tvInvest2);
        tvInvest3 = findViewById(R.id.tvInvest3);
        tVTotalAmountInvested = findViewById(R.id.tVTotalAmountInvested);
        tVTotalIncomeMonthly = findViewById(R.id.tVTotalIncomeMonthly);
        tvmonthly_return1 = findViewById(R.id.tvmonthly_return1);
        tvRor1 = findViewById(R.id.tvRor1);
        tvmonthly_return_adjusted1 = findViewById(R.id.tvmonthly_return_adjusted1);
        tvAdjustedRor1 = findViewById(R.id.tvAdjustedRor1);
        tvmonthly_return2 = findViewById(R.id.tvmonthly_return2);
        tvRor2 = findViewById(R.id.tvRor2);
        tvmonthly_return_adjusted2 = findViewById(R.id.tvmonthly_return_adjusted2);
        tvAdjustedRor2 = findViewById(R.id.tvAdjustedRor2);
        tvmonthly_return3 = findViewById(R.id.tvmonthly_return3);
        tvRor3 = findViewById(R.id.tvRor3);
        tvmonthly_return_adjusted3 = findViewById(R.id.tvmonthly_return_adjusted3);
        tvAdjustedRor3 = findViewById(R.id.tvAdjustedRor3);
        layoutInput = findViewById(R.id.layoutInput);
        layoutAllInvest = findViewById(R.id.layoutAllInvest);
        otherLayout = findViewById(R.id.otherLayout);
        layoutInvest1 = findViewById(R.id.layoutInvest1);
        layoutInvest2 = findViewById(R.id.layoutInvest2);
        layoutInvest3 = findViewById(R.id.layoutInvest3);
        btnCalculateInvest = findViewById(R.id.btnCalculate);
        btnCalculateInvest1 = findViewById(R.id.btnCalculate1);
        btnBack = findViewById(R.id.btnBack);
        btnCompareInvestment = findViewById(R.id.btnCompare);
        imInvest1 = findViewById(R.id.imInvest1);
        imInvest2 = findViewById(R.id.imInvest2);
        imInvest3 = findViewById(R.id.imInvest3);
        imInvest4 = findViewById(R.id.imInvest4);
        imInvest5 = findViewById(R.id.imInvest5);
        imInvest6 = findViewById(R.id.imInvest6);
        imInvest7 = findViewById(R.id.imInvest7);
        imInvest8 = findViewById(R.id.imInvest8);
        imInvest9 = findViewById(R.id.imInvest9);
        imInvest10 = findViewById(R.id.imInvest10);
        tvAbout1 = findViewById(R.id.tvAbout1);
        tvAbout2 = findViewById(R.id.tvAbout2);
        tvAbout3 = findViewById(R.id.tvAbout3);
        tvAbout4 = findViewById(R.id.tvAbout4);
        tvAbout5 = findViewById(R.id.tvAbout5);
        tvAbout6 = findViewById(R.id.tvAbout6);
        tvAbout7 = findViewById(R.id.tvAbout7);
        tvAbout8 = findViewById(R.id.tvAbout8);
        tvAbout9 = findViewById(R.id.tvAbout9);
        tvAbout10 = findViewById(R.id.tvAbout10);

        tvInvest01 = findViewById(R.id.tvInvest01);
        tvInvest02 = findViewById(R.id.tvInvest02);
        tvInvest03 = findViewById(R.id.tvInvest03);
        tvInvest04 = findViewById(R.id.tvInvest04);
        tvInvest05 = findViewById(R.id.tvInvest05);
        tvInvest06 = findViewById(R.id.tvInvest06);
        tvInvest07 = findViewById(R.id.tvInvest07);
        tvInvest08 = findViewById(R.id.tvInvest08);
        tvInvest09 = findViewById(R.id.tvInvest09);
        tvInvest010 = findViewById(R.id.tvAbout10);

        btnSave = findViewById(R.id.btnSave);



        btnInvestmentJourney = findViewById(R.id.btnInvestmentJourney);



        assignFreq();
        addExplanation();
    }

    private void addExplanation() {
        imInvest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setExplanationText(tvAbout1);
            }
        });
        imInvest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setExplanationText(tvAbout2);
            }
        });
        imInvest3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setExplanationText(tvAbout3);
            }
        });
        imInvest4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setExplanationText(tvAbout4);
            }
        });
        imInvest5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setExplanationText(tvAbout5);
            }
        });
        imInvest6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setExplanationText(tvAbout6);
            }
        });
        imInvest7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setExplanationText(tvAbout7);
            }
        });
        imInvest8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setExplanationText(tvAbout8);
            }
        });
        imInvest9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setExplanationText(tvAbout9);
            }
        });
        imInvest10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setExplanationText(tvAbout10);
            }
        });
    }

    private void setExplanationText(TextView tv) {

        if (tv.getVisibility() == View.VISIBLE) {

            hideAll();

        } else {
            hideAll();
            tv.setVisibility(View.VISIBLE);
        }

    }

    private void hideAll() {
        tvAbout1.setVisibility(View.GONE);
        tvAbout2.setVisibility(View.GONE);
        tvAbout3.setVisibility(View.GONE);
        tvAbout4.setVisibility(View.GONE);
        tvAbout5.setVisibility(View.GONE);
        tvAbout6.setVisibility(View.GONE);
        tvAbout7.setVisibility(View.GONE);
        tvAbout8.setVisibility(View.GONE);
        tvAbout9.setVisibility(View.GONE);
        tvAbout10.setVisibility(View.GONE);
    }


    private void addTextListener() {

        numberFormatOnTyping.setNumberFormatOnTyping(ed_amount1, tvFreq1);
        numberFormatOnTyping.setNumberFormatOnTyping(ed_amount2, tvFreq2);
        numberFormatOnTyping.setNumberFormatOnTyping(ed_amount3, tvFreq3);
        numberFormatOnTyping.setNumberFormatOnTyping(ed_amount4, tvFreq4);
        numberFormatOnTyping.setNumberFormatOnTyping(ed_amount5, tvFreq5);
        numberFormatOnTyping.setNumberFormatOnTyping(ed_amount6, tvFreq6);
        numberFormatOnTyping.setNumberFormatOnTyping(ed_amount7, tvFreq7);
        numberFormatOnTyping.setNumberFormatOnTyping(ed_amount8, tvFreq8);
        numberFormatOnTyping.setNumberFormatOnTyping(ed_amount9, tvFreq9);
        numberFormatOnTyping.setNumberFormatOnTyping(ed_amount10, tvFreq10);

    }


    private void backToInput() {
        otherLayout.setVisibility(View.VISIBLE);
        layoutAllInvest.setVisibility(View.GONE);
    }

    public void showInvestList(TextView tvInvestLine) {
        TextView txtclose;
        ListView invest_listView;

        myDialog.setContentView(R.layout.invest_list_popup);
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        invest_listView = (ListView) myDialog.findViewById(R.id.invest_list);

        invest_listView.setAdapter(myAdapter);
        invest_listView.setClickable(true);

        txtclose.setText("x");

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myDialog.dismiss();

            }
        });

        invest_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tvInvestLine.setText(dataClassArrayList.get(i).getInvest_name());
//                dataClassArrayList.get(i).getInvest_desc();
                myDialog.dismiss();

            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }



    private String getAmountValue(EditText edText) {
        String str = edText.getText().toString();

        if (!str.isEmpty()) {
            str = str.replaceAll(",", "");
        }
        return str;
    }


    public int convertToInt(String str) {
        String amnt1 = str.replaceAll(",", "");

        amnt1 = amnt1.replaceAll(" ", "");
        amnt1 = amnt1.replaceAll("KES.", "");

//        int amount1 = Float.parseFloat(amnt1);
        int amount1 = Integer.parseInt(amnt1);

        Log.e("TAG", "String in " + str + ", Int out " + amount1);

        return amount1;
    }

    private void getInvestList() {

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, "https://mwalimubiashara.com/app/investment_list.php", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);

                dataClassArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    int l = 0;
                    l = jsonArray.length();

                    for (int i = 0; i < l; i++) {

                        JSONObject object = jsonArray.getJSONObject(i);

                        String invest = object.getString("invest");
                        String desc = object.getString("desc");

                        investmentItem = new InvestmentItem(invest, desc);

                        dataClassArrayList.add(investmentItem);
                    }

                    myAdapter.notifyDataSetChanged();

                } catch (Exception e) {

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(InvestmentCalculator.this, "Fail to get response ", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {

                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

        };

        queue.add(request);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        if(layoutInvest2.getVisibility()==View.VISIBLE){
//            getMenuInflater().inflate(R.menu.calculator_menu, menu);
////        MenuItem editItem = menu.findItem(R.id.action_edit);
//            MenuItem saveMenu = menu.findItem(R.id.action_save);
//            MenuItem notificationMenu = menu.findItem(R.id.notification);
//
//            saveMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
//                    saveInvestment();
//                    return true;
//                }
//            });
//
//
//        return super.onCreateOptionsMenu(menu);
//    }
    private  void saveInvestment(){


        String trial = sharedPreferences.getString(TRIAL, "");
        String package_type = sharedPreferences.getString(PACKAGE_TYPE,"");

        Intent intent = new Intent(InvestmentCalculator.this, PaymentActivity.class);

        intent.putExtra("from_activity","investment");
        intent.putExtra("next_activity","InvestmentRecommendationCalculator");

        Log.e("TAG","Account status is "+trial+" Package is "+package_type);
        if(!package_type.equals("basic")||!package_type.equals("Trial")){
            if(trial.equals("active")){

                Log.e("TAG","Account status is "+trial+" Package is "+package_type);
                intent = new Intent(InvestmentCalculator.this, InvestmentRecommendationCalculator.class);
                intent.putExtra("url", "investment_plan_download.php");
                intent.putExtra("activity_name", "Investment Plan Pdf");

            }
        }

        startActivity(intent);

    }
    private void setupPieChart(List<DataModel> dataModels) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (DataModel dataModel : dataModels) {
            entries.add(new PieEntry(dataModel.getValue(), dataModel.getLabel()));
        }
        ArrayList<PieEntry> entriesInvest = new ArrayList<>();
        for (DataModel dataModel : dataModelsInvest) {
            entriesInvest.add(new PieEntry(dataModel.getValue(), dataModel.getLabel()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(20f); // Set size for value text (e.g., percentages)
        dataSet.setValueTextColor(Color.BLACK); // Set color for value text


        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextColor(getResources().getColor(R.color.text_color));
        legend.setWordWrapEnabled(true);
        legend.setTextSize(11);
        legend.setFormSize(10);
        legend.setFormToTextSpace(2);
        legend.setEnabled(true);

        PieData data = new PieData(dataSet);
//        data.setDrawValues(false);

        PieDataSet dataSetInvest = new PieDataSet(entriesInvest, "");
        dataSetInvest.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSetInvest.setValueTextSize(16f); // Set size for value text (e.g., percentages)
        dataSetInvest.setValueTextColor(Color.BLACK); // Set color for value text

        Legend legendInvest = pieChartInvest.getLegend();
        legendInvest.setEnabled(false);



        PieData dataInvest = new PieData(dataSetInvest);
//        dataInvest.setHighlightEnabled(true);

        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.invalidate(); // refresh

        pieChartInvest.setData(dataInvest);
        pieChartInvest.setDrawEntryLabels(false);
        pieChartInvest.getDescription().setEnabled(false);
        pieChartInvest.invalidate();
    }

}