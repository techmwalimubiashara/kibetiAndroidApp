package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;
import static com.mb.kibeti.LoginActivity.PACKAGE_TYPE;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PaymentRequiredPopup {

    private Dialog myDialog;
    private Context context;
    private TextView txtclose,tvInfo;
    private Button upgradeBtn;
    private String strInfo;
    private SharedPreferences sharedPreferences;

    public PaymentRequiredPopup(Context context, Dialog myDialog, String strInfo) {

        this.context = context;
        this.myDialog = myDialog;
        this.strInfo = strInfo;

        sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

//        checkPayment();

    }

    public boolean checkPayment() {
        String pack = sharedPreferences.getString(PACKAGE_TYPE, "");
        if (pack.equals("Trial")) {

            showPopup();
            return false;

        } else {
//            Intent intent = new Intent(context, Makepayment.class);
//            context.startActivity(intent);

            myDialog.dismiss();
            return true;
        }

    }
    public void showCongratulations(){
        Intent intent = new Intent(context, CelebrationPopActivity.class);
        context.startActivity(intent);
    }


    public void showPopup() {

        myDialog.setContentView(R.layout.payment_popup);

        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        tvInfo = (TextView) myDialog.findViewById(R.id.tvInfo);
        upgradeBtn = (Button) myDialog.findViewById(R.id.upgradeBtn);

        Button upgradeBtn = (Button) myDialog.findViewById(R.id.upgradeBtn);
        tvInfo.setText(strInfo);

        upgradeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upgrade();
            }
        });

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();

            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void hidePopup() {
        myDialog.dismiss();
    }

    private void upgrade() {
        Intent intent = new Intent(context, Makepayment.class);
        context.startActivity(intent);
        myDialog.dismiss();
    }
}
