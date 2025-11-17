package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mb.kibeti.goal_tracker.GoalTrackerActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mb.kibeti.landingPageTask.ui.PaymentActivity;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CashflowPdfActivity extends AppCompatActivity {
    private PDFView pdfView;
    ProgressBar progressBar;
    private static final int PERMISSION_STORAGE_CODE = 1000;
    PipeLines utils=new PipeLines();
    private Button  btnNext;
    private TextView btnClose;
    //    Button button;
    private String email = "";
    private SharedPreferences sharedPreferences;
    private FloatingActionButton floatingActionButton;
    private String web_url = utils.BASEURL;
    private String url = "";
    private String activity_name = "document pdf";
    private Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashflow_pdf);

        Intent prevInt = getIntent();
        url = prevInt.getStringExtra("url");
        activity_name = prevInt.getStringExtra("activity_name");

        myDialog = new Dialog(this);



        pdfView = findViewById(R.id.pdfview);
        progressBar = findViewById(R.id.progressbar);
        btnClose = findViewById(R.id.btnClose);
        btnNext = findViewById(R.id.btnNext);
        progressBar.setVisibility(View.VISIBLE);
        floatingActionButton = findViewById(R.id.floating_action_button_download);

        btnNext.setVisibility(View.GONE);

        sharedPreferences = this.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL, "");

        web_url = web_url +"/"+ url + "?email=" + email;

        getSupportActionBar().setTitle(activity_name);

        addExtraButton();

        new CashflowPdfActivity.Retrivepdf().execute(web_url, email);


        Log.e("Web URL","Url is "+web_url);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    String[] permissions = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permissions, PERMISSION_STORAGE_CODE);
                    if (checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_DENIED) {
//                        String[] permissions
                    }
                } else {
                    startDownloading();
                }
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (activity_name.equals("Outflow budget pdf")) {
            UserExperienceFeedback bottomSheet =
                    new UserExperienceFeedback();
                    Bundle bundle = new Bundle();
        bundle.putString("title", "Autobudgeting");
        bundle.putString("email", email);
        bottomSheet.setArguments(bundle);
            bottomSheet.show(getSupportFragmentManager(),
                    "Rate your budgeting process");
        }
    }

    private void startDownloading() {
        String url = web_url;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle(activity_name);
        request.setDescription("Downloading "+activity_name+".....");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "" + System.currentTimeMillis());

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //do whatever you want here
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if(layoutInvest2.getVisibility()==View.VISIBLE){
        getMenuInflater().inflate(R.menu.calculator_menu, menu);
//        MenuItem editItem = menu.findItem(R.id.action_edit);
        MenuItem saveMenu = menu.findItem(R.id.action_save);
        MenuItem notificationMenu = menu.findItem(R.id.notification);


        saveMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    String[] permissions = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permissions, PERMISSION_STORAGE_CODE);
                    if (checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_DENIED) {
//                        String[] permissions
                    }
                } else {
                    startDownloading();
                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    startDownloading();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    class Retrivepdf extends AsyncTask<String, Void, InputStream> {

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());

                }
            } catch (IOException e) {
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            progressBar.setVisibility(View.GONE);
            pdfView.fromStream(inputStream).load();
        }
    }
    private void addExtraButton(){
        if(activity_name.equals("Outflow budget pdf")) {
            btnNext.setText("I would like to track my budget");
            btnNext.setVisibility(View.VISIBLE);
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PaymentRequiredPopup paymentRequiredPopup = new PaymentRequiredPopup(getApplicationContext(), myDialog,
                            "To track your budget, please upgrade your account by tapping Upgrade Account");

                    if (paymentRequiredPopup.checkPayment()) {
//                        Intent intent = new Intent(CashflowPdfActivity.this, BudgetTracker.class);
//                        startActivity(intent);

                        Intent intent = new Intent(CashflowPdfActivity.this, CashflowAnalysis.class);
                        intent.putExtra("cashflow", "outflow");
                        startActivity(intent);

                    }else{
                        Intent intent = new Intent(CashflowPdfActivity.this, PaymentActivity.class);

                        intent.putExtra("from_activity","budget");
                        intent.putExtra("next_activity","actuals");
                        startActivity(intent);
                    }

                }
            });
        }else if(activity_name.equals("Investment Plan Pdf")) {
            btnNext.setText("See your best investments");
            btnNext.setVisibility(View.VISIBLE);
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    PaymentRequiredPopup paymentRequiredPopup = new PaymentRequiredPopup(getApplicationContext(), myDialog,
//                            "To start your investment journey, please upgrade your account by tapping Upgrade Account");
//
//                    if (paymentRequiredPopup.checkPayment()) {
                        Intent intent = new Intent(CashflowPdfActivity.this, InvestmentRecommendationCalculator.class);
                        startActivity(intent);
//                    }
//                    Toast.makeText(CashflowPdfActivity.this, "Coming Soon!!", Toast.LENGTH_SHORT).show();

                }
            });
        }else if(activity_name.equals("Goal Plan Pdf")) {

//            Toast.makeText(this, "Coming soon ...", Toast.LENGTH_SHORT).show();
            btnNext.setText("Implement Goal");
            btnNext.setVisibility(View.VISIBLE);
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(CashflowPdfActivity.this, GoalTrackerActivity.class);
                    startActivity(intent);

                }
            });
        }else{
            btnNext.setVisibility(View.GONE);
        }
    }

}