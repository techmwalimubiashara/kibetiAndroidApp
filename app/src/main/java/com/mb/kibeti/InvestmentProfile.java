package com.mb.kibeti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvestmentProfile extends AppCompatActivity {
    ImageButton backButton;
    BottomSheetDialog bottomSheetDialog;
    DataClass dataClass;
    ArrayList<DataClass> dataClassArrayList100 = new ArrayList<>();
    ArrayList<DataClass> dataClassArrayList75 = new ArrayList<>();
    ArrayList<DataClass> dataClassArrayList50 = new ArrayList<>();
    ArrayList<DataClass> dataClassArrayList25 = new ArrayList<>();
    ArrayList<DataClass> allClassArrayList = new ArrayList<>();
    ArrayList<DataClass> investClassArrayList = new ArrayList<>();
    String age_grp = "", experience = "", tolerance = "", appetite = "";
    ListView listView100, listView50, listView25, listViewall, quarterlistview, listView75;
    TaskListAdpater myAdapter100, myAdapter75, myAdapter50, myAdapter25, allAdapter,investAdapter;
    ProgressDialog pDialog;
    CardView cardView100, cardView75, cardView50, cardView25, cardView0;
    CardView cardTitle100,cardTitle75,cardTitle50,cardTitle25,cardTitle0;
    Dialog myDialog;
    ProgressBar progressBar;
    ListView invest_list;
    LinearLayout layoutComingSoon;
    private static final String PHONE_NUMBER = "+254746380606";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.risk_profile);

        pDialog = new ProgressDialog(this);

        backButton = findViewById(R.id.backButton);

        myDialog = new Dialog(this);

        listView100 = findViewById(R.id.listview100);
        listView75 = findViewById(R.id.listview75);
        listView50 = findViewById(R.id.listview50);
        listView25 = findViewById(R.id.listview25);
        listViewall = findViewById(R.id.listview0);

        cardView25 = findViewById(R.id.card25);
        cardView50 = findViewById(R.id.card50);
        cardView75 = findViewById(R.id.card75);
        cardView100 = findViewById(R.id.card100);
        cardView0 = findViewById(R.id.card0);

        cardTitle100 = findViewById(R.id.cardTitle100);
        cardTitle75 = findViewById(R.id.cardTitle75);
        cardTitle50 = findViewById(R.id.cardTitle50);
        cardTitle25 = findViewById(R.id.cardTitle25);
        cardTitle0 = findViewById(R.id.cardTitle0);

        pDialog.setMessage("Getting recommended investments ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        Intent intent = getIntent();
        age_grp = intent.getStringExtra("age_group");
        appetite = intent.getStringExtra("appetite");
        experience = intent.getStringExtra("experience");
        tolerance = intent.getStringExtra("tolerance");

        getInflow();


        myAdapter100 = new TaskListAdpater(this, dataClassArrayList100);
        myAdapter75 = new TaskListAdpater(this, dataClassArrayList75);
        myAdapter50 = new TaskListAdpater(this, dataClassArrayList50);
        myAdapter25 = new TaskListAdpater(this, dataClassArrayList25);
        allAdapter = new TaskListAdpater(this, allClassArrayList);

        investAdapter = new TaskListAdpater(this,investClassArrayList);
        listView100.setAdapter(myAdapter100);
        listView100.setClickable(true);


        listView75.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String phoneNumber = "tel:1234567890";  // Replace with your number
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse(phoneNumber));
                startActivity(callIntent);
            }
        });
        listView100.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                ShowPopup(myAdapter100.getItem(i).getLine(),myAdapter100.getItem(i).getCat());

            }
        });

        listView75.setAdapter(myAdapter75);
        listView75.setClickable(true);

        listView75.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                ContactUsPopup contactUsPopup = new ContactUsPopup();
                createBottomSheetDialog();
            }
        });

        listView50.setAdapter(myAdapter50);
        listView50.setClickable(true);

        listView25.setAdapter(myAdapter25);
        listView25.setClickable(true);

        listViewall.setAdapter(allAdapter);
        listViewall.setClickable(true);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void adjustListViewHeight(ListView listView) {
        android.widget.ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // No adapter, nothing to do
            return;
        }

        int totalHeight = 0;
        int desiredWidth = android.view.View.MeasureSpec.makeMeasureSpec(listView.getWidth(), android.view.View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            android.view.View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, android.view.View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        android.view.ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void adjustCardViewHeight(ListView listView, int num, CardView cardView) {
        listView.post(() -> {
            // Calculate total height of ListView items
            int totalHeight = 71;
            for (int i = 0; i < listView.getCount(); i++) {
                View listItem = listView.getAdapter().getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
                Log.e("TAG", "Height as at " + i + " is " + totalHeight);
            }
            Log.e("TAG", "Total count for listview " + num + " is " + listView.getCount());


            // Add the divider height (if applicable)
            totalHeight += listView.getDividerHeight() * (listView.getCount() - 1);

            // Update the CardView height to wrap the ListView content
            ViewGroup.LayoutParams params = cardView.getLayoutParams();
            params.height = totalHeight + listView.getPaddingTop() + listView.getPaddingBottom();
            Log.e("TAG", "Total height is " + totalHeight);
            cardView.setLayoutParams(params);
        });
    }

    private void getInflow() {

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, "https://mwalimubiashara.com/app/invest_journey.php", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);

                pDialog.dismiss();
                dataClassArrayList100.clear();
                allClassArrayList.clear();
                dataClassArrayList75.clear();
                dataClassArrayList50.clear();
                dataClassArrayList25.clear();
//                int d1 = 0, w1 = 0, m1 = 0, q1 = 0, a1 = 0;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {

                        int l = 0;
                        l = jsonArray.length();

                        for (int i = 0; i < l; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String invest = object.getString("invest");
                            String score = object.getString("score");
                            String code = object.getString("code");

                            Log.e("TAG", "Invest " + invest + " score " + score + " code is  code");

                            if (score.equals("4")) {

                                Log.e("TAG", "Adding list 100 " + invest);
                                dataClass = new DataClass("qa", invest, code, "view", "vv", "22");
                                dataClassArrayList100.add(dataClass);
                            }
                            if (score.equals("3")) {

                                dataClass = new DataClass("", invest, code, "call", "", "");
                                dataClassArrayList75.add(dataClass);
                            }
                            if (score.equals("2")) {

                                dataClass = new DataClass("", invest, code, "c", "", "");
                                dataClassArrayList50.add(dataClass);
                            }
                            if (score.equals("1")) {

                                dataClass = new DataClass("", invest, code, "c", "", "");
                                dataClassArrayList25.add(dataClass);
                            }
                            if (score.equals("0")) {

                                dataClass = new DataClass("", invest, code, "c", "", "");
                                allClassArrayList.add(dataClass);
                            }

                        }

                        myAdapter100.notifyDataSetChanged();
                        myAdapter75.notifyDataSetChanged();
                        myAdapter50.notifyDataSetChanged();
                        myAdapter25.notifyDataSetChanged();
                        allAdapter.notifyDataSetChanged();

                    }

                    if (listView100.getCount() != 0) {
                        adjustCardViewHeight(listView100, 1, cardView100);
                        cardTitle100.setVisibility(View.GONE);
                    }else{
                        listView100.setVisibility(View.GONE);
                        cardTitle100.setVisibility(View.VISIBLE);
                    }

                    if (listView75.getCount() != 0) {
                        adjustCardViewHeight(listView75, 2, cardView75);
                        cardTitle75.setVisibility(View.GONE);
                    }else{
                        listView75.setVisibility(View.GONE);
                        cardTitle75.setVisibility(View.VISIBLE);
                    }

                    if (listView50.getCount() != 0) {
                        adjustCardViewHeight(listView50, 3, cardView50);
                        cardTitle50.setVisibility(View.GONE);
                    }else{
                        listView50.setVisibility(View.GONE);
                        cardTitle50.setVisibility(View.VISIBLE);
                    }

                    if (listView25.getCount() != 0) {
                        adjustCardViewHeight(listView25, 4, cardView25);
                        cardTitle25.setVisibility(View.GONE);
                    }else{
                        listView25.setVisibility(View.GONE);
                        cardTitle25.setVisibility(View.VISIBLE);
                    }

                    if (listViewall.getCount() != 0) {
                        adjustCardViewHeight(listViewall, 5, cardView0);
                        cardTitle0.setVisibility(View.GONE);
                    }else{
                        listViewall.setVisibility(View.GONE);
                        cardTitle0.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {

                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Fail to get response ", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {

                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("age_grp", age_grp);
                params.put("exp", experience);
                params.put("app", appetite);
                params.put("tol", tolerance);

                return params;
            }
        };

        queue.add(request);
    }

    private void ShowPopup(String cat,String invest) {
        TextView txtclose,tvLink,tvTitle;
        Button btnFollow, submitBtn, btnClose;
        EditText edCat, edLine, edAmount;
        Spinner freq;
        ScrollView scrollView;



        myDialog.setContentView(R.layout.invest_popup);
        btnClose = myDialog.findViewById(R.id.idBtnClose);
        invest_list = myDialog.findViewById(R.id.invest_list);
        tvTitle = myDialog.findViewById(R.id.tvTitle);
        layoutComingSoon = myDialog.findViewById(R.id.layoutComingSoon);
        progressBar = myDialog.findViewById(R.id.progressBar);


        invest_list.setAdapter(investAdapter);
        invest_list.setClickable(true);


        tvTitle.setText("Best place to invest in "+invest);

        getInvestList(cat);



        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void getInvestList(String code){
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, "https://mwalimubiashara.com/app/invest_details.php", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);

//                pDialog.dismiss();
                investClassArrayList.clear();
//                int d1 = 0, w1 = 0, m1 = 0, q1 = 0, a1 = 0;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {

                        int l = 0;
                        l = jsonArray.length();

                        for (int i = 0; i < l; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String invest = object.getString("invest");
                            String url = object.getString("url");

                            dataClass = new DataClass("qa", invest, "xx", "view", "vv", "22");

                                dataClass = new DataClass("", invest, url, "url", "", "");
                                investClassArrayList.add(dataClass);
                        }

                    }
                    investAdapter.notifyDataSetChanged();

                    if(invest_list.getCount()<1){
                        layoutComingSoon.setVisibility(View.VISIBLE);
                        invest_list.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    }else {
                        invest_list.setVisibility(View.VISIBLE);
                        layoutComingSoon.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    }

                } catch (Exception e) {

                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Fail to get response ", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {

                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("code", code);

                return params;
            }
        };

        queue.add(request);
    }

    private void createBottomSheetDialog() {
        if (bottomSheetDialog == null) {
            View v = LayoutInflater.from(this).inflate(R.layout.contact_us_popup, null);

            Button btnCall = v.findViewById(R.id.btnCall);
            Button btnWhatsApp = v.findViewById(R.id.btnWhatsApp);

            // WhatsApp Button Click
            btnWhatsApp.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   openWhatsApp(PHONE_NUMBER, "Hello! I need assistance.");
                                               }
                                           });



            // Call Button Click
            btnCall.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               makePhoneCall(PHONE_NUMBER);
                                           }
                                       });

            bottomSheetDialog = new BottomSheetDialog(this);

            bottomSheetDialog.setContentView(v);

            bottomSheetDialog.show();
        } else {
            bottomSheetDialog.show();
        }
    }
    private void openWhatsApp(String phoneNumber, String message) {
        try {
            String url = "https://wa.me/" + phoneNumber.replace("+", "") + "?text=" + Uri.encode(message);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "WhatsApp is not installed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void makePhoneCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }


}