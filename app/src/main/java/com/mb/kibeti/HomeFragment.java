package com.mb.kibeti;

//import static com.example.navigationdrawer.LoginActivity.EMAIL;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.GOALSET;
import static com.mb.kibeti.LoginActivity.PACKAGE_TYPE;
import static com.mb.kibeti.LoginActivity.USERNAME;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.github.mikephil.charting.charts.PieChart;
//import com.github.mikephil.charting.components.Legend;
//import com.github.mikephil.charting.data.PieData;
//import com.github.mikephil.charting.data.PieDataSet;
//import com.github.mikephil.charting.data.PieEntry;
//import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mb.kibeti.coupon.ui.InviteFriendActivity;
import com.mb.kibeti.eod_dashboard.EODFeelingActivity;
import com.mb.kibeti.invest_guide.InputScreenActivity;
import com.mb.kibeti.invest_guide.InvestmentModelActivity;
//import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    //    CardView cardViewInvestmentReturn;
//    CardView cardViewLumpsum;
//    CardView cardViewGoal;
    BottomSheetDialog bottomSheetDialog;
    DataTypeConvertion dataTypeConvertion;
    CardView cardhome;
    ImageView yourGoal, lumpsumIV, investmentReturnIV, spendingIV, budgetIV, trackerIV, shoppingIV, investImg, businessIm;
    View view;
    String email = "";

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 5000;
    LinearLayout layoutTransaction;
    List<String> categories = new ArrayList<String>();
    TextView username;
    TextView monthlyAmount;
    TextView tvTransaction;
    PipeLines util = new PipeLines();
    String url = util.GET_WORTH_URL;//"https://mwalimubiashara.com/app/get_worth.php";
    //    SliderView sliderView;
    ArrayList<SliderData> sliderDataArrayList;
    LinearLayout stateOther, layoutothers;
    TextView tvOthers;
    TextView tvAllocateAll;
    ImageView blur, eye_close, eye_open;
    SharedPreferences sharedPreferences;
    DataClass dataClass;
    //    PieChart pieChartGoals;
    private static ArrayList<DataClass> dataClassArrayList = new ArrayList<>();
    private ListView listView;
    private MpesaCheckingAdapter myAdapter;
    ProgressDialog pDialog;
    ArrayList barEnteriesArrayList;
    UpdateMpesaPop showPop;
    TextView tvInvestAdjust, tvBudgetAdjust, tvGoalAdjust;

    CardView tvGoalNothing;
    boolean isGoalSet = false;
    String package_type = "";
    CardView myGoalCardView, myBudgetCardView, myInvestmentCardView, myBusinessCardView;
    Button tvGoalSet;
    BasicProfile basicProfile;
    Button tvInvestStatus, btnEod;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataTypeConvertion = new DataTypeConvertion();
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.home_layout, container, false);
        sharedPreferences = getContext().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL, "");
        package_type = sharedPreferences.getString(PACKAGE_TYPE, "basic");
        isGoalSet = sharedPreferences.getBoolean(GOALSET, false);

        basicProfile = new BasicProfile(getContext(), email);

        showPop = new UpdateMpesaPop(getContext(), email);
        boolean isConnected = ConnectivityHelper.isConnectedToNetwork(getContext());

        barEnteriesArrayList = new ArrayList<>();

        if (isConnected) {
//            Toast.makeText(this, "Connected to the Internet", Toast.LENGTH_SHORT).show();
            loadDetails();
//            getPieChartGoalsData();
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


        getActivity().setTitle("Kibeti");

        yourGoal = view.findViewById(R.id.imGoalCard);
        tvGoalAdjust = view.findViewById(R.id.tvGoalAdjust);
        blur = view.findViewById(R.id.blur);
        eye_close = view.findViewById(R.id.eye_close);
        eye_open = view.findViewById(R.id.eye_open);
        lumpsumIV = view.findViewById(R.id.view1);
        investmentReturnIV = view.findViewById(R.id.view2);
        budgetIV = view.findViewById(R.id.imBudgetCard);
        trackerIV = view.findViewById(R.id.imTracker);
        shoppingIV = view.findViewById(R.id.imShopping);
        spendingIV = view.findViewById(R.id.view3);
        listView = view.findViewById(R.id.listview);
        tvTransaction = view.findViewById(R.id.tvTransaction);
        tvOthers = view.findViewById(R.id.tvOthers);
        tvAllocateAll = view.findViewById(R.id.tvAllocateAll);
        layoutothers = view.findViewById(R.id.layoutothers);
        layoutTransaction = view.findViewById(R.id.layoutTransaction);
        investImg = view.findViewById(R.id.investImgCard);
        businessIm = view.findViewById(R.id.imBusinessCard);
        myGoalCardView = view.findViewById(R.id.myGoalCardView);
        myBudgetCardView = view.findViewById(R.id.myBudgetCardView);
        myInvestmentCardView = view.findViewById(R.id.myInvestmentCardView);
        myBusinessCardView = view.findViewById(R.id.myBusinessCardView);
        tvInvestStatus = view.findViewById(R.id.tvInvestStatus);
        tvGoalSet = view.findViewById(R.id.tvGoalSet);
        btnEod = view.findViewById(R.id.btnEod);

        tvGoalNothing = view.findViewById(R.id.tvGoalNothing);

        tvBudgetAdjust = view.findViewById(R.id.tvBudgetAdjust);

//        pieChartGoals = view.findViewById(R.id.pieChartGoals);

        tvInvestAdjust = view.findViewById(R.id.tvInvestAdjust);

        sliderDataArrayList = new ArrayList<>();


        // initializing the slider view.
//        sliderView = view.findViewById(R.id.slider);


        username = view.findViewById(R.id.usernameTV);

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        String greeting = "Hi";
        if (hour >= 6 && hour < 12) {
            greeting = "Good morning";
        } else if (hour >= 12 && hour < 17) {
            greeting = "Good afternoon";
        } else if (hour >= 17 && hour < 21) {
            greeting = "Good evening";
        } else if (hour >= 21 && hour < 24) {
            greeting = "Good night";
        }

        if (package_type.equals("basic")) {
            tvInvestStatus.setText("Start investment today");
        }
        username.setText(greeting + " " + sharedPreferences.getString(USERNAME, ""));

        myAdapter = new MpesaCheckingAdapter(getContext(), dataClassArrayList);

        myAdapter = new MpesaCheckingAdapter(getContext(), dataClassArrayList);

        listView.setAdapter(myAdapter);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                showPop.showPopup(dataClassArrayList.get(i).getCat(), dataClassArrayList.get(i).getLine(),
                        dataClassArrayList.get(i).getAmount(), dataClassArrayList.get(i).getFrequency(),
                        dataClassArrayList.get(i).getInflowId());

                dataClassArrayList.remove(i);
                myAdapter.notifyDataSetChanged();

            }
        });

