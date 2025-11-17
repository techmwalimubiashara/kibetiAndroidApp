package com.mb.kibeti;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.mb.kibeti.landingPageTask.ui.LandingPageActivity;

public class SplashActivity extends AppCompatActivity {


    private static final long SPLASH_TIME_OUT = 2000; // 2 seconds
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkConnectionAndRetrieveData();
        // Use a Handler to delay the transition to the main activity

    }

    private void checkConnectionAndRetrieveData() {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

//                    IntroActivity introActivity = new IntroActivity();
//                    introActivity.restorePrefData();
                    Intent intent = new Intent(SplashActivity.this, LandingPageActivity.class);

                        startActivity(intent);
                        finish();


                    // Close the splash screen activity
                }
            }, SPLASH_TIME_OUT);

    }

}