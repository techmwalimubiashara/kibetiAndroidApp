package com.mb.kibeti;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class SpecifiedDateDvc extends AppCompatActivity {
    DecimalFormat formatter;
    Cursor cur1, cur2, cur3, cur4, cur5, cur6;
    SQLiteDatabase db1, db2, db3, db4, db5, db6;

    PipeLines Genetics = new PipeLines();
    String think_big = Genetics.think_big;

    //Tables
    String table_in_use = Genetics.table_in_use;
    String daily_target_table = Genetics.daily_target_table;
    String weekday_targets_table = Genetics.weekday_targets_table;
    String monthly_targets_table = Genetics.monthly_targets_table;
    String quarterly_targets_table = Genetics.quarterly_targets_table;
    String yearly_target_table = Genetics.yearly_target_table;

    //Table Columns
    String table_in_use_columns = Genetics.table_in_use_columns;
    String daily_target_table_columns = Genetics.daily_target_table_columns;
    String weekday_targets_table_columns = Genetics.weekday_targets_table_columns;
    String monthly_targets_table_columns = Genetics.monthly_targets_table_columns;
    String quarterly_targets_table_columns = Genetics.quarterly_targets_table_columns;
    String yearly_target_table_columns = Genetics.yearly_target_table_columns;

    String create_table = Genetics.create_table;
    String select_from = Genetics.select_from;

    String strSelectedMonthPh = Genetics.strSelectedMonthPh;
    String strRevenueGeneratedPh = Genetics.strRevenueGeneratedPh;
    String strRevenueTargetPh = Genetics.strRevenueTargetPh;
    String strSelectedDatePh = Genetics.strSelectedDatePh;
    String strSelectedDayPh = Genetics.strSelectedDayPh;

    String strTabColBizNam = Genetics.strTabColBizNam;

    String strDaily;
    String strSundays, strMondays, strTuesdays, strWednesdays, strThursdays, strFridays, strSaturdays;
    String strJanuary, strFebruary, strMarch, strApril, strMay, strJune, strJuly, strAugust, strSeptember, strOctober, strNovember, strDecember;
    String strQuarter1, strQuarter2, strQuarter3, strQuarter4;
    String strYearly;

    String intDaily;
    String intSundays, intMondays, intTuesdays, intWednesdays, intThursdays, intFridays, intSaturdays;
    String intJanuary, intFebruary, intMarch, intApril, intMay, intJune, intJuly, intAugust, intSeptember, intOctober, intNovember, intDecember;
    String intQuarter1, intQuarter2, intQuarter3, intQuarter4;
    String intYearly;

    EditText etTheChallenge;
    TextView tvSelectedDay, tvTheChallenge, tvActualRevenue;
    String strTableInUse, strSelectedMonth, strSelectedDate, strSelectedDay, strMergeAlpha, strMergeBeta, strRevenueTarget, strRevenueGenerated, strActualRevenue;
    int intDailyTarget, intMonthlyTarget, intYearlyTarget;
    String strDailyTarget, strMonthlyTarget, strYearlyTarget;

    String strAppName = Genetics.America;
    String strAppDescription = Genetics.Europe;
    //String strAppDescription = Genetics.Europe.replace("From", "");
    String strUsernamePh = Genetics.Mercury;
    String strEmailAddressPh = Genetics.Venus;
    String strBusinessNamePh = Genetics.Earth;
    String strUsername, strEmailAddress, strBusinessName;
    String strCurrentYear = " 2023";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specified_date_dvc);

        formatter = new DecimalFormat("#,###,###,###,###,###,###");

        Intent prevInt = getIntent();
        strSelectedMonth = prevInt.getStringExtra(strSelectedMonthPh);
        strSelectedDate = prevInt.getStringExtra(strSelectedDatePh);
        strSelectedDay = prevInt.getStringExtra(strSelectedDayPh);
        strUsername = prevInt.getStringExtra(strUsernamePh);
        strEmailAddress = prevInt.getStringExtra(strEmailAddressPh);
        strBusinessName = prevInt.getStringExtra(strBusinessNamePh);

        DecimalFormat formatter = new DecimalFormat("#,###,###,###");

        db1 = openOrCreateDatabase(think_big, Context.MODE_PRIVATE, null);
        db2 = openOrCreateDatabase(think_big, Context.MODE_PRIVATE, null);
        db3 = openOrCreateDatabase(think_big, Context.MODE_PRIVATE, null);
        db4 = openOrCreateDatabase(think_big, Context.MODE_PRIVATE, null);
        db5 = openOrCreateDatabase(think_big, Context.MODE_PRIVATE, null);
        db6 = openOrCreateDatabase(think_big, Context.MODE_PRIVATE, null);

        db1.execSQL(create_table + table_in_use + table_in_use_columns);
        db2.execSQL(create_table + daily_target_table + daily_target_table_columns);
        db3.execSQL(create_table + weekday_targets_table + weekday_targets_table_columns);
        db4.execSQL(create_table + monthly_targets_table + monthly_targets_table_columns);
        db5.execSQL(create_table + quarterly_targets_table + quarterly_targets_table_columns);
        db6.execSQL(create_table + yearly_target_table + yearly_target_table_columns);

        cur1 = db1.rawQuery(select_from + " " + table_in_use + " WHERE " + strTabColBizNam + " = '" + strBusinessName + "'", null);
        cur2 = db2.rawQuery(select_from + " " + daily_target_table + " WHERE " + strTabColBizNam + " = '" + strBusinessName + "'", null);
        cur3 = db3.rawQuery(select_from + " " + weekday_targets_table + " WHERE " + strTabColBizNam + " = '" + strBusinessName + "'", null);
        cur4 = db4.rawQuery(select_from + " " + monthly_targets_table + " WHERE " + strTabColBizNam + " = '" + strBusinessName + "'", null);
        cur5 = db5.rawQuery(select_from + " " + quarterly_targets_table + " WHERE " + strTabColBizNam + " = '" + strBusinessName + "'", null);
        cur6 = db5.rawQuery(select_from + " " + yearly_target_table + " WHERE " + strTabColBizNam + " = '" + strBusinessName + "'", null);

        if (cur1.getCount()<=0){
            //No table in use: Redirect to MainActivity:
            Intent nextInt = new Intent(this, MainActivity.class);
            startActivity(nextInt);
            finish();
        } else {
            while (cur1.moveToNext()) {
                strTableInUse = cur1.getString(2);
            }
        }
