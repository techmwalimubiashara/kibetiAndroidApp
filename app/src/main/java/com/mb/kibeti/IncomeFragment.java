package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;
import static com.mb.kibeti.LoginActivity.PACKAGE_TYPE;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class IncomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    TextView currentTV;
    View view;
    ListView listView, todaylistview, weeklistview, monthlistview, quarterlistview, alllistview;
    SpendingAdapter allAdapter, myAdapter, weekAdapter, monthlyAdapter, quarterAdapter;
    String url = "https://mwalimubiashara.com/app/get_actual_inflow.php";
    String email = "";
    SharedPreferences sharedPreferences;
    ArrayList<DataClass> dataClassArrayList = new ArrayList<>();
    ArrayList<DataClass> weekClassArrayList = new ArrayList<>();
    ArrayList<DataClass> monthClassArrayList = new ArrayList<>();
    ArrayList<DataClass> quarterClassArrayList = new ArrayList<>();
    ArrayList<DataClass> allClassArrayList = new ArrayList<>();
    DataClass dataClass, dailyDataClass, weekDataClass, monthDataClass, quarterDataClass;
    LinearLayout todayLayout, weekLayout, monthLayout, quarterLayout, allLayout, layoutNotFound;
    Spinner freqSpinner;
    TextView todayTv, weekTv, monthTv, quarterTv, allTv;
    String[] frequencylist = {};
    String ddate = "";
    DecimalFormat formatter;
    String All = "All", Daily = "Daily", Weekly = "Weekly", Monthly = "Monthly", Quarterly = "Quarterly", SemiAnnually = "Semi Annually", Annually = "Annually";
    String pack = "";
    LinearLayout layout_active, layout_expired;
    CardView unaccountedAmount;
    Button btnSubmitCourse;
    TextView mpesaamount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_income, container, false);
        currentTV = view.findViewById(R.id.timeTV);

        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("###,###,###,###");

        sharedPreferences = getContext().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL, "");

        unaccountedAmount = view.findViewById(R.id.unaccountedAmount);
        mpesaamount = view.findViewById(R.id.statusTv);
        btnSubmitCourse = view.findViewById(R.id.idBtnSubmitCourse);

        layout_active = view.findViewById(R.id.layout_active);
        layout_expired = view.findViewById(R.id.layout_expired);
        listView = view.findViewById(R.id.listview);
        alllistview = view.findViewById(R.id.alllistview);
        todaylistview = view.findViewById(R.id.todaylistview);
        weeklistview = view.findViewById(R.id.weeklistview);
        monthlistview = view.findViewById(R.id.monthlistview);
        quarterlistview = view.findViewById(R.id.quarterlistview);

        allLayout = view.findViewById(R.id.allhidden_view);
        todayLayout = view.findViewById(R.id.hidden_view);
        weekLayout = view.findViewById(R.id.hidden_view1);
        monthLayout = view.findViewById(R.id.hidden_view2);
        quarterLayout = view.findViewById(R.id.hidden_view3);
        layoutNotFound = view.findViewById(R.id.layoutNotFound);

        todayTv = view.findViewById(R.id.todayTv);
        weekTv = view.findViewById(R.id.weekTv);
        monthTv = view.findViewById(R.id.monthTv);
        quarterTv = view.findViewById(R.id.quarterTv);

        Button upgradeBtn = view.findViewById(R.id.btnMakePayment);

        upgradeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upgrade();
            }
        });


        frequencylist = new String[]{All, Daily, Weekly, Monthly, Quarterly, SemiAnnually, Annually};


        freqSpinner = (Spinner) view.findViewById(R.id.idFreqSpinner);

        pack = sharedPreferences.getString(PACKAGE_TYPE, "");

        Log.e("TAG", "PACKAGE TYPE is " + pack);
        if (pack.equals("basic")) {
            layout_active.setVisibility(View.GONE);
            layout_expired.setVisibility(View.VISIBLE);

        } else {
            layout_expired.setVisibility(View.GONE);
            layout_active.setVisibility(View.VISIBLE);
            getInflow();
        }

        // on below line we are initializing adapter for our spinner
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, frequencylist);

        // on below line we are setting drop down view resource for our adapter.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // on below line we are setting adapter for spinner.
        freqSpinner.setAdapter(adapter);

        // on below line we are adding click listener for our spinner
        freqSpinner.setOnItemSelectedListener(this);

        freqSpinner.setSelection(0);


        SimpleDateFormat sdf = new SimpleDateFormat(" dd-MM-yyyy ");
        ddate = sdf.format(new Date());
        currentTV.setText(ddate);

        getWorth();


        Log.e("TAG", "Package type " + pack);


        currentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                ddate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                currentTV.setText(ddate);
                                getInflow();

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });

        unaccountedAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MpesaUpdates.class);
                intent.putExtra("cashflow", "inflow");
                startActivity(intent);
                getWorth();
