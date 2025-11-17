package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.CURRENCY;
import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MyAdapter extends ArrayAdapter<DataClass> {

    Context context;
    SharedPreferences sharedPreferences;
    List<DataClass> dataClassList;
    String currency="";
    public MyAdapter(@NonNull Context context, List<DataClass>dataClasses) {
        super(context, R.layout.data_item,dataClasses );
        this.context = context;
        this.dataClassList = dataClasses;

        sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        currency = sharedPreferences.getString(CURRENCY, "");

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item,null,true);
        TextView nametv = view.findViewById(R.id.nameTv);
        TextView cat = view.findViewById(R.id.cat);
        TextView agetv = view.findViewById(R.id.ageTv);
        TextView gendertv = view.findViewById(R.id.genderTv);

        cat.setText(dataClassList.get(position).getCat());
        nametv.setText(dataClassList.get(position).getLine());
        agetv.setText(dataClassList.get(position).getFrequency());
        gendertv.setText(currency+". "+dataClassList.get(position).getAmount());

        return view;
    }
}