//        strTableInUse = "Daily";

        //Revenue targets as raw strings
        while (cur2.moveToNext()) {
            strDaily = cur2.getString(2);
        }
//        strDaily = "1200";

        while (cur3.moveToNext()) {
            strSundays = cur3.getString(3);
            strMondays = cur3.getString(4);
            strTuesdays = cur3.getString(5);
            strWednesdays = cur3.getString(6);
            strThursdays = cur3.getString(7);
            strFridays = cur3.getString(8);
            strSaturdays = cur3.getString(9);
        }

        while (cur4.moveToNext()) {
            strJanuary = cur4.getString(3);
            strFebruary = cur4.getString(4);
            strMarch = cur4.getString(5);
            strApril = cur4.getString(6);
            strMay = cur4.getString(7);
            strJune = cur4.getString(8);
            strJuly = cur4.getString(9);
            strAugust = cur4.getString(10);
            strSeptember = cur4.getString(11);
            strOctober = cur4.getString(12);
            strNovember = cur4.getString(13);
            strDecember = cur4.getString(14);
        }

        while (cur5.moveToNext()) {
            strQuarter1 = cur5.getString(1);
            strQuarter2 = cur5.getString(2);
            strQuarter3 = cur5.getString(3);
            strQuarter4 = cur5.getString(4);
        }

        while (cur6.moveToNext()) {
            strYearly = cur6.getString(3);
        }

        if (strTableInUse.equals("Daily")) {
            //Revenue targets as formatted strings
            intDaily = formatter.format(Integer.parseInt(strDaily));
            strRevenueTarget = intDaily;
            strMergeBeta = "Your revenue target for today is " + strRevenueTarget + "\n\n\nHow much revenue did you generate today?";

        } else if (strTableInUse.equals("Weekdays")) {
            //Revenue targets as formatted strings
            intSundays = formatter.format(Integer.parseInt(strSundays));
            intMondays = formatter.format(Integer.parseInt(strMondays));
            intTuesdays = formatter.format(Integer.parseInt(strTuesdays));
            intWednesdays = formatter.format(Integer.parseInt(strWednesdays));
            intThursdays = formatter.format(Integer.parseInt(strThursdays));
            intFridays = formatter.format(Integer.parseInt(strFridays));
            intSaturdays = formatter.format(Integer.parseInt(strSaturdays));

            if (strSelectedDay.equals("Sunday")) {
                strRevenueTarget = intSundays;

            } else if (strSelectedDay.equals("Monday")) {
                strRevenueTarget = intMondays;

            } else if (strSelectedDay.equals("Tuesday")) {
                strRevenueTarget = intTuesdays;

            } else if (strSelectedDay.equals("Wednesday")) {
                strRevenueTarget = intWednesdays;

            } else if (strSelectedDay.equals("Thursday")) {
                strRevenueTarget = intThursdays;

            } else if (strSelectedDay.equals("Friday")) {
                strRevenueTarget = intFridays;
            } else {
                strRevenueTarget = intSaturdays;
            }

            strMergeBeta = "Your revenue target for today is " + strRevenueTarget + "\n\n\nHow much revenue did you generate today?";

        } else if (strTableInUse.equals("Monthly")) {
            intJanuary = formatter.format(Integer.parseInt(strJanuary));
            intFebruary = formatter.format(Integer.parseInt(strFebruary));
            intMarch = formatter.format(Integer.parseInt(strMarch));
            intApril = formatter.format(Integer.parseInt(strApril));
            intMay = formatter.format(Integer.parseInt(strMay));
            intJune = formatter.format(Integer.parseInt(strJune));
            intJuly = formatter.format(Integer.parseInt(strJuly));
            intAugust = formatter.format(Integer.parseInt(strAugust));
            intSeptember = formatter.format(Integer.parseInt(strSeptember));
            intOctober = formatter.format(Integer.parseInt(strOctober));
            intNovember = formatter.format(Integer.parseInt(strNovember));
            intDecember = formatter.format(Integer.parseInt(strDecember));

            if (strSelectedMonth.equals("January" + strCurrentYear)) {
                strMonthlyTarget = intJanuary;
                intDailyTarget = Math.round(Integer.parseInt(strJanuary)/31);

            } else if (strSelectedMonth.equals("February" + strCurrentYear)) {
                strMonthlyTarget = intFebruary;
                intDailyTarget = Math.round(Integer.parseInt(strFebruary)/28);

            } else if (strSelectedMonth.equals("March" + strCurrentYear)) {
                strMonthlyTarget = intMarch;
                intDailyTarget = Math.round(Integer.parseInt(strMarch)/31);

            } else if (strSelectedMonth.equals("April" + strCurrentYear)) {
                strMonthlyTarget = intApril;
                intDailyTarget = Math.round(Integer.parseInt(strApril)/30);

            } else if (strSelectedMonth.equals("May" + strCurrentYear)) {
                strMonthlyTarget = intMay;
                intDailyTarget = Math.round(Integer.parseInt(strMay)/31);

            } else if (strSelectedMonth.equals("June" + strCurrentYear)) {
                strMonthlyTarget = intJune;
                intDailyTarget = Math.round(Integer.parseInt(strJune)/30);

            } else if (strSelectedMonth.equals("July" + strCurrentYear)) {
                strMonthlyTarget = intJuly;
                intDailyTarget = Math.round(Integer.parseInt(strJuly)/31);

            } else if (strSelectedMonth.equals("August" + strCurrentYear)) {
                strMonthlyTarget = intAugust;
                intDailyTarget = Math.round(Integer.parseInt(strAugust)/31);

            } else if (strSelectedMonth.equals("September" + strCurrentYear)) {
                strMonthlyTarget = intSeptember;
                intDailyTarget = Math.round(Integer.parseInt(strSeptember)/30);

            } else if (strSelectedMonth.equals("October" + strCurrentYear)) {
                strMonthlyTarget = intOctober;
                intDailyTarget = Math.round(Integer.parseInt(strOctober)/31);

            } else if (strSelectedMonth.equals("November" + strCurrentYear)) {
                strMonthlyTarget = intNovember;
                intDailyTarget = Math.round(Integer.parseInt(strNovember)/30);

            } else {
                strMonthlyTarget = intDecember;
                intDailyTarget = Math.round(Integer.parseInt(strDecember)/31);
            }

            strDailyTarget = formatter.format(intDailyTarget);
            strRevenueTarget = "" + intDailyTarget;
            strMergeBeta = "The monthly target is " + strMonthlyTarget +
                    "\n\n\n\nThe daily target is " + strDailyTarget +
                    "\n\n\n\nYour revenue target for today is " + strDailyTarget + "\n\n\nHow much revenue did you generate today?";

        } else if (strTableInUse.equals("Quarterly")) {
            //Revenue targets as formatted strings
            intQuarter1 = formatter.format(Integer.parseInt(strQuarter1));
            intQuarter2 = formatter.format(Integer.parseInt(strQuarter2));
            intQuarter3 = formatter.format(Integer.parseInt(strQuarter3));
            intQuarter4 = formatter.format(Integer.parseInt(strQuarter4));

            int intQuarter1Target = Integer.parseInt(strQuarter1);
            int intQuarter2Target = Integer.parseInt(strQuarter2);
            int intQuarter3Target = Integer.parseInt(strQuarter3);
            int intQuarter4Target = Integer.parseInt(strQuarter4);

            int intQuarter1MonthlyTarget = intQuarter1Target/3;
            int intQuarter1DailyTarget = intQuarter1Target/90;

            int intQuarter2MonthlyTarget = intQuarter2Target/3;
            int intQuarter2DailyTarget = intQuarter2Target/90;

            int intQuarter3MonthlyTarget = intQuarter3Target/3;
            int intQuarter3DailyTarget = intQuarter3Target/90;

            int intQuarter4MonthlyTarget = intQuarter4Target/3;
            int intQuarter4DailyTarget = intQuarter4Target/90;

            String strQuarter1MonthlyTargetFormatted = formatter.format(Integer.parseInt("" + intQuarter1MonthlyTarget));
            String strQuarter1DailyTargetFormatted = formatter.format(Integer.parseInt("" + intQuarter1DailyTarget));

            String strQuarter2MonthlyTargetFormatted = formatter.format(Integer.parseInt("" + intQuarter2MonthlyTarget));
            String strQuarter2DailyTargetFormatted = formatter.format(Integer.parseInt("" + intQuarter2DailyTarget));

            String strQuarter3MonthlyTargetFormatted = formatter.format(Integer.parseInt("" + intQuarter3MonthlyTarget));
            String strQuarter3DailyTargetFormatted = formatter.format(Integer.parseInt("" + intQuarter3DailyTarget));

            String strQuarter4MonthlyTargetFormatted = formatter.format(Integer.parseInt("" + intQuarter4MonthlyTarget));
            String strQuarter4DailyTargetFormatted = formatter.format(Integer.parseInt("" + intQuarter4DailyTarget));

            String strSituation = "\n\nQuarter 1:\nQuarterly target: " + intQuarter1 + "\nMonthly target: " + strQuarter1MonthlyTargetFormatted + "\nDaily target: " + strQuarter1DailyTargetFormatted + "\n\n" +
                    "\n\nQuarter 2:\nQuarterly target: " + intQuarter2 + "\nMonthly target: " + strQuarter2MonthlyTargetFormatted + "\nDaily target: " + strQuarter2DailyTargetFormatted + "\n\n" +
                    "\n\nQuarter 3:\nQuarterly target: " + intQuarter3 + "\nMonthly target: " + strQuarter3MonthlyTargetFormatted + "\nDaily target: " + strQuarter3DailyTargetFormatted + "\n\n" +
                    "\n\nQuarter 4:\nQuarterly target: " + intQuarter4 + "\nMonthly target: " + strQuarter4MonthlyTargetFormatted + "\nDaily target: " + strQuarter4DailyTargetFormatted + "\n\n";

            strMergeBeta = "Your quarterly revenue targets are as follows:" + strSituation + "\nHow much revenue did you generate today?";

        } else if (strTableInUse.equals("Yearly")) {
            //Revenue targets as formatted strings
            intYearly = formatter.format(Integer.parseInt(strYearly));
            intMonthlyTarget = Integer.parseInt(strYearly)/12;
            intDailyTarget = Integer.parseInt(strYearly)/365;
            strRevenueTarget = "" + intDailyTarget;

            strMonthlyTarget = formatter.format(intMonthlyTarget);
            strDailyTarget = formatter.format(intDailyTarget);

            strMergeBeta = "The yearly target is " + intYearly +
                    "\n\n\n\nThe monthly target is " + strMonthlyTarget +
                    "\n\n\n\nThe daily target is " + strDailyTarget +
                    "\n\n\n\nYour revenue target for today is " + strDailyTarget + "\n\n\nHow much revenue did you generate today?";
        }

        strMergeAlpha = strSelectedDate + " " + strSelectedMonth;

        getSupportActionBar().setTitle(strMergeAlpha);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvSelectedDay = findViewById(R.id.tvSelectedDay);
        tvTheChallenge = findViewById(R.id.tvTheChallenge);
        etTheChallenge = findViewById(R.id.etTheChallenge);

        etTheChallenge.addTextChangedListener(formatterTextWatcher);

        tvActualRevenue = findViewById(R.id.tvActualRevenue);

        tvSelectedDay.setText(strSelectedDay + "\n\n" + strBusinessName);
        tvTheChallenge.setText(strMergeBeta);
    }

    TextWatcher formatterTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //Toast.makeText(AlternativeDaily.this, s, Toast.LENGTH_SHORT).show();
            //if (etDaily.equals("")) {
            if (etTheChallenge.getText().toString().equals("")) {
                //Relax: No point of trying to attempt to number format nulls
            } else {
                int intCharLen = etTheChallenge.getText().toString().length();
                String strCharLen = "" + intCharLen;

                if (strCharLen.equals("19")) {
                    Toast.makeText(SpecifiedDateDvc.this, "Character limit reached", Toast.LENGTH_SHORT).show();
                } else {
                    Long lngDaily = Long.parseLong(s.toString());
                    strActualRevenue = formatter.format(lngDaily);
                    tvActualRevenue.setText(strActualRevenue);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public void Analyse (View v) {
        strRevenueGenerated = etTheChallenge.getText().toString().trim().replace(",", "");

        if (strRevenueGenerated.equals("")) {
            Toast.makeText(this, "Please enter the revenue you generated today", Toast.LENGTH_SHORT).show();
        } else {
            if (strTableInUse.equals("Quarterly")) {
                Toast.makeText(this, "Work in progress", Toast.LENGTH_SHORT).show();

            } else {
//                //Toast.makeText(this, "\n" + strRevenueTarget + "\n", Toast.LENGTH_SHORT).show();
//                Intent nextInt = new Intent(this, OperationAnalyse.class);
//                nextInt.putExtra(strRevenueGeneratedPh, strRevenueGenerated);
//                nextInt.putExtra(strRevenueTargetPh, strRevenueTarget);
//                nextInt.putExtra(strSelectedMonthPh, strSelectedMonth);
//                nextInt.putExtra(strSelectedDatePh, strSelectedDate);
//                nextInt.putExtra(strUsernamePh, strUsername);
//                nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//                nextInt.putExtra(strBusinessNamePh, strBusinessName);
//                startActivity(nextInt);
//                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_beta, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
            //super.onBackPressed();
            return true;
        }else if(id == R.id.action_share){
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            share.putExtra(Intent.EXTRA_SUBJECT, strAppName);
            share.putExtra(Intent.EXTRA_TEXT, strAppDescription + strUsername);
            startActivity(Intent.createChooser(share, "Share link!"));
            return true;
        }else if(id == R.id.action_home){
            Intent intHome = new Intent(this, DashboardAlpha.class);
            intHome.putExtra(strUsernamePh, strUsername);
            intHome.putExtra(strEmailAddressPh, strEmailAddress);
            startActivity(intHome);
            finish();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
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
    }
}