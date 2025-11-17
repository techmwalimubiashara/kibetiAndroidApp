package com.mb.kibeti;



import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;
import static com.mb.kibeti.LoginActivity.USERNAME;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyPerformanceFragment extends Fragment {

    View view;
    ProgressBar progressBar;
    PipeLines Genetics = new PipeLines();
    String strUsernamePh = Genetics.Mercury;
    String strEmailAddressPh = Genetics.Venus;
    String strBusinessNamePh = Genetics.Earth;
    String strBusinessName, strUsername, strEmailAddress;
    SharedPreferences sharedPreferences;
    FloatingActionButton floatingActionButton;

    Cursor cur, cur2;
    SQLiteDatabase db, db2;
    ListView listView;
    TabulationCLVA adapter;
    DecimalFormat formatter;
    ArrayList<FeedbackRowItem> rowItems;

    String think_big = Genetics.think_big;
    String my_profile_table = Genetics.my_profile_table;
    String my_profile_table_columns = Genetics.my_profile_table_columns;

    String create_table = Genetics.create_table;
    String select_from = Genetics.select_from;
    String drop_table = Genetics.drop_table;

    String table_in_use = Genetics.table_in_use;
    String table_in_use_columns = Genetics.table_in_use_columns;
    String strTabColBizNam = Genetics.strTabColBizNam;

    String strAppName = Genetics.America;
    String strAppDescription = Genetics.Europe;
    String strErrorToast = Genetics.strErrorToast;
    ProgressDialog pDialog;
    String URL_OPERATION_RETRIEVE_TARGETS = Genetics.URL_OPERATION_RETRIEVE_TARGETS;
    String  strWeekDate, strWeekDay, strTargetRevenue;
    int intCumulativeTargetRevenue = 0;
    int intCumulativeActual = 0;
    int intCumulativeVariance = 0;
    private Dialog myDialog;

    TextView tvCumulativeTargetRevenue, tvCumulativeActual, tvCumulativeVariance;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.tabulation365, container, false);


        if(getArguments()!=null)
        {
            strBusinessName = getArguments().getString(strBusinessNamePh,null);
        }
        sharedPreferences = getContext().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        strEmailAddress = sharedPreferences.getString(EMAIL, "");
        strUsername = sharedPreferences.getString(USERNAME, "");
//        getActivity().setTitle("My Budget - Inflow");

        myDialog = new Dialog(getContext());
        floatingActionButton=view.findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent nextInt = new Intent(getContext(), CalendarActivity.class);
//                nextInt.putExtra(strUsernamePh, strUsername);
//                nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//                nextInt.putExtra(strBusinessNamePh, strBusinessName);
//                startActivity(nextInt);

            }
        });


        formatter = new DecimalFormat("#,###,###,###");

        db = getContext().openOrCreateDatabase(think_big, Context.MODE_PRIVATE, null);
        db.execSQL(create_table + my_profile_table + my_profile_table_columns);

        cur = db.rawQuery(select_from + " " + my_profile_table, null);

