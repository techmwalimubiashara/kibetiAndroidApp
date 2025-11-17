package com.mb.kibeti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MyAdapterIncomeHist extends ArrayAdapter<DataClass> {

    Context context;
    List<DataClass> dataClassList;
    public MyAdapterIncomeHist(@NonNull Context context, List<DataClass>dataClasses) {
        super(context, R.layout.data_item_income_hist,dataClasses );

        this.context = context;
        this.dataClassList = dataClasses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item_income_hist,null,true);
        TextView nametv = view.findViewById(R.id.nameTv);
        TextView endDate = view.findViewById(R.id.endDate);
        TextView startDate = view.findViewById(R.id.startDate);
        TextView amountTv = view.findViewById(R.id.amountTv);
        TextView spent_total_tv = view.findViewById(R.id.cash_bal_tv);

        endDate.setText(dataClassList.get(position).getCat());
        nametv.setText(dataClassList.get(position).getLine());
        startDate.setText(dataClassList.get(position).getFrequency());
        amountTv.setText(dataClassList.get(position).getAmount());
        spent_total_tv.setText(dataClassList.get(position).getStatus());

        return view;
    }
}
