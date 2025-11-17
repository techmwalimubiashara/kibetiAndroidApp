package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;
import static com.mb.kibeti.LoginActivity.PASSCODE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

//import com.hanks.passcodeview.PasscodeView;

public class Passcode extends AppCompatActivity {


    String passcode = "";
//    PasscodeView passcodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_passcode);

//        passcodeView = findViewById(R.id.passcodeview);

        SharedPreferences sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        passcode = sharedPreferences.getString(PASSCODE,"");

        // to set length of password as here
        // we have set the length as 5 digits
//        passcodeView.setPasscodeLength(4)
//                // to set pincode or passcode
//                .setLocalPasscode(passcode)
//
//                // to set listener to it to check whether
//                // passwords has matched or failed
//                .setListener(new PasscodeView.PasscodeViewListener() {
//                    @Override
//                    public void onFail() {
//                        // to show message when Password is incorrect
//                        Toast.makeText(Passcode.this, "Wrong passcode", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onSuccess(String number) {
//                        // here is used so that when password
//                        // is correct user will be
//                        // directly navigated to next activity
//                        Intent intent_passcode = new Intent(Passcode.this, MainActivity.class);
//                        startActivity(intent_passcode);
//                    }
//                });
    }
}