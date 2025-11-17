package com.mb.kibeti;


import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;
import static com.mb.kibeti.LoginActivity.USERNAME;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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

public class MyClientsFragment extends Fragment {

    View view;
    ProgressBar progressBar;
    PipeLines Genetics = new PipeLines();
    String strBusinessNamePh = Genetics.Earth;
    String strUsernamePh = Genetics.Mercury;
    String strEmailAddressPh = Genetics.Venus;
    String strBusinessName = "",strUsername="", strEmailAddress="";
    FloatingActionButton floatingActionButton;
    SharedPreferences sharedPreferences;

    Cursor cur;
    SQLiteDatabase db;
    ListView listView;
    TabulationCLVA adapter;
    ProgressDialog pDialog;
    DecimalFormat formatter;
    ArrayList<FeedbackRowItem> rowItems;

    String think_big = Genetics.think_big;
    String my_profile_table = Genetics.my_profile_table;
    String my_profile_table_columns = Genetics.my_profile_table_columns;
    String create_table = Genetics.create_table;
    String select_from = Genetics.select_from;
    String strAppName = Genetics.America;
    String strAppDescription = Genetics.Europe;
    String strErrorToast = Genetics.strErrorToast;
    String URL_OPERATION_PIPELINES_RETRIEVE = Genetics.URL_OPERATION_PIPELINES_RETRIEVE;
    String strClientName, strValueOfBusiness, strApproximateDealDate, strPhoneNumber;
    TextView tvCumulativeActual;

    int intCumulativeTargetRevenue = 0;
    int intCumulativeActual = 0;
    int intCumulativeVariance = 0;
    LinearLayout clients_download_pdf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.retrieve_pipelines, container, false);

       //setTitle(strBusinessName);

//        strBusinessName = ((AppCompatActivity)getActivity()).getSupportActionBar().getTitle().toString();
//        Bundle bundle = this.getArguments();
//        strBusinessName = bundle.getString(strBusinessNamePh);
//        strEmailAddress = bundle.getString(strEmailAddressPh);
//        strUsername = bundle.getString(strUsernamePh);

        sharedPreferences = getContext().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        strEmailAddress = sharedPreferences.getString(EMAIL, "");
        strUsername = sharedPreferences.getString(USERNAME, "");

        if(getArguments()!=null)
        {
            strBusinessName = getArguments().getString(strBusinessNamePh,null);
        }

        formatter = new DecimalFormat("#,###,###,###");


        //openOrCreateDatabase "globally":
//        db = getActivity().openOrCreateDatabase(think_big, Context.MODE_PRIVATE, null);
//        db.execSQL(create_table + my_profile_table + my_profile_table_columns);
//
//        cur = db.rawQuery(select_from + " " + my_profile_table, null);
//        //cur = db.rawQuery(select_from + " " + my_profile_table + " WHERE `username` != ''", null);
//
//        while (cur.moveToNext()) {
//            strUsername = cur.getString(0);
//            strEmailAddress = cur.getString(2);
//        }

        listView = view.findViewById(R.id.list_view);
        tvCumulativeActual = view.findViewById(R.id.tvCumulativeActual);
        clients_download_pdf = view.findViewById(R.id.clientsPDF);
        rowItems = new ArrayList<>();

        checkConnectionAndRetrieveData();

        adapter = new TabulationCLVA(getContext(), R.layout.list_item_tabulation, rowItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvClientName = view.findViewById(R.id.tvBeta);
                TextView tvValueOfBusiness = view.findViewById(R.id.tvGamma);
                TextView tvApproximateDealDate = view.findViewById(R.id.tvDelta);
                TextView tvPhoneNumber = view.findViewById(R.id.tvEpsilon);

                strClientName = tvClientName.getText().toString();
                strValueOfBusiness = tvValueOfBusiness.getText().toString();
                strApproximateDealDate = tvApproximateDealDate.getText().toString();
                strPhoneNumber = tvPhoneNumber.getText().toString();


//                Intent nextInt = new Intent(getContext(), ClientDVC.class);
//                nextInt.putExtra("1", strClientName);
//                nextInt.putExtra("2", strValueOfBusiness);
//                nextInt.putExtra("3", strApproximateDealDate);
//                nextInt.putExtra("4", strPhoneNumber);
//                nextInt.putExtra("5", strUsername);
//                nextInt.putExtra("6", strEmailAddress);
//                nextInt.putExtra("7", strBusinessName);
//                startActivity(nextInt);

            }
        });

        floatingActionButton=view.findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextInt = new Intent(view.getContext(), ClientNew.class);
                    nextInt.putExtra(strUsernamePh, strUsername);
                    nextInt.putExtra(strEmailAddressPh, strEmailAddress);
                    nextInt.putExtra(strBusinessNamePh, strBusinessName);
                view.getContext().startActivity(nextInt);