//        if(listView.getCount()>2){
//            tvAllocateAll.setVisibility(View.VISIBLE);
//        }else{
//            tvAllocateAll.setVisibility(View.GONE);
//        }

        monthlyAmount = view.findViewById(R.id.monthlyAmount);
        String pack = sharedPreferences.getString(PACKAGE_TYPE, "");

        Log.e("TAG", "Package type " + pack);

        tvOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOther();
            }
        });
        btnEod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewRecentTransactions();
            }
        });
        tvAllocateAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(getContext());
                pDialog.setMessage("Allocating outflow to others ...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
                allocateAll();
            }
        });

        monthlyAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), CashflowPdfActivity.class);

//                Intent intent = new Intent(view.getContext(), Message.class);
                view.getContext().startActivity(intent);
            }
        });

        eye_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthlyAmount.setVisibility(View.GONE);
                eye_close.setVisibility(View.GONE);
                eye_open.setVisibility(View.VISIBLE);
                blur.setVisibility(View.VISIBLE);
            }
        });
        eye_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eye_open.setVisibility(View.GONE);
                blur.setVisibility(View.GONE);
                monthlyAmount.setVisibility(View.VISIBLE);
                eye_close.setVisibility(View.VISIBLE);
            }
        });

//        yourGoal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Intent intent = new Intent(view.getContext(), YourGoalActivity.class);
//                Intent intent = new Intent(view.getContext(), SettingGoals.class);
////                Intent intent = new Intent(view.getContext(), GoalActivity.class);
//                view.getContext().startActivity(intent);
//
//            }
//        });
        tvGoalNothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), YourGoalActivity.class);
                Intent intent = new Intent(view.getContext(), SettingGoals.class);
