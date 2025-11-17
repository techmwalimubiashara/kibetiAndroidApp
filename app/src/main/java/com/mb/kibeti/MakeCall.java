package com.mb.kibeti;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.core.app.ActivityCompat;

public class MakeCall {
//    private static final int REQUEST_CALL_PHONE = 1;
//
//    Context context;
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_main);
//
//            // Replace with the phone number you want to call
//            String phoneNumber = "tel:1234567890";
//
//            // Check for permission
//            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                // Request permission if not granted
//                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
//            } else {
//                // Permission already granted, make the call
//                makeCall(phoneNumber);
//            }
//        }
//
//        private void makeCall(String phoneNumber) {
//            Intent callIntent = new Intent(Intent.ACTION_CALL);
//            callIntent.setData(Uri.parse(phoneNumber));
//
//            // Ensure the device can handle the call intent
//            if (callIntent.resolveActivity(context.getPackageManager()) != null) {
//                context.startActivity(callIntent);
//            }
//        }
//
//        @Override
//        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//            if (requestCode == REQUEST_CALL_PHONE) {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Permission granted, make the call
//                    String phoneNumber = "tel:1234567890"; // Replace with the phone number
//                    makeCall(phoneNumber);
//                } else {
//                    // Permission denied, show a message or handle accordingly
//                    // For example, show a toast or disable the call feature
//                }
//            }
//        }
}