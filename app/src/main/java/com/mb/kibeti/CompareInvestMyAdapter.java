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

public class CompareInvestMyAdapter extends ArrayAdapter<DataClass> {

    Context context;
    List<DataClass> dataClassList;
    public CompareInvestMyAdapter(@NonNull Context context, List<DataClass>dataClasses) {
        super(context, R.layout.data_item,dataClasses );

        this.context = context;
        this.dataClassList = dataClasses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item,null,true);
        TextView nametv = view.findViewById(R.id.nameTv);
        TextView cat = view.findViewById(R.id.cat);
        TextView agetv = view.findViewById(R.id.ageTv);
        TextView gendertv = view.findViewById(R.id.genderTv);
        TextView cat_label = view.findViewById(R.id.cat_label);
        TextView freq_label = view.findViewById(R.id.imageLocation);

        cat_label.setText("Return Rate: ");
        freq_label.setText("Special Offer: ");

        cat.setText(dataClassList.get(position).getCat());
        nametv.setText(dataClassList.get(position).getLine());
        agetv.setText(dataClassList.get(position).getFrequency());
        gendertv.setText(dataClassList.get(position).getAmount());

        return view;
    }
}
