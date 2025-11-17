package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SmsReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsReceiver";
    PipeLines util = new PipeLines();

    String add_actual_inflow_url = util.ADD_ACTUAL_INFLOW_URL;
    String add_mpesa_trans_url = util.ADD_MPESA_TRANS_URL;
    DBHandler dbHandler;

    //    NetworkChangeListener networkChangeListener;
    //    @Override
//    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "Sms Received", Toast.LENGTH_SHORT).show();
//    }
    @Override
    public void onReceive(Context context, Intent intent) {
        // Saving transactions offline
        dbHandler = new DBHandler(context);
        try {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                String format = bundle.getString("format");

                if (pdus != null) {
                    for (int i = 0; i < pdus.length; i++) {
                        SmsMessage smsMessage;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                        } else {
                            smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        }

                        if (smsMessage != null) {

                            String senderNumber = smsMessage.getDisplayOriginatingAddress();
                            String message = smsMessage.getMessageBody();

                            //Checking the Sender is MPESA
                            if (senderNumber.equals("MPESA")) {
                                mpesasorting(context, message);
                                parseMpesaMessage(message);
                            }


                            // Process the received SMS message here
                            // You can save it to a database, display it in your app, etc.

//                        Toast.makeText(context, "Sender :"+senderNumber+"\n Message : " + message , Toast.LENGTH_SHORT).show();

                            sendNotification(context);
                        } else {
                            Log.w(TAG, "SmsMessage was null at index: " + i);
                        }


                    }
                }
            } else {
                Log.w(TAG, "PDUs array is null");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error receiving SMS", e);
        }
    }


    private void createNotificationAllocated(Context context, String title, String content,
                                             String channedId, String intro, int priorty, int notificationID) {

        Intent intent = new Intent(context, CashflowAnalysis.class);


//        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("cashflow", "notification");

        intent.setAction(Intent.ACTION_VIEW);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationID, intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channedId)
                        .setSmallIcon(R.drawable.logo)
                        .setAutoCancel(true)
                        .setLights(Color.BLUE, 500, 500)
                        .setVibrate(new long[]{500, 500, 500})
                        .setPriority(priorty)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setContentIntent(pendingIntent)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);


        // Since android Oreo notification channel is needed.
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channedId,
                    channedId,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(channel);
        }
        Notification notification = notificationBuilder.build();

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(notificationID, notification);

//        playNotificationSound();
    }


    private void createNotification(Context context, String title, String content,
                                    String channedId, String intro, int priorty, int notificationID) {

        Intent intent = new Intent(context, JourneyMpesaAllocation.class);

//        startActivity(intent);
//        getWorth();

//        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("allocate", "outflow");
        intent.putExtra("cashflow","outflow");

        intent.setAction(Intent.ACTION_VIEW);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationID, intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channedId)
                        .setSmallIcon(R.drawable.logo)
                        .setAutoCancel(true)
                        .setLights(Color.BLUE, 500, 500)
                        .setVibrate(new long[]{500, 500, 500})
                        .setPriority(priorty)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setContentIntent(pendingIntent)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);


        // Since android Oreo notification channel is needed.
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channedId,
                    channedId,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(channel);
        }
        Notification notification = notificationBuilder.build();

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(notificationID, notification);

