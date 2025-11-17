package com.mb.kibeti;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ShowPopup {
    private Context context;
    private Dialog myDialog;
    private String heading;
    private String item1;
    private String item2;
    private String item3;
    private String item4;
    private String item5;
    private String item6;
    private String item7;
    private String item8;
    private String id;
    private TextView txtclose,tvHeading,edItem1,edItem2,edItem3,edItem4,edItem5,edItem6,edItem7,edItem8;
    private Button btnFollow,submitBtn,deleteBtn;
    private EditText edCat,edLine,edAmount,tvItem1,tvItem2,tvItem3,tvItem4,tvItem5,tvItem6,tvItem7;

    public ShowPopup(Context context,Dialog myDialog) {

       this.context= context;
       this.myDialog = myDialog;

    }
    public void showPopup(String heading,String item1,String item2,String item3,String item4,String item5,String item6,String item7,String item8) {

        myDialog.setContentView(R.layout.goal_popup);

        this.heading = heading;
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
        this.item4 = item4;
        this.item5 = item5;
        this.item6 = item6;
        this.item7 = item7;
        this.item8 = item8;

        initialPop();

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
    private void initialPop(){
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        tvHeading =(TextView) myDialog.findViewById(R.id.tvHeading);
        deleteBtn =(Button) myDialog.findViewById(R.id.idBtnDelete);
        submitBtn =(Button) myDialog.findViewById(R.id.idBtnSave);
        edItem1 = (EditText) myDialog.findViewById(R.id.edItem1);
        edItem2 = (EditText) myDialog.findViewById(R.id.edItem2);
        edItem3 = (EditText) myDialog.findViewById(R.id.edItem3);
        edItem4 = (EditText) myDialog.findViewById(R.id.edItem4);
        edItem5 = (EditText) myDialog.findViewById(R.id.edItem5);
        edItem6 = (EditText) myDialog.findViewById(R.id.edItem6);
        edItem7 = (EditText) myDialog.findViewById(R.id.edItem7);
        edItem8 = (EditText) myDialog.findViewById(R.id.edItem8);

        tvHeading.setText(heading);
        edItem1.setText(item1);
        edItem2.setText(item2);
        edItem3.setText(item3);
        edItem4.setText(item4);
        edItem5.setText(item5);
        edItem6.setText(item6);
        edItem7.setText(item7);
        edItem8.setText(item8);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();

            }
        });

    }

}
