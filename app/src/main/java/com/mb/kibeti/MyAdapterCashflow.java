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

public class MyAdapterCashflow extends ArrayAdapter<DataClassOutflow> {

    Context context;
    List<DataClassOutflow> dataClassList;
    public MyAdapterCashflow(@NonNull Context context, List<DataClassOutflow> dataClasses) {
        super(context, R.layout.data_item_cashflow,dataClasses );

        this.context = context;
        this.dataClassList = dataClasses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item_cashflow,null,true);
        TextView nametv = view.findViewById(R.id.nameTv);
        TextView cat = view.findViewById(R.id.cat);
        TextView agetv = view.findViewById(R.id.ageTv);
        TextView gendertv = view.findViewById(R.id.genderTv);

        cat.setText(dataClassList.get(position).getId());
        nametv.setText(dataClassList.get(position).getName());
        agetv.setText(dataClassList.get(position).getAge());
        gendertv.setText(dataClassList.get(position).getGender());

        return view;
    }
}