//        while (cur.moveToNext()) {
//            strUsername = cur.getString(0);
//            strEmailAddress = cur.getString(2);
//        }

        db2 = getContext().openOrCreateDatabase(think_big, Context.MODE_PRIVATE, null);
        db2.execSQL(create_table + table_in_use + table_in_use_columns);

        cur2 = db2.rawQuery(select_from + " " + table_in_use + " WHERE " + strTabColBizNam + " = '" + strBusinessName + "'", null);

        if (cur2.getCount() == 0) {
//            Intent nextInt = new Intent(getActivity(), TargetsNotSet.class);
//            nextInt.putExtra(strUsernamePh, strUsername);
//            nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//            nextInt.putExtra(strBusinessNamePh, strBusinessName);
//            startActivity(nextInt);
//            finish();
            showPopup();

        } else {
            //Relax
            listView = view.findViewById(R.id.list_view);
            tvCumulativeTargetRevenue = view.findViewById(R.id.tvCumulativeTargetRevenue);
            tvCumulativeActual = view.findViewById(R.id.tvCumulativeActual);
            tvCumulativeVariance = view.findViewById(R.id.tvCumulativeVariance);

            checkConnectionAndRetrieveData();

            rowItems = new ArrayList<>();

            adapter = new TabulationCLVA(getActivity(), R.layout.list_item_tabulation, rowItems);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView tvWeekDate = view.findViewById(R.id.tvBeta);
                    TextView tvTargetRevenue = view.findViewById(R.id.tvGamma);
                    TextView tvActual = view.findViewById(R.id.tvDelta);
                    TextView tvVariance = view.findViewById(R.id.tvEpsilon);
                    TextView tvReason = view.findViewById(R.id.tvZeta);

                    strWeekDate = tvWeekDate.getText().toString();
                    strTargetRevenue = tvTargetRevenue.getText().toString();
                    String strActual = tvActual.getText().toString();
                    String strVariance = tvVariance.getText().toString();
                    String strReason = tvReason.getText().toString();

//                    Intent nextInt = new Intent(getContext(), TabulationDVC.class);
//                    nextInt.putExtra(strUsernamePh, strUsername);
//                    nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//                    nextInt.putExtra("1", strWeekDate);
//                    nextInt.putExtra("2", strTargetRevenue);
//                    nextInt.putExtra("3", strActual);
//                    nextInt.putExtra("4", strVariance);
//                    nextInt.putExtra("5", strReason);
//                    startActivity(nextInt);
                }
            });
        }
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
//        checkConnectionAndRetrieveData();
    }
    private void checkConnectionAndRetrieveData(){
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            //All good
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Retrieving clients for "+strBusinessName);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            operationRetrieveRevenueTargets();
        } else {
            new AlertDialog.Builder(getContext())
                    .setMessage("Internet connection is required to proceed...")
                    .setCancelable(false)
                    .setPositiveButton("Check settings",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                                    Toast.makeText(getContext(), "Kindly relaunch this App after reconfiguring the internet settings...", Toast.LENGTH_LONG).show();
                                    //finish();
                                    //finish();
                                    //finish();
                                }
                            }).create().show();
        }

    }

    private void operationRetrieveRevenueTargets() {

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String strFinalUrl = URL_OPERATION_RETRIEVE_TARGETS;

        StringRequest request = new StringRequest(Request.Method.POST, strFinalUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                pDialog.dismiss();

//                dataClassArrayList.clear();
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    Boolean error = jsonObject.getBoolean("error");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(!error) {
//
                        int l = 0;
                        l = jsonArray.length();

                        for (int i = 0; i < l; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);


                            int intCounter = i + 1;
                            String strCounter = "" + intCounter;

//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String strTargetDate = object.getString("tb_date");
                            String strTargetRevenueBeta = object.getString("target_revenue");
                            String strActualRevenue = object.getString("tb_actual");
                            String strVarianceAlpha = "(0)" ;
//                            String strVarianceAlpha = object.getString("tb_variance");
                            String strVarianceReason = object.getString("tb_reason");



                            String strActual = strActualRevenue;
//                            if (strActualRevenue.equals("")) {
//                                strActual = "0";
//                            } else {
//                                strActual = strActualRevenue;
//                            }
//
                            String strTargetRevenue = strTargetRevenueBeta;
//                            if (strVarianceAlpha.equals("")) {
//                                strVarianceBeta = strTargetRevenueBeta;
//
//                            } else {
//                                strVarianceBeta = strVarianceAlpha;
//                            }
//
//                            int intTargetRevenue = Integer.parseInt(strTargetRevenueBeta);
                            int intActualRevenue = Integer.parseInt(strActual);
                            int intTargetRevenue = Integer.parseInt(strTargetRevenue);
                            int intVariance =  intActualRevenue - intTargetRevenue;
                            intCumulativeTargetRevenue += intTargetRevenue;
                            intCumulativeActual += intActualRevenue;
                            intCumulativeVariance = intCumulativeActual - intCumulativeTargetRevenue;
//                            strTargetRevenue = "Kshs. " + jsonObject.getString("target_revenue") + "/=";
//                            String strMatthew = formatter.format(intTargetRevenue);
//                            String strMark = formatter.format(intActualRevenue);
//                            String strLuke = formatter.format(intVarianceRevenue);
                            if(intVariance<0){
                                intVariance = 0-intVariance;
                                strVarianceAlpha = "("+intVariance+")";
                            }else{
                                strVarianceAlpha =  intVariance+"";
                            }

                            FeedbackRowItem item = new FeedbackRowItem(strCounter, strTargetDate, strTargetRevenueBeta, strActualRevenue,strVarianceAlpha, strVarianceReason);
                            rowItems.add(item);

                        }
//
//                            String strVarianceGamma;
//                            if (intTargetRevenue < intActualRevenue) {
//                                strVarianceGamma = "+" + strLuke;
//                            } else {
//                                strVarianceGamma = "(" + strLuke + ")";
//                            }
//
//                            FeedbackRowItem item = new FeedbackRowItem(strCounter, strTargetDate, strMatthew, strMark, strVarianceGamma, strVarianceReason);
////                            FeedbackRowItem item = new FeedbackRowItem("1", strTargetDate, strVarianceAlpha, strTargetRevenueBeta, strActualRevenue, strVarianceReason);
//                            rowItems.add(item);
//
//
//                        }
////                        if(l>0){
////                            Log.e("jsonArray ", "jsonArray length is  " +l);
////                        }else{
////                            Log.e("jsonArray 2 ", "jsonArray has nothing  ");
////                        }
//
                        String strCumulativeTargetRevenue = formatter.format(intCumulativeTargetRevenue);
                        String strCumulativeActual = formatter.format(intCumulativeActual);
                        String strCumulativeVariance;

                        if (intCumulativeActual > intCumulativeTargetRevenue) {
                            //Surplus
                            strCumulativeVariance = "+" + formatter.format(intCumulativeVariance);
                        } else {
                            //Deficit
                            strCumulativeVariance = "(" + formatter.format(intCumulativeVariance) + ")";
                            strCumulativeVariance = strCumulativeVariance.replace("-", "");
                        }

                        tvCumulativeTargetRevenue.setText(strCumulativeTargetRevenue);
                        tvCumulativeActual.setText(strCumulativeActual);
                        tvCumulativeVariance.setText(strCumulativeVariance);
//
//
//
                        adapter.notifyDataSetChanged();
//
////                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }else{
                        pDialog.dismiss();
//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                    }
                    Log.e("checking ",message);
                }catch (Exception e){

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                pDialog.dismiss();
                Toast.makeText(getActivity(), "Fail to get response ", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {

                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("email", strEmailAddress);
                params.put("business_name", strBusinessName);

                Log.e("Email ",strEmailAddress);

                return params;
            }
        };

        queue.add(request);
    }
    public void showPopup() {

        Button btnToSet;
        TextView txtclose;

        myDialog.setContentView(R.layout.target_not_set_popup);
        btnToSet =(Button) myDialog.findViewById(R.id.idBtnToSet);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);

        btnToSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent nextInt = new Intent(getContext(), RevenueTargetAlternatives.class);
                nextInt.putExtra(Genetics.strOrigin, Genetics.strDestinationRevenueTargets);
                nextInt.putExtra(strUsernamePh, strUsername);
                nextInt.putExtra(strEmailAddressPh, strEmailAddress);
                nextInt.putExtra(strBusinessNamePh, strBusinessName);
                startActivity(nextInt);
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


}