package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.CURRENCY;
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class SpendingAdapter extends ArrayAdapter<DataClass> {

    Context context;
    SharedPreferences sharedPreferences;
    List<DataClass> dataClassList;

    String currency="";
    public SpendingAdapter(@NonNull Context context, List<DataClass>dataClasses) {
        super(context, R.layout.spending_data_item,dataClasses );

        this.context = context;
        this.dataClassList = dataClasses;

        sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        currency = sharedPreferences.getString(CURRENCY, "");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("###,###,###,###");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spending_data_item,null,true);
        TextView nametv = view.findViewById(R.id.nameTv);
        TextView cat = view.findViewById(R.id.cat);
        TextView agetv = view.findViewById(R.id.ageTv);
        TextView gendertv = view.findViewById(R.id.genderTv);
        TextView status = view.findViewById(R.id.statusTv);
        TextView variance = view.findViewById(R.id.varAmtTv);
        TextView lastUpTv = view.findViewById(R.id.lastUpTv);
        TextView currentTv = view.findViewById(R.id.currentTv);

//        String catdata = object.getString("income_cat");
//        String line = dataClassList.get(position).getLine();
//        String freq = object.getString("income_freq");
        String amount = dataClassList.get(position).getAmount();
        String actual_amount = dataClassList.get(position).getLine();
//        String id = object.getString("income_id");

        String no_o = amount.replaceAll(" ", "");
        String no_o1 = no_o.replaceAll("KES", "");
        String no_o2 = no_o1.replaceAll(",", "");

        no_o = actual_amount.replaceAll(" ", "");
        no_o1 = no_o.replaceAll("KES", "");
        no_o = no_o1.replaceAll(",", "");

        String freq = dataClassList.get(position).getFrequency();

        String current = "";

        if(freq.equals("Daily")){
            current = "Today : ";
        }else if(freq.equals("Weekly")){
            current = "This Week : ";
        }else if(freq.equals("Monthly")){
            current = "This Month : ";
        }else if(freq.equals("Semi Annually")){
            current = "This Semi Annual : ";
        }else if(freq.equals("Annually")){
            current = "This Year : ";
        }else{

        }

        currentTv.setText(current);

        String strStatus = "Not Updated";

        if(Integer.parseInt(no_o2)!=0){
            strStatus = "Updated";
        }
        int number =  Integer.parseInt(no_o)-Integer.parseInt(no_o2);

        cat.setText(currency+" "+dataClassList.get(position).getLine());
        nametv.setText(dataClassList.get(position).getCat());
        agetv.setText(freq);
        gendertv.setText(currency+" "+dataClassList.get(position).getAmount());
        status.setText(strStatus);
        lastUpTv.setText(dataClassList.get(position).getStatus());
        variance.setText(currency+" "+formatter.format(number));

        return view;
    }
}
