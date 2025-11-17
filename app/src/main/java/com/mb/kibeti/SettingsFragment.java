package com.mb.kibeti;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.mb.kibeti.LoginActivity.DAILY_REMINDER;
import static com.mb.kibeti.LoginActivity.MONTHLY_REMINDER;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;
import static com.mb.kibeti.LoginActivity.PASSCODE;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;

public class SettingsFragment extends Fragment {
    View view;
    TextView selectedTime, selectedDate;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    Intent intent;
    SharedPreferences sharedPreferences;
    TextView chooseTime;
    SwitchMaterial toggleBtn, passcodeToggleBtn;
    Dialog myDialog;
    TextView tvChangePasscode;
    LinearLayout hidden_view, hidden_view1;
    Button setTimeBtn, setDateBtn, cancelAlarmBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        sharedPreferences = getContext().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        sharedPreferences = getContext().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
//        email = sharedPreferences.getString(EMAIL,"");

        view = inflater.inflate(R.layout.fragment_settings, container, false);
        setDateBtn = view.findViewById(R.id.setDateBtn);
        setDateBtn = view.findViewById(R.id.setDateBtn);
        chooseTime = view.findViewById(R.id.chooseTime);
        cancelAlarmBtn = view.findViewById(R.id.cancelAlarmBtn);
        selectedTime = view.findViewById(R.id.selectedTime);
        selectedDate = view.findViewById(R.id.selectedDate);
        toggleBtn = view.findViewById(R.id.toggleBtn);
        passcodeToggleBtn = view.findViewById(R.id.passcodeToggleBtn);
        hidden_view = view.findViewById(R.id.hidden_view);
        hidden_view1 = view.findViewById(R.id.hidden_view1);
        tvChangePasscode = view.findViewById(R.id.tvChangePasscode);

        myDialog = new Dialog(getContext());
        selectedTime.setText(sharedPreferences.getString(DAILY_REMINDER, ""));
        String storedTime = sharedPreferences.getString(DAILY_REMINDER, "");
//        storedTime = "xxxxx";

        if (!sharedPreferences.getString(DAILY_REMINDER, "").toString().equals("Tap to set time")) {
            toggleBtn.setChecked(true);
            chooseTime.setText(storedTime);
        } else {
            toggleBtn.setChecked(false);
        }

