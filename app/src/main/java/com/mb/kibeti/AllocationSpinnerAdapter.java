package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.CURRENCY;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class AllocationSpinnerAdapter extends ArrayAdapter<DataClass> {

    Context context;
    SharedPreferences sharedPreferences;
    List<DataClass> dataClassList;
    String currency="";
    public AllocationSpinnerAdapter(@NonNull Context context, List<DataClass>dataClasses) {
        super(context, R.layout.allocation_spinner_item,dataClasses );
        this.context = context;
        this.dataClassList = dataClasses;

        sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        currency = sharedPreferences.getString(CURRENCY, "");

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allocation_spinner_item,null,true);

        TextView tvPercentage,tvLine;
        ProgressBar progressBar;
        tvPercentage = view.findViewById(R.id.tvPercentage);
        progressBar = view.findViewById(R.id.progressBar);
        tvLine = view.findViewById(R.id.tvLine);

        Log.e("TAG Percent 1",dataClassList.get(position).getFrequency());

        String percent_str = dataClassList.get(position).getFrequency();

//        if(percent_str.toLowerCase().equals("unbudgeted")){
//            tvPercentage.setText(percent_str);
//            tvPercentage.setTextColor(context.getResources().getColor(R.color.lavander));
//
//        }else if(percent_str.toLowerCase().equals("suggested")){
//            tvPercentage.setText(percent_str);
//            tvPercentage.setTextColor(context.getResources().getColor(R.color.lavander));
//            progressBar.setVisibility(View.GONE);
//        }
//        else {

            try {
                int progressStatus = Integer.parseInt(percent_str);
                if (progressStatus > 90) {
                    tvPercentage.setTextColor(context.getResources().getColor(R.color.red));
                    progressBar.getIndeterminateDrawable().setColorFilter(
                            context.getResources().getColor(R.color.red), PorterDuff.Mode.SRC_IN
                    );
                } else if (progressStatus < 50) {
                    tvPercentage.setTextColor(context.getResources().getColor(R.color.green));
                    progressBar.getIndeterminateDrawable().setColorFilter(
                            context.getResources().getColor(R.color.green), PorterDuff.Mode.SRC_IN
                    );
                } else {
                    tvPercentage.setTextColor(context.getResources().getColor(R.color.orange));
                    progressBar.getIndeterminateDrawable().setColorFilter(
                            context.getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_IN
                    );
                }
//            progressBar.setBackgroundColor();
                progressBar.setProgress(progressStatus);

                tvPercentage.setText(progressStatus + " %");
            }catch (Exception e){

            }
//        }


        tvLine.setText(dataClassList.get(position).getLine());

        return view;
    }
}
