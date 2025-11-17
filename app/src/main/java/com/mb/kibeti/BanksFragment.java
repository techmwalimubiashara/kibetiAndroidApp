package com.mb.kibeti;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BanksFragment extends Fragment {

    private String url = "https://mwalimubiashara.com/app/";
    View view;
    ProgressBar progressBar;
    ListView listView;
    CompareInvestMyAdapter myAdapter;
    DataClass dataClass;
    private ArrayList<DataClass> dataClassArrayList = new ArrayList<>();

    public BanksFragment(String urlBank) {
        this.url= url+urlBank;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_banks, container, false);
        listView = view.findViewById(R.id.listview);
        progressBar = view.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        myAdapter = new CompareInvestMyAdapter(this.getContext(),dataClassArrayList);
        listView.setAdapter(myAdapter);
        listView.setClickable(true);
        boolean isConnected = ConnectivityHelper.isConnectedToNetwork(getContext());

        if (isConnected) {
            getBankDeposit();
        }

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        boolean isConnected = ConnectivityHelper.isConnectedToNetwork(getContext());

        if (isConnected) {
            getBankDeposit();
        }
    }

    private void getBankDeposit() {

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

                            String name = object.getString("name");
                            String rate = object.getString("rate");
                            String link = object.getString("link");
                            String amount = object.getString("amount");
                            String id = "";
                            dataClass = new DataClass(id,  rate,name, amount, link, "");
                            dataClassArrayList.add(dataClass);

                            Log.e("TAG", "Data is name" +name );
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

//                params.put("email", email);

                return params;
            }
        };

        queue.add(request);
    }
}