package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.mb.kibeti.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class InflowFragment extends Fragment {

    View view;
    ProgressBar progressBar;
    public ListView listView;
    public MyAdapter myAdapter;
    DataClass dataClass;
    String url = "https://mwalimubiashara.com/app/get_inflow.php";
    String email = "";
    SharedPreferences sharedPreferences;
    LinearLayout linearLayout;
    ActivityMainBinding binding;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 3000;
    Dialog myDialog;
    DecimalFormat formatter;

    public static ArrayList<DataClass> dataClassArrayList = new ArrayList<>();
    FloatingActionButton floatingActionButton;
    Button idBtnAddInflow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_inflow, container, false);
        listView = view.findViewById(R.id.listview);
        idBtnAddInflow = view.findViewById(R.id.idBtnAddInflow);
        progressBar = view.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        linearLayout = view.findViewById(R.id.linearLayout);

        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("###,###,###,###");

        myAdapter = new MyAdapter(this.getContext(), dataClassArrayList);

        getActivity().setTitle("My Budget - Inflow");

        myDialog = new Dialog(getContext());

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
                ShowPopup(dataClassArrayList.get(i).getCat(), dataClassArrayList.get(i).getLine(),
                        dataClassArrayList.get(i).getAmount(), dataClassArrayList.get(i).getFrequency(), dataClassArrayList.get(i).getInflowId());
            }
        });
        sharedPreferences = getContext().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL, "");

//          handler.postDelayed(runnable = new Runnable() {
//             public void run() {
//                  handler.postDelayed(runnable, delay);
//                  getData();
        getInflow(email, url);
//             }
//          }, delay);


        floatingActionButton = view.findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddInflowActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        idBtnAddInflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddInflowActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        return view;
    }

    public void ShowPopup(String cat, String line, String am, String f, String id) {
        TextView txtclose;
        Button btnFollow, submitBtn, deleteBtn;
        EditText edCat, edLine, edAmount;
        Spinner freq;
        myDialog.setContentView(R.layout.custompopup);
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        deleteBtn = (Button) myDialog.findViewById(R.id.idBtnDelete);
        submitBtn = (Button) myDialog.findViewById(R.id.idBtnSave);
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
//            edAmount.setText(no_o2);

        Long longval = Long.parseLong(no_o2);

        String formattedString = formatter.format(longval);

        edAmount.setText(formattedString);
//            freq.setSelection(f);
//        }

        edAmount.addTextChangedListener(new TextWatcher() {
            //            int budgetAmount1=0;
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                int number = Integer.parseInt(s.toString());
                edAmount.removeTextChangedListener(this);

                String amountStr = edAmount.getText().toString();
                if (!TextUtils.isEmpty(amountStr)) {

                    String originalString = amountStr;

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);


                    String formattedString = formatter.format(longval);

                    edAmount.setText(formattedString);
                    edAmount.setSelection(edAmount.getText().length());

                    int number = Integer.parseInt(originalString);

//                    valencyAmount =  number - budgetAmount1;
                } else {
//                    valencyAmount = 0 - budgetAmount1;
                }
//                valency.setText(formatter.format(valencyAmount));

                edAmount.addTextChangedListener(this);
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progressBar.setVisibility(View.VISIBLE);
//                income_id = "0";
                String income_frequency, category, income_line, income_amount;
                income_frequency = freq.getSelectedItem().toString();
                category = edCat.getText().toString();
                income_line = edLine.getText().toString();
                income_amount = edAmount.getText().toString().replaceAll(",", "");
//                income_id = "0";
                // validating the text fields if empty or not.

                if (TextUtils.isEmpty(income_amount)) {
                    edAmount.setError("Please enter amount");
                }
//                if (income_frequency.equals("select Frequency")) {
////                    freq.setError("Please enter Goal Period");
//                }
                if (!TextUtils.isEmpty(income_frequency) && !TextUtils.isEmpty(income_amount) && !income_frequency.equals("select Frequency")) {
                    // calling method to add data to Firebase Firestore.
                    addDataToDatabase(id, category, income_line, income_amount, income_frequency);
//                    progressBar.setVisibility(View.VISIBLE);
                    getInflow(email, url);
                } else {
//                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(id);
            }
        });

//        btnFollow = (Button) myDialog.findViewById(R.id.btnfollow);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                delete();
                myDialog.dismiss();
                progressBar.setVisibility(View.VISIBLE);
                getInflow(email, url);
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        getInflow(email, url);
    }

    private void getInflow(String email, String url) {

        RequestQueue queue = Volley.newRequestQueue(this.getContext());

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                progressBar.setVisibility(View.GONE);
                dataClassArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {

                        int l = 0;
                        l = jsonArray.length();

                        for (int i = 0; i < l; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String cat = object.getString("income_cat");
                            String line = object.getString("income_line");
                            String freq = object.getString("income_freq");
                            String amount = object.getString("income_amount");
                            String id = object.getString("income_id");

                            dataClass = new DataClass(id, cat, line, amount, freq, "");
                            dataClassArrayList.add(dataClass);


                        }
                        if (l > 0) {
                            linearLayout.setVisibility(View.GONE);
                        } else {
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                    }
                    myAdapter.notifyDataSetChanged();

                } catch (Exception e) {

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
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
        String url = "https://mwalimubiashara.com/app/delete_inflow.php";

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

//    private void popupSnackbar(String msg) {
//        Snackbar snackbar =
//                Snackbar.make(view.findViewById(android.R.id.content),
//                        msg,
//                        Snackbar.LENGTH_INDEFINITE);
//        snackbar.setAction("OK", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        snackbar.setActionTextColor(
//                getResources().getColor(R.color.primary_green));
//        snackbar.show();
//    }

    private void addDataToDatabase(String id, String cat, String line, String amount, String frequency) {

        // url to post our data
        String url = "https://mwalimubiashara.com/app/update_inflow.php";

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
                        myDialog.dismiss();

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
                params.put("category", cat);
                params.put("income_line", line);
                params.put("income_amount", amount);
                params.put("income_frequency", frequency);
                params.put("id", id);

                return params;
            }
        };

        getInflow(email, url);
//        myDialog.dismiss();

        queue.add(request);

    }

}