//        playNotificationSound();
    }

    public void parseMpesaMessage(String message) {
        String transactionCode = "";
        String receiverName = "";
        String receiverPhone = "";
        String amount = "";
        String date = "";
        String time = "";

        try {
            // 1️⃣ Extract Transaction Code (before "Confirmed.")
            int txEnd = message.indexOf(" Confirmed");
            if (txEnd != -1) {
                transactionCode = message.substring(0, txEnd).trim();
            }

            // 2️⃣ Extract Amount
            int amountStart = message.indexOf("Ksh");
            int amountEnd = message.indexOf(" sent to");
            if (amountStart != -1 && amountEnd != -1) {
                amount = message.substring(amountStart + 3, amountEnd).trim();
            }

            // 3️⃣ Extract Receiver (between "sent to" and "on")
            int receiverStart = message.indexOf("sent to");
            int receiverEnd = message.indexOf(" on ", receiverStart);
            if (receiverStart != -1 && receiverEnd != -1) {
                String receiverFull = message.substring(receiverStart + 7, receiverEnd).trim();
                // Extract phone number (assumed to be last word)
                String[] receiverParts = receiverFull.split(" ");
                if (receiverParts.length > 0) {
                    receiverPhone = receiverParts[receiverParts.length - 1];
                    // Get receiver name (everything except phone number)
                    receiverName = receiverFull.replace(receiverPhone, "").trim();
                }
            }

            // 4️⃣ Extract Date
            int dateStart = message.indexOf("on");
            int dateEnd = message.indexOf("at", dateStart);
            if (dateStart != -1 && dateEnd != -1) {
                date = message.substring(dateStart + 3, dateEnd).trim();
            }

            // 5️⃣ Extract Time
            int timeStart = message.indexOf("at");
            int timeEnd = message.indexOf(".", timeStart);
            if (timeStart != -1 && timeEnd != -1) {
                time = message.substring(timeStart + 3, timeEnd).trim();
            }

            // ✅ Print results
            System.out.println("Transaction Code: " + transactionCode);
            System.out.println("Receiver Name: " + receiverName);
            System.out.println("Receiver Phone: " + receiverPhone);
            System.out.println("Amount: Ksh" + amount);
            System.out.println("Date: " + date);
            System.out.println("Time: " + time);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String mpesasorting(Context context, String message) {

        Log.w(TAG, "Message body is " + message);

        String trans_type = "unknown";
        String[] sp = message.split(" ");
        String trans_code = sp[0];

        int result = findIndex(sp, "sent");
        String amount = "Ksh0.00";
        String time = "00:00 AM";

        String date = "01/01/99";
        String recipient = "";
        String type = "none";

        if (result != -1) {
            if (sp[result + 1].equals("to")) {


                if (sp[result + 3] != "for") {

                    recipient = sp[result + 2] + " " + sp[result + 3];

                    type = "Paybill";

                } else {

                    recipient = sp[result + 2];
                    type = "send money";
                }

                result = findIndex(sp, "account");


                if (result == -1) {

                    amount = sp[2].replace("Ksh", "");
                    type = "send money";
                } else {
                    amount = sp[2].replace("Ksh", "");
                    type = "Paybill";
                }
            }
        } else {

            result = findIndex(sp, "paid");

            if (result != -1) {

                recipient = sp[result + 2] + " " + sp[result + 3];

                amount = sp[2].replace("Ksh", "");
                type = "Till";

            }
        }

        result = findIndex(sp, "on");

        if (result > 0) {

            String[] datesp1 = sp[result + 1].split("/");

            time = sp[result + 3] + sp[result + 4];

            time = time.replace(".", "");

            Log.e(TAG, "Date is " + sp[result + 1]);
            Log.e(TAG, "time is " + time);

//                            String date = "20"+datesp1[2]+"-"+ datesp1[1]+"-"+ datesp1[0];


            date = sp[result + 1];
        }

//                            if(sp[3].equals("sent")){
//                                Toast.makeText(context, "Code "+trans_code+"\n Outflow of " + amount +" on "+date , Toast.LENGTH_SHORT).show();
//
//                                String amount  = sp[2].replace("Ksh","");
//
//                                String[] datesp1 = sp[10].split("/");
//                                String date = "20"+datesp1[2]+"-"+ datesp1[1]+"-"+ datesp1[0];
//
//                                addDataToDatabaseOutflow( context, amount, date);
//                            }
//                            if(sp[3].equals("paid")){
//
//                                String amount  = sp[2].replace("Ksh","");
//
//                                String[] datesp1 = sp[9].split("/");
//
//                                String date = "20"+datesp1[2]+"-"+ datesp1[1]+"-"+ datesp1[0];

//                            Toast.makeText(context, "Code is " + trans_code + " Amount is" + amount + " Date is " + date + " Time " + time + " Recipient " + recipient+" Transaction type "+type, Toast.LENGTH_SHORT).show();
        if (!Common.isConnectedToInternet(context)) {
//                                Toast.makeText(context, "Code is "+trans_code+" Amount is"+amount+" Date is "+date+" Time "+time+" Recipient "+recipient, Toast.LENGTH_SHORT).show();
            dbHandler.addTransactionOffline(context, amount, date, time, trans_code, recipient, type, "outflow");

        } else {


            addDataToDatabaseOutflow(context, amount, date, time, trans_code, recipient, type, "outflow");


            trans_type = "outflow";

        }

//                            }else if(sp[3].equals("sent")) {
//
//                                String amount  = sp[2].replace("Ksh","");
//
//                                String date = "";
//                                String[] datesp1 ={};
//
//                                if(sp[9].equals("account")){
//                                    datesp1 = sp[12].split("/");
//
//                                }else{
//                                    datesp1 = sp[10].split("/");
//                                }
//                                date = "20"+datesp1[2]+"-"+ datesp1[1]+"-"+ datesp1[0];

//                                System.out.println("Date : "+date+"\n Amount: "+amount+"\n");
//                                Toast.makeText(context, "Outflow of " + amount , Toast.LENGTH_SHORT).show();

//                                addDataToDatabaseOutflow( context, amount, date,trans_code,"outflow");

//                            if(sp[3].equals("received")){
////                                Toast.makeText(context, "Inflow of " + sp[4] , Toast.LENGTH_SHORT).show();
//                                 amount = sp[4].replace("Ksh","");
////                                String[] datesp = sp[11].split("/");
//
////                                String date = "20"+datesp[2]+"-"+ datesp[1]+"-"+ datesp[0];
////                                String date = sp[11];
//
////                                addDataToDatabase(context,amount,date);
////                                Toast.makeText(context, "Amount " + amount+"\nDate :"+date+", Code"+trans_code , Toast.LENGTH_SHORT).show();
//                                addDataToDatabaseOutflow( context, amount, date,trans_code,"inflow");
//
//                            }

        return trans_type;
    }

    public void addDataToDatabaseOutflow(Context context, String amount, String date, String time, String trans_code, String recipient, String type, String cashflow) {


        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(EMAIL, "");
        // url to post our data
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST, add_mpesa_trans_url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("TAG Sending MPESA to the server", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("error");
                    Boolean isAllocated = jsonObject.getBoolean("is_allocated");
//                    JSONArray jsonArray =jsonObject.getJSONArray("data");
                    if (!success) {
//                        sendNotification(context);
//                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        sendNotification(context);
//                        Toast.makeText(
//                                context,"Invoking Notification",Toast.LENGTH_SHORT).show();

//                        Toast.makeText(context,"Response: Successfully. Trans saved", Toast.LENGTH_SHORT).show();
                        Log.e("TAG Transactions","Allocation status: "+isAllocated);
                        if (isAllocated) {

                            createNotificationAllocated(context,
                                    "Take control of your money",
                                    "\uD83D\uDCB8 Ksh " + amount + " spent — stay smart and on top of your goals! Tap to manage your money.",
                                    "100",
                                    "Intro",
                                    NotificationCompat.PRIORITY_HIGH,
                                    100);
                        } else {
                            createNotification(
                                    context,
                                    "Take control of your money",
                                    "\uD83D\uDCB8 Ksh " + amount + " spent — stay smart and on top of your goals! Tap to check.",
                                    "100",
                                    "Intro",
                                    NotificationCompat.PRIORITY_HIGH,
                                    100
                            );

                        }

                    } else {


                    }


                } catch (JSONException e) {

                    Log.e(TAG, "Error response: Failed Error in Catch " + e.getMessage());
                    e.printStackTrace();

//                    Toast.makeText(context,"Response: Failed Error in Catch", Toast.LENGTH_SHORT).show();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
//                Toast.makeText(context, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                Log.e("TAG", "RESPONSE IS " + error);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("trans_code", trans_code);
                params.put("amount", amount);
                params.put("cashflow", cashflow);
                params.put("email", email);
                params.put("trans_name", recipient);
                params.put("date", date);
                params.put("time", time);
                params.put("type", type);

                return params;
            }
        };
        queue.add(request);
//        Toast.makeText(this, "Id "+income_id+". Freq "+income_frequency+". Amnt "+income_amount, Toast.LENGTH_SHORT).show();
    }

    public static int findIndex(String[] array, String searchText) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(searchText)) {

                return i; // Return the index if text is found
            }
        }
        return -1; // Return -1 if text is not found
    }

    private void sendNotification(Context context) {

        Intent mainIntent = new Intent(context, MainActivity.class);
        Notification noti = new Notification.Builder(context)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(context, 131314, mainIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle("Required cashflow update!")
                .setContentText("Update your cashflow for the outflow transaction done. .")
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.logo)
                .setTicker("Update your outflow .")
                .setWhen(System.currentTimeMillis())
                .getNotification();

        NotificationManager notificationManager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(131315, noti);

        Log.v(TAG, "Notification sent");
    }


    private void addDataToDatabase(Context context, String amnt, String date) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(EMAIL, "");

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(context);


        StringRequest request = new StringRequest(Request.Method.POST, add_actual_inflow_url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                progressBar.setVisibility(View.GONE);
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("error");
//                    JSONArray jsonArray =jsonObject.getJSONArray("data");
                    if (!success) {

//                        popupSnackbar("Income Actual has been updated successfully");
//                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        finish();
//                        line.setText(formatter.format(budgetAmountAv));
//                        amount.setText("0");
//                        date.setText("");
//                        valency.setText("0");
//                        line.setText(formatter.format(budgetAmount));

                    }

                    //              Toast.makeText(YourGoalActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
//                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(context, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                params.put("actual_income_amount", amnt);
                params.put("id", "0");
                params.put("date", date);

                return params;
            }
        };
        queue.add(request);
//        Toast.makeText(AddActualActivity.this, "Id "+income_id+". Freq "+income_frequency+". Amnt "+income_amount, Toast.LENGTH_SHORT).show();
    }
//    private void addToDatabase(String crt,Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
//        String email = sharedPreferences.getString(EMAIL, "");
//        String url = "https://mwalimubiashara.com/app/get_actual_history.php";
//        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
////                varTotal = 0;
////                budTotal = 0;
////                actTotal = 0;
////
////                ArrayList dateList = new ArrayList();
////                if (!crt.equals("all")) {
////                    budTotal = budgetAmount;
////                }
////                String display = "";
////                progressBar.setVisibility(View.GONE);
////                dataClassArrayList.clear();
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("success");
//                    JSONArray jsonArray = jsonObject.getJSONArray("data");
//
////                    if(success.equals("1")){
////                    int l = 0;
////                    l = jsonArray.length();
////
////                    for (int i = 0; i < l; i++) {
////
////                        JSONObject object = jsonArray.getJSONObject(i);
////                        String start = object.getString("date");
////                        String name = object.getString("name");
////                        String end = object.getString("end");
////                        String amount = object.getString("amount");
////                        String id = object.getString("id");
////
////
////                        String no_o = amount.replaceAll(" ", "");
////                        String no_o1 = no_o.replaceAll("KES", "");
////                        String no_o2 = no_o1.replaceAll(",", "");
////
//////                        valencyAmount = budgetAmount - Integer.parseInt(no_o2);
////
//////                        int number = Integer.parseInt(no_o2) - Integer.parseInt(no_o);
////                        int actAmt = Integer.parseInt(no_o2);
////
////
////                        actTotal = actTotal + actAmt;
////
////                        if (crt.equals("all")) {
////                            if (!dateList.isEmpty()) {
////
////                                int dateCount = 0;
////                                for (int x = 0; x < dateList.size(); x++) {
////
//////                                    String s = (String) dateList.get(x);
////                                    String dateDisplayed = dateList.get(x).toString();
////
////                                    if(freq_string.equals("Daily")){
////                                        if (dateDisplayed.equals(start)) {
////                                            dateCount++;
////                                        }
////                                    }
////                                    if(freq_string.equals("Monthly")){
//////                                        Log.e("Tag","Date is "+dateDisplayed+" Month is "+findDateMonth(dateDisplayed)+" Frequency is "+freq_string+" Week is "+findDateWeek(dateDisplayed));
////
////                                        if (findDateMonth(start).equals(findDateMonth(dateDisplayed))) {
////                                            dateCount++;
////                                        }
////                                    }
////                                    if(freq_string.equals("Weekly")){
////
////                                        if (findDateWeek(start)==findDateWeek(dateDisplayed)) {
////                                            dateCount++;
////                                        }
////                                    }
////
////                                }
////                                if(dateCount==0){
////                                    dateList.add(start);
////                                }
//////                                if () {
//////
//////                                }
////                            } else {
////                                dateList.add(start);
////                            }
//////                            budTotal = budTotal + budgetAmount;
////
//////                            number = budgetAmount - actAmt;
////                            budTotal = dateList.size()*budgetAmount;
////                        }
////
//////                        else{
//////
////////                            number = budTotal - actAmt;
////////                            budTotal = budTotal + budgetAmount;
//////                        }
//////                        varTotal = varTotal + number;
////
////                        dataClass = new DataClass(id, name, start, amount, end, "");
////
//////                            dataClass = new DataClass(id,name,age,gender);
////                        dataClassArrayList.add(dataClass);
////
////                    }
//////                    if(l>0){
//////                        linearLayout.setVisibility(View.GONE);
//////                    }else{
//////                        linearLayout.setVisibility(View.VISIBLE);
//////                    }
//////                    }
////                    myAdapter.notifyDataSetChanged();
//
//                } catch (Exception e) {
//
//                }
//
////                tvVarTotal.setText("" + formatter.format(budTotal - actTotal));
////                tvBudTotal.setText("" + formatter.format(budTotal));
////                tvActTotal.setText("" + formatter.format(actTotal));
////
////                if (dataClassArrayList.isEmpty()) {
////                    layoutNotFound.setVisibility(View.VISIBLE);
////                    listView.setVisibility(View.GONE);
////                } else {
////                    layoutNotFound.setVisibility(View.GONE);
////                    listView.setVisibility(View.VISIBLE);
////                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getCont, error.getMessage(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, "Something is wrong. Please check your internet connection", Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            public String getBodyContentType() {
//
//                return "application/x-www-form-urlencoded; charset=UTF-8";
//            }
//
//            @Override
//            protected Map<String, String> getParams() {
//
//                Map<String, String> params = new HashMap<String, String>();
//
//                params.put("email", email);
//                params.put("income_id", "0");
//                params.put("sort", crt);
//                params.put("frequency", "once");
//
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        requestQueue.add(request);
//    }
}
