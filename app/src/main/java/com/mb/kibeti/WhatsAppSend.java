package com.mb.kibeti;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;
import static com.mb.kibeti.LoginActivity.PHONENUMBER;

public class WhatsAppSend {
    private Context context;
    private PipeLines utils = new PipeLines();
    private String SEND_WHATSAPP_MESSAGE_URL  = utils.SEND_WHATSAPP_MESSAGE_URL;
    String phoneNumber;
    SharedPreferences sharedPreferences;

    public WhatsAppSend(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(MY_PREFERENCES,MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString(PHONENUMBER,"");
    }


    public void sendMessage(String activity) {

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(context);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, SEND_WHATSAPP_MESSAGE_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("TAG", "SENDING WHATSAPP RESPONSE " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray =jsonObject.getJSONArray("data");
                    if(success.equals("1")){


                    }

//                    Toast.makeText(YourGoalActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(context, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                // so we are passing the content type below
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                // our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // key and value pair to our parameters.
                params.put("phoneNumber", phoneNumber);
                params.put("activity", activity);

                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}