//                Toast.makeText(getContext(), "Checking unaccounted amount", Toast.LENGTH_SHORT).show();
            }
        });


        myAdapter = new SpendingAdapter(this.getContext(), dataClassArrayList);
        allAdapter = new SpendingAdapter(this.getContext(), allClassArrayList);
        weekAdapter = new SpendingAdapter(this.getContext(), weekClassArrayList);
        monthlyAdapter = new SpendingAdapter(this.getContext(), monthClassArrayList);
        quarterAdapter = new SpendingAdapter(this.getContext(), quarterClassArrayList);

        alllistview.setAdapter(allAdapter);
        alllistview.setClickable(true);
        alllistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), AddActualActivity.class);
                intent.putExtra("cat", allClassArrayList.get(i).getCat());
                intent.putExtra("line", allClassArrayList.get(i).getLine());
                intent.putExtra("amount", allClassArrayList.get(i).getAmount());
                intent.putExtra("id", allClassArrayList.get(i).getInflowId());
                intent.putExtra("frequency", allClassArrayList.get(i).getFrequency());
                startActivity(intent);
            }
        });
        todaylistview.setAdapter(myAdapter);
        todaylistview.setClickable(true);
        todaylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), AddActualActivity.class);
                intent.putExtra("cat", dataClassArrayList.get(i).getCat());
                intent.putExtra("line", dataClassArrayList.get(i).getLine());
                intent.putExtra("amount", dataClassArrayList.get(i).getAmount());
                intent.putExtra("id", dataClassArrayList.get(i).getInflowId());
                intent.putExtra("frequency", "Daily");
                startActivity(intent);
            }
        });
        weeklistview.setAdapter(weekAdapter);
        weeklistview.setClickable(true);
        weeklistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), AddActualActivity.class);
                intent.putExtra("cat", weekClassArrayList.get(i).getCat());
                intent.putExtra("line", weekClassArrayList.get(i).getLine());
                intent.putExtra("amount", weekClassArrayList.get(i).getAmount());
                intent.putExtra("id", weekClassArrayList.get(i).getInflowId());
                intent.putExtra("frequency", weekClassArrayList.get(i).getFrequency());
                intent.putExtra("frequency", "Weekly");
                startActivity(intent);
            }
        });
        monthlistview.setAdapter(monthlyAdapter);
        monthlistview.setClickable(true);
        monthlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), AddActualActivity.class);
                intent.putExtra("cat", monthClassArrayList.get(i).getCat());
                intent.putExtra("line", monthClassArrayList.get(i).getLine());
                intent.putExtra("amount", monthClassArrayList.get(i).getAmount());
                intent.putExtra("id", monthClassArrayList.get(i).getInflowId());
                intent.putExtra("frequency", monthClassArrayList.get(i).getFrequency());
                intent.putExtra("frequency", "Monthly");
                startActivity(intent);
            }
        });
        quarterlistview.setAdapter(quarterAdapter);
        quarterlistview.setClickable(true);
        quarterlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), AddActualActivity.class);
                intent.putExtra("cat", quarterClassArrayList.get(i).getCat());
                intent.putExtra("line", quarterClassArrayList.get(i).getLine());
                intent.putExtra("amount", quarterClassArrayList.get(i).getAmount());
                intent.putExtra("id", quarterClassArrayList.get(i).getInflowId());
                intent.putExtra("frequency", quarterClassArrayList.get(i).getFrequency());
                intent.putExtra("frequency", "Quarterly");
                startActivity(intent);
            }
        });

        btnSubmitCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), AddInflowActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    public void addInflow(View view){
        Intent intent = new Intent(getContext(), AddInflowActivity.class);
        startActivity(intent);
    }

