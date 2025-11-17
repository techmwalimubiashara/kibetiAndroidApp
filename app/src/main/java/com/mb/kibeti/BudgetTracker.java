package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;
import static com.mb.kibeti.LoginActivity.SMSREAD;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;


public class BudgetTracker extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    View view;
    Dialog myDialog;
    NetworthViewPagerAdapter networthViewPagerAdapter;
    boolean smsIsRead = false;
    SmsReceiver smsReceiver;
    private static final int PERMISSION_REQUEST_CODE = 1;
    ImageView backButton;
    String email = "";
    TrialPopUp trialPopUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_spending);
        // Inflate the layout for this fragment

        myDialog = new Dialog(this);
        smsReceiver = new SmsReceiver();

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        smsIsRead = sharedPreferences.getBoolean(SMSREAD, false);
        email = sharedPreferences.getString(EMAIL, "");

        tabLayout = findViewById(R.id.tablayout);
        viewPager2 = findViewById(R.id.view_pager);
        backButton = findViewById(R.id.backButton);
        ConstraintLayout backButtonLayout = findViewById(R.id.imBackLayout);

        backButtonLayout.setVisibility(View.VISIBLE);

        backButton.setVisibility(View.VISIBLE);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        networthViewPagerAdapter = new NetworthViewPagerAdapter(this);

        viewPager2.setAdapter(networthViewPagerAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                            != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, PERMISSION_REQUEST_CODE);
                showPermissionExplanationDialog();
            } else {
                if (!smsIsRead) {
                    if (readSms() > 0) {
                        Intent intent = new Intent(BudgetTracker.this, Read5SMS.class);
                        startActivity(intent);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putBoolean(SMSREAD, true);
//                        editor.apply();
                    }
                }
            }
        } else {
            if (!smsIsRead) {
                readSms();
            }

        }

//        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
//                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
//            @Override
//            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//            }        }
//        );

//        viewPager2.setAdapter(cashflowViewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
    }


    private void AddSpendingPopup() {
//        TextView txtclose;
        TextView cancelBtn;
        Button submitBtn;

        myDialog.setContentView(R.layout.allow_permission_pop_up);
//        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        cancelBtn = (TextView) myDialog.findViewById(R.id.idBtnCancel);
        submitBtn = (Button) myDialog.findViewById(R.id.idBtnReset);

//        txtclose.setText("x");

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                resetting();
                ActivityCompat.requestPermissions(BudgetTracker.this,
                        new String[]{android.Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
                        PERMISSION_REQUEST_CODE);
                myDialog.dismiss();

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

//        txtclose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                myDialog.dismiss();
//            }
//        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!smsIsRead) {

                    readSms();
                        Intent intent = new Intent(BudgetTracker.this, Read5SMS.class);
                        startActivity(intent);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putBoolean(SMSREAD, true);
//                        editor.apply();

//                    }
                }
//                readSms();
            } else {
//                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                showPermissionExplanationDialog();
            }
        }
    }

    public void showPermissionExplanationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Record your spending automatically")
                .setMessage("With your permission, this app can read your SMS message to help you manage your spending.")
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(BudgetTracker.this,
                                new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
                                PERMISSION_REQUEST_CODE);
                    }
                })
                .create()
                .show();
//        AddSpendingPopup();
    }


    public int readSms() {
        ContentResolver contentResolver = getContentResolver();
        int count = 0;
        Cursor cursor = contentResolver.query(
                Telephony.Sms.CONTENT_URI,
                null,
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {

            do {
                String address = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                String body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY));
                if (address.equals("MPESA")) {
//                smsList.add("Sender: " + address + "\nMessage: " + body);
                    if (smsReceiver.mpesasorting(this, body).equals("outflow")) {
//                        smsList.add("Sender: " + address + "\nMessage: " + body);
                        count++;
                    }

                }
            } while (cursor.moveToNext() && count <= 5);

        }

        if (cursor != null) {
            cursor.close();
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SMSREAD, true);
        editor.apply();

//        if (count > 0) {
//            Intent intent = new Intent(BudgetTracker.this, Read5SMS.class);
//            startActivity(intent);
////            return true;
//        }

        return count;
    }
}