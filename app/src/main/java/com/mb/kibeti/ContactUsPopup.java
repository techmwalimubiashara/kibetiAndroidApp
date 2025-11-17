package com.mb.kibeti;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ContactUsPopup extends BottomSheetDialogFragment {

    private static final String PHONE_NUMBER = "+254746380606";

    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.contact_us_popup,
                container, false);

        Button btnCall = v.findViewById(R.id.btnCall);
        Button btnWhatsApp = v.findViewById(R.id.btnWhatsApp);

        // WhatsApp Button Click
        btnWhatsApp.setOnClickListener(v -> openWhatsApp(PHONE_NUMBER, "Hello! I need assistance."));

        // Call Button Click
        btnCall.setOnClickListener(v -> makePhoneCall(PHONE_NUMBER));

        return  v;
    }

    private void openWhatsApp(String phoneNumber, String message) {
        try {
            String url = "https://wa.me/" + phoneNumber.replace("+", "") + "?text=" + Uri.encode(message);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(), "WhatsApp is not installed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void makePhoneCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

}