//                Intent intent = new Intent(view.getContext(), GoalActivity.class);
                view.getContext().startActivity(intent);
            }
        });


//        pieChartGoals.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), MyGoalsActivity.class);
//                view.getContext().startActivity(intent);
//            }
//        });
        tvGoalSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MyGoalsActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        lumpsumIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(view.getContext(), LumpsumActivity.class);
                Intent intent = new Intent(view.getContext(), CelebrationPopActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        myInvestmentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(view.getContext(), InvestmentProfile.class);
//                Intent intent = new Intent(view.getContext(), InvestmentCalculator.class);

//                Intent intent;
//
//                if(basicProfile.getNetworth()==0){
//                    intent = new Intent(view.getContext(), CashflowBudget.class);
                createBottomSheetDialog();
//                }else {
//                    intent = new Intent(view.getContext(), MySpendingBudget.class);
//                    view.getContext().startActivity(intent);
//                }


//                if(package_type.equals("Lifetime")||package_type.equals("Subscription")){
//                    Intent intent = new Intent(view.getContext(), InvestmentRecommendationCalculator.class);
//                    view.getContext().startActivity(intent);
//                }

//                view.getContext().startActivity(intent);
            }
        });
        tvInvestStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(view.getContext(), InvestmentProfile.class);
                Intent intent = new Intent(view.getContext(), InvestmentCalculator.class);


                if (package_type.equals("Lifetime") || package_type.equals("Subscription")) {
                    intent = new Intent(view.getContext(), InvestmentRecommendationCalculator.class);
                }

                view.getContext().startActivity(intent);
            }
        });
        myBusinessCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(view.getContext(), BPMainActivity.class);
//                Intent intent = new Intent(view.getContext(), InviteFriendActivity.class);
//                view.getContext().startActivity(intent);
                Toast.makeText(getContext(), "Coming Soon...", Toast.LENGTH_SHORT).show();
            }
        });
        investmentReturnIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), InvestmentReturnActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        spendingIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TablayoutWithIconsActivity.class);
                view.getContext().startActivity(intent);
            }
        });

//        sliderView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(myBudgetCardViewgetContext(), "Clicked item is ", Toast.LENGTH_SHORT).show();
//            }
//        });

        myBudgetCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CashFragment()).commit();
//                Intent intent = new Intent(view.getContext(), CashflowBudget.class);


                Intent intent;

                if (basicProfile.getNetworth() == 0) {

                    intent = new Intent(view.getContext(), CashflowBudget.class);

                } else {
//                    intent = new Intent(view.getContext(), MySpendingBudget.class);
                    intent = new Intent(view.getContext(), CashActivtity.class);
                }


                view.getContext().startActivity(intent);

            }
        });

        shoppingIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CashFragment()).commit();
//                Intent intent = new Intent(view.getContext(), ShoppingAssistance.class);
//                Intent intent = new Intent(view.getContext(), ShoppingBudget.class);
//                view.getContext().startActivity(intent);
                Toast.makeText(getContext(), "Coming Soon", Toast.LENGTH_SHORT).show();

            }
        });

        trackerIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SpendingFragment()).commit();

            }
        });

        myGoalCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), MyGoalsActivity.class);

                view.getContext().startActivity(intent);
            }
        });

