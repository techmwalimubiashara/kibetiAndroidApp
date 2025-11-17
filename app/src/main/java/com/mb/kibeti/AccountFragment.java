package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;
import static com.mb.kibeti.LoginActivity.USERNAME;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccountFragment extends Fragment {
    TextView username,txtEmail,tvStatus,tvTariff,tvExpiry;
    View view;
    String email;
    PipeLines utils = new PipeLines();


    String url = utils.GET_ACCOUNT_URL;
    String url_reset = utils.RESET_ACCOUNT_URL;
    Button btnButton,btnCancel,btnMakepayment,btnChangeAcc;
    CardView card1,card2,card3,card4;
    ImageView imReset,ivPass;
    LinearLayout linearLayout;
    Dialog myDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        username = view.findViewById(R.id.usernameTV);
        txtEmail = view.findViewById(R.id.txtEmail);
        tvStatus = view.findViewById(R.id.txtStatus);
        tvTariff = view.findViewById(R.id.txtTariff);
        tvExpiry = view.findViewById(R.id.txtExpiry);
        btnButton = view.findViewById(R.id.idBtnReset);

        card1 = view.findViewById(R.id.base_cardview1);
        card2 = view.findViewById(R.id.base_cardview2);
        card3 = view.findViewById(R.id.base_cardview3);
        card4 = view.findViewById(R.id.base_cardview4);
        linearLayout = view.findViewById(R.id.linearLayout2);
        imReset = view.findViewById(R.id.imReset);
        ivPass = view.findViewById(R.id.ivPass);
        btnCancel = view.findViewById(R.id.idBtnCancel);
        btnMakepayment = view.findViewById(R.id.makepaymentBtn);
        btnChangeAcc = view.findViewById(R.id.changeAccBtn);
        username.setText(sharedPreferences.getString(USERNAME,""));
        email=sharedPreferences.getString(EMAIL,"");
        myDialog = new Dialog(getContext());
        txtEmail.setText(email);
        getAccount(email,url);

        btnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resetting();
                card1.setVisibility(view.VISIBLE);
                card2.setVisibility(view.VISIBLE);
                card3.setVisibility(view.VISIBLE);
                card4.setVisibility(view.VISIBLE);
                linearLayout.setVisibility(view.GONE);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card1.setVisibility(view.VISIBLE);
                card2.setVisibility(view.VISIBLE);
                card3.setVisibility(view.VISIBLE);
                card4.setVisibility(view.VISIBLE);
                linearLayout.setVisibility(view.GONE);
            }
        });
        btnMakepayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Makepayment.class);
                view.getContext().startActivity(intent);
            }});
        btnChangeAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), BPMainActivity.class);
                view.getContext().startActivity(intent);
            }});
        imReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                card1.setVisibility(view.GONE);
//                card2.setVisibility(view.GONE);
//                card3.setVisibility(view.GONE);
//                card4.setVisibility(view.GONE);
//                linearLayout.setVisibility(view.VISIBLE);
                AddSpendingPopup();
            }
        });
        ivPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChangePassword.class);
                view.getContext().startActivity(intent);
            }
        });


        return view;
    }

    private void getAccount(String email,String url) {

        RequestQueue queue = Volley.newRequestQueue(this.getContext());

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
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

                            username.setText(object.getString("first_name")+"  "+object.getString("other_name"));
                            tvStatus.setText(object.getString("trialExpired"));
                            tvTariff.setText(object.getString("package"));
                            tvExpiry.setText(object.getString("expiry"));

                            String tariff = "";

                            tariff = object.getString("package").toString();

                            if(tariff.equals("Lifetime")){
                                tvExpiry.setText("Lifetime");
                            }
                        }
                    }

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
    private void AddSpendingPopup() {
        TextView txtclose,cancelBtn;
        Button submitBtn;

        myDialog.setContentView(R.layout.reset_account_popup);
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        cancelBtn = (TextView) myDialog.findViewById(R.id.idBtnCancel);
        submitBtn = (Button) myDialog.findViewById(R.id.idBtnReset);

        txtclose.setText("x");

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetting();
                myDialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myDialog.dismiss();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
    public void resetting(){


            class Resetting extends AsyncTask<Void, Void, String> {

                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("email", email);

                    //returning the response
                    return requestHandler.sendPostRequest(url_reset, params);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);

                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(s);
                        //if no error in response
                        if (!obj.getBoolean("error")) {

                            Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_LONG).show();


                        }else{
//                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
//                            ed_email.setError(obj.getString("message"));
//                            ed_password.setError(obj.getString("message"));
                            Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
//                        e.printStackTrace();
                        Toast.makeText(getContext(), "Something is wrong", Toast.LENGTH_LONG).show();

                    }
                }
            }

        Resetting resetting = new Resetting();
        resetting.execute();

    }
}