package com.mb.kibeti;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class MessageBroadcastReceiver extends BroadcastReceiver {
    // creating a variable for a message listener interface on below line.
    private static MessageListenerInterface mListener;
    @Override
    public void onReceive(Context context, Intent intent) {
        // getting bundle data on below line from intent.
        Bundle data = intent.getExtras();
        // creating an object on below line.
        Object[] pdus = (Object[]) data.get("pdus");
        // running for loop to read the sms on below line.
        for (int i = 0; i < pdus.length; i++) {
            // getting sms message on below line.
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            // extracting the sms from sms message and setting it to string on below line.
            String message = "Sender : " + smsMessage.getDisplayOriginatingAddress()
                    + "Message: " + smsMessage.getMessageBody();
            // adding the message to listener on below line.
            mListener.messageReceived(message);
        }
    }
    // on below line we are binding the listener.
    public static void bindListener(MessageListenerInterface listener) {
        mListener = listener;
    }
}