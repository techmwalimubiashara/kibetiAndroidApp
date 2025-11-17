package com.mb.kibeti;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.text.TextWatcher;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatOnTyping {

//    EditText edAmount;
    DecimalFormat formatter;
    Context context;
    boolean isTyped = false;

    public NumberFormatOnTyping(Context c) {
        context = c;

        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("###,###,###,###");
    }
    public boolean setNumberFormatOnTyping(EditText edAmount, TextView tv){

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
                    tv.setText("Frequency required");
                    tv.setTextColor(ContextCompat.getColor(context,R.color.red));

                    isTyped = true;

                }else{
                    tv.setText("Frequency");
                    tv.setTextColor(ContextCompat.getColor(context,R.color.text_color));
                    isTyped = false;
                }

                edAmount.addTextChangedListener(this);
            }

        });

//        Log.e("TAG", "Is typing stop? " + isTyped);
        return isTyped;
    }

    public void setNumberFormatOnTypingWithEditText(EditText edAmount) {

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
    public boolean setNumberFormatOnTyping(EditText edAmount, TextView tv, String noTextStr,String textString){

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
                    tv.setText(textString);
                    tv.setTextColor(ContextCompat.getColor(context,R.color.red));

                    isTyped = true;

                }else{
                    tv.setText(noTextStr);
                    tv.setTextColor(ContextCompat.getColor(context,R.color.text_color));
                    isTyped = false;
                }

                edAmount.addTextChangedListener(this);
            }

        });

        Log.e("TAG", "Is typing stop? " + isTyped);
        return isTyped;
    }
    public boolean setNumberFormatOnTypingAndSub(EditText edAmount, TextView tv, int amount){

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
                String amountRemain = tv.getText().toString();
                if (!TextUtils.isEmpty(amountStr)) {

                    String originalString = amountStr;

                    Long longval, amountRemainVal;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    if (amountRemain.contains(",")) {
                        amountRemain = amountRemain.replaceAll(",", "");
                    }

                    longval = Long.parseLong(originalString);
                    amountRemainVal = Long.parseLong(amountRemain);

                    String formattedString = formatter.format(longval);

                    edAmount.setText(formattedString);
                    edAmount.setSelection(edAmount.getText().length());
                    Long finalAmount = amount-longval;
                    tv.setText(formatter.format(finalAmount));
                    tv.setTextColor(ContextCompat.getColor(context,R.color.text_color));
                    if(finalAmount<0){
                        tv.setTextColor(ContextCompat.getColor(context,R.color.red));
                    }

                    isTyped = true;

                }else{
//                    tv.setText("Frequency");
//                    tv.setTextColor(ContextCompat.getColor(context,R.color.text_color));
                    isTyped = false;
                }

                edAmount.addTextChangedListener(this);
            }

        });

        Log.e("TAG", "Is typing stop? " + isTyped);
        return isTyped;
    }
    public boolean setNumberFormatOnTyping(EditText edAmount){

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
                amountStr = amountStr.replaceAll(" ", "");
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

                    isTyped = true;

                }else{

                    isTyped = false;
                }

                edAmount.addTextChangedListener(this);
            }

        });

        Log.e("TAG", "Is typing stop? " + isTyped);
        return isTyped;
    }
}