//        getMpesaStatus();

        return view;
    }

    private void getPieChartGoalsData() {

//        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest request = new StringRequest(Request.Method.POST, "https://mwalimubiashara.com/app/get_goals.php", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS goals " + response);

                barEnteriesArrayList.clear();

                try {

                    JSONObject jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("inflow_success");
//                    String success_out = jsonObject.getString("outflow_success");
//                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    JSONArray jsonArrayout = jsonObject.getJSONArray("outflow");

                    int l = 0;
                    l = jsonArrayout.length();


                    Log.e("TAG", "outflow size " + l);
                    ArrayList<DataModel> dataModels = new ArrayList<>();

                    for (int k = 0; k < l; k++) {
                        JSONObject objectOut = jsonArrayout.getJSONObject(k);

//                        visitors.add(new PieEntry(entry4,"2019"));
//                        visitors.add(new PieEntry(entry6,"2020"));

                        Log.e("TAG", "Cat " + objectOut.getString("cat_2") + " 1 " + objectOut.getString("cat_3"));


                        if (dataTypeConvertion.convertToInt(objectOut.getString("amount_2")) != 0) {

                            dataModels.add(new DataModel(objectOut.getString("cat_2"), dataTypeConvertion.convertToInt(objectOut.getString("amount_2"))));
                        }
                        if (dataTypeConvertion.convertToInt(objectOut.getString("amount_3")) != 0) {
                            dataModels.add(new DataModel(objectOut.getString("cat_3"), dataTypeConvertion.convertToInt(objectOut.getString("amount_3"))));
                        }

                    }
//                    myAdapter.notifyDataSetChanged();

                    setupPieChartGoals(dataModels);
                    Log.e("TAG", "Data entries length " + barEnteriesArrayList.size());

                } catch (Exception e) {

                    e.printStackTrace();
                }
                pDialog.dismiss();

//                PieDataSet pieDataSet = new PieDataSet(visitors, "Outflows");
//                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//                pieDataSet.setValueTextColor(Color.BLACK);
//                pieDataSet.setSliceSpace(4f);
//                pieDataSet.setValueTextSize(17f);
//
//                Legend legend = pieChart.getLegend();
////                    legend.setForm(Legend.LegendForm.CIRCLE);
////                    legend.setWordWrapEnabled(true);
////                    legend.setTextSize(11);
////                    legend.setFormSize(10);
////                    legend.setFormToTextSpace(2);
//                legend.setEnabled(false);


//                PieData pieData = new PieData(pieDataSet);
//                pieChart.setData(pieData);
//                pieChart.getDescription().setEnabled(false);
//                pieChart.setHoleRadius(60);
////                pieChart.setNoDataText("Tap to view the graph");
//                pieChart.setNoDataText("Please try again later.");
//                pieChart.setCenterText("Outflows");
//                pieChart.setCenterTextColor(Color.BLACK);
//                pieChart.notifyDataSetChanged();
//                pieChart.animate();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                error.printStackTrace();
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

                params.put("email", email);

                return params;
            }
        };

        queue.add(request);
    }

    private void setupPieChartGoals(List<DataModel> dataModels) {
//        ArrayList<PieEntry> entries = new ArrayList<>();
//        Log.e("TAG", "barEnteriesArrayList size is" + barEnteriesArrayList.size());
//
//
//        if (!dataModels.isEmpty()) {
////            pieChartGoals.setVisibility(View.VISIBLE);
//            tvGoalSet.setVisibility(View.VISIBLE);
//            tvGoalNothing.setVisibility(View.GONE);
////            tvGoalAdjust.setVisibility(View.VISIBLE);
//            for (DataModel dataModel : dataModels) {
//                entries.add(new PieEntry(dataModel.getValue(), dataModel.getLabel()));
//            }
//
//            PieDataSet dataSet = new PieDataSet(entries, "Goals");
//            dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
//
//            Legend legend = pieChartGoals.getLegend();
//            legend.setEnabled(false);
//
//            PieData data = new PieData(dataSet);
//            data.setDrawValues(false);
//            pieChartGoals.setData(data);
//            pieChartGoals.setDrawEntryLabels(false);
//            pieChartGoals.getDescription().setEnabled(false);
//            pieChartGoals.invalidate(); // refresh
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putBoolean(GOALSET,true);
//            editor.commit();
//        } else {
//
//
//            pieChartGoals.setVisibility(View.GONE);
//            tvGoalSet.setVisibility(View.GONE);
//            tvGoalAdjust.setVisibility(View.GONE);
//            tvGoalNothing.setVisibility(View.VISIBLE);
//            if(!isGoalSet){
////                Intent intent = new Intent(getContext(),SettingGoals.class);
////                getContext().startActivity(intent);
////                SharedPreferences.Editor editor = sharedPreferences.edit();
////                editor.putBoolean(GOALSET,true);
////                editor.commit();
//            }
//        }
    }

    private void listOther() {
//        allOutflow.setVisibility(View.VISIBLE);
        if (layoutothers.getVisibility() == View.VISIBLE) {
//            TransitionManager.beginDelayedTransition(tvOthers, new AutoTransition());
            layoutothers.setVisibility(View.GONE);
            tvOthers.setText("See more");
        } else {
//            TransitionManager.beginDelayedTransition(tvOthers, new AutoTransition());
            layoutothers.setVisibility(View.VISIBLE);
            tvOthers.setText("See less");
        }
    }

    private void loadDetails() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        getPieChartGoalsData();

