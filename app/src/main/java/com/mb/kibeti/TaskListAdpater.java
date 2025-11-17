package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.CURRENCY;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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

public class TaskListAdpater extends ArrayAdapter<DataClass> {

    Context context;
    SharedPreferences sharedPreferences;
    List<DataClass> dataClassList;

    String currency="";
    public TaskListAdpater(@NonNull Context context, List<DataClass>dataClasses) {
        super(context, R.layout.movies_list,dataClasses );

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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_list,null,true);
        TextView nametv = view.findViewById(R.id.title);
        TextView invest_link = view.findViewById(R.id.invest_link);
//        TextView agetv = view.findViewById(R.id.ageTv);
//        TextView gendertv = view.findViewById(R.id.genderTv);
//        TextView status = view.findViewById(R.id.statusTv);
//        TextView variance = view.findViewById(R.id.varAmtTv);
//        TextView lastUpTv = view.findViewById(R.id.lastUpTv);
//        TextView currentTv = view.findViewById(R.id.currentTv);

//        String catdata = object.getString("income_cat");
//        String line = dataClassList.get(position).getLine();
//        String freq = object.getString("income_freq");
        String amount = dataClassList.get(position).getAmount();
        String actual_amount = dataClassList.get(position).getLine();

        String freq = dataClassList.get(position).getFrequency();



//        cat.setText(dataClassList.get(position).getLine());
        nametv.setText(dataClassList.get(position).getCat());
        if(dataClassList.get(position).getAmount()=="view"){
            invest_link.setVisibility(View.VISIBLE);
        }else if(dataClassList.get(position).getAmount()=="call"){
            invest_link.setText("Contact Now");



        }else if(dataClassList.get(position).getAmount()=="c"){
            invest_link.setVisibility(View.GONE);
        }

        else if(dataClassList.get(position).getAmount()=="url"){
            // Define a new URL
            String newUrl = dataClassList.get(position).getLine();
            String linkedText = "<a href='" + newUrl + "'>Invest</a>";

            // Set the new URL in TextView
            invest_link.setText(Html.fromHtml(linkedText, Html.FROM_HTML_MODE_LEGACY));
            invest_link.setMovementMethod(LinkMovementMethod.getInstance()); // Make link clickable

            invest_link.setVisibility(View.VISIBLE);
        }


//        agetv.setText(freq);
//        gendertv.setText(dataClassList.get(position).getAmount());
//        status.setText(dataClassList.get(position).getAmount());
//        lastUpTv.setText(dataClassList.get(position).getStatus());
//        variance.setText(dataClassList.get(position).getId());

        return view;
    }
}
