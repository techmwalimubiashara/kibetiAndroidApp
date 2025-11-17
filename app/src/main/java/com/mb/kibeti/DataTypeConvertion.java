package com.mb.kibeti;

import android.util.Log;

public class DataTypeConvertion {

    public DataTypeConvertion(){}

    public int convertToInt(String str) {
        String amnt1 = str.replaceAll(",", "");

        amnt1 = amnt1.replaceAll(" ", "");
        amnt1 = amnt1.replaceAll("KES.", "");

//        int amount1 = Float.parseFloat(amnt1);
        int amount1 = Integer.parseInt(amnt1);

        Log.e("TAG", "String in " + str + ", Int out " + amount1);

        return amount1;
    }
}
