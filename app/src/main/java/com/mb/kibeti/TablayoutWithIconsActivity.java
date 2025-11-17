package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mb.kibeti.databinding.ActivityTablayoutWithIconsBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TablayoutWithIconsActivity extends AppCompatActivity {

    ActivityTablayoutWithIconsBinding binding;

    TabLayoutMediator mediator;

//    PagerAdapter adapter;
    int total_income_count=0;
    NetworthViewPagerAdapter adapter;

    String email;
    private static final String url =  "https://mwalimubiashara.com/app/get_inflow_actual.php";

    int [] imageList={R.drawable.arrow_up,R.drawable.arrow_down};
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tablayout_with_icons);

        binding= ActivityTablayoutWithIconsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL, "");

        getInflow(email,url);

        adapter = new NetworthViewPagerAdapter(getSupportFragmentManager(),getLifecycle());
        mediator = new TabLayoutMediator(binding.tablayout, binding.viewPager, ((tab, position) ->
        {
            tab.setText(adapter.getHeader(position));
//            tab.setIcon(getResources().getDrawable(imageList[position]));

            BadgeDrawable drawable = tab.getOrCreateBadge();
            drawable.setBackgroundColor(getResources().getColor(R.color.primary_green));

            drawable.setVisible(false);

            switch (position){
                case 0: {
//                    total_income_count = 4;
                    if (total_income_count >
                            0) {
                        drawable.setNumber(total_income_count);
                        drawable.setVisible(true);
                    }
                }
                    break;
                case 1:
                    drawable.setNumber(5);
                    drawable.setVisible(true);
                    break;
            }

        }));

        binding.viewPager.setAdapter(adapter);

        if(!mediator.isAttached()) mediator.attach();
    }
    private void getInflow(String email,String url) {

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);

                total_income_count = 4;

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

//                    if(success.equals("1")){

//                    int amount = jsonObject.getInt("total_actual");
//
////                            total_income_count = Integer.parseInt(amount);
//                    total_income_count = amount;
                    total_income_count = 4;
                        int l=0;
                        l=jsonArray.length();

                        for(int i=0;i<l;i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            int amount1 = object.getInt("total_actual");

//                            total_income_count = Integer.parseInt(amount);
//                            total_income_count = 4;

                        }

//                    }

                }catch (Exception e){

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
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

                params.put("email", email);

                return params;
            }
        };

        queue.add(request);
    }
}