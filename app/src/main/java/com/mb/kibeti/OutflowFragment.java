package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OutflowFragment extends Fragment {

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 3000;
    View view;
    ProgressBar progressBar;
    ListView listView;
    MyAdapter myAdapter;
    DataClass dataClass;
    PipeLines utils = new PipeLines();
    String url = utils.GET_OUTFLOW_URL;
//    String url = "https://mwalimubiashara.com/app/get_outflow.php";
    String email = "";
    Button idBtnAddOutflow;
    SharedPreferences sharedPreferences;
    public static ArrayList<DataClass> dataClassArrayList = new ArrayList<>();
    FloatingActionButton floatingActionButton;

    LinearLayout linearLayout;
    Dialog myDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_outflow, container, false);
        listView = view.findViewById(R.id.listview);
        idBtnAddOutflow = view.findViewById(R.id.idBtnAddOutflow);
        progressBar = view.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        getActivity().setTitle("My Budget - Outflow");

        myDialog = new Dialog(getContext());

        linearLayout = view.findViewById(R.id.linearLayout);

        myAdapter = new MyAdapter(this.getContext(),dataClassArrayList);
        listView.setAdapter(myAdapter);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(getContext(),EditInflow.class);
//                intent.putExtra("cat", dataClassArrayList.get(i).getCat());
//                intent.putExtra("line",dataClassArrayList.get(i).getLine());
//                intent.putExtra("amount",dataClassArrayList.get(i).getAmount());
//                intent.putExtra("id",dataClassArrayList.get(i).getInflowId());
//                intent.putExtra("frequency",dataClassArrayList.get(i).getFrequency());
//                startActivity(intent);
//                myDialog = new Dialog(getContext());
                ShowPopup(dataClassArrayList.get(i).getCat(),dataClassArrayList.get(i).getLine(),
                        dataClassArrayList.get(i).getAmount(),dataClassArrayList.get(i).getFrequency(),dataClassArrayList.get(i).getInflowId());
            }
        });
        sharedPreferences = getContext().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL,"");

//        handler.postDelayed(runnable = new Runnable() {
//            public void run() {
//                handler.postDelayed(runnable, delay);
////                getData();
                getInflow(email,url);
//            }
//        }, delay);

        floatingActionButton=view.findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(view.getContext(), CashflowBudget.class);
//                Intent intent = new Intent(view.getContext(), OutflowChart.class);
                Intent intent = new Intent(view.getContext(),AddOutflowActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        idBtnAddOutflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), CashflowBudget.class);
