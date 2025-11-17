package com.mb.kibeti;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RevenueTargetAlternatives extends AppCompatActivity {
    PipeLines Genetics = new PipeLines();
    String strAppName = Genetics.America;
    String strAppDescription = Genetics.Europe;
    String strUsernamePh = Genetics.Mercury;
    String strEmailAddressPh = Genetics.Venus;
    String strBusinessNamePh = Genetics.Earth;
    String strUsername, strEmailAddress, strBusinessName;

    LinearLayout llGenesis, llExodus, llLeviticus, llNumbers, llDeuteronomy;
    AlphaAnimation animGenesis, animExodus, animLeviticus, animNumbers, animDeuteronomy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.revenue_target_alternatives);

        Intent prevInt = getIntent();
        strUsername = prevInt.getStringExtra(strUsernamePh);
        strEmailAddress = prevInt.getStringExtra(strEmailAddressPh);
        strBusinessName = prevInt.getStringExtra(strBusinessNamePh);

        //Toast.makeText(this, "@Revenue target alternatives: " + strEmailAddress, Toast.LENGTH_SHORT).show();

        SimpleDateFormat sdf = new SimpleDateFormat("ss", Locale.getDefault());
        String currentSecond = sdf.format(new Date());

        getSupportActionBar().setTitle("Revenue target alternatives");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        llGenesis = findViewById(R.id.llGenesis);
        llExodus = findViewById(R.id.llExodus);
        llLeviticus = findViewById(R.id.llLeviticus);
        llNumbers = findViewById(R.id.llNumbers);
        llDeuteronomy = findViewById(R.id.llDeuteronomy);

        //The Animations
        animGenesis = new AlphaAnimation(0.0f, 1.0f);
        animGenesis.setDuration(700);
        animGenesis.setStartOffset(400);

        animExodus = new AlphaAnimation(0.0f, 1.0f);
        animExodus.setDuration(700);
        animExodus.setStartOffset(800);

        animLeviticus = new AlphaAnimation(0.0f, 1.0f);
        animLeviticus.setDuration(2100);
        animLeviticus.setStartOffset(1200);

        animNumbers = new AlphaAnimation(0.0f, 1.0f);
        animNumbers.setDuration(700);
        animNumbers.setStartOffset(1600);

        animDeuteronomy = new AlphaAnimation(0.0f, 1.0f);
        animDeuteronomy.setDuration(700);
        animDeuteronomy.setStartOffset(2000);

        //Assign/ Allocate/ Attach:
        if (currentSecond.startsWith("1")) {
            //Top to bottom:
            //On second thought..
            llGenesis.startAnimation(animExodus);
            llExodus.startAnimation(animGenesis);
            llLeviticus.startAnimation(animNumbers);
            llNumbers.startAnimation(animLeviticus);
            llDeuteronomy.startAnimation(animDeuteronomy);

        } else if (currentSecond.startsWith("2")) {
            //Bottom to top:
            llGenesis.startAnimation(animDeuteronomy);
            llExodus.startAnimation(animNumbers);
            llLeviticus.startAnimation(animLeviticus);
            llNumbers.startAnimation(animExodus);
            llDeuteronomy.startAnimation(animGenesis);

        } else if (currentSecond.startsWith("3")) {
            //Converging at the center:
            llGenesis.startAnimation(animGenesis);
            llExodus.startAnimation(animLeviticus);
            llLeviticus.startAnimation(animNumbers);
            llNumbers.startAnimation(animExodus);
            llDeuteronomy.startAnimation(animGenesis);

        } else if (currentSecond.startsWith("4")) {
            //Diverging from the center:
            llGenesis.startAnimation(animLeviticus);
            llExodus.startAnimation(animExodus);
            llLeviticus.startAnimation(animGenesis);
            llNumbers.startAnimation(animExodus);
            llDeuteronomy.startAnimation(animLeviticus);

        } else if (currentSecond.startsWith("5")) {
            //First: First and third: Even numbers
            //Second: Second and fourth: Odd numbers

            llGenesis.startAnimation(animGenesis);
            llExodus.startAnimation(animLeviticus);
            llLeviticus.startAnimation(animExodus);
            llNumbers.startAnimation(animNumbers);
            llDeuteronomy.startAnimation(animLeviticus);

        } else {
            //First: Second and fourth: Odd numbers
            //Second: First and third: Even numbers

            llGenesis.startAnimation(animLeviticus);
            llExodus.startAnimation(animGenesis);
            llLeviticus.startAnimation(animNumbers);
            llNumbers.startAnimation(animExodus);
            llDeuteronomy.startAnimation(animDeuteronomy);
        }
    }

    public void AlternativeDaily (View v) {
//        Intent nextInt = new Intent(this, AlternativeDaily.class);
//        nextInt.putExtra(strUsernamePh, strUsername);
//        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//        nextInt.putExtra(strBusinessNamePh, strBusinessName);
//        startActivity(nextInt);
    }

    public void AlternativeWeekly (View v) {
//        Intent nextInt = new Intent(this, AlternativeWeekly.class);
//        nextInt.putExtra(strUsernamePh, strUsername);
//        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//        nextInt.putExtra(strBusinessNamePh, strBusinessName);
//        startActivity(nextInt);
    }

    public void AlternativeMonthly (View v) {
//        Intent nextInt = new Intent(this, AlternativeMonthly.class);
//        nextInt.putExtra(strUsernamePh, strUsername);
//        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//        nextInt.putExtra(strBusinessNamePh, strBusinessName);
//        startActivity(nextInt);
    }

    public void AlternativeQuarterly (View v) {
//        Intent nextInt = new Intent(this, AlternativeQuarterly.class);
//        nextInt.putExtra(strUsernamePh, strUsername);
//        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//        nextInt.putExtra(strBusinessNamePh, strBusinessName);
//        startActivity(nextInt);
    }

    public void AlternativeAnnually (View v) {
//        Intent nextInt = new Intent(this, AlternativeYearly.class);
//        nextInt.putExtra(strUsernamePh, strUsername);
//        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//        nextInt.putExtra(strBusinessNamePh, strBusinessName);
//        startActivity(nextInt);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_beta, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            //case R.id.home:
//            case android.R.id.home:
//                onBackPressed();
//                //super.onBackPressed();
//                return true;
//            case R.id.action_share:
//                Intent share = new Intent(Intent.ACTION_SEND);
//                share.setType("text/plain");
//                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//                share.putExtra(Intent.EXTRA_SUBJECT, strAppName);
//                share.putExtra(Intent.EXTRA_TEXT, strAppDescription + strUsername);
//                startActivity(Intent.createChooser(share, "Share link!"));
//                return true;
//            case R.id.action_home:
//                Intent intHome = new Intent(this, DashboardAlpha.class);
//                intHome.putExtra(strUsernamePh, strUsername);
//                intHome.putExtra(strEmailAddressPh, strEmailAddress);
//                startActivity(intHome);
//                finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}