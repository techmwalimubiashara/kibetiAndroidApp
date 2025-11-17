package com.mb.kibeti;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class InvestAdapter extends ArrayAdapter<InvestmentItem> {

    Context context;
    List<InvestmentItem> dataClassList;
    public InvestAdapter(@NonNull Context context, List<InvestmentItem>dataClasses) {
        super(context, R.layout.invest_data_item,dataClasses );

        this.context = context;
        this.dataClassList = dataClasses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invest_data_item,null,true);
        ImageView info = view.findViewById(R.id.imInfoInvest);
        TextView tvInvestName = view.findViewById(R.id.tvInvestName);
        TextView tvAboutInvest = view.findViewById(R.id.tvAboutInvest);

        tvInvestName.setText(dataClassList.get(position).getInvest_name());
        tvAboutInvest.setText(dataClassList.get(position).getInvest_desc());


        return view;
    }
}
