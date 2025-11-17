package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

public class Read5SMS extends AppCompatActivity {
    private ArrayList<String> smsList = new ArrayList<>();
    private ListView listView;
    private static final int READ_SMS_PERMISSION_CODE = 1;
    DataClass dataClass;
    private MpesaCheckingAdapter myAdapter;
    SmsReceiver smsReceiver;
    String email;
    private static ArrayList<DataClass> dataClassArrayList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    UpdateMpesaPop showPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read5_sms);


        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL, "");

        showPop = new UpdateMpesaPop(this, email);

        getMpesaStatus();

        listView = findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, smsList);
        listView.setAdapter(adapter);

//        myAdapter = new MpesaCheckingAdapter(this, dataClassArrayList);

        myAdapter = new MpesaCheckingAdapter(this, dataClassArrayList);

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

//                Toast.makeText(getContext(), dataClassArrayList.get(i).getCat()+""+dataClassArrayList.get(i).getLine()+""+
//                        dataClassArrayList.get(i).getAmount()+""+dataClassArrayList.get(i).getFrequency()+""+
//                        dataClassArrayList.get(i).getInflowId(), Toast.LENGTH_SHORT).show();

            }
        });

        if(dataClassArrayList.isEmpty()){
            finish();
        }

    }


    private void readSms() {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(
                Telephony.Sms.CONTENT_URI,
                null,
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            int count=0;
            do {
                String address = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                String body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY));
                if(address.equals("MPESA")){
//                smsList.add("Sender: " + address + "\nMessage: " + body);
                if(smsReceiver.mpesasorting(this,body).equals("outflow")){
                    smsList.add("Sender: " + address + "\nMessage: " + body);
                    count++;
                }

                }
            } while (cursor.moveToNext()&&count<5);
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    public void getMpesaStatus() {

        RequestQueue queue = Volley.newRequestQueue(this);

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
                            String type  = object.getString("type");
                            dataClass = new DataClass(id, cat, line, amount, freq, time,type);
                            dataClassArrayList.add(dataClass);

                        }

                    }

                    myAdapter.notifyDataSetChanged();

                } catch (Exception e) {

                }
//                pDialog.dismiss();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
//                pDialog.dismiss();
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readSms();
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) listView.getAdapter();
                adapter.notifyDataSetChanged();
            }
        }
    }
}