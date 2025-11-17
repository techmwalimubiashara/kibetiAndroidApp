package com.mb.kibeti;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserExperienceFeedback extends BottomSheetDialogFragment {
    private TextView tvHeader, emojiGood, emojiNeutral, emojiBad, tvNotNow;
    Button submitFeedbackBtn;
    String selectedRate = "";
    String area="",email="";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_experience_feedback,
                container, false);



        area = this.getArguments().getString("title");
        email = this.getArguments().getString("email");

//        tvHeader = v.findViewById(R.id.title_text);
        emojiBad = v.findViewById(R.id.emojiBad);
        emojiNeutral = v.findViewById(R.id.emojiNeutral);
        emojiGood = v.findViewById(R.id.emojiGood);
        tvNotNow = v.findViewById(R.id.tv_not_now);
        submitFeedbackBtn = v.findViewById(R.id.submitFeedbackBtn);

        setupListeners();

        return v;
    }

    public void setTitle(String title) {
        tvHeader.setText(title);
    }

    private void setupListeners() {

        emojiBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedRate = "Bad";
                highlightSelectedRate(emojiBad);
                submitFeedback();
            }
        });
        emojiNeutral.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                selectedRate = "Neutral";
                                                highlightSelectedRate(emojiNeutral);
                                                submitFeedback();
                                            }
                                        }
        );
        emojiGood.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             selectedRate = "Good";
                                             highlightSelectedRate(emojiGood);

                                             submitFeedback();
                                         }
                                     }
        );
        tvNotNow.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dismiss();
                                        }
                                    }
        );

        submitFeedbackBtn.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View view) {
                                                     submitFeedback();
                                                 }
                                             }
        );
    }

    private void submitFeedback() {
        dismiss();
        postFeedback();
    }

    private void postFeedback() {
        String url = "https://mwalimubiashara.com/app/post_feedback.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                } catch (Exception e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getCont, error.getMessage(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getContext(), "Something is wrong. Please check your internet connection", Toast.LENGTH_SHORT).show();
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
                params.put("area", area);
                params.put("comment", selectedRate);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private void highlightSelectedRate(TextView selectedEmoji) {

        emojiBad.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        emojiNeutral.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        emojiGood.setBackgroundColor(android.graphics.Color.TRANSPARENT);

        selectedEmoji.setBackgroundColor(android.graphics.Color.LTGRAY);
    }

}