        selectedDate.setText(sharedPreferences.getString(MONTHLY_REMINDER, ""));
        chooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//
//                final Calendar c = Calendar.getInstance();
//
//                int hour = c.get(Calendar.HOUR_OF_DAY);
//                int minute = c.get(Calendar.MINUTE);
//
//                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
//                        new TimePickerDialog.OnTimeSetListener() {
//                            @Override
//                            public void onTimeSet(TimePicker view, int hourOfDay,
//                                                  int minute) {
//                                String time = "";
//                                if (hourOfDay < 12) {
//                                    time = hourOfDay + ":" + minute + " AM";
//                                } else if (hourOfDay == 12) {
//                                    time = (hourOfDay) + ":" + minute + " PM";
//                                } else {
//                                    time = (hourOfDay - 12) + ":" + minute + " PM";
//                                    if (minute < 10) {
//                                        time = (hourOfDay - 12) + ":" + "0" + minute + " PM";
//                                    }
//
//                                }
//                                SharedPreferences.Editor editor2 = sharedPreferences.edit();
//                                editor2.putString(DAILY_REMINDER, time);
//                                editor2.apply();
//
//                                chooseTime.setText(time);
//
//                                intent = new Intent(getContext(), AlarmReceiver.class);
//
//                                pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
//
//                                alarmManager.cancel(pendingIntent);
//
//                                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
//                                        AlarmManager.INTERVAL_DAY, pendingIntent);
//
//                            }
//                        }, hour, minute, false);
//
//                timePickerDialog.show();

            }
        });

        tvChangePasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopup("change passcode");
            }
        });
        setDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                selectedDate.setText(dayOfMonth + "");

                            }
                        },

                        year, month, day);

                datePickerDialog.show();
            }
        });

        cancelAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        if (!sharedPreferences.getString(PASSCODE, "").toString().equals("")) {
            passcodeToggleBtn.setChecked(true);
        } else {
            passcodeToggleBtn.setChecked(false);
        }

        if (passcodeToggleBtn.isChecked()) {
            // if button is checked we are setting
            // text as Toggle Button is On
//            statusTV.setText("Toggle Button is On");
            hidden_view1.setVisibility(View.VISIBLE);
        } else {
//            hidden_view.setVisibility(View.GONE);
            // if button is unchecked we are setting
            // text as Toggle Button is Off
//            statusTV.setText("Toggle Button is Off");
            hidden_view1.setVisibility(View.GONE);
        }

        passcodeToggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are checking if
                // toggle button is checked or not.
                if (passcodeToggleBtn.isChecked()) {
                    // on below line we are setting message
                    // for status text view if toggle button is checked.
//                    statusTV.setText("Toggle Button is On");
                    ShowPopup("new passcode");
//                    myDialog.dismiss();
                } else {
                    // on below line we are setting message for
                    // status text view if toggle button is un checked.
//                    statusTV.setText("Toggle Button is Off");
//                    hidden_view.setVisibility(View.GONE);
//                    myDialog.dismiss();
                    ShowPopup("remove passcode");
                    hidden_view1.setVisibility(View.GONE);

                }
            }
        });


        if (toggleBtn.isChecked()) {
            // if button is checked we are setting
            // text as Toggle Button is On
//            statusTV.setText("Toggle Button is On");
            hidden_view.setVisibility(View.VISIBLE);
        } else {
            hidden_view.setVisibility(View.GONE);
            // if button is unchecked we are setting
            // text as Toggle Button is Off
//            statusTV.setText("Toggle Button is Off");

        }

        // on below line we are adding click listener for our toggle button
        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are checking if
                // toggle button is checked or not.
                if (toggleBtn.isChecked()) {

                    hidden_view.setVisibility(View.VISIBLE);
                } else {

//                    intent = new Intent(getContext(), AlarmReceiver.class);
//                    pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
//                    alarmManager.cancel(pendingIntent);
//                    pendingIntent.cancel();
//
//                    String time = "Tap to set time";
//
//                    chooseTime.setText(time);
//
//                    SharedPreferences.Editor editor2 = sharedPreferences.edit();
//                    editor2.putString(DAILY_REMINDER, time);
//                    editor2.apply();
                    hidden_view.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

    public void ShowPopup(String type) {
        TextView txtclose;
        Button btnFollow, submitBtn, deleteBtn, btnContinue;
        EditText edCat, edLine, edAmount, edPasscodeRemove;
        Spinner freq;
        LinearLayout createPasscodeLayout, changePasscodeLayout, deletePasscodeLayout;
        myDialog.setContentView(R.layout.passcodepopup);
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        deleteBtn = (Button) myDialog.findViewById(R.id.idBtnDelete);
        submitBtn = (Button) myDialog.findViewById(R.id.idBtnSave);
        btnContinue = (Button) myDialog.findViewById(R.id.idBtnContinue);
        edCat = (EditText) myDialog.findViewById(R.id.idCat);
        edAmount = (EditText) myDialog.findViewById(R.id.idAmount);
        edPasscodeRemove = (EditText) myDialog.findViewById(R.id.idPasscodeRemove);
        createPasscodeLayout = (LinearLayout) myDialog.findViewById(R.id.createPasscodeLayout);
        changePasscodeLayout = (LinearLayout) myDialog.findViewById(R.id.changePasscodeLayout);
        deletePasscodeLayout = (LinearLayout) myDialog.findViewById(R.id.deletePasscodeLayout);

        if (type.equals("change passcode")) {
            changePasscodeLayout.setVisibility(View.VISIBLE);
            createPasscodeLayout.setVisibility(View.GONE);
            deletePasscodeLayout.setVisibility(View.GONE);
        }
        if (type.equals("new passcode")) {
            deletePasscodeLayout.setVisibility(View.GONE);
            createPasscodeLayout.setVisibility(View.VISIBLE);
            changePasscodeLayout.setVisibility(View.GONE);
        }
        if (type.equals("remove passcode")) {
            deletePasscodeLayout.setVisibility(View.VISIBLE);
            createPasscodeLayout.setVisibility(View.GONE);
            changePasscodeLayout.setVisibility(View.GONE);
        }
        txtclose.setText("x");


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pass1;

                pass1 = edPasscodeRemove.getText().toString();

                if (TextUtils.isEmpty(pass1)) {
                    edPasscodeRemove.setError("Please enter Passcode");
                }


                if (!TextUtils.isEmpty(pass1)) {
//                    SharedPreferences sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
                    String passcode = sharedPreferences.getString(PASSCODE, "");

                    if (pass1.equals(passcode)) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(PASSCODE, "");
                        editor.apply();
                        myDialog.dismiss();
                    } else {
                        edPasscodeRemove.setError("Incorrect Passcode");
                    }

                }
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pass1, pass2;

                pass2 = edCat.getText().toString();

                pass1 = edAmount.getText().toString();


                if (TextUtils.isEmpty(pass1)) {
                    edAmount.setError("Please enter Passcode");
                }
                if (TextUtils.isEmpty(pass2)) {
                    edCat.setError("Please confirm Passcode");
                }

                if (!TextUtils.isEmpty(pass1) && !TextUtils.isEmpty(pass2)) {
                    if (pass2.equals(pass1)) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(PASSCODE, pass1);
                        editor.apply();
                        myDialog.dismiss();
                    } else {
                        edCat.setError("Passcode not matching");
                    }

                }
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

//    public void setAlarm() {
//
//        Intent serviceIntent = new Intent(getContext(), MainActivity.class);
//        PendingIntent pi = PendingIntent.getService(getContext(), 131313, serviceIntent,
//                PendingIntent.FLAG_CANCEL_CURRENT);
//
//        AlarmManager am = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
//        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
//        Log.v(TAG, "Alarm set");
//    }

    public void sendNotification() {

        Intent mainIntent = new Intent(getContext(), MainActivity.class);
        @SuppressWarnings("deprecation")
        Notification noti = new Notification.Builder(getContext())
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(getContext(), 131314, mainIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle("We Miss You!")
                .setContentText("Please play our game again soon.")
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.logo)
                .setTicker("We Miss You! Please come back and play our game again soon.")
                .setWhen(System.currentTimeMillis())
                .getNotification();

        NotificationManager notificationManager
                = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(131315, noti);

        Log.v(TAG, "Notification sent");
    }


}