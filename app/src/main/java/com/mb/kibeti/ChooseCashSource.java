package com.mb.kibeti;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ChooseCashSource extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choose_cash_source_snackbar,
                container, false);

        Button btnGoInvest = v.findViewById(R.id.btnGoInvest);
        Button btnGoSaving = v.findViewById(R.id.btnGoSaving);
        btnGoInvest.setBackgroundColor(R.color.primary_orange);
        btnGoSaving.setBackgroundColor(R.color.primary_orange);

        // now handle the same button with onClickListener
        btnGoInvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), InvestmentCalculator.class);
                startActivity(intent);

            }
        });
        btnGoSaving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CashflowBudget.class);
                startActivity(intent);

            }
        });

        return v;
    }
}
