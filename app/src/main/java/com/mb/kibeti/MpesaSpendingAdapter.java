package com.mb.kibeti;

import static android.content.Context.MODE_PRIVATE;
import static com.mb.kibeti.LoginActivity.CURRENCY;
import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MpesaSpendingAdapter extends ArrayAdapter<DataClass> {

    String currency;
    String email;
    Context context;
    SharedPreferences sharedPreferences;
    List<DataClass> dataClassList;
    public MpesaSpendingAdapter(@NonNull Context context, List<DataClass>dataClasses) {
        super(context, R.layout.confirming_spending_data_item,dataClasses );

        this.context = context;
        this.dataClassList = dataClasses;
        sharedPreferences = context.getSharedPreferences(MY_PREFERENCES,MODE_PRIVATE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("###,###,###,###");

        currency = sharedPreferences.getString(CURRENCY,"KES");
        email = sharedPreferences.getString(EMAIL, "");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mpesa_confirmation_data_item,null,true);
        TextView agetv = view.findViewById(R.id.nameTv);
        TextView cat = view.findViewById(R.id.cat);
        TextView nametv = view.findViewById(R.id.ageTv);
        TextView gendertv = view.findViewById(R.id.genderTv);
        TextView status = view.findViewById(R.id.statusTv);
        TextView variance = view.findViewById(R.id.varAmtTv);
        TextView lastUpTv = view.findViewById(R.id.lastUpTv);
        TextView lineTv = view.findViewById(R.id.catTv);
        Button makaChangesBtn = view.findViewById(R.id.makeChangesBtn);

//      String catdata = object.getString("income_cat");
//      String line = dataClassList.get(position).getLine();
//      String freq = object.getString("income_freq");
        String strStatus = dataClassList.get(position).getStatus();
//        String actual_amount = dataClassList.get(position).getLine();
//      String id = object.getString("income_id");
//
//        String no_o = amount.replaceAll(" ", "");
//        String no_o1 = no_o.replaceAll("KES", "");
//        String no_o2 = no_o1.replaceAll(",", "");

//        no_o = actual_amount.replaceAll(" ", "");
//        no_o1 = no_o.replaceAll("KES", "");
//        no_o = no_o1.replaceAll(",", "");

        String freq = dataClassList.get(position).getFrequency();
//
//        String strStatus = "Not Updated";
//
//        if(Integer.parseInt(no_o2)!=0){
//            strStatus = "Updated";
//        }
//      int number =  Integer.parseInt(no_o)-Integer.parseInt(no_o2);

        cat.setText(currency+" "+dataClassList.get(position).getAmount());
        nametv.setText(dataClassList.get(position).getLine());
        agetv.setText(dataClassList.get(position).getCat());
        lineTv.setText(dataClassList.get(position).getRecipient());
//      agetv.setText(freq);
      gendertv.setText(dataClassList.get(position).getType());
        status.setText(strStatus);
//      lastUpTv.setText(dataClassList.get(position).getStatus());
        lastUpTv.setText(freq);
        variance.setText("XXX");

        UpdateMpesaPop pop = new UpdateMpesaPop(context,email);
        makaChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("E/Pop Parameters", "Category =>"+dataClassList.get(position).getCat()+"\n" +
                        "Line => "+dataClassList.get(position).getLine()+"\n " +
                        "Amount => "+dataClassList.get(position).getAmount()+"\n" +
                        "Frequency => "+dataClassList.get(position).getFrequency()+"\n" +
                        "Inflow id => "+dataClassList.get(position).getInflowId());

                pop.showPopup(dataClassList.get(position).getCat(), dataClassList.get(position).getLine(),
                        dataClassList.get(position).getAmount(), dataClassList.get(position).getFrequency(),
                        dataClassList.get(position).getInflowId());
            }
        });


        return view;
    }
}
