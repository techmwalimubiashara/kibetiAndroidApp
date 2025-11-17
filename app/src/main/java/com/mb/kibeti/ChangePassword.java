package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ChangePassword extends AppCompatActivity {

    String url_change_pass = "https://mwalimubiashara.com/app/reset_pass.php";
    TextInputEditText oldPass, passC1,passC2;
    Button btnPassCancel,btnChangePass;
    TextView passMsg;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL,"");
        oldPass = findViewById(R.id.oldPass);
        passC1 = findViewById(R.id.newPass1);
        passC2 = findViewById(R.id.newPass2);
        passMsg = findViewById(R.id.passMsg);
        btnChangePass = findViewById(R.id.idBtnChangePass);

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePass(email,url_change_pass);
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //do whatever you want here
        finish();
        return true;
    }
    public void changePass(String email,String url){
        final String oldpass = oldPass.getText().toString();
        final String pass1 = passC1.getText().toString();
        final String pass2 = passC2.getText().toString();

        // validating the text fields if empty or not.
        if (TextUtils.isEmpty(oldpass)) {
            oldPass.setError("Old password is required");
        }  if (TextUtils.isEmpty(pass1)) {
            passC1.setError("Password is required");
        }
        if(TextUtils.isEmpty(pass1)) {
            passC2.setError("Confirm password is required");
        }
        if(!pass1.equals(pass2)){
            passC1.setError("Password not matching");
            passC2.setError("Password not matching");
        }

        if (!TextUtils.isEmpty(oldpass)&&!TextUtils.isEmpty(pass1)&&!TextUtils.isEmpty(pass2)&&pass1.equals(pass2)){
            // calling method to add data to Firebase Firestore.

//            progressBar.setVisibility(View.VISIBLE);
            class Register extends AsyncTask<Void, Void, String> {

                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", oldpass);
                    params.put("password_new", pass1);


                    //returning the response
                    return requestHandler.sendPostRequest(url, params);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);

//                    progressBar.setVisibility(View.GONE);

                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(s);
                        //if no error in response
                        if (!obj.getBoolean("error")) {

//                            String username = obj.getString("username");
//                            String email = obj.getString("email");
                            String trial = obj.getString("success");


                            if(trial.equals("1")){

                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                passMsg.setText(obj.getString("message"));
                                passMsg.setTextColor(Color.parseColor("#0F9D58"));
                                passMsg.setVisibility(View.VISIBLE);
                                finish();

                            }else{
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                passMsg.setText(obj.getString("message"));
                                passMsg.setTextColor(Color.parseColor("#FF0000"));
                                passMsg.setVisibility(View.VISIBLE);

//                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                                startActivity(intent);
//                                ed_email.setError(obj.getString("message"));

                            }

                        }else{
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                            passMsg.setText(obj.getString("oldPassErro"));
                            passMsg.setTextColor(Color.parseColor("#FF0000"));
                            passMsg.setVisibility(View.VISIBLE);
                            oldPass.setError(obj.getString("oldPassErro"));

//                            passC1.setError(obj.getString("passErro"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Something is wrong "+e, Toast.LENGTH_LONG).show();
                    }
                }
            }

            Register register = new Register();
            register.execute();
        }
    }
}