package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class NotifyActivity extends AppCompatActivity {



    public static String notify_title = "notify_title";
    public static String notify_content = "notify_content";
    public static String notify_intro = "notify_intro";
    public static String notify_id = "0";
    public static String email = "";

    private TextView textView,notifyMsg;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = this.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL,"");

        setContentView(R.layout.activity_notify);
        textView = findViewById(R.id.notifyText);
        notifyMsg = findViewById(R.id.notifymsg);
        updateUI();
        updateNotification(notify_id,email);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //do whatever you want here
        finish();
        return true;
    }
    private void updateUI() {
        String notifyData = getIntent().getExtras().get(notify_title) + "";

        String notifyMsgtext = getIntent().getExtras().get(notify_content)+"";

        notify_id = getIntent().getExtras().get(notify_id)+"";
        textView.setText(notifyData);
        notifyMsg.setText(notifyMsgtext);
//        Toast.makeText(this, "Id no"+notify_id, Toast.LENGTH_SHORT).show();
    }
    private void updateNotification(String id, String email) {

        String url = "https://mwalimubiashara.com/app/update_notification.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
//                progressBar.setVisibility(View.GONE);
//                dataClassArrayList.clear();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
//                Toast.makeText(this, "Fail to get response ", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("notify_id", id);
                params.put("email", email);

                return params;
            }
        };

        queue.add(request);
    }

}