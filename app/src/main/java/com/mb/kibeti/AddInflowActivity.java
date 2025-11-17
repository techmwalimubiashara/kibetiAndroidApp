package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mb.kibeti.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddInflowActivity extends AppCompatActivity {

    // creating variables for our edit text
    ImageButton arrow, arrow1, arrow2, arrow3, arrow4, arrow5, arrow6, arrow7, arrow8;
    LinearLayout hiddenView, hiddenView1, hiddenView2, hiddenView3, hiddenView4, hiddenView5, hiddenView6, hiddenView7, hiddenView8, allOutflow;
    CardView cardView, cardView1, cardView2, cardView3, cardView4, cardView5, cardView6, cardView7, cardView8;

    EditText amount1, amount2, amount3, amount4, amount5, amount6, amount7, amount8, amount9, amount10, amount11, amount12, amount13, amount14, amount15, amount16, amount17,
            amount18, amount19, amount20, amount21, amount22, amount23, amount24, amount25, amount26, amount27, amount28, amount29, amount30, amount31, amount32, amount33, amount34,
            amount35, amount36, amount37, amount38, amount39, amount40, amount41, amount42, amount43, amount44, amount45, amount46, amount47, amount48, amount49, amount50, amount51, amount52, amount53, amount54;
    TextView tvFreq1, tvFreq2, tvFreq3, tvFreq4, tvFreq5, tvFreq6, tvFreq7, tvFreq8, tvFreq9, tvFreq10, tvFreq11, tvFreq12, tvFreq13, tvFreq14, tvFreq15, tvFreq16, tvFreq17,
            tvFreq18, tvFreq19, tvFreq20, tvFreq21, tvFreq22, tvFreq23, tvFreq24, tvFreq25, tvFreq26, tvFreq27, tvFreq28, tvFreq29, tvFreq30, tvFreq31, tvFreq32, tvFreq33, tvFreq34,
            tvFreq35, tvFreq36, tvFreq37, tvFreq38, tvFreq39, tvFreq40, tvFreq41, tvFreq42,
            tvFreq43, tvFreq44, tvFreq45, tvFreq46, tvFreq47, tvFreq48, tvFreq49, tvFreq50, tvFreq51, tvFreq52, tvFreq53, tvFreq54;

    TextView imFreq1, imFreq2, imFreq3, imFreq4, imFreq5, imFreq6, imFreq7, imFreq8, imFreq9, imFreq10, imFreq11, imFreq12, imFreq13, imFreq14, imFreq15, imFreq16, imFreq17,
            imFreq18, imFreq19, imFreq20, imFreq21, imFreq22, imFreq23, imFreq24, imFreq25, imFreq26, imFreq27, imFreq28, imFreq29, imFreq30, imFreq31, imFreq32,
            imFreq33, imFreq34, imFreq35, imFreq36, imFreq37, imFreq38, imFreq39, imFreq40, imFreq41, imFreq42,
            imFreq43, imFreq44, imFreq45, imFreq46, imFreq47, imFreq48, imFreq49, imFreq50, imFreq51, imFreq52, imFreq53, imFreq54;

    private Button submitCourseBtn;
    Frequency inflowFreq;
    String email = "";
    String cat, line, amount, frequency = "";
    SharedPreferences sharedPreferences;
    DecimalFormat formatter;
    LinearLayout stateOther;
    NumberFormatOnTyping numberFormatOnTyping;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inflow);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL, "");

        numberFormatOnTyping = new NumberFormatOnTyping(this);

        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("###,###,###,###");

        inflowFreq = new Frequency(getApplicationContext());
        assign();

        addTextListener();

        cardView = findViewById(R.id.base_cardview);
        arrow = findViewById(R.id.arrow_button);
        hiddenView = findViewById(R.id.hidden_view);

        allOutflow = findViewById(R.id.allOutflow);
        stateOther = findViewById(R.id.stateOther);

        cardView1 = findViewById(R.id.base_cardview1);
        arrow1 = findViewById(R.id.arrow_button1);
        hiddenView1 = findViewById(R.id.hidden_view1);

        cardView2 = findViewById(R.id.base_cardview2);
        arrow2 = findViewById(R.id.arrow_button2);
        hiddenView2 = findViewById(R.id.hidden_view2);

        cardView3 = findViewById(R.id.base_cardview3);
        arrow3 = findViewById(R.id.arrow_button3);
        hiddenView3 = findViewById(R.id.hidden_view3);

        cardView4 = findViewById(R.id.base_cardview4);
        arrow4 = findViewById(R.id.arrow_button4);
        hiddenView4 = findViewById(R.id.hidden_view4);

        cardView5 = findViewById(R.id.base_cardview5);
        arrow5 = findViewById(R.id.arrow_button5);
        hiddenView5 = findViewById(R.id.hidden_view5);

        cardView6 = findViewById(R.id.base_cardview6);
        arrow6 = findViewById(R.id.arrow_button6);
        hiddenView6 = findViewById(R.id.hidden_view6);

        cardView7 = findViewById(R.id.base_cardview7);
        arrow7 = findViewById(R.id.arrow_button7);
        hiddenView7 = findViewById(R.id.hidden_view7);

        cardView8 = findViewById(R.id.base_cardview8);
        arrow8 = findViewById(R.id.arrow_button8);
        hiddenView8 = findViewById(R.id.hidden_view8);

        assignFreq();

        submitCourseBtn = findViewById(R.id.idBtnSubmitCourse);
        submitCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addInflow(cat, line, amount, frequency);

            }
        });
        arrow.setOnClickListener(view -> {
            if (hiddenView.getVisibility() == View.VISIBLE) {

                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                hiddenView.setVisibility(View.GONE);
                arrow.setImageResource(R.drawable.baseline_expand_right);
            } else {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                hiddenView.setVisibility(View.VISIBLE);
                arrow.setImageResource(R.drawable.baseline_expand_less_24);
            }
        });
        arrow1.setOnClickListener(view -> {
            if (hiddenView1.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(cardView1, new AutoTransition());
                hiddenView1.setVisibility(View.GONE);
                arrow1.setImageResource(R.drawable.baseline_expand_right);
            } else {
                TransitionManager.beginDelayedTransition(cardView1, new AutoTransition());
                hiddenView1.setVisibility(View.VISIBLE);
                arrow1.setImageResource(R.drawable.baseline_expand_less_24);
            }
        });
        arrow2.setOnClickListener(view -> {
            if (hiddenView2.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(cardView2, new AutoTransition());
                hiddenView2.setVisibility(View.GONE);
                arrow2.setImageResource(R.drawable.baseline_expand_right);
            } else {
                TransitionManager.beginDelayedTransition(cardView2, new AutoTransition());
                hiddenView2.setVisibility(View.VISIBLE);
                arrow2.setImageResource(R.drawable.baseline_expand_less_24);
            }
        });
        arrow3.setOnClickListener(view -> {
            if (hiddenView3.getVisibility() == View.VISIBLE) {

                TransitionManager.beginDelayedTransition(cardView3, new AutoTransition());
                hiddenView3.setVisibility(View.GONE);
                arrow3.setImageResource(R.drawable.baseline_expand_right);
            } else {
                TransitionManager.beginDelayedTransition(cardView3, new AutoTransition());
                hiddenView3.setVisibility(View.VISIBLE);
                arrow3.setImageResource(R.drawable.baseline_expand_less_24);
            }
        });
        arrow4.setOnClickListener(view -> {
            if (hiddenView4.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(cardView4, new AutoTransition());
                hiddenView4.setVisibility(View.GONE);
                arrow4.setImageResource(R.drawable.baseline_expand_right);
            } else {
                TransitionManager.beginDelayedTransition(cardView4, new AutoTransition());
                hiddenView4.setVisibility(View.VISIBLE);
                arrow4.setImageResource(R.drawable.baseline_expand_less_24);
            }
        });
        arrow5.setOnClickListener(view -> {
            if (hiddenView5.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(cardView5, new AutoTransition());
                hiddenView5.setVisibility(View.GONE);
                arrow5.setImageResource(R.drawable.baseline_expand_right);
            } else {
                TransitionManager.beginDelayedTransition(cardView5, new AutoTransition());
                hiddenView5.setVisibility(View.VISIBLE);
                arrow5.setImageResource(R.drawable.baseline_expand_less_24);
            }
        });
        arrow6.setOnClickListener(view -> {
            if (hiddenView6.getVisibility() == View.VISIBLE) {

                TransitionManager.beginDelayedTransition(cardView6, new AutoTransition());
                hiddenView6.setVisibility(View.GONE);
                arrow6.setImageResource(R.drawable.baseline_expand_right);
            } else {
                TransitionManager.beginDelayedTransition(cardView6, new AutoTransition());
                hiddenView6.setVisibility(View.VISIBLE);
                arrow6.setImageResource(R.drawable.baseline_expand_less_24);
            }
        });
        arrow7.setOnClickListener(view -> {
            if (hiddenView7.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(cardView7, new AutoTransition());
                hiddenView7.setVisibility(View.GONE);
                arrow7.setImageResource(R.drawable.baseline_expand_right);
            } else {
                TransitionManager.beginDelayedTransition(cardView7, new AutoTransition());
                hiddenView7.setVisibility(View.VISIBLE);
                arrow7.setImageResource(R.drawable.baseline_expand_less_24);
            }
        });
        arrow8.setOnClickListener(view -> {
            if (hiddenView8.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(cardView8, new AutoTransition());
                hiddenView8.setVisibility(View.GONE);
                arrow8.setImageResource(R.drawable.baseline_expand_right);
            } else {
                TransitionManager.beginDelayedTransition(cardView8, new AutoTransition());
                hiddenView8.setVisibility(View.VISIBLE);
                arrow8.setImageResource(R.drawable.baseline_expand_less_24);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //do whatever you want here
        finish();
        return true;
    }

    public void listOtherOutflow(View view) {
//        allOutflow.setVisibility(View.VISIBLE);
        if (allOutflow.getVisibility() == View.VISIBLE) {
            TransitionManager.beginDelayedTransition(stateOther, new AutoTransition());
            allOutflow.setVisibility(View.GONE);
        } else {
            TransitionManager.beginDelayedTransition(stateOther, new AutoTransition());
            allOutflow.setVisibility(View.VISIBLE);
        }
    }

    private void assignFreq() {
        tvFreq1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq1);
            }
        });
        tvFreq2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq2);
            }
        });
        tvFreq3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq3);
            }
        });
        tvFreq4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq4);
            }
        });
        tvFreq5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq5);
            }
        });
        tvFreq6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq6);
            }
        });
        tvFreq7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq7);
            }
        });
        tvFreq8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq8);
            }
        });
        tvFreq9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq9);
            }
        });
        tvFreq10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq10);
            }
        });
        tvFreq11.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq11);
            }
        });
        tvFreq12.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq12);
            }
        });
        tvFreq13.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq13);
            }
        });
        tvFreq14.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq14);
            }
        });
        tvFreq15.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq15);
            }
        });
        tvFreq16.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq16);
            }
        });
        tvFreq17.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq17);
            }
        });
        tvFreq18.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq18);
            }
        });
        tvFreq19.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq19);
            }
        });
        tvFreq20.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq20);
            }
        });
        tvFreq21.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq21);
            }
        });
        tvFreq22.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq22);
            }
        });
        tvFreq23.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq23);
            }
        });
        tvFreq24.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq24);
            }
        });
        tvFreq25.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq25);
            }
        });
        tvFreq26.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq26);
            }
        });
        tvFreq27.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq27);
            }
        });
        tvFreq28.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq28);
            }
        });
        tvFreq29.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq29);
            }
        });
        tvFreq30.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq30);
            }
        });
        tvFreq31.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq31);
            }
        });
        tvFreq32.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq32);
            }
        });
        tvFreq33.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq33);
            }
        });
        tvFreq34.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq34);
            }
        });
        tvFreq35.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq35);
            }
        });
        tvFreq36.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq36);
            }
        });
        tvFreq37.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq37);
            }
        });
        tvFreq38.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq38);
            }
        });
        tvFreq39.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq39);
            }
        });
        tvFreq40.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq40);
            }
        });
        tvFreq41.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq41);
            }
        });
        tvFreq42.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq42);
            }
        });
        tvFreq43.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq43);
            }
        });
        tvFreq44.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq44);
            }
        });
        tvFreq45.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq45);
            }
        });
        tvFreq46.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq46);
            }
        });
        tvFreq47.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq47);
            }
        });
        tvFreq48.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq48);
            }
        });
        tvFreq49.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq49);
            }
        });
        tvFreq50.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq50);
            }
        });
        tvFreq51.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq51);
            }
        });
        tvFreq52.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq52);
            }
        });
        tvFreq53.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq53);
            }
        });
        tvFreq54.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflowFreq.showPopup(v, tvFreq54);
            }
        });
    }

    private void addTextListener() {

        numberFormatOnTyping.setNumberFormatOnTyping(amount1, tvFreq1);
        numberFormatOnTyping.setNumberFormatOnTyping(amount2, tvFreq2);
        numberFormatOnTyping.setNumberFormatOnTyping(amount3, tvFreq3);
        numberFormatOnTyping.setNumberFormatOnTyping(amount4, tvFreq4);
        numberFormatOnTyping.setNumberFormatOnTyping(amount5, tvFreq5);
        numberFormatOnTyping.setNumberFormatOnTyping(amount6, tvFreq6);
        numberFormatOnTyping.setNumberFormatOnTyping(amount7, tvFreq7);
        numberFormatOnTyping.setNumberFormatOnTyping(amount8, tvFreq8);
        numberFormatOnTyping.setNumberFormatOnTyping(amount9, tvFreq9);
        numberFormatOnTyping.setNumberFormatOnTyping(amount10, tvFreq10);
        numberFormatOnTyping.setNumberFormatOnTyping(amount11, tvFreq11);
        numberFormatOnTyping.setNumberFormatOnTyping(amount12, tvFreq12);
        numberFormatOnTyping.setNumberFormatOnTyping(amount13, tvFreq13);
        numberFormatOnTyping.setNumberFormatOnTyping(amount14, tvFreq14);
        numberFormatOnTyping.setNumberFormatOnTyping(amount15, tvFreq15);
        numberFormatOnTyping.setNumberFormatOnTyping(amount16, tvFreq16);
        numberFormatOnTyping.setNumberFormatOnTyping(amount17, tvFreq17);
        numberFormatOnTyping.setNumberFormatOnTyping(amount18, tvFreq18);
        numberFormatOnTyping.setNumberFormatOnTyping(amount19, tvFreq19);
        numberFormatOnTyping.setNumberFormatOnTyping(amount20, tvFreq20);
        numberFormatOnTyping.setNumberFormatOnTyping(amount21, tvFreq21);
        numberFormatOnTyping.setNumberFormatOnTyping(amount22, tvFreq22);
        numberFormatOnTyping.setNumberFormatOnTyping(amount23, tvFreq23);
        numberFormatOnTyping.setNumberFormatOnTyping(amount24, tvFreq24);
        numberFormatOnTyping.setNumberFormatOnTyping(amount25, tvFreq25);
        numberFormatOnTyping.setNumberFormatOnTyping(amount26, tvFreq26);
        numberFormatOnTyping.setNumberFormatOnTyping(amount27, tvFreq27);
        numberFormatOnTyping.setNumberFormatOnTyping(amount28, tvFreq28);
        numberFormatOnTyping.setNumberFormatOnTyping(amount29, tvFreq29);
        numberFormatOnTyping.setNumberFormatOnTyping(amount30, tvFreq30);
        numberFormatOnTyping.setNumberFormatOnTyping(amount31, tvFreq31);
        numberFormatOnTyping.setNumberFormatOnTyping(amount32, tvFreq32);
        numberFormatOnTyping.setNumberFormatOnTyping(amount33, tvFreq33);
        numberFormatOnTyping.setNumberFormatOnTyping(amount34, tvFreq34);
        numberFormatOnTyping.setNumberFormatOnTyping(amount35, tvFreq35);
        numberFormatOnTyping.setNumberFormatOnTyping(amount36, tvFreq36);
        numberFormatOnTyping.setNumberFormatOnTyping(amount37, tvFreq37);
        numberFormatOnTyping.setNumberFormatOnTyping(amount38, tvFreq38);
        numberFormatOnTyping.setNumberFormatOnTyping(amount39, tvFreq39);
        numberFormatOnTyping.setNumberFormatOnTyping(amount40, tvFreq40);
        numberFormatOnTyping.setNumberFormatOnTyping(amount41, tvFreq41);
        numberFormatOnTyping.setNumberFormatOnTyping(amount42, tvFreq42);
        numberFormatOnTyping.setNumberFormatOnTyping(amount43, tvFreq43);
        numberFormatOnTyping.setNumberFormatOnTyping(amount44, tvFreq44);
        numberFormatOnTyping.setNumberFormatOnTyping(amount45, tvFreq45);
        numberFormatOnTyping.setNumberFormatOnTyping(amount46, tvFreq46);
        numberFormatOnTyping.setNumberFormatOnTyping(amount47, tvFreq47);
        numberFormatOnTyping.setNumberFormatOnTyping(amount48, tvFreq48);
        numberFormatOnTyping.setNumberFormatOnTyping(amount49, tvFreq49);
        numberFormatOnTyping.setNumberFormatOnTyping(amount50, tvFreq50);
        numberFormatOnTyping.setNumberFormatOnTyping(amount51, tvFreq51);
        numberFormatOnTyping.setNumberFormatOnTyping(amount52, tvFreq52);
        numberFormatOnTyping.setNumberFormatOnTyping(amount53, tvFreq53);
        numberFormatOnTyping.setNumberFormatOnTyping(amount54, tvFreq54);
    }

    private boolean checkFrequencyRequired(EditText editText) {
        if (editText.getText().toString().equals("Frequency required")) {
            return false;
        } else {
            return true;
        }
    }

    public void disabledAllAmountEt(EditText tv) {
        amount1.setEnabled(false);
        amount2.setEnabled(false);
        amount3.setEnabled(false);
        amount4.setEnabled(false);
        amount5.setEnabled(false);
        amount6.setEnabled(false);
        amount7.setEnabled(false);
        amount8.setEnabled(false);
        amount9.setEnabled(false);
        amount10.setEnabled(false);
        amount11.setEnabled(false);
        amount12.setEnabled(false);
        amount13.setEnabled(false);
        amount14.setEnabled(false);
        amount15.setEnabled(false);
        amount16.setEnabled(false);
        amount17.setEnabled(false);
        amount18.setEnabled(false);
        amount19.setEnabled(false);
        amount20.setEnabled(false);

        tv.setEnabled(true);
    }

    private void assign() {
        amount1 = findViewById(R.id.idAmount1);
        amount2 = findViewById(R.id.idAmount2);
        amount3 = findViewById(R.id.idAmount3);
        amount4 = findViewById(R.id.idAmount4);
        amount5 = findViewById(R.id.idAmount5);
        amount6 = findViewById(R.id.idAmount6);
        amount7 = findViewById(R.id.idAmount7);
        amount8 = findViewById(R.id.idAmount8);
        amount9 = findViewById(R.id.idAmount9);
        amount10 = findViewById(R.id.idAmount10);
        amount11 = findViewById(R.id.idAmount11);
        amount12 = findViewById(R.id.idAmount12);
        amount13 = findViewById(R.id.idAmount13);
        amount14 = findViewById(R.id.idAmount14);
        amount15 = findViewById(R.id.idAmount15);
        amount16 = findViewById(R.id.idAmount16);
        amount17 = findViewById(R.id.idAmount17);
        amount18 = findViewById(R.id.idAmount18);
        amount19 = findViewById(R.id.idAmount19);
        amount20 = findViewById(R.id.idAmount20);
        amount21 = findViewById(R.id.idAmount21);
        amount22 = findViewById(R.id.idAmount22);
        amount23 = findViewById(R.id.idAmount23);
        amount24 = findViewById(R.id.idAmount24);
        amount25 = findViewById(R.id.idAmount25);
        amount26 = findViewById(R.id.idAmount26);
        amount27 = findViewById(R.id.idAmount27);
        amount28 = findViewById(R.id.idAmount28);
        amount29 = findViewById(R.id.idAmount29);
        amount30 = findViewById(R.id.idAmount30);
        amount31 = findViewById(R.id.idAmount31);
        amount32 = findViewById(R.id.idAmount32);
        amount33 = findViewById(R.id.idAmount33);
        amount34 = findViewById(R.id.idAmount34);
        amount35 = findViewById(R.id.idAmount35);
        amount36 = findViewById(R.id.idAmount36);
        amount37 = findViewById(R.id.idAmount37);
        amount38 = findViewById(R.id.idAmount38);
        amount39 = findViewById(R.id.idAmount39);
        amount40 = findViewById(R.id.idAmount40);
        amount41 = findViewById(R.id.idAmount41);
        amount42 = findViewById(R.id.idAmount42);
        amount43 = findViewById(R.id.idAmount43);
        amount44 = findViewById(R.id.idAmount44);
        amount45 = findViewById(R.id.idAmount45);
        amount46 = findViewById(R.id.idAmount46);
        amount47 = findViewById(R.id.idAmount47);
        amount48 = findViewById(R.id.idAmount48);
        amount49 = findViewById(R.id.idAmount49);
        amount50 = findViewById(R.id.idAmount50);
        amount51 = findViewById(R.id.idAmount51);
        amount52 = findViewById(R.id.idAmount52);
        amount53 = findViewById(R.id.idAmount53);
        amount54 = findViewById(R.id.idAmount54);

        imFreq1 = findViewById(R.id.imFreq1);
        imFreq2 = findViewById(R.id.imFreq2);
        imFreq3 = findViewById(R.id.imFreq3);
        imFreq4 = findViewById(R.id.imFreq4);
        imFreq5 = findViewById(R.id.imFreq5);
        imFreq6 = findViewById(R.id.imFreq6);
        imFreq7 = findViewById(R.id.imFreq7);
        imFreq8 = findViewById(R.id.imFreq8);
        imFreq9 = findViewById(R.id.imFreq9);
        imFreq10 = findViewById(R.id.imFreq10);
        imFreq11 = findViewById(R.id.imFreq11);
        imFreq12 = findViewById(R.id.imFreq12);
        imFreq13 = findViewById(R.id.imFreq13);
        imFreq14 = findViewById(R.id.imFreq14);
        imFreq15 = findViewById(R.id.imFreq15);
        imFreq16 = findViewById(R.id.imFreq16);
        imFreq17 = findViewById(R.id.imFreq17);
        imFreq18 = findViewById(R.id.imFreq18);
        imFreq19 = findViewById(R.id.imFreq19);
        imFreq20 = findViewById(R.id.imFreq20);
        imFreq21 = findViewById(R.id.imFreq21);
        imFreq22 = findViewById(R.id.imFreq22);
        imFreq23 = findViewById(R.id.imFreq23);
        imFreq24 = findViewById(R.id.imFreq24);
        imFreq25 = findViewById(R.id.imFreq25);
        imFreq26 = findViewById(R.id.imFreq26);
        imFreq27 = findViewById(R.id.imFreq27);
        imFreq28 = findViewById(R.id.imFreq28);
        imFreq29 = findViewById(R.id.imFreq29);
        imFreq30 = findViewById(R.id.imFreq30);
        imFreq31 = findViewById(R.id.imFreq31);
        imFreq32 = findViewById(R.id.imFreq32);
        imFreq33 = findViewById(R.id.imFreq33);
        imFreq34 = findViewById(R.id.imFreq34);
        imFreq35 = findViewById(R.id.imFreq35);
        imFreq36 = findViewById(R.id.imFreq36);
        imFreq37 = findViewById(R.id.imFreq37);
        imFreq38 = findViewById(R.id.imFreq38);
        imFreq39 = findViewById(R.id.imFreq39);
        imFreq40 = findViewById(R.id.imFreq40);
        imFreq41 = findViewById(R.id.imFreq41);
        imFreq42 = findViewById(R.id.imFreq42);
        imFreq43 = findViewById(R.id.imFreq43);
        imFreq44 = findViewById(R.id.imFreq44);
        imFreq45 = findViewById(R.id.imFreq45);
        imFreq46 = findViewById(R.id.imFreq46);
        imFreq47 = findViewById(R.id.imFreq47);
        imFreq48 = findViewById(R.id.imFreq48);
        imFreq49 = findViewById(R.id.imFreq49);
        imFreq50 = findViewById(R.id.imFreq50);
        imFreq51 = findViewById(R.id.imFreq51);
        imFreq52 = findViewById(R.id.imFreq52);
        imFreq53 = findViewById(R.id.imFreq53);
        imFreq54 = findViewById(R.id.imFreq54);

        tvFreq1 = findViewById(R.id.tvFreq1);
        tvFreq2 = findViewById(R.id.tvFreq2);
        tvFreq3 = findViewById(R.id.tvFreq3);
        tvFreq4 = findViewById(R.id.tvFreq4);
        tvFreq5 = findViewById(R.id.tvFreq5);
        tvFreq6 = findViewById(R.id.tvFreq6);
        tvFreq7 = findViewById(R.id.tvFreq7);
        tvFreq8 = findViewById(R.id.tvFreq8);
        tvFreq9 = findViewById(R.id.tvFreq9);
        tvFreq10 = findViewById(R.id.tvFreq10);
        tvFreq11 = findViewById(R.id.tvFreq11);
        tvFreq12 = findViewById(R.id.tvFreq12);
        tvFreq13 = findViewById(R.id.tvFreq13);
        tvFreq14 = findViewById(R.id.tvFreq14);
        tvFreq15 = findViewById(R.id.tvFreq15);
        tvFreq16 = findViewById(R.id.tvFreq16);
        tvFreq17 = findViewById(R.id.tvFreq17);
        tvFreq18 = findViewById(R.id.tvFreq18);
        tvFreq19 = findViewById(R.id.tvFreq19);
        tvFreq20 = findViewById(R.id.tvFreq20);
        tvFreq21 = findViewById(R.id.tvFreq21);
        tvFreq22 = findViewById(R.id.tvFreq22);
        tvFreq23 = findViewById(R.id.tvFreq23);
        tvFreq24 = findViewById(R.id.tvFreq24);
        tvFreq25 = findViewById(R.id.tvFreq25);
        tvFreq26 = findViewById(R.id.tvFreq26);
        tvFreq27 = findViewById(R.id.tvFreq27);
        tvFreq28 = findViewById(R.id.tvFreq28);
        tvFreq29 = findViewById(R.id.tvFreq29);
        tvFreq30 = findViewById(R.id.tvFreq30);
        tvFreq31 = findViewById(R.id.tvFreq31);
        tvFreq32 = findViewById(R.id.tvFreq32);
        tvFreq33 = findViewById(R.id.tvFreq33);
        tvFreq34 = findViewById(R.id.tvFreq34);
        tvFreq35 = findViewById(R.id.tvFreq35);
        tvFreq36 = findViewById(R.id.tvFreq36);
        tvFreq37 = findViewById(R.id.tvFreq37);
        tvFreq38 = findViewById(R.id.tvFreq38);
        tvFreq39 = findViewById(R.id.tvFreq39);
        tvFreq40 = findViewById(R.id.tvFreq40);
        tvFreq41 = findViewById(R.id.tvFreq41);
        tvFreq42 = findViewById(R.id.tvFreq42);
        tvFreq43 = findViewById(R.id.tvFreq43);
        tvFreq44 = findViewById(R.id.tvFreq44);
        tvFreq45 = findViewById(R.id.tvFreq45);
        tvFreq46 = findViewById(R.id.tvFreq46);
        tvFreq47 = findViewById(R.id.tvFreq47);
        tvFreq48 = findViewById(R.id.tvFreq48);
        tvFreq49 = findViewById(R.id.tvFreq49);
        tvFreq50 = findViewById(R.id.tvFreq50);
        tvFreq51 = findViewById(R.id.tvFreq51);
        tvFreq52 = findViewById(R.id.tvFreq52);
        tvFreq53 = findViewById(R.id.tvFreq53);
        tvFreq54 = findViewById(R.id.tvFreq54);

    }

    private void addInflow(String cat, String line, String amount, String frequency) {


        // url to post our data
        String url = "https://mwalimubiashara.com/app/post_inflow.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(AddInflowActivity.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // on below line we are displaying a success toast message.
                    Toast.makeText(AddInflowActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                getApplicationContext().startActivity(intent);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(AddInflowActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                // as we are passing data in the form of url encoded
                // so we are passing the content type below
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                // below line we are creating a map for storing
                // our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our
                // key and value pair to our parameters.
                params.put("email", email);
                params.put("inflow_cat1", "A. Employment Income");
                params.put("inflow_line1", "Salary");
                params.put("amount1", amount1.getText().toString().replaceAll(",", ""));
                params.put("income_frequency1", tvFreq1.getText().toString());
                params.put("inflow_cat2", "A. Employment Income");
                params.put("inflow_line2", "Bonus");
                params.put("amount2", amount2.getText().toString().replaceAll(",", ""));
                params.put("income_frequency2", tvFreq2.getText().toString());
                params.put("inflow_cat3", "A. Employment Income");
                params.put("inflow_line3", "Overtime");
                params.put("amount3", amount3.getText().toString().replaceAll(",", ""));
                params.put("income_frequency3", tvFreq3.getText().toString());
                params.put("inflow_cat4", "A. Employment Income");
                params.put("inflow_line4", "Other Employment Income");
                params.put("amount4", amount4.getText().toString().replaceAll(",", ""));
                params.put("income_frequency4", tvFreq4.getText().toString());
                params.put("inflow_cat5", "B. Employment Benefit");
                params.put("inflow_line5", "Pension (Employer) benefit");
                params.put("amount5", amount5.getText().toString().replaceAll(",", ""));
                params.put("income_frequency5", tvFreq5.getText().toString());
                params.put("inflow_cat6", "B. Employment Benefit");
                params.put("inflow_line6", "Medical benefit");
                params.put("amount6", amount6.getText().toString().replaceAll(",", ""));
                params.put("income_frequency6", tvFreq6.getText().toString());
                params.put("inflow_cat7", "B. Employment Benefit");
                params.put("inflow_line7", "Group Life benefit");
                params.put("amount7", amount7.getText().toString().replaceAll(",", ""));
                params.put("income_frequency7", tvFreq7.getText().toString());
                params.put("inflow_cat8", "B. Employment Benefit");
                params.put("inflow_line8", "Telephone benefit");
                params.put("amount8", amount8.getText().toString().replaceAll(",", ""));
                params.put("income_frequency8", tvFreq8.getText().toString());
                params.put("inflow_cat9", "B. Employment Benefit");
                params.put("inflow_line9", "Car benefit");
                params.put("amount9", amount9.getText().toString().replaceAll(",", ""));
                params.put("income_frequency9", tvFreq9.getText().toString());
                params.put("inflow_cat10", "B. Employment Benefit");
                params.put("inflow_line10", "Meals benefit");
                params.put("amount10", amount10.getText().toString().replaceAll(",", ""));
                params.put("income_frequency10", tvFreq10.getText().toString());
                params.put("inflow_cat11", "B. Employment Benefit");
                params.put("inflow_line11", "Education benefit");
                params.put("amount11", amount11.getText().toString().replaceAll(",", ""));
                params.put("income_frequency11", tvFreq11.getText().toString());
                params.put("inflow_cat12", "B. Employment Benefit");
                params.put("inflow_line12", "Leave benefit");
                params.put("amount12", amount12.getText().toString().replaceAll(",", ""));
                params.put("income_frequency12", tvFreq12.getText().toString());
                params.put("inflow_cat13", "B. Employment Benefit");
                params.put("inflow_line13", "Housing benefit");
                params.put("amount13", amount13.getText().toString().replaceAll(",", ""));
                params.put("income_frequency13", tvFreq13.getText().toString());
                params.put("inflow_cat14", "C. Employment Income -Spouse");
                params.put("inflow_line14", "Salary - Spouse");
                params.put("amount14", amount14.getText().toString().replaceAll(",", ""));
                params.put("income_frequency14", tvFreq14.getText().toString());
                params.put("inflow_cat15", "C. Employment Income -Spouse");
                params.put("inflow_line15", "Bonus - Spouse");
                params.put("amount15", amount15.getText().toString().replaceAll(",", ""));
                params.put("income_frequency15", tvFreq15.getText().toString());
                params.put("inflow_cat16", "C. Employment Income -Spouse");
                params.put("inflow_line16", "Overtime - Spouse");
                params.put("amount16", amount16.getText().toString().replaceAll(",", ""));
                params.put("income_frequency16", tvFreq16.getText().toString());
                params.put("inflow_cat17", "C. Employment Income -Spouse");
                params.put("inflow_line17", "Other employement Income -Spouse");
                params.put("amount17", amount17.getText().toString().replaceAll(",", ""));
                params.put("income_frequency17", tvFreq17.getText().toString());
                params.put("inflow_cat18", "D. Employment Benefit - Spouse");
                params.put("inflow_line18", "Pension (Employer) benefit");
                params.put("amount18", amount18.getText().toString().replaceAll(",", ""));
                params.put("income_frequency18", tvFreq18.getText().toString());
                params.put("inflow_cat19", "D. Employment Benefit - Spouse");
                params.put("inflow_line19", "Medical benefit");
                params.put("amount19", amount19.getText().toString().replaceAll(",", ""));
                params.put("income_frequency19", tvFreq19.getText().toString());
                params.put("inflow_cat20", "D. Employment Benefit - Spouse");
                params.put("inflow_line20", "Group Life benefit");
                params.put("amount20", amount20.getText().toString().replaceAll(",", ""));
                params.put("income_frequency20", tvFreq20.getText().toString());
                params.put("inflow_cat21", "D. Employment Benefit - Spouse");
                params.put("inflow_line21", "Telephone benefit");
                params.put("amount21", amount21.getText().toString().replaceAll(",", ""));
                params.put("income_frequency21", tvFreq21.getText().toString());
                params.put("inflow_cat22", "D. Employment Benefit - Spouse");
                params.put("inflow_line22", "Car benefit");
                params.put("amount22", amount22.getText().toString().replaceAll(",", ""));
                params.put("income_frequency22", tvFreq22.getText().toString());
                params.put("inflow_cat23", "D. Employment Benefit - Spouse");
                params.put("inflow_line23", "Meals benefit");
                params.put("amount23", amount23.getText().toString().replaceAll(",", ""));
                params.put("income_frequency23", tvFreq23.getText().toString());
                params.put("inflow_cat24", "D. Employment Benefit - Spouse");
                params.put("inflow_line24", "Education benefit");
                params.put("amount24", amount24.getText().toString().replaceAll(",", ""));
                params.put("income_frequency24", tvFreq24.getText().toString());
                params.put("inflow_cat25", "D. Employment Benefit - Spouse");
                params.put("inflow_line25", "Housing benefit");
                params.put("amount25", amount25.getText().toString().replaceAll(",", ""));
                params.put("income_frequency25", tvFreq25.getText().toString());
                params.put("inflow_cat26", "E. Self-Employment");
                params.put("inflow_line26", "Salary from self-employment hustle");
                params.put("amount26", amount26.getText().toString().replaceAll(",", ""));
                params.put("income_frequency26", tvFreq26.getText().toString());
                params.put("inflow_cat27", "E. Self-Employment");
                params.put("inflow_line27", "Other income from self-employment");
                params.put("amount27", amount27.getText().toString().replaceAll(",", ""));
                params.put("income_frequency27", tvFreq27.getText().toString());
                params.put("inflow_cat28", "E. Self-Employment");
                params.put("inflow_line28", "Salary from self-employment - Spouse");
                params.put("amount28", amount28.getText().toString().replaceAll(",", ""));
                params.put("income_frequency28", tvFreq28.getText().toString());
                params.put("inflow_cat29", "E. Self-Employment");
                params.put("inflow_line29", "Other income self-employment - Spouse");
                params.put("amount29", amount29.getText().toString().replaceAll(",", ""));
                params.put("income_frequency29", tvFreq29.getText().toString());
                params.put("inflow_cat30", "F. Business Income");
                params.put("inflow_line30", "Business 1");
                params.put("amount30", amount30.getText().toString().replaceAll(",", ""));
                params.put("income_frequency30", tvFreq30.getText().toString());
                params.put("inflow_cat31", "F. Business Income");
                params.put("inflow_line31", "Business 2");
                params.put("amount31", amount31.getText().toString().replaceAll(",", ""));
                params.put("income_frequency31", tvFreq31.getText().toString());
                params.put("inflow_cat32", "F. Business Income");
                params.put("inflow_line32", "Business 3");
                params.put("amount32", amount32.getText().toString().replaceAll(",", ""));
                params.put("income_frequency32", tvFreq32.getText().toString());
                params.put("inflow_cat33", "F. Business Income");
                params.put("inflow_line33", "Business 4");
                params.put("amount33", amount33.getText().toString().replaceAll(",", ""));
                params.put("income_frequency33", tvFreq33.getText().toString());
                params.put("inflow_cat34", "F. Business Income");
                params.put("inflow_line34", "Business 5");
                params.put("amount34", amount34.getText().toString().replaceAll(",", ""));
                params.put("income_frequency34", tvFreq34.getText().toString());
                params.put("inflow_cat35", "F. Business Income");
                params.put("inflow_line35", "Commission");
                params.put("amount35", amount35.getText().toString().replaceAll(",", ""));
                params.put("income_frequency35", tvFreq35.getText().toString());
                params.put("inflow_cat36", "G. Investment Income");
                params.put("inflow_line36", "Interest Income");
                params.put("amount36", amount36.getText().toString().replaceAll(",", ""));
                params.put("income_frequency36", tvFreq36.getText().toString());
                params.put("inflow_cat37", "G. Investment Income");
                params.put("inflow_line37", "Dividend Income");
                params.put("amount37", amount37.getText().toString().replaceAll(",", ""));
                params.put("income_frequency37", tvFreq37.getText().toString());
                params.put("inflow_cat38", "G. Investment Income");
                params.put("inflow_line38", "Rental Income");
                params.put("amount38", amount38.getText().toString().replaceAll(",", ""));
                params.put("income_frequency38", tvFreq38.getText().toString());
                params.put("inflow_cat39", "G. Investment Income");
                params.put("inflow_line39", "Capital gains");
                params.put("amount39", amount39.getText().toString().replaceAll(",", ""));
                params.put("income_frequency39", tvFreq39.getText().toString());
                params.put("inflow_cat40", "G. Investment Income");
                params.put("inflow_line40", "Pension");
                params.put("amount40", amount40.getText().toString().replaceAll(",", ""));
                params.put("income_frequency40", tvFreq40.getText().toString());
                params.put("inflow_cat41", "G. Investment Income");
                params.put("inflow_line41", "Annuity");
                params.put("amount41", amount41.getText().toString().replaceAll(",", ""));
                params.put("income_frequency41", tvFreq41.getText().toString());
                params.put("inflow_cat42", "G. Investment Income");
                params.put("inflow_line42", "Annuity");
                params.put("amount42", amount42.getText().toString().replaceAll(",", ""));
                params.put("income_frequency42", tvFreq42.getText().toString());
                params.put("inflow_cat43", "G. Investment Income");
                params.put("inflow_line43", "Inheritance");
                params.put("amount43", amount43.getText().toString().replaceAll(",", ""));
                params.put("income_frequency43", tvFreq43.getText().toString());
                params.put("inflow_cat44", "G. Investment Income");
                params.put("inflow_line44", "Other investment income");
                params.put("amount44", amount44.getText().toString().replaceAll(",", ""));
                params.put("income_frequency44", tvFreq44.getText().toString());
                params.put("inflow_cat45", "H. Support Income");
                params.put("inflow_line45", "Grants and donations");
                params.put("amount45", amount45.getText().toString().replaceAll(",", ""));
                params.put("income_frequency45", tvFreq45.getText().toString());
                params.put("inflow_cat46", "H. Support Income");
                params.put("inflow_line46", "Family support");
                params.put("amount46", amount46.getText().toString().replaceAll(",", ""));
                params.put("income_frequency46", tvFreq46.getText().toString());
                params.put("inflow_cat47", "H. Support Income");
                params.put("inflow_line47", "Friends support");
                params.put("amount47", amount47.getText().toString().replaceAll(",", ""));
                params.put("income_frequency47", tvFreq47.getText().toString());
                params.put("inflow_cat48", "H. Support Income");
                params.put("inflow_line48", "Merry-go-round");
                params.put("amount48", amount48.getText().toString().replaceAll(",", ""));
                params.put("income_frequency48", tvFreq48.getText().toString());
                params.put("inflow_cat49", "H. Support Income");
                params.put("inflow_line49", "Government support");
                params.put("amount49", amount49.getText().toString().replaceAll(",", ""));
                params.put("income_frequency49", tvFreq49.getText().toString());
                params.put("inflow_cat50", "H. Support Income");
                params.put("inflow_line50", "Spouse support - upkeep");
                params.put("amount50", amount50.getText().toString().replaceAll(",", ""));
                params.put("income_frequency50", tvFreq50.getText().toString());
                params.put("inflow_cat51", "H. Support Income");
                params.put("inflow_line51", "Spouse support - health");
                params.put("amount51", amount51.getText().toString().replaceAll(",", ""));
                params.put("income_frequency51", tvFreq51.getText().toString());
                params.put("inflow_cat52", "H. Support Income");
                params.put("inflow_line52", "Spouse support - education");
                params.put("amount52", amount52.getText().toString().replaceAll(",", ""));
                params.put("income_frequency52", tvFreq52.getText().toString());
                params.put("inflow_cat53", "H. Support Income");
                params.put("inflow_line53", "Other support income");
                params.put("amount53", amount53.getText().toString().replaceAll(",", ""));
                params.put("income_frequency53", tvFreq53.getText().toString());
                params.put("inflow_cat54", "I. Other Income");
                params.put("inflow_line54", "I. Other Income");
                params.put("amount54", amount54.getText().toString().replaceAll(",", ""));
                params.put("income_frequency54", tvFreq54.getText().toString());


                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}