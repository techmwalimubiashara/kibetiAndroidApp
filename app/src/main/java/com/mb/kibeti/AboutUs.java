package com.mb.kibeti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
//import com.mb.kibeti.BuildConfig;

import com.mb.kibeti.feedback.screens.FeedbackActivity;

public class AboutUs extends AppCompatActivity {
    TextView versionTV;
    TextView tvFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        versionTV = findViewById(R.id.versionTV);
        tvFeedback = findViewById(R.id.tvFeedback);
//        String version =
//                "App Version : " + (BuildConfig.VERSION_NAME != null ? BuildConfig.VERSION_NAME : "Unknown");
//        versionTV.setText(version);

        tvFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent feedback = new Intent(AboutUs.this, FeedbackActivity.class);
                startActivity(feedback);
            }
        });
        setupHyperlink();
    }

    private void setupHyperlink() {
        TextView tac = findViewById(R.id.tac);
        tac.setMovementMethod(LinkMovementMethod.getInstance());
        tac.setLinkTextColor(Color.parseColor("#404040"));
        TextView pp = findViewById(R.id.privacy_policy);
        pp.setMovementMethod(LinkMovementMethod.getInstance());
        pp.setLinkTextColor(Color.parseColor("#404040"));
    }
}