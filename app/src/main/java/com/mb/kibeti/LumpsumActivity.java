package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LumpsumActivity extends AppCompatActivity {

    View view;
    ListView listView;
    MyAdapterLumpsum myAdapter;
    DataClass dataClass;
    ProgressBar progressBar;
    String url = "https://mwalimubiashara.com/app/get_lumpsum.php";
    public static ArrayList<DataClass> dataClassArrayList = new ArrayList<>();
    FloatingActionButton floatingActionButton;
    FloatingActionButton floatingActionButtonDownload;

    String email="";
    SharedPreferences sharedPreferences;

    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lumpsum);
//        view = inflater.inflate(R.layout.fragment_inflow, container, false);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL,"");

        listView = findViewById(R.id.listview);
        progressBar = findViewById(R.id.progressbar);
        linearLayout = findViewById(R.id.linearLayout);
        progressBar.setVisibility(View.VISIBLE);
        myAdapter = new MyAdapterLumpsum(LumpsumActivity.this,dataClassArrayList);
        listView.setAdapter(myAdapter);
        listView.setClickable(true);

        getData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(LumpsumActivity.this,ViewLumpsum.class);
                intent.putExtra("cat", dataClassArrayList.get(i).getCat());
                intent.putExtra("line",dataClassArrayList.get(i).getLine());
                intent.putExtra("amount",dataClassArrayList.get(i).getAmount());
                intent.putExtra("id",dataClassArrayList.get(i).getInflowId());
                intent.putExtra("frequency",dataClassArrayList.get(i).getFrequency());
                startActivity(intent);
//                myDialog = new Dialog(getContext());
//                ShowPopup(dataClassArrayList.get(i).getCat(),dataClassArrayList.get(i).getLine(),
//                        dataClassArrayList.get(i).getAmount(),dataClassArrayList.get(i).getFrequency(),dataClassArrayList.get(i).getInflowId());
            }
        });

        floatingActionButton = findViewById(R.id.floating_action_button);

        floatingActionButtonDownload = findViewById(R.id.floating_action_button_download);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), AddLumpsumActivity.class);
                view.getContext().startActivity(intent);

            }
        });
        floatingActionButtonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), LumpsumPdfActivity.class);
                view.getContext().startActivity(intent);

            }
        });

//        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        getData();
    }
    private  void getData() {
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                progressBar.setVisibility(View.GONE);
                dataClassArrayList.clear();
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray =jsonObject.getJSONArray("data");

//                    if(success.equals("1")){
                        int l=0;
                        l=jsonArray.length();

                        for(int i=0;i<l;i++){
                            JSONObject object=jsonArray.getJSONObject(i);

                            String start = object.getString("start");
                            String name = object.getString("name");
                            String end = object.getString("end");
                            String amount = object.getString("amount");
                            String id = object.getString("id");
                            String totalspent = object.getString("spent_total");

                            dataClass = new DataClass(id,end, name, amount,start, totalspent);

//                            dataClass = new DataClass(id,name,age,gender);
                            dataClassArrayList.add(dataClass);

                        }
                        if(l>0){
                            linearLayout.setVisibility(View.GONE);
                        }else{
                            linearLayout.setVisibility(View.VISIBLE);
                        }
//                    }
                    myAdapter.notifyDataSetChanged();

                }catch (Exception e){

                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
//                Toast.makeText(getCont, error.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(LumpsumActivity.this, "Something is wrong. Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }){
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
        RequestQueue requestQueue = Volley.newRequestQueue(LumpsumActivity.this);
        requestQueue.add(request);
    }
}