package com.mb.kibeti;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class ShowCalendar {

    private Context context;

    public ShowCalendar(Context context){
        this.context = context;
    }

    public void showCalendar(TextView ed){
        final Calendar c = Calendar.getInstance();

        // on below line we are getting
        // our day, month and year.
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // on below line we are creating a variable for date picker dialog.
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                // on below line we are passing context.
                context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // on below line we are setting date to our text view.
                        ed.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        ed.setTextColor(ContextCompat.getColor(context,R.color.text_color));

                    }
                },
                // on below line we are passing year,
                // month and day for selected date in our date picker.
                year, month, day);

        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        // at last we are calling show to
        // display our date picker dialog.
        datePickerDialog.show();
    }
}
