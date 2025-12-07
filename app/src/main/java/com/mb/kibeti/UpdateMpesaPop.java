package com.mb.kibeti;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mb.kibeti.invest_guide.InputScreenActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Response;

public class UpdateMpesaPop {

    PipeLines util = new PipeLines();
    String ADD_ACTUAL_EXPENSES_URL = util.ADD_ACTUAL_EXPENSES_URL;
    String GET_ACTUAL_EXPENSE_LIST_URL = util.GET_ACTUAL_EXPENSE_LIST_URL;
    TextView txtclose;
    Button submitBtn, deleteBtn;
    EditText edCat, edLine, edAmount, edDate;
    TextView etExpenseLine;
    Spinner freq;
    Dialog myDialog, myDialogNotBudgeted, myDialogBudget, lineDialogPopup, popupView;
    Dialog showDescriptionPopUp;
    BottomSheetDialog bottomSheetDialog;
    Context c;
    Boolean hasNotAccepted = false;
    ArrayList<String> categories = new ArrayList<String>();
    DecimalFormat formatter;
    String email = "";
    Boolean isMpesaAdded = false;
    CheckBox checkBox;
    ListView allocation_spinner_listview;
    String line = "", strDate = "", mpesa_trans = "", expense_amount = "0";
    String expense = "0";
    ArrayList<String> basicCat = new ArrayList<String>();
    ArrayList<DataClass> dataClassArrayList = new ArrayList<>();
    AllocationSpinnerAdapter myAdapter;
    DataClass dataClass;
    String expense_id = "0";
    String expense_line = "none";
    NumberFormatOnTyping numberFormatOnTyping;

    private List<String> itemList = new ArrayList<>();
    private PopupSearchAdapter otherLinesAdapter;


    public UpdateMpesaPop(Context c, String email) {
        this.c = c;
        this.email = email;

        basicCat.add("Car Fuel");
        basicCat.add("Fare");
        basicCat.add("School fees");
        basicCat.add("Shopping groceries");
        basicCat.add("Utilities(elect water gas wi-fi)");
        basicCat.add("Entertainment");
        basicCat.add("Savings- Emergency Fund");
        basicCat.add("Shelter/Rent");

        getBudgetedLine();

        onCreatePop();
    }

    private String checkRepeat() {
        String msg = "";

        if (checkBox.isChecked())
            msg = msg + "repeat";


        return msg;
    }

    private void showCategoryBottomSheet() {
        BottomSheetDialog dialog = new BottomSheetDialog(c);
        View view = LayoutInflater.from(c).inflate(R.layout.outflow_budget_pop, null);

        RecyclerView rv = view.findViewById(R.id.rvCategories);

        List<String> data;
        data = Arrays.asList(
                "Shopping", "Food and Beverage", "Entertainment", "Transport and Travel",
                "Bills, Airtime and Services", "Education", "County and Government",
                "Loans", "Family and Friends", "Contributions", "Shelter", "View More"
        );

        OutflowBudgetPopupAdapter adapter = new OutflowBudgetPopupAdapter(data, text -> {
//            Toast.makeText(c, "Clicked: " + text, Toast.LENGTH_SHORT).show();
            if (!text.equals("View More")) {
                etExpenseLine.setText(text);
            } else {
                showOtherLinesPopup();
            }

            dialog.dismiss();
        });

        rv.setLayoutManager(new LinearLayoutManager(c));
        rv.setAdapter(adapter);

        dialog.setContentView(view);
        dialog.show();
    }

    private void createBottomSheetDialog() {
        if (bottomSheetDialog == null) {
            View v = LayoutInflater.from(c).inflate(R.layout.outflow_budget_pop, null);

//            Button btnPlan = v.findViewById(R.id.idBtnInvestmentPlan);
//
//            Button btnLumpsum = v.findViewById(R.id.idBtnLumpsum);
//
//            btnLumpsum.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    bottomSheetDialog.dismiss();
//                    Intent intent = new Intent(view.getContext(), InputScreenActivity.class);
////                    Intent intent = new Intent(view.getContext(), InvestmentModelActivity.class);
//                    view.getContext().startActivity(intent);
//                }
//            });
//            btnPlan.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    bottomSheetDialog.dismiss();
////                    Intent intent = new Intent(view.getContext(), InvestmentRecommendationCalculator.class);
//                    Intent intent = new Intent(view.getContext(), InvestmentCalculator.class);
//                    view.getContext().startActivity(intent);
//                }
//            });

            bottomSheetDialog = new BottomSheetDialog(c);

            bottomSheetDialog.setContentView(v);

            bottomSheetDialog.show();
        } else {
            bottomSheetDialog.show();
        }
    }

    private void onCreatePop() {

        myDialog = new Dialog(c);
        myDialog.setContentView(R.layout.custom_mpesa_popup);

        numberFormatOnTyping = new NumberFormatOnTyping(c);
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        etExpenseLine = (TextView) myDialog.findViewById(R.id.etExpenseLine);
        deleteBtn = (Button) myDialog.findViewById(R.id.idBtnDelete);
        submitBtn = (Button) myDialog.findViewById(R.id.idBtnSave);
        edCat = (EditText) myDialog.findViewById(R.id.idCat);
        edLine = (EditText) myDialog.findViewById(R.id.idSent);
        edDate = (EditText) myDialog.findViewById(R.id.idDate);
        edAmount = (EditText) myDialog.findViewById(R.id.idAmount);
        freq = (Spinner) myDialog.findViewById(R.id.idLine);
        checkBox = (CheckBox) myDialog.findViewById(R.id.checkBox);


        myAdapter = new AllocationSpinnerAdapter(c, dataClassArrayList);
    }

    public void showPopup(String cat, String line, String am, String f, String id) {


        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("###,###,###,###");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, categories);


//        categories.add("Fare");
//        categories.add("Others");

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        freq.setAdapter(dataAdapter);
//        etExpenseLine.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopup();
//            }
//        });
        txtclose.setText("x");


        String s1 = am;
//        String mpesa_trans_id = id;

        String no_o = s1.replaceAll(" ", "");
        String no_o1 = no_o.replaceAll("KES", "");
        String no_o2 = no_o1.replaceAll(",", "");

        edCat.setText(cat);
        edLine.setText(line);
        edDate.setText(f);
        checkBox.setText("Auto-allocate all future and unallocated transactions under " + line);

        Long longval = Long.parseLong(no_o2);

        String formattedString = formatter.format(longval);

        edAmount.setText(formattedString);


        edAmount.addTextChangedListener(new TextWatcher() {
            //            int budgetAmount1=0;
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                int number = Integer.parseInt(s.toString());
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

                    int number = Integer.parseInt(originalString);

//                    valencyAmount =  number - budgetAmount1;
                } else {
//                    valencyAmount = 0 - budgetAmount1;
                }
//                valency.setText(formatter.format(valencyAmount));

                edAmount.addTextChangedListener(this);
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progressBar.setVisibility(View.VISIBLE);
//                income_id = "0";

                String income_frequency, category, income_line, mpesa_trans, income_amount, strDate;

                income_frequency = etExpenseLine.getText().toString();
                mpesa_trans = edCat.getText().toString();
                income_line = edLine.getText().toString();
                strDate = edDate.getText().toString();
                String[] datesp1 = strDate.split("-");

                String date = datesp1[2] + "-" + datesp1[1] + "-" + datesp1[0];


                income_amount = edAmount.getText().toString().replaceAll(",", "");

                // validating the text fields if empty or not.

                if (TextUtils.isEmpty(income_amount)) {
                    edAmount.setError("Please enter amount");
                }

                if (!TextUtils.isEmpty(income_frequency) && !TextUtils.isEmpty(income_amount) && !income_frequency.equals("select line")) {


                    String[] sp = income_frequency.split("-");

                    String id = expense_id;
                    String line = "none";

                    if (id.equals("0")) {

                        line = income_frequency;
                        id = "0";
//                        setLine(line);
                        setStrtDate(strDate);
                        setTransCode(mpesa_trans);
                        setAmount(income_amount);

                        if (hasNotAccepted) {

                            addDataToDatabase(income_amount, expense_id, expense_line, strDate, mpesa_trans, email);

                        } else {
                            showLineNotBudgeted(line);

                        }


                    } else {
                        addDataToDatabase(income_amount, expense_id, expense_line, strDate, mpesa_trans, email);
                    }


                } else {
//                    progressBar.setVisibility(View.GONE);
                }
            }


        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                delete(id);
            }
        });

//        btnFollow = (Button) myDialog.findViewById(R.id.btnfollow);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                delete();
                myDialog.dismiss();
//                progressBar.setVisibility(View.VISIBLE);
//                getMpesaStatus();
            }
        });

        etExpenseLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showPopup();
//                createBottomSheetDialog();
                showCategoryBottomSheet();
//                Toast.makeText(c, "Popup Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        myDialog.show();
    }


    private void setAmount(String i_amount) {
//                i_amount = i_amount;
        expense_amount = i_amount;
    }

    private void setTransCode(String m_trans) {
        mpesa_trans = m_trans;
    }

    private void setStrtDate(String strDt) {
        strDate = strDt;
    }

    private void showLineNotBudgeted(String line) {

        myDialogNotBudgeted = new Dialog(c);
//        myDialogNotBudgeted.dismiss();
        Button submitBtn;
        TextView txtShowMessage, tvHeader, cancelBtn;

        myDialogNotBudgeted.setContentView(R.layout.allow_permission_pop_up);
        txtShowMessage = (TextView) myDialogNotBudgeted.findViewById(R.id.monthlyAmount);
        tvHeader = (TextView) myDialogNotBudgeted.findViewById(R.id.textView2);
        cancelBtn = (TextView) myDialogNotBudgeted.findViewById(R.id.idBtnCancel);
        submitBtn = (Button) myDialogNotBudgeted.findViewById(R.id.idBtnReset);

//        txtShowMessage.setText(line + " has not been budgeted for, would you like to budget now before allocating?");
        txtShowMessage.setText(" ");
        tvHeader.setText(line + "");
        tvHeader.setTextSize(20);

        cancelBtn.setVisibility(View.VISIBLE);

        submitBtn.setText("Create budget for this item");
        cancelBtn.setText("Continue without budgeting");
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myDialogNotBudgeted.dismiss();

                showBudgetLinePopup(line);

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialogNotBudgeted.dismiss();
                showDescriptionNotBudgeted();
//              myDialogNotBudgeted.setContentView(R.layout.custom_mpesa_popup);
            }
        });

        myDialogNotBudgeted.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialogNotBudgeted.show();

    }

    private void showDescriptionNotBudgeted() {

        showDescriptionPopUp = new Dialog(c);
        Button submitBtn;
        TextView txtShowMessage, tvHeader, cancelBtn;

        showDescriptionPopUp.setContentView(R.layout.allow_permission_pop_up);
        txtShowMessage = (TextView) showDescriptionPopUp.findViewById(R.id.monthlyAmount);
        tvHeader = (TextView) showDescriptionPopUp.findViewById(R.id.textView2);
        cancelBtn = (TextView) showDescriptionPopUp.findViewById(R.id.idBtnCancel);
        submitBtn = (Button) showDescriptionPopUp.findViewById(R.id.idBtnReset);

//        txtShowMessage.setText(line + " has not been budgeted for, would you like to budget now before allocating?");
        txtShowMessage.setText("Budgeting for this item takes less than a minute — and it can boost your chances of success by more than 50%!\n\n " +
                "This is done once in a lifetime.");
        tvHeader.setText("Why budget?");
        tvHeader.setTextSize(15);

        cancelBtn.setVisibility(View.VISIBLE);

        submitBtn.setText("Budget Now");
        cancelBtn.setText("Remind me later");
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDescriptionPopUp.dismiss();
                hasNotAccepted = true;
                showBudgetLinePopup(line);

                ;

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDescriptionPopUp.dismiss();
                hasNotAccepted = true;
//              myDialogNotBudgeted.setContentView(R.layout.custom_mpesa_popup);
            }
        });


        showDescriptionPopUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        showDescriptionPopUp.show();

    }

    private void showBudgetLinePopup(String line) {
        myDialogBudget = new Dialog(c);

        TextView txtclose, tvHeader, tvAmount;
        Button btnFollow, submitBtn, deleteBtn;
        EditText edLine, edAmount;
        LinearLayout layoutCat;
        Spinner freq;
        myDialogBudget.setContentView(R.layout.outflowpopup);
        txtclose = (TextView) myDialogBudget.findViewById(R.id.txtclose);
        deleteBtn = (Button) myDialogBudget.findViewById(R.id.idBtnDelete);
        submitBtn = (Button) myDialogBudget.findViewById(R.id.idBtnSave);
//        edCat = (EditText) myDialogBudget.findViewById(R.id.idCat);
        edLine = (EditText) myDialogBudget.findViewById(R.id.idLine);
        tvHeader = (TextView) myDialogBudget.findViewById(R.id.tvHeader);
        tvAmount = (TextView) myDialogBudget.findViewById(R.id.tvAmount);
        layoutCat = (LinearLayout) myDialogBudget.findViewById(R.id.layoutCat);
        edAmount = (EditText) myDialogBudget.findViewById(R.id.idAmount);
        freq = (Spinner) myDialogBudget.findViewById(R.id.idFrequency);


        numberFormatOnTyping.setNumberFormatOnTyping(edAmount);
        layoutCat.setVisibility(View.GONE);
        deleteBtn.setVisibility(View.GONE);
        tvHeader.setText("Set Budget");
        tvAmount.setText("Budget Amount");
        submitBtn.setText("Set Budget");
        List<String> categories = new ArrayList<String>();
        categories.add("select Frequency");
        categories.add("Daily");
        categories.add("Weekly");
        categories.add("Monthly");
        categories.add("Quarterly");
        categories.add("Semi Annually");
        categories.add("Annually");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        freq.setAdapter(dataAdapter);

        txtclose.setText("x");
//        edCat.setText(cat);
        edLine.setText(line);
//        edAmount.setText(am);

//        Intent intent = this.getIntent();
//        if (intent != null) {

//        String s1 = am;
//        String income_id = id;
//
//        String no_o = s1.replaceAll(" ", "");
//        String no_o1 = no_o.replaceAll("KES", "");
//        String no_o2 = no_o1.replaceAll(",", "");
//
//        edCat.setText("");
//        edLine.setText(line);
//        edAmount.setText(no_o2);
//            freq.setSelection(f);
//        }

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progressBar.setVisibility(View.VISIBLE);
//                income_id = "0";
                String income_frequency, category, income_line, income_amount;
                income_frequency = freq.getSelectedItem().toString();
                category = edCat.getText().toString();
                income_line = edLine.getText().toString();
                income_amount = edAmount.getText().toString();
//                income_id = "0";
                // validating the text fields if empty or not.

                if (category.contains("Tax")) {
                    edCat.setError("You cannot edit tax");
                } else {

                    if (TextUtils.isEmpty(income_amount)) {
                        edAmount.setError("Please enter amount");
                    }
                    if (income_frequency.equals("select Frequency")) {
                        Toast.makeText(c, "Please select frequency", Toast.LENGTH_SHORT).show();
                    }
                    if (!TextUtils.isEmpty(income_frequency) && !TextUtils.isEmpty(income_amount) && !income_frequency.equals("select Frequency")) {
                        // calling method to add data to Firebase Firestore.
//                        addDataToDatabase(id, category, income_line, income_amount, income_frequency);
//                        progressBar.setVisibility(View.VISIBLE);
                        addDataToDatabase(income_amount, income_frequency, line);


//
                    } else {
//                    progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });
//        deleteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String category = edCat.getText().toString();
//                if (category.contains("Tax")) {
//                    edCat.setError("You cannot edit tax");
//                } else {
////                    delete(id);
//
//                }
//            }
//        });

//        btnFollow = (Button) myDialogBudget.findViewById(R.id.btnfollow);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                delete();
                myDialogBudget.dismiss();
//                progressBar.setVisibility(View.VISIBLE);
//                getInflow(email,url);
            }
        });
        hasNotAccepted = false;
        myDialogBudget.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialogBudget.show();
    }

    public boolean isStringInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private void addDataToDatabase(String income_amount, String frequency, String line) {


        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(c);


        StringRequest request = new StringRequest(Request.Method.POST, util.UPDATE_OUTFLOW_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                progressBar.setVisibility(View.GONE);
                Log.e("TAG", "RESPONSE IS " + response);

                Intent intent = new Intent(c, OutflowChart.class);
//                Intent intent = new Intent(c, PieChartActivity.class);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    isMpesaAdded = jsonObject.getBoolean("error");
//                    JSONArray jsonArray =jsonObject.getJSONArray("data");
                    if (!isMpesaAdded) {

                        Toast.makeText(c, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        SuccessDialog successDialog = new SuccessDialog();
//                        c.startActivity(intent);
                        getBudgetedLine();
                        expense = jsonObject.getString("expense_id");

                        addDataToDatabase(expense_amount, expense, line, strDate, mpesa_trans, email);

//                        Toast.makeText(c, "Amount "+expense_amount+" expense "+expense+"line "+line+" date "+strDate+" trn cd "+mpesa_trans, Toast.LENGTH_SHORT).show();
                        myDialogBudget.dismiss();

                    } else {

                        Toast.makeText(c, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                    //              Toast.makeText(YourGoalActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
//                progressBar.setVisibility(View.GONE);
                Toast.makeText(c, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                params.put("amount", income_amount);
                params.put("frequency", frequency);
                params.put("line", line);
                return params;
            }
        };
        queue.add(request);
//        Toast.makeText(AddActualActivity.this, "Id "+income_id+". Freq "+income_frequency+". Amnt "+income_amount, Toast.LENGTH_SHORT).show();
    }

    private void addDataToDatabase(String income_amount, String income_id, String line, String strDate, String mpesa_trans_id, String email) {


//        if (cashflow.equals("inflow")) {
//            url = "https://mwalimubiashara.com/app/add_actual_inflow.php";
//        }

        // creating a new variable for our request queue
        String repeat = checkRepeat();
        RequestQueue queue = Volley.newRequestQueue(c);

        StringRequest request = new StringRequest(Request.Method.POST, ADD_ACTUAL_EXPENSES_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                progressBar.setVisibility(View.GONE);
                Log.e("TAG", "RESPONSE IS " + response);

                Intent intent = new Intent(c, OutflowChart.class);
//                Intent intent = new Intent(c, PieChartActivity.class);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    isMpesaAdded = jsonObject.getBoolean("error");
//                    JSONArray jsonArray =jsonObject.getJSONArray("data");
                    if (!isMpesaAdded) {

                        Toast.makeText(c, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        c.startActivity(intent);
                        myDialog.dismiss();

                    } else {

                        Toast.makeText(c, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                    //              Toast.makeText(YourGoalActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
//                progressBar.setVisibility(View.GONE);
                Toast.makeText(c, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                params.put("line", line);
                params.put("mpesa_trans_id", mpesa_trans_id);
                params.put("repeat", repeat);

                return params;
            }
        };
        queue.add(request);
//        Toast.makeText(AddActualActivity.this, "Id "+income_id+". Freq "+income_frequency+". Amnt "+income_amount, Toast.LENGTH_SHORT).show();
    }

//    private void showPopup() {
//
//        lineDialogPopup = new Dialog(c);
//        lineDialogPopup.setContentView(R.layout.allocation_spinner);
//
//
////        getOutflowLine();
//
////        getBudgetedLine(email);
//        loadItems();
//        getInflow();
//        allocation_spinner_listview = lineDialogPopup.findViewById(R.id.allocation_spinner_listview);
//        Button otherLine = lineDialogPopup.findViewById(R.id.idBtnOther);
//
//
//
//        allocation_spinner_listview.setAdapter(myAdapter);
//        allocation_spinner_listview.setClickable(true);
//
//
//        otherLine.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showOtherLinesPopup();
//
//                lineDialogPopup.dismiss();
//            }
//        });
//        allocation_spinner_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                expense_line = dataClassArrayList.get(i).getLine();
//
//                expense_id = dataClassArrayList.get(i).getId();
//
//                Log.e("TAG Item Tapped ", "Line >> " + dataClassArrayList.get(i).getLine() + "\n Id is >> " + dataClassArrayList.get(i).getId());
//
//                etExpenseLine.setText(expense_line);
//                lineDialogPopup.dismiss();
//            }
//        });
//
//        lineDialogPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        lineDialogPopup.show();
//
//
//    }

    //    private void getOutflowLine() {
//
//        RequestQueue queue = Volley.newRequestQueue(c);
//
//        StringRequest request = new StringRequest(Request.Method.POST, "https://mwalimubiashara.com/app/get_mpesa_status.php", new com.android.volley.Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.e("TAG", "RESPONSE IS " + response);
////                progressBar.setVisibility(View.GONE);
//                dataClassArrayList.clear();
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("success");
//                    JSONArray jsonArray = jsonObject.getJSONArray("data");
//
//                    if (success.equals("1")) {
//
//                        int l = 0;
//                        l = jsonArray.length();
//
//                        for (int i = 0; i < l; i++) {
//                            JSONObject object = jsonArray.getJSONObject(i);
//
//                            String cat = object.getString("income_cat");
//                            String line = object.getString("income_line");
//                            String freq = object.getString("income_freq");
//                            String amount = object.getString("income_amount");
//                            String id = object.getString("income_id");
//                            String time = object.getString("income_actual_amount");
//
//                            dataClass = new DataClass(id, cat, line, amount, freq, time);
//                            dataClassArrayList.add(dataClass);
//
//
//                        }

    /// /                        if (l > 0) {
    /// /                            linearLayout.setVisibility(View.GONE);
    /// /                        } else {
    /// /                            linearLayout.setVisibility(View.VISIBLE);
    /// /                        }
//                    }
//                    myAdapter.notifyDataSetChanged();
//
//                } catch (Exception e) {
//
//                }
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // method to handle errors.
//                Toast.makeText(c, "Fail to get response ", Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            public String getBodyContentType() {
//
//                return "application/x-www-form-urlencoded; charset=UTF-8";
//            }
//
//            @Override
//            protected Map<String, String> getParams() {
//
//                Map<String, String> params = new HashMap<String, String>();
//
//                params.put("email", email);
//                params.put("cashflow", "outflow");
//
//                return params;
//            }
//        };
//
//        queue.add(request);
//    }
    private void addBasicCat() {
        addToCategoryList("Car Fuel");
        addToCategoryList("Fare");
        addToCategoryList("School fees");
        addToCategoryList("Utilities(elect water gas wi-fi)");
        addToCategoryList("Entertainment");
        addToCategoryList("Savings- Emergency Fund");
        addToCategoryList("Shelter Rent");

        for (String value : basicCat) {
            dataClass = new DataClass(0 + "", value, value, "0", "-", "cat");
            dataClassArrayList.add(dataClass);

        }

    }


    private void showOtherLinesPopup() {

//        loadItems();

        ImageButton arrow_button, arrow_button1, arrow_button2, arrow_button3, arrow_button4, arrow_button5, arrow_button6, arrow_button7, arrow_button8;
        LinearLayout hidden_view, hidden_view1, hidden_view2, hidden_view3, hidden_view4, hidden_view5, hidden_view6, hidden_view7, hidden_view8;
        CardView cardView, cardView1, cardView2, cardView3, cardView4, cardView5, cardView6, cardView7, cardView8;

        TextView tvLine1, tvLine2, tvLine3, tvLine4, tvLine5, tvLine6, tvLine7, tvLine8, tvLine9, tvLine10;
        TextView tvLine11, tvLine12, tvLine13, tvLine14, tvLine15, tvLine16, tvLine17, tvLine18, tvLine19, tvLine20;
        TextView tvLine21, tvLine22, tvLine23, tvLine24, tvLine25, tvLine26, tvLine27, tvLine28, tvLine29, tvLine30;
        TextView tvLine31, tvLine32, tvLine33, tvLine34, tvLine35, tvLine36, tvLine37, tvLine38, tvLine39, tvLine40;
        TextView tvLine41, tvLine42, tvLine43, tvLine44, tvLine45, tvLine46, tvLine47, tvLine48, tvLine49, tvLine50;
        TextView tvLine51, tvLine52, tvLine53, tvLine54, tvLine55, tvLine56, tvLine57, tvLine58, tvLine59, tvLine60;
        TextView tvLine61, tvLine62, tvLine63, tvLine64, tvLine65, tvLine66, tvLine67, tvLine68, tvLine69, tvLine70;


        popupView = new Dialog(c);
        popupView.setContentView(R.layout.popup_search);

        EditText searchBox = popupView.findViewById(R.id.searchBox);
        RecyclerView recyclerView = popupView.findViewById(R.id.recyclerView);

        arrow_button = popupView.findViewById(R.id.arrow_button);
        arrow_button1 = popupView.findViewById(R.id.arrow_button1);
        arrow_button2 = popupView.findViewById(R.id.arrow_button2);
        arrow_button3 = popupView.findViewById(R.id.arrow_button3);
        arrow_button4 = popupView.findViewById(R.id.arrow_button4);
        arrow_button5 = popupView.findViewById(R.id.arrow_button5);
        arrow_button6 = popupView.findViewById(R.id.arrow_button6);
        arrow_button7 = popupView.findViewById(R.id.arrow_button7);
        arrow_button8 = popupView.findViewById(R.id.arrow_button8);

        hidden_view = popupView.findViewById(R.id.hidden_view);
        hidden_view1 = popupView.findViewById(R.id.hidden_view1);
        hidden_view2 = popupView.findViewById(R.id.hidden_view2);
        hidden_view3 = popupView.findViewById(R.id.hidden_view3);
        hidden_view4 = popupView.findViewById(R.id.hidden_view4);
        hidden_view5 = popupView.findViewById(R.id.hidden_view5);
        hidden_view6 = popupView.findViewById(R.id.hidden_view6);
        hidden_view7 = popupView.findViewById(R.id.hidden_view7);
        hidden_view8 = popupView.findViewById(R.id.hidden_view8);

        cardView = popupView.findViewById(R.id.base_cardview);
        cardView1 = popupView.findViewById(R.id.base_cardview1);
        cardView2 = popupView.findViewById(R.id.base_cardview2);
        cardView3 = popupView.findViewById(R.id.base_cardview3);
        cardView4 = popupView.findViewById(R.id.base_cardview4);
        cardView5 = popupView.findViewById(R.id.base_cardview5);
        cardView6 = popupView.findViewById(R.id.base_cardview6);
        cardView7 = popupView.findViewById(R.id.base_cardview7);
        cardView8 = popupView.findViewById(R.id.base_cardview8);


        tvLine1 = popupView.findViewById(R.id.tvLine1);
        tvLine2 = popupView.findViewById(R.id.tvLine2);
        tvLine3 = popupView.findViewById(R.id.tvLine3);
        tvLine4 = popupView.findViewById(R.id.tvLine4);
        tvLine5 = popupView.findViewById(R.id.tvLine5);
        tvLine6 = popupView.findViewById(R.id.tvLine6);
        tvLine7 = popupView.findViewById(R.id.tvLine7);
        tvLine8 = popupView.findViewById(R.id.tvLine8);
        tvLine9 = popupView.findViewById(R.id.tvLine9);
        tvLine10 = popupView.findViewById(R.id.tvLine10);

        tvLine11 = popupView.findViewById(R.id.tvLine11);
        tvLine12 = popupView.findViewById(R.id.tvLine12);
        tvLine13 = popupView.findViewById(R.id.tvLine13);
        tvLine14 = popupView.findViewById(R.id.tvLine14);
        tvLine15 = popupView.findViewById(R.id.tvLine15);
        tvLine16 = popupView.findViewById(R.id.tvLine16);
        tvLine17 = popupView.findViewById(R.id.tvLine17);
        tvLine18 = popupView.findViewById(R.id.tvLine18);
        tvLine19 = popupView.findViewById(R.id.tvLine19);
        tvLine20 = popupView.findViewById(R.id.tvLine20);

        tvLine21 = popupView.findViewById(R.id.tvLine21);
        tvLine22 = popupView.findViewById(R.id.tvLine22);
        tvLine23 = popupView.findViewById(R.id.tvLine23);
        tvLine24 = popupView.findViewById(R.id.tvLine24);
        tvLine25 = popupView.findViewById(R.id.tvLine25);
        tvLine26 = popupView.findViewById(R.id.tvLine26);
        tvLine27 = popupView.findViewById(R.id.tvLine27);
        tvLine28 = popupView.findViewById(R.id.tvLine28);
        tvLine29 = popupView.findViewById(R.id.tvLine29);
        tvLine30 = popupView.findViewById(R.id.tvLine30);

        tvLine31 = popupView.findViewById(R.id.tvLine31);
        tvLine32 = popupView.findViewById(R.id.tvLine32);
        tvLine33 = popupView.findViewById(R.id.tvLine33);
        tvLine34 = popupView.findViewById(R.id.tvLine34);
        tvLine35 = popupView.findViewById(R.id.tvLine35);
        tvLine36 = popupView.findViewById(R.id.tvLine36);
        tvLine37 = popupView.findViewById(R.id.tvLine37);
        tvLine38 = popupView.findViewById(R.id.tvLine38);
        tvLine39 = popupView.findViewById(R.id.tvLine39);
        tvLine40 = popupView.findViewById(R.id.tvLine40);

        tvLine41 = popupView.findViewById(R.id.tvLine41);
        tvLine42 = popupView.findViewById(R.id.tvLine42);
        tvLine43 = popupView.findViewById(R.id.tvLine43);
        tvLine44 = popupView.findViewById(R.id.tvLine44);
        tvLine45 = popupView.findViewById(R.id.tvLine45);
        tvLine46 = popupView.findViewById(R.id.tvLine46);
        tvLine47 = popupView.findViewById(R.id.tvLine47);
        tvLine48 = popupView.findViewById(R.id.tvLine48);
        tvLine49 = popupView.findViewById(R.id.tvLine49);
        tvLine50 = popupView.findViewById(R.id.tvLine50);

        tvLine51 = popupView.findViewById(R.id.tvLine51);
        tvLine52 = popupView.findViewById(R.id.tvLine52);
        tvLine53 = popupView.findViewById(R.id.tvLine53);
        tvLine54 = popupView.findViewById(R.id.tvLine54);
        tvLine55 = popupView.findViewById(R.id.tvLine55);
        tvLine56 = popupView.findViewById(R.id.tvLine56);
        tvLine57 = popupView.findViewById(R.id.tvLine57);
        tvLine58 = popupView.findViewById(R.id.tvLine58);
        tvLine59 = popupView.findViewById(R.id.tvLine59);
        tvLine60 = popupView.findViewById(R.id.tvLine60);

        setOnclickListenerTextView(tvLine1);
        setOnclickListenerTextView(tvLine2);
        setOnclickListenerTextView(tvLine3);
        setOnclickListenerTextView(tvLine4);
        setOnclickListenerTextView(tvLine5);
        setOnclickListenerTextView(tvLine6);
        setOnclickListenerTextView(tvLine7);
        setOnclickListenerTextView(tvLine8);
        setOnclickListenerTextView(tvLine9);
        setOnclickListenerTextView(tvLine10);

        setOnclickListenerTextView(tvLine11);
        setOnclickListenerTextView(tvLine12);
        setOnclickListenerTextView(tvLine13);
        setOnclickListenerTextView(tvLine14);
        setOnclickListenerTextView(tvLine15);
        setOnclickListenerTextView(tvLine16);
        setOnclickListenerTextView(tvLine17);
        setOnclickListenerTextView(tvLine18);
        setOnclickListenerTextView(tvLine19);
        setOnclickListenerTextView(tvLine20);

        setOnclickListenerTextView(tvLine21);
        setOnclickListenerTextView(tvLine22);
        setOnclickListenerTextView(tvLine23);
        setOnclickListenerTextView(tvLine24);
        setOnclickListenerTextView(tvLine25);
        setOnclickListenerTextView(tvLine26);
        setOnclickListenerTextView(tvLine27);
        setOnclickListenerTextView(tvLine28);
        setOnclickListenerTextView(tvLine29);
        setOnclickListenerTextView(tvLine30);

        setOnclickListenerTextView(tvLine31);
        setOnclickListenerTextView(tvLine32);
        setOnclickListenerTextView(tvLine33);
        setOnclickListenerTextView(tvLine34);
        setOnclickListenerTextView(tvLine35);
        setOnclickListenerTextView(tvLine36);
        setOnclickListenerTextView(tvLine37);
        setOnclickListenerTextView(tvLine38);
        setOnclickListenerTextView(tvLine39);
        setOnclickListenerTextView(tvLine40);

        setOnclickListenerTextView(tvLine41);
        setOnclickListenerTextView(tvLine42);
        setOnclickListenerTextView(tvLine43);
        setOnclickListenerTextView(tvLine44);
        setOnclickListenerTextView(tvLine45);
        setOnclickListenerTextView(tvLine46);
        setOnclickListenerTextView(tvLine47);
        setOnclickListenerTextView(tvLine48);
        setOnclickListenerTextView(tvLine49);
        setOnclickListenerTextView(tvLine50);

        setOnclickListenerTextView(tvLine51);
        setOnclickListenerTextView(tvLine52);
        setOnclickListenerTextView(tvLine53);
        setOnclickListenerTextView(tvLine54);
        setOnclickListenerTextView(tvLine55);
        setOnclickListenerTextView(tvLine56);
        setOnclickListenerTextView(tvLine57);
        setOnclickListenerTextView(tvLine58);
        setOnclickListenerTextView(tvLine59);
        setOnclickListenerTextView(tvLine60);


        recyclerView.setLayoutManager(new LinearLayoutManager(c));
        arrow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        otherLinesAdapter = new PopupSearchAdapter(itemList, (item, position) -> {
//            Toast.makeText(c, "Clicked: " + item, Toast.LENGTH_SHORT).show();
            expense_line = item;
            etExpenseLine.setText(expense_line);
            popupView.dismiss();
        });

        setOnclickListenToView(arrow_button, hidden_view, cardView);
        setOnclickListenToView(arrow_button1, hidden_view1, cardView1);
        setOnclickListenToView(arrow_button2, hidden_view2, cardView2);
        setOnclickListenToView(arrow_button3, hidden_view3, cardView3);
        setOnclickListenToView(arrow_button4, hidden_view4, cardView4);
        setOnclickListenToView(arrow_button5, hidden_view5, cardView5);
        setOnclickListenToView(arrow_button6, hidden_view6, cardView6);
        setOnclickListenToView(arrow_button7, hidden_view7, cardView7);
        setOnclickListenToView(arrow_button8, hidden_view8, cardView8);


        recyclerView.setAdapter(otherLinesAdapter);

        searchBox.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                otherLinesAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
            }
        });

        popupView.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupView.show();
    }

    public void setOnclickListenerTextView(TextView tv) {
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etExpenseLine.setText(tv.getText());
                popupView.dismiss();
            }
        });
    }

    public void setOnclickListenToView(ImageButton imageButton, LinearLayout layoutView, CardView cardView) {
        imageButton.setOnClickListener(view -> {
            if (layoutView.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                layoutView.setVisibility(View.GONE);
                imageButton.setImageResource(R.drawable.baseline_expand_right);
            } else {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                layoutView.setVisibility(View.VISIBLE);
                imageButton.setImageResource(R.drawable.baseline_expand_less_24);
            }
        });
    }

    private void loadItems() {

        class FetchDataTask extends AsyncTask<String, Void, String> {
            OkHttpClient client = new OkHttpClient();

            @Override
            protected String doInBackground(String... urls) {
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(util.GET_EXPENSE_LINE_URL)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {

                itemList.clear();
                if (result != null) {
                    try {


                        JSONObject jsonObject = new JSONObject(result);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        Log.e("TAG Expense", result);
                        if (success.equals("1")) {

                            int l = 0;
                            l = jsonArray.length();

                            for (int i = 0; i < l; i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String line = object.getString("line");
                                itemList.add(line);

                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
//                textView.setText("Failed to load data");
                    Log.e("TAG", "Failed to load category");

                }
            }
        }

        FetchDataTask fetchDataTask = new FetchDataTask();
        fetchDataTask.execute();
    }

    private void addToCategoryList(String cat) {

        Log.e("TAG", "addToCategoryList method called");

//
//        for (String element : categories) {
//            Log.e("TAG", "Item listed " + element);
//            if (element.contains(cat)) {
////                categories.add(cat);
//                basicCat.remove(cat);
//                Log.e("TAG", "FOUND " + cat);
//            }
//        }

        for (int i = 0; i < dataClassArrayList.size(); i++) {
            if (dataClassArrayList.get(i).getLine().equals(cat)) {
                basicCat.remove(cat);
                Log.e("TAG", "FOUND " + cat);
            }

        }
    }

//    private void setLine(String ln) {
//        line = ln;
//    }

    private void getBudgetedLine() {

        RequestQueue queue = Volley.newRequestQueue(c);

        StringRequest request = new StringRequest(Request.Method.POST, GET_ACTUAL_EXPENSE_LIST_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS  budgeted line" + response);

                categories.clear();
                dataClassArrayList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {

                        int l = 0;
                        l = jsonArray.length();

                        for (int i = 0; i < l; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);


                            String cat = object.getString("income_cat");
                            String line = object.getString("income_line");
                            String freq = object.getString("income_freq");
                            String amount = object.getString("income_amount");
                            String actual_amount = object.getString("income_actual_amount");
                            String id = object.getString("income_id");

//                            int percent =   Integer.parseInt(no_o)/Integer.parseInt(no_o2);
                            int percent = 0;//   Integer.parseInt(actual_amount)/Integer.parseInt(amount);

                            Log.e("TAG percent difference", " Amount percent dd is" + percent);
                            String status = "Updated";

                            dataClass = new DataClass(id + "", line, line, amount, percent + "", cat);
                            dataClassArrayList.add(dataClass);

                        }

                    }


                } catch (Exception e) {

                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(c, "Fail to get response ", Toast.LENGTH_SHORT).show();
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
                params.put("date", "");

                return params;
            }
        };

        queue.add(request);
    }

    private void getInflow() {

        RequestQueue queue = Volley.newRequestQueue(c);

        StringRequest request = new StringRequest(Request.Method.POST,
                GET_ACTUAL_EXPENSE_LIST_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS for Lines" + response);

                dataClassArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {

                        int l = 0;
                        l = jsonArray.length();

                        for (int i = 0; i < l; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String cat = object.getString("income_cat");
                            String line = object.getString("income_line");
                            String freq = object.getString("income_freq");
                            String amount = object.getString("income_amount");
                            String actual_amount = object.getString("income_actual_amount");
                            String id = object.getString("income_id");

                            String percent = "0";
                            int budget_amount = Integer.parseInt(amount);
                            if (budget_amount != 0) {
                                percent = (int) (((double) Integer.parseInt(actual_amount) / budget_amount) * 100) + "";
                            } else {
                                percent = "unbudgeted";
                            }

                            Log.e("TAG percent difference", " Amount percent " + percent);
                            String status = "Updated";

                            dataClass = new DataClass(id + "", line, line, amount, percent + "", cat);
                            dataClassArrayList.add(dataClass);

                        }
                        addBasicCat();

                    }
                    myAdapter.notifyDataSetChanged();


                } catch (Exception e) {

                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(c, "Fail to get response ", Toast.LENGTH_SHORT).show();
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
                params.put("date", "");

                return params;
            }
        };

        queue.add(request);
    }
}
