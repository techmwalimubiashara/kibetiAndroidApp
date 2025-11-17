package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationFragment extends Fragment {

    View view;
    ProgressBar progressBar;
    public ListView listView;
    public MyAdapter myAdapter;
    DataClass dataClass;
    String url = "https://mwalimubiashara.com/app/get_allnotification.php";
    String email ="";
    SharedPreferences sharedPreferences;
    LinearLayout linearLayout;
    ActivityMainBinding binding;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 3000;

    public static ArrayList<DataClass> dataClassArrayList = new ArrayList<>();
    FloatingActionButton floatingActionButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_notification, container, false);
        listView = view.findViewById(R.id.listview);
        progressBar = view.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        linearLayout = view.findViewById(R.id.linearLayout);

        myAdapter = new MyAdapter(this.getContext(),dataClassArrayList);
        listView.setAdapter(myAdapter);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(),ViewNotificationActivity.class);
                intent.putExtra("cat", dataClassArrayList.get(i).getCat());
                intent.putExtra("line",dataClassArrayList.get(i).getLine());
                intent.putExtra("amount",dataClassArrayList.get(i).getAmount());
                intent.putExtra("id",dataClassArrayList.get(i).getInflowId());
                intent.putExtra("frequency",dataClassArrayList.get(i).getFrequency());
                startActivity(intent);
            }
        });
        sharedPreferences = getContext().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL,"");

//          handler.postDelayed(runnable = new Runnable() {
//             public void run() {
//                  handler.postDelayed(runnable, delay);
//                  getData();
        getInflow(email,url);
//             }
//          }, delay);


        floatingActionButton=view.findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddInflowActivity.class);
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

                            String cat = object.getString("noti_title");
                            String line = object.getString("noti_msg");
                            String freq = object.getString("noti_intro");
                            String amount = object.getString("noti_id");
                            String id = object.getString("noti_id");

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
}