package com.mb.kibeti;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ShoppingAssistance extends AppCompatActivity {

    TextView tvFreq1, amountAvialable;
    DecimalFormat formatter;
    NumberFormatOnTyping numberFormatOnTyping;
    private static ArrayList<DataClass> dataClassArrayList = new ArrayList<>();
    DataClass dataClass;
    MyAdapterIncomeHist myAdapter;
    EditText amount, edBudgetAmount;
    int amnt = 0;
    Button addPriceBtn;
    Boolean isPosted = false;
    ListView listView;
    LinearLayout linearLayout1, linearLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_assistance);

        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("###,###,###,###");

        amount = findViewById(R.id.amountAct);
        edBudgetAmount = findViewById(R.id.amount);
        amountAvialable = findViewById(R.id.amountAv);
        addPriceBtn = findViewById(R.id.btn_add);
        listView = findViewById(R.id.listView);
        linearLayout1 = findViewById(R.id.linearLayout1);
        linearLayout2 = findViewById(R.id.linearLayout2);

        numberFormatOnTyping = new NumberFormatOnTyping(this);

        dataClass = new DataClass("0", "", "start", "20", "end", "");

//                            dataClass = new DataClass(id,name,age,gender);
        dataClassArrayList.add(dataClass);

        myAdapter = new MyAdapterIncomeHist(ShoppingAssistance.this, dataClassArrayList);
        listView.setAdapter(myAdapter);
        listView.setClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                new AlertDialog.Builder(ShoppingAssistance.this)
                        .setTitle("Do you want to remove this shopping from the list?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                int finalAmount = 0;

                                String amountDeleted = dataClassArrayList.get(i).getCat();

//                                        String amountStr = amountAvialable.getText().toString();
                                String amountRemain = amountAvialable.getText().toString();

//                                            String originalString = amountStr;

                                int longval, amountRemainVal;
//                                            if (originalString.contains(",")) {
//                                                originalString = originalString.replaceAll(",", "");
//                                            }
                                if (amountRemain.contains(",")) {
                                    amountRemain = amountRemain.replaceAll(",", "");
                                }

//                                            longval = Long.parseLong(originalString);
                                amountRemainVal = Integer.parseInt(amountRemain);

                                if (amountDeleted.contains(",")) {
                                    amountDeleted = amountDeleted.replaceAll(",", "");
                                }
                                if (!amountDeleted.equals("")) {


                                    int amountE = 0;

                                    try {
                                        amountE = Integer.parseInt(amountDeleted);

                                        finalAmount = amountRemainVal + amountE;

                                        Toast.makeText(ShoppingAssistance.this, "Amount " + finalAmount, Toast.LENGTH_SHORT).show();

                                    } catch (NumberFormatException nfe) {
//                        System.out.println("Could not parse " + nfe);
                                    }
                                }

                                amnt = finalAmount;

                                String formattedString = formatter.format(finalAmount);

                                amountAvialable.setText(formattedString);


                                dataClassArrayList.remove(i);
                                myAdapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();

            }
        });

        numberFormatOnTyping.setNumberFormatOnTyping(edBudgetAmount);


//        numberFormatOnTyping.setNumberFormatOnTyping(amount);
        if (!isPosted) {
            numberFormatOnTyping.setNumberFormatOnTypingAndSub(amount, amountAvialable, amnt);
        }


        addPriceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountEntered = amount.getText().toString();

                if (amountEntered.contains(",")) {
                    amountEntered = amountEntered.replaceAll(",", "");
                }
                if (!amountEntered.equals("")) {
                    int finalAmount = 0;

                    int amountE = 0;

                    try {
                        amountE = Integer.parseInt(amountEntered);

                        finalAmount = amnt - amountE;

                    } catch (NumberFormatException nfe) {
//                        System.out.println("Could not parse " + nfe);
                    }
//                    Long amountE = Int.parseInt(amountEntered);
//
//                    Long finalAmount = amnt-amountE;

                    dataClass = new DataClass("0", "", formatter.format(amountE), "Delete", "end", "");

//                            dataClass = new DataClass(id,name,age,gender);
                    dataClassArrayList.add(dataClass);

                    myAdapter.notifyDataSetChanged();

                    amnt = finalAmount;

                    String formattedString = formatter.format(finalAmount);

                    amountAvialable.setText(formattedString);

                    isPosted = true;

                    numberFormatOnTyping.setNumberFormatOnTypingAndSub(amount, amountAvialable, amnt);

                    amount.setText("");
                }

            }
        });

    }

    public void nextStep1(View view) {
        String amountEntered = edBudgetAmount.getText().toString();

        if (amountEntered.contains(",")) {
            amountEntered = amountEntered.replaceAll(",", "");
        }
        if (!amountEntered.equals("")) {

            try {
                amnt = Integer.parseInt(amountEntered);

                amountAvialable.setText(edBudgetAmount.getText().toString());

                dataClassArrayList.clear();
                myAdapter.notifyDataSetChanged();
//                linearLayout1.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.VISIBLE);

                isPosted = true;

                numberFormatOnTyping.setNumberFormatOnTypingAndSub(amount, amountAvialable, amnt);

            } catch (NumberFormatException nfe) {
//                        System.out.println("Could not parse " + nfe);
            }

        } else {
            edBudgetAmount.setError("Enter amount you want to spend");
        }


    }

}