//                Intent intent = new Intent(view.getContext(), OutflowChart.class);
                view.getContext().startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        getInflow(email,url);
    }
    public void ShowPopup(String cat,String line,String am,String f,String id) {
        TextView txtclose;
        Button btnFollow,submitBtn,deleteBtn;
        EditText edCat,edLine,edAmount;
        Spinner freq;
        myDialog.setContentView(R.layout.outflowpopup);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        deleteBtn =(Button) myDialog.findViewById(R.id.idBtnDelete);
        submitBtn =(Button) myDialog.findViewById(R.id.idBtnSave);
        edCat = (EditText) myDialog.findViewById(R.id.idCat);
        edLine = (EditText) myDialog.findViewById(R.id.idLine);
        edAmount = (EditText) myDialog.findViewById(R.id.idAmount);
        freq = (Spinner) myDialog.findViewById(R.id.idFrequency);

        List<String> categories = new ArrayList<String>();
        categories.add("select Frequency");
        categories.add("Daily");
        categories.add("Weekly");
        categories.add("Monthly");
        categories.add("Quarterly");
        categories.add("Semi Annually");
        categories.add("Annually");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        freq.setAdapter(dataAdapter);
        txtclose.setText("x");
//        edCat.setText(cat);
//        edLine.setText(line);
//        edAmount.setText(am);

//        Intent intent = this.getIntent();
//        if (intent != null) {

        String s1 = am;
        String income_id = id;

        String no_o = s1.replaceAll(" ", "");
        String no_o1 = no_o.replaceAll("KES", "");
        String no_o2 = no_o1.replaceAll(",", "");

        edCat.setText(cat);
        edLine.setText(line);
        edAmount.setText(no_o2);
//            freq.setSelection(f);
//        }

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progressBar.setVisibility(View.VISIBLE);
//                income_id = "0";
                String income_frequency,category,income_line,income_amount;
                income_frequency = freq.getSelectedItem().toString();
                category = edCat.getText().toString();
                income_line = edLine.getText().toString();
                income_amount = edAmount.getText().toString();
//                income_id = "0";
                // validating the text fields if empty or not.

                if(category.contains("Tax")){
                    edCat.setError("You cannot edit tax");
                }else {

                    if (TextUtils.isEmpty(income_amount)) {
                        edAmount.setError("Please enter amount");
                    }
//                if (income_frequency.equals("select Frequency")) {
////                    freq.setError("Please enter Goal Period");
//                }
                    if (!TextUtils.isEmpty(income_frequency) && !TextUtils.isEmpty(income_amount) && !income_frequency.equals("select Frequency")) {
                        // calling method to add data to Firebase Firestore.
                        addDataToDatabase(id, category, income_line, income_amount, income_frequency);
//                        progressBar.setVisibility(View.VISIBLE);
                        getInflow(email, url);
//
                    } else {
//                    progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = edCat.getText().toString();
                if(category.contains("Tax")){
                    edCat.setError("You cannot edit tax");
                }else {
                    delete(id);

                    getInflow(email, url);
                }
            }});

//        btnFollow = (Button) myDialog.findViewById(R.id.btnfollow);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                delete();
                myDialog.dismiss();
                progressBar.setVisibility(View.VISIBLE);
                getInflow(email,url);
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
    private void getInflow(String email,String url) {

        RequestQueue queue = Volley.newRequestQueue(this.getContext());

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                progressBar.setVisibility(View.GONE);
                dataClassArrayList.clear();
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")){

                        int l=0;
                        l=jsonArray.length();

                        for(int i=0;i<l;i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            String cat = object.getString("category");
                            String line = object.getString("line");
                            String freq = object.getString("frequency");
                            String amount = object.getString("amount");
                            String id = object.getString("outflow_id");
                            dataClass = new DataClass(id,cat, line, amount, freq,"");
                            dataClassArrayList.add(dataClass);

                        }
                        if(l>0){
                            linearLayout.setVisibility(View.GONE);
                        }else{
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                    }
                    myAdapter.notifyDataSetChanged();

                }catch (Exception e){

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                progressBar.setVisibility(View.GONE);
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

                params.put("email", email);

                return params;
            }
        };

        queue.add(request);
    }

    private void delete(String id) {

        // url to post our data
        String url = "https://mwalimubiashara.com/app/delete_outflow.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(getContext());


        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                progressBar.setVisibility(View.GONE);
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("error");


                    if (!success) {
                        Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        myDialog.dismiss();
                        progressBar.setVisibility(View.VISIBLE);
//                        getInflow(email,url);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
//                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("email", email);
                params.put("id", id);

                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
    private void addDataToDatabase(String id,String cat,String line,String amount,String frequency) {


        // url to post our data
        String url = "https://mwalimubiashara.com/app/update_outflow.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                progressBar.setVisibility(View.GONE);
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("error");
//                    JSONArray jsonArray =jsonObject.getJSONArray("data");
                    if (!success) {
                        Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        popupSnackbar(jsonObject.getString("message"));

                    }
                    myDialog.dismiss();
//                    progressBar.setVisibility(View.VISIBLE);
//                    getInflow(email,url);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
//                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("email", email);
                params.put("category", cat);
                params.put("income_line", line);
                params.put("income_amount", amount);
                params.put("income_frequency", frequency);
                params.put("id", id);

                return params;
            }
        };

//        getInflow(email,url);
//        myDialog.dismiss();

        queue.add(request);
    }

}