//        getMpesaStatus();
//
//        getBudgetedLine();
//
//        getAccount(getContext());
//
//        getWorth();
//
//        setSlider();

    }

    private void getAccount(Context context) {

        RequestQueue queue = Volley.newRequestQueue(this.getContext());

        String income_type = "";
        StringRequest request = new StringRequest(Request.Method.POST, "https://mwalimubiashara.com/app/get_account.php", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "ACCOUNT STATUS " + response);
//                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {

                        String income_type1 = "";
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String tariff = "";


                            tariff = object.getString("package").toString();
                            income_type1 = object.getString("income_type");

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(PACKAGE_TYPE, tariff);
                            editor.commit();

//                            if(income_type1.equals("not_set")){
//                                Intent intent = new Intent(context, ChhoseAccountType.class);
//                                context.startActivity(intent);
//                            }else if(income_type1.equals("non-employed")){
//                                Intent intent = new Intent(context, DashboardAlpha.class);
//                                context.startActivity(intent);
//                            }
                        }
                    }


                } catch (Exception e) {

                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                pDialog.dismiss();

                Toast.makeText(getContext(), "Fail to get response " + error.toString(), Toast.LENGTH_SHORT).show();
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

    private void setSlider() {

        RequestQueue queue = Volley.newRequestQueue(this.getContext());


//        sliderView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "You clicked Home", Toast.LENGTH_SHORT).show();
//            }
//        });

        String urlSlider = "https://mwalimubiashara.com/app/get_slider.php";
        StringRequest request = new StringRequest(Request.Method.POST, urlSlider, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("TAG", "RESPONSE IS " + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

//                        if(success.equals("1")){
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
//
                        String dir = object.getString("url");

                        sliderDataArrayList.add(new SliderData("https://mwalimubiashara.com/app/images/" + dir));

                    }

                } catch (Exception e) {

                }
                SliderAdapter adapter = new SliderAdapter(getContext(), sliderDataArrayList);

//                sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
//
//                sliderView.setSliderAdapter(adapter);
//                sliderView.setScrollTimeInSec(4);
//
//                sliderView.setAutoCycle(true);
//
//                sliderView.startAutoCycle();

                pDialog.dismiss();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                pDialog.dismiss();
                Toast.makeText(getContext(), "Fail to get response " + error.toString(), Toast.LENGTH_SHORT).show();
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


    private void getWorth() {

        RequestQueue queue = Volley.newRequestQueue(this.getContext());

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
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
                            monthlyAmount.setText(object.getString("moneyformat"));
                            if (worth > 0) {
                                monthlyAmount.setTextColor(Color.parseColor("#0F9D58"));
                            } else {
                                monthlyAmount.setTextColor(Color.parseColor("#FF0000"));
                            }
                        }
                    }

                } catch (Exception e) {

                }
                pDialog.dismiss();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                pDialog.dismiss();
                Toast.makeText(getContext(), "Fail to get response " + error.toString(), Toast.LENGTH_SHORT).show();
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

    private void allocateAll() {

        String url = "https://mwalimubiashara.com/app/add_actual_expenses.php";
        RequestQueue queue = Volley.newRequestQueue(this.getContext());

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
//                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject object = jsonArray.getJSONObject(i);
//
//                            int worth = object.getInt("money");
//                            monthlyAmount.setText(object.getString("moneyformat"));
//                            if (worth > 0) {
//                                monthlyAmount.setTextColor(Color.parseColor("#0F9D58"));
//                            } else {
//                                monthlyAmount.setTextColor(Color.parseColor("#FF0000"));
//                            }
//                        }
                    }

                } catch (Exception e) {

                }
                getMpesaStatus();
                pDialog.dismiss();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                pDialog.dismiss();
                Toast.makeText(getContext(), "Fail to get response " + error.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("others", "others");

                return params;
            }
        };

        queue.add(request);
    }

    public void getMpesaStatus() {

        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest request = new StringRequest(Request.Method.POST, "https://mwalimubiashara.com/app/get_mpesa_status.php", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE FOR MPESA STATUS " + response);
//                progressBar.setVisibility(View.GONE);
                dataClassArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    int l = 0;

                    if (success.equals("1")) {


                        l = jsonArray.length();

                        for (int i = 0; i < l; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String cat = object.getString("income_cat");
                            String line = object.getString("income_line");
                            String freq = object.getString("income_freq");
                            String amount = object.getString("income_amount");
                            String id = object.getString("income_id");
                            String time = object.getString("income_actual_amount");
                            String type = object.getString("type");

                            dataClass = new DataClass(id, cat, line, amount, freq, time, type);
                            dataClassArrayList.add(dataClass);

                        }

                    }

                    if (l > 0) {
//                        layoutTransaction.setVisibility(View.VISIBLE);
                    } else {
                        layoutTransaction.setVisibility(View.GONE);
                    }
                    myAdapter.notifyDataSetChanged();

                } catch (Exception e) {

                }
                pDialog.dismiss();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                pDialog.dismiss();
//                Toast.makeText(getContext(), "Fail to get response ", Toast.LENGTH_SHORT).show();
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
                params.put("cashflow", "outflow");

                return params;
            }
        };

        queue.add(request);
    }

    private void createBottomSheetDialog() {
        if (bottomSheetDialog == null) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.investplan_popup, null);

            Button btnPlan = v.findViewById(R.id.idBtnInvestmentPlan);

            Button btnLumpsum = v.findViewById(R.id.idBtnLumpsum);

            btnLumpsum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    bottomSheetDialog.dismiss();
                    Intent intent = new Intent(view.getContext(), InputScreenActivity.class);
//                    Intent intent = new Intent(view.getContext(), InvestmentModelActivity.class);
                    view.getContext().startActivity(intent);
                }
            });
            btnPlan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    bottomSheetDialog.dismiss();
//                    Intent intent = new Intent(view.getContext(), InvestmentRecommendationCalculator.class);
                    Intent intent = new Intent(view.getContext(), InvestmentCalculator.class);
                    view.getContext().startActivity(intent);
                }
            });

            bottomSheetDialog = new BottomSheetDialog(getContext());

            bottomSheetDialog.setContentView(v);

            bottomSheetDialog.show();
        } else {
            bottomSheetDialog.show();
        }
    }

    //    public void showOutflowChart(View v){
//
//    }

    public void viewEndDay() {

        Intent intent = new Intent(view.getContext(), EODFeelingActivity.class);
        view.getContext().startActivity(intent);
    }

    public void viewRecentTransactions() {



//        Intent intent = new Intent(view.getContext(), JourneyMpesaAllocation.class);
        Intent intent = new Intent(view.getContext(), CashflowAnalysis.class);
        intent.putExtra("cashflow", "outflow");
        view.getContext().startActivity(intent);
    }


    private void getBudgetedLine() {

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://mwalimubiashara.com/app/get_actual_expenses.php";

//        if (cashflow.equals("inflow")) {
//            url = "https://mwalimubiashara.com/app/get_actual_inflow.php";
//        }

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);

                categories.clear();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {

                        int l = 0;
                        l = jsonArray.length();

                        for (int i = 0; i < l; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String line = object.getString("income_line");

                            String id = object.getString("income_id");


                            categories.add(id + "-" + line);

                        }

                        categories.add("Others");

                    }


                } catch (Exception e) {

                }

                pDialog.dismiss();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                pDialog.dismiss();

                Toast.makeText(getContext(), "Fail to get response " + error.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("date", "");

                return params;
            }
        };

        queue.add(request);
    }

//    private  void getData() {
//        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){
//            @Override
//            public void onResponse(String response){
//                try{
//                    JSONObject jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("success");
//                    JSONArray jsonArray =jsonObject.getJSONArray("data");
//
//                    if(success.equals("1")){
//                        for(int i=0;i<jsonArray.length();i++){
//                            JSONObject object=jsonArray.getJSONObject(i);
//
//                            int worth = object.getInt("money");
//                            monthlyAmount.setText(object.getString("moneyformat"));
//                            if(worth>0){
//                                monthlyAmount.setTextColor(Color.parseColor("#0F9D58"));
//                            }else{
//                                monthlyAmount.setTextColor(Color.parseColor("#FF0000"));
//                            }
//
//                        }
//                    }
//
//                }catch (Exception e){
//                }
//
//            }
//        }, new Response.ErrorListener(){
//            @Override
//            public void onErrorResponse(VolleyError error){
//                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
//        requestQueue.add(request);
//    }

}