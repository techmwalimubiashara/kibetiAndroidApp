package com.mb.kibeti;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class EditBudgetPopup extends BottomSheetDialogFragment {

    NumberFormatOnTyping numberFormatOnTyping;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_budget_popup,
                container, false);

        String str_amnt1 = this.getArguments().getString("str_amnt1");
        String str_amnt2 = this.getArguments().getString("str_amnt2");
        String str_amnt3 = this.getArguments().getString("str_amnt3");
        String str_amnt4 = this.getArguments().getString("str_amnt4");
        String str_amnt5 = this.getArguments().getString("str_amnt5");
        String str_amnt6 = this.getArguments().getString("str_amnt6");
        String str_amnt7 = this.getArguments().getString("str_amnt7");
        String str_amnt8 = this.getArguments().getString("str_amnt8");
        String str_amnt9 = this.getArguments().getString("str_amnt9");
        String str_amnt10 = this.getArguments().getString(" str_amnt10");

        numberFormatOnTyping = new NumberFormatOnTyping(getContext());

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

        edItem1.setText( str_amnt1);
        edItem2.setText( str_amnt2);
        edItem3.setText( str_amnt3);
        edItem4.setText( str_amnt4);
        edItem5.setText( str_amnt5);
        edItem6.setText( str_amnt6);
        edItem7.setText( str_amnt7);
        edItem8.setText( str_amnt8);
        edItem9.setText( str_amnt9);
        edItem10.setText( str_amnt10);

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

//        Button btnGoInvest = v.findViewById(R.id.btnGoInvest);
//        Button btnGoSaving = v.findViewById(R.id.btnGoSaving);
//        btnGoInvest.setBackgroundColor(R.color.primary_orange);
//        btnGoSaving.setBackgroundColor(R.color.primary_orange);
//
//        // now handle the same button with onClickListener
//        btnGoInvest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), InvestmentCalculator.class);
//                startActivity(intent);
//
//            }
//        });
//        btnGoSaving.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), CashflowBudget.class);
//                startActivity(intent);
//
//            }
//        });

        return v;
    }


}