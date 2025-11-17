package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.CUSTOMER_JOURNEY;
import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TrialPopUp {

    Dialog myDialog;
    Context c;
    LinearLayout layoutStart;
    SharedPreferences sharedPreferences;
    String email = "";
    String income_type_clicked="";


    public TrialPopUp(Context c,String email) {
        this.c = c;
        this.email = email;
        onCreatePop();
    }

    private void onCreatePop() {

        myDialog = new Dialog(c);

        myDialog.setContentView(R.layout.trial_version_pop_up);
        layoutStart = myDialog.findViewById(R.id.layoutStart);

        sharedPreferences = c.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL, "");

        layoutStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.hide();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(CUSTOMER_JOURNEY,"checked");
                editor.apply();

                boolean isConnected = ConnectivityHelper.isConnectedToNetwork(c);

                if (isConnected) {
//            Toast.makeText(this, "Connected to the Internet", Toast.LENGTH_SHORT).show();
                    getAccount();
                } else {
                    Toast.makeText(c, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
    public void show_dialog(){
        myDialog.show();
    }
    public void hide_dialog(){
        myDialog.dismiss();
    }
    public String get_income_type_clicked(){
        if(income_type_clicked.isEmpty()){
            return "";
        }
        return income_type_clicked;
    }

    private void getAccount() {

        RequestQueue queue = Volley.newRequestQueue(c);

        StringRequest request = new StringRequest(Request.Method.POST, "https://mwalimubiashara.com/app/get_account.php", new com.android.volley.Response.Listener<String>() {

            String income_type="";
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
//                progressBar.setVisibility(View.GONE);

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")){

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            income_type_clicked = object.getString("income_type");

                        }
                    }

                }catch (Exception e){

                }
//                Toast.makeText(c, "Response "+income_type, Toast.LENGTH_SHORT).show();
//                if(income_type.equals("not_set")){
//                    Intent intent = new Intent(c, ChhoseAccountType.class);
//                    c.startActivity(intent);
//                }else if(income_type.equals("non-employed")){
//
//                    Intent intent = new Intent(c, BPMainActivity.class);
//                    c.startActivity(intent);
//                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(c, "Fail to get response ", Toast.LENGTH_SHORT).show();
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