//                Toast.makeText(getActivity(), "Business "+strBusinessName+"" +
//                        "\nEmail"+strEmailAddress+"\n"+" Username "+strUsername, Toast.LENGTH_SHORT).show();
            }
        });

        clients_download_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportClientsPDF();
            }
        });
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
        operationRetrievePipelines();
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
    private void operationRetrievePipelines() {

        RequestQueue queue = Volley.newRequestQueue(getContext());

//        String strFinalUrl = URL_OPERATION_RETRIEVE_TARGETS;

        StringRequest request = new StringRequest(Request.Method.POST, URL_OPERATION_PIPELINES_RETRIEVE, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                pDialog.dismiss();

//                dataClassArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    Boolean error = jsonObject.getBoolean("error");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (!error) {
//
                        int l = 0;
                        l = jsonArray.length();

                        for (int i = 0; i < l; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            int intCounter = i + 1;
                            String strCounter = "" + intCounter;

                            strClientName = object.getString("client_name");
                            strValueOfBusiness = object.getString("value_of_business");
                            strApproximateDealDate = object.getString("deal_date");
                            strPhoneNumber = object.getString("phone_number");


                            String strActual = strValueOfBusiness;
                            if (strValueOfBusiness.equals("")) {
                                strActual = "0";
                            } else {
                                strActual = strValueOfBusiness;
                            }

//
//                            int intTargetRevenue = Integer.parseInt(strTargetRevenueBeta);
                            int intActualRevenue = Integer.parseInt(strActual);
                            int intValueOfBusiness = Integer.parseInt(strValueOfBusiness);
//                            int intTargetRevenue = Integer.parseInt(strTargetRevenue);
//                            int intVariance =  intActualRevenue - intTargetRevenue;
//                            intCumulativeTargetRevenue += intTargetRevenue;
                            intCumulativeActual += intActualRevenue;
//                            intCumulativeVariance = intCumulativeActual - intCumulativeTargetRevenue;
//                            strTargetRevenue = "Kshs. " + jsonObject.getString("target_revenue") + "/=";
//                            String strMatthew = formatter.format(intTargetRevenue);
                            String strMark = formatter.format(intActualRevenue);
                            String strValueOfBusinessFomartted = formatter.format(intValueOfBusiness);
//                            String strLuke = formatter.format(intVarianceRevenue);

                            FeedbackRowItem item = new FeedbackRowItem(strCounter, strClientName, strValueOfBusinessFomartted, strApproximateDealDate, strPhoneNumber, "");

                            rowItems.add(item);

                        }
                        String strCumulativeActual = formatter.format(intCumulativeActual);

                        tvCumulativeActual.setText(strCumulativeActual);

                        adapter.notifyDataSetChanged();

//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                    } else {

                        pDialog.dismiss();

                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                    }
//                    Log.e("checking ",message);
                } catch (Exception e) {

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                pDialog.dismiss();

                Toast.makeText(getContext(), "Fail to get response ", Toast.LENGTH_SHORT).show();

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

                return params;
            }
        };

        queue.add(request);
    }
    public void exportClientsPDF() {
//        Intent nextInt = new Intent(getContext(), ClientsDownloadPDF.class);
//        nextInt.putExtra(strUsernamePh, strUsername);
//        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//        nextInt.putExtra(strBusinessNamePh, strBusinessName);
//        startActivity(nextInt);


    }
    public void AddClient(View v) {
        //Intent nextInt = new Intent(this, ClientsRetrieve.class);
        Intent nextInt = new Intent(getActivity(), ClientNew.class);
//        nextInt.putExtra(strUsernamePh, strUsername);
//        nextInt.putExtra(strEmailAddressPh, strEmailAddress);
//        nextInt.putExtra(strBusinessNamePh, strBusinessName);
        startActivity(nextInt);
    }

}