//
//    public void addOutflow(View view) {
//        Intent intent = new Intent(OutflowChart.this, AddOutflowActivity.class);
//        startActivity(intent);
//        finish();
//    }
    private void getWorth() {

        RequestQueue queue = Volley.newRequestQueue(this.getContext());

        StringRequest request = new StringRequest(Request.Method.POST, "https://mwalimubiashara.com/app/get_mpesa_data.php", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
//                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            int worth = object.getInt("money");
//                            float mpesa_amount =
//                            monthlyAmount.setText(object.getString("moneyformat"));
                            if (worth != 0) {
//                                monthlyAmount.setTextColor(Color.parseColor("#0F9D58"));
                                mpesaamount.setText("KES. " + worth);
                                unaccountedAmount.setVisibility(View.VISIBLE);
                            } else {
//                                monthlyAmount.setTextColor(Color.parseColor("#FF0000"));
                                unaccountedAmount.setVisibility(View.GONE);

                            }
                        }
                    }

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
                params.put("cashflow", "inflow");

                return params;
            }
        };

        queue.add(request);
    }

    private void getInflow() {

        RequestQueue queue = Volley.newRequestQueue(this.getContext());

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);

                dataClassArrayList.clear();
                allClassArrayList.clear();
                weekClassArrayList.clear();
                monthClassArrayList.clear();
                quarterClassArrayList.clear();
                int d1 = 0, w1 = 0, m1 = 0, q1 = 0, a1 = 0;
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
                            String actual_amount = object.getString("income_actual_amount");
                            String id = object.getString("income_id");

                            String no_o = amount.replaceAll(" ", "");
                            String no_o1 = no_o.replaceAll("KES", "");
                            String no_o2 = no_o1.replaceAll(",", "");

                            no_o = actual_amount.replaceAll(" ", "");
                            no_o1 = no_o.replaceAll("KES", "");
                            no_o = no_o1.replaceAll(",", "");
                            int number = Integer.parseInt(no_o) - Integer.parseInt(no_o2);

                            String status = "Updated";


                            if (freq.equals("Daily")) {
                                if (no_o.equals("0")) {
                                    d1 = d1 + 1;
                                    status = "Not updated";
                                }
//                                dataClass = new DataClass(id, line, amount, actual_amount, formatter.format(number), status);
                                dataClass = new DataClass(id, line, amount, actual_amount, freq, cat);
                                dataClassArrayList.add(dataClass);
                            }
                            if (freq.equals("Weekly")) {
                                if (no_o.equals("0")) {
                                    w1 = w1 + 1;
                                    status = "Not updated";
                                }
                                dataClass = new DataClass(id, line, amount, actual_amount, freq, cat);
                                weekClassArrayList.add(dataClass);
                            }
                            if (freq.equals("Monthly")) {
                                if (no_o.equals("0")) {
                                    status = "Not updated";
                                    m1 = m1 + 1;
                                }
                                dataClass = new DataClass(id, line, amount, actual_amount, freq, cat);
                                monthClassArrayList.add(dataClass);
                            }
                            if (freq.equals("Quarterly")) {
                                if (Integer.parseInt(no_o) == 0) {
                                    status = "Not updated";
                                    q1 = q1 + 1;
                                }
                                dataClass = new DataClass(id, line, amount, actual_amount, freq, cat);
                                quarterClassArrayList.add(dataClass);
                            }
                            dataClass = new DataClass(id, line, amount, actual_amount, freq, cat);
                            allClassArrayList.add(dataClass);

                        }
                        if (a1 > 0) {
                            All = "All(" + a1 + ")";
                        } else {
                            All = "All";
                        }
                        if (d1 > 0) {
                            Daily = "Daily(" + d1 + ")";

                        } else {
                            Daily = "Daily";
                        }
                        if (w1 > 0) {
                            Weekly = "Weekly(" + w1 + ")";
                        } else {
                            Weekly = "Weekly";
                        }
                        if (m1 > 0) {
                            Monthly = "Monthly(" + m1 + ")";
                        } else {
                            Monthly = "Monthly";
                        }
                        if (q1 > 0) {
                            Quarterly = "Quarterly(" + q1 + ")";
                        } else {
                            Quarterly = "Quarterly";
                        }
                        frequencylist[0] = All;
                        frequencylist[1] = Daily;
                        frequencylist[2] = Weekly;
                        frequencylist[3] = Monthly;
                        frequencylist[4] = Quarterly;
//                        d1=w1=m1=q1=0;
                    }
                    myAdapter.notifyDataSetChanged();
                    weekAdapter.notifyDataSetChanged();
                    monthlyAdapter.notifyDataSetChanged();
                    quarterAdapter.notifyDataSetChanged();
                    allAdapter.notifyDataSetChanged();

                } catch (Exception e) {

                }
                if (allClassArrayList.isEmpty()) {
                    layoutNotFound.setVisibility(View.VISIBLE);
                    alllistview.setVisibility(View.GONE);
                } else {
                    layoutNotFound.setVisibility(View.GONE);
                    alllistview.setVisibility(View.VISIBLE);
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
                params.put("date", ddate);

                return params;
            }
        };

        queue.add(request);
    }

    private void upgrade() {
        Intent intent = new Intent(getContext(), Makepayment.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
//        progressBar.setVisibility(View.VISIBLE);
        pack = sharedPreferences.getString(PACKAGE_TYPE, "");
        if (pack.equals("basic")) {
            layout_active.setVisibility(View.GONE);
            layout_expired.setVisibility(View.VISIBLE);

        } else {
            layout_expired.setVisibility(View.GONE);
            layout_active.setVisibility(View.VISIBLE);

            getInflow();

        }

        getWorth();
        Log.e("TAG", "Package type " + pack);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            TransitionManager.beginDelayedTransition(allLayout, new AutoTransition());
            if (allClassArrayList.isEmpty()) {
                layoutNotFound.setVisibility(View.VISIBLE);
                alllistview.setVisibility(View.GONE);
            } else {
                layoutNotFound.setVisibility(View.GONE);
                alllistview.setVisibility(View.VISIBLE);
            }
            todaylistview.setVisibility(View.GONE);
            weeklistview.setVisibility(View.GONE);
            monthlistview.setVisibility(View.GONE);
            quarterlistview.setVisibility(View.GONE);
            todayTv.setTextColor(getResources().getColor(R.color.primary_green));
            monthTv.setTextColor(getResources().getColor(R.color.grey));
            weekTv.setTextColor(getResources().getColor(R.color.grey));
            quarterTv.setTextColor(getResources().getColor(R.color.grey));

        } else if (position == 1) {
            TransitionManager.beginDelayedTransition(todayLayout, new AutoTransition());
            if (dataClassArrayList.isEmpty()) {
                layoutNotFound.setVisibility(View.VISIBLE);
                todaylistview.setVisibility(View.GONE);
            } else {
                layoutNotFound.setVisibility(View.GONE);
                todaylistview.setVisibility(View.VISIBLE);
            }
            alllistview.setVisibility(View.GONE);
            weeklistview.setVisibility(View.GONE);
            monthlistview.setVisibility(View.GONE);
            quarterlistview.setVisibility(View.GONE);
            todayTv.setTextColor(getResources().getColor(R.color.primary_green));
            monthTv.setTextColor(getResources().getColor(R.color.grey));
            weekTv.setTextColor(getResources().getColor(R.color.grey));
            quarterTv.setTextColor(getResources().getColor(R.color.grey));

        } else if (position == 2) {
            TransitionManager.beginDelayedTransition(weekLayout, new AutoTransition());
            todaylistview.setVisibility(View.GONE);
            if (weekClassArrayList.isEmpty()) {
                layoutNotFound.setVisibility(View.VISIBLE);
                weeklistview.setVisibility(View.GONE);
            } else {
                layoutNotFound.setVisibility(View.GONE);
                weeklistview.setVisibility(View.VISIBLE);
            }
            alllistview.setVisibility(View.GONE);
            monthlistview.setVisibility(View.GONE);
            quarterlistview.setVisibility(View.GONE);
            todayTv.setTextColor(getResources().getColor(R.color.grey));
            monthTv.setTextColor(getResources().getColor(R.color.grey));
            weekTv.setTextColor(getResources().getColor(R.color.primary_green));
            quarterTv.setTextColor(getResources().getColor(R.color.grey));

        } else if (position == 3) {
            TransitionManager.beginDelayedTransition(monthLayout, new AutoTransition());
            if (monthClassArrayList.isEmpty()) {
                layoutNotFound.setVisibility(View.VISIBLE);
                monthlistview.setVisibility(View.GONE);
            } else {
                layoutNotFound.setVisibility(View.GONE);
                monthlistview.setVisibility(View.VISIBLE);
            }
            todaylistview.setVisibility(View.GONE);
            weeklistview.setVisibility(View.GONE);
            alllistview.setVisibility(View.GONE);
            quarterlistview.setVisibility(View.GONE);
            todayTv.setTextColor(getResources().getColor(R.color.grey));
            monthTv.setTextColor(getResources().getColor(R.color.primary_green));
            weekTv.setTextColor(getResources().getColor(R.color.grey));
            quarterTv.setTextColor(getResources().getColor(R.color.grey));

        } else if (position == 4) {
            TransitionManager.beginDelayedTransition(quarterLayout, new AutoTransition());
            if (quarterClassArrayList.isEmpty()) {
                layoutNotFound.setVisibility(View.VISIBLE);
                quarterlistview.setVisibility(View.GONE);
            } else {
                layoutNotFound.setVisibility(View.GONE);
                quarterlistview.setVisibility(View.VISIBLE);
            }
            todaylistview.setVisibility(View.GONE);
            weeklistview.setVisibility(View.GONE);
            alllistview.setVisibility(View.GONE);
            monthlistview.setVisibility(View.GONE);
            todayTv.setTextColor(getResources().getColor(R.color.grey));
            monthTv.setTextColor(getResources().getColor(R.color.grey));
            weekTv.setTextColor(getResources().getColor(R.color.grey));
            quarterTv.setTextColor(getResources().getColor(R.color.primary_green));

        } else {
            Toast.makeText(getContext(), "Work in progress!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}