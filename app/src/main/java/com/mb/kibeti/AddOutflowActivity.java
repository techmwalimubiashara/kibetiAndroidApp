package com.mb.kibeti;

import static com.mb.kibeti.LoginActivity.EMAIL;
import static com.mb.kibeti.LoginActivity.MY_PREFERENCES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddOutflowActivity extends AppCompatActivity {

    ImageButton arrow, arrow1, arrow2, arrow3, arrow4, arrow5, arrow6, arrow7, arrow8;
    LinearLayout hiddenView, hiddenView1, hiddenView2, hiddenView3, hiddenView4, hiddenView5, hiddenView6, hiddenView7, hiddenView8, allOutflow;
    CardView cardView, cardView1, cardView2, cardView3, cardView4, cardView5, cardView6, cardView7, cardView8;

    EditText amount1, amount2, amount3, amount4, amount5, amount6, amount7, amount8, amount9, amount10, amount11, amount12, amount13, amount14, amount15, amount16, amount17,
            amount18, amount19, amount20, amount21, amount22, amount23, amount24, amount25, amount26, amount27, amount28, amount29, amount30, amount31, amount32, amount33, amount34,
            amount35, amount36, amount37, amount38, amount39, amount40, amount41, amount42, amount43, amount44, amount45, amount46, amount47, amount48, amount49, amount50, amount51,
            amount52, amount53, amount54, amount55, amount56, amount57, amount58, amount59, amount60, amount61, amount62, amount63, amount64,
            amount65, amount66, amount67, amount68, amount69, amount70, amount71, amount72, amount73, amount74,
            amount75, amount76, amount77;

    TextView tvFreq1, tvFreq2, tvFreq3, tvFreq4, tvFreq5, tvFreq6, tvFreq7, tvFreq8, tvFreq9, tvFreq10, tvFreq11, tvFreq12, tvFreq13, tvFreq14, tvFreq15, tvFreq16, tvFreq17,
            tvFreq18, tvFreq19, tvFreq20, tvFreq21, tvFreq22, tvFreq23, tvFreq24, tvFreq25, tvFreq26, tvFreq27, tvFreq28, tvFreq29, tvFreq30, tvFreq31, tvFreq32, tvFreq33, tvFreq34,
            tvFreq35, tvFreq36, tvFreq37, tvFreq38, tvFreq39, tvFreq40, tvFreq41, tvFreq42, tvFreq43, tvFreq44, tvFreq45, tvFreq46, tvFreq47, tvFreq48, tvFreq49, tvFreq50, tvFreq51,
            tvFreq52, tvFreq53, tvFreq54, tvFreq55, tvFreq56, tvFreq57, tvFreq58, tvFreq59, tvFreq60, tvFreq61, tvFreq62, tvFreq63, tvFreq64, tvFreq65, tvFreq66, tvFreq67, tvFreq68, tvFreq69,
            tvFreq70, tvFreq71, tvFreq72, tvFreq73, tvFreq74, tvFreq75, tvFreq76, tvFreq77;

    LinearLayout stateOther;
    private Button submitCourseBtn;
    String email = "";
    SharedPreferences sharedPreferences;
    DecimalFormat formatter;
    Frequency outflowFreq;
    ProgressDialog pDialog;

    NumberFormatOnTyping numberFormatOnTyping;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_outflow);

        outflowFreq = new Frequency(getApplicationContext());

        pDialog = new ProgressDialog(this);

        numberFormatOnTyping = new NumberFormatOnTyping(this);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL, "");

        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("###,###,###,###");


        cardView = findViewById(R.id.base_cardview);
        arrow = findViewById(R.id.arrow_button);
        hiddenView = findViewById(R.id.hidden_view);

        cardView1 = findViewById(R.id.base_cardview1);
        arrow1 = findViewById(R.id.arrow_button1);
        hiddenView1 = findViewById(R.id.hidden_view1);
        allOutflow = findViewById(R.id.allOutflow);
        stateOther = findViewById(R.id.stateOther);

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

        assign();
        assignFreq();
        addTextListener();

        submitCourseBtn = findViewById(R.id.idBtnSubmitCourse);
        submitCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addInflow();
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

    private void assignFreq() {

        tvFreq1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq1);
            }
        });
        tvFreq2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq2);
            }
        });
        tvFreq3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq3);
            }
        });
        tvFreq4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq4);
            }
        });
        tvFreq5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq5);
            }
        });
        tvFreq6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq6);
            }
        });
        tvFreq7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq7);
            }
        });
        tvFreq8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq8);
            }
        });
        tvFreq9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq9);
            }
        });
        tvFreq10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq10);
            }
        });
        tvFreq11.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq11);
            }
        });
        tvFreq12.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq12);
            }
        });
        tvFreq13.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq13);
            }
        });
        tvFreq14.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq14);
            }
        });
        tvFreq15.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq15);
            }
        });
        tvFreq16.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq16);
            }
        });
        tvFreq17.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq17);
            }
        });
        tvFreq18.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq18);
            }
        });
        tvFreq19.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq19);
            }
        });
        tvFreq20.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq20);
            }
        });
        tvFreq21.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq21);
            }
        });
        tvFreq22.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq22);
            }
        });
        tvFreq23.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq23);
            }
        });
        tvFreq24.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq24);
            }
        });
        tvFreq25.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq25);
            }
        });
        tvFreq26.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq26);
            }
        });
        tvFreq27.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq27);
            }
        });
        tvFreq28.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq28);
            }
        });
        tvFreq29.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq29);
            }
        });
        tvFreq30.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq30);
            }
        });
        tvFreq31.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq31);
            }
        });
        tvFreq32.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq32);
            }
        });
        tvFreq33.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq33);
            }
        });
        tvFreq34.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq34);
            }
        });
        tvFreq35.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq35);
            }
        });
        tvFreq36.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq36);
            }
        });
        tvFreq37.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq37);
            }
        });
        tvFreq38.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq38);
            }
        });
        tvFreq39.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq39);
            }
        });
        tvFreq40.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq40);
            }
        });
        tvFreq41.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq41);
            }
        });
        tvFreq42.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq42);
            }
        });
        tvFreq43.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq43);
            }
        });
        tvFreq44.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq44);
            }
        });
        tvFreq45.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq45);
            }
        });
        tvFreq46.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq46);
            }
        });
        tvFreq47.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq47);
            }
        });
        tvFreq48.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq48);
            }
        });
        tvFreq49.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq49);
            }
        });
        tvFreq50.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq50);
            }
        });
        tvFreq51.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq51);
            }
        });
        tvFreq52.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq52);
            }
        });
        tvFreq53.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq53);
            }
        });
        tvFreq54.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq54);
            }
        });
        tvFreq55.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq55);
            }
        });
        tvFreq56.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq56);
            }
        });
        tvFreq57.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq57);
            }
        });
        tvFreq58.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq58);
            }
        });
        tvFreq59.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq59);
            }
        });
        tvFreq60.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq60);
            }
        });
        tvFreq61.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq61);
            }
        });
        tvFreq62.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq62);
            }
        });
        tvFreq63.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq63);
            }
        });
        tvFreq64.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq64);
            }
        });
        tvFreq65.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq65);
            }
        });
        tvFreq66.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq66);
            }
        });
        tvFreq67.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq67);
            }
        });
        tvFreq68.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq68);
            }
        });
        tvFreq69.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq69);
            }
        });

        tvFreq70.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq70);
            }
        });
        tvFreq71.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq71);
            }
        });
        tvFreq72.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq72);
            }
        });
        tvFreq73.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq73);
            }
        });
        tvFreq74.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq74);
            }
        });
        tvFreq75.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq75);
            }
        });
        tvFreq76.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq76);
            }
        });
        tvFreq77.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                outflowFreq.showPopup(v, tvFreq77);
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
        numberFormatOnTyping.setNumberFormatOnTyping(amount55, tvFreq55);
        numberFormatOnTyping.setNumberFormatOnTyping(amount56, tvFreq56);
        numberFormatOnTyping.setNumberFormatOnTyping(amount57, tvFreq57);
        numberFormatOnTyping.setNumberFormatOnTyping(amount58, tvFreq58);
        numberFormatOnTyping.setNumberFormatOnTyping(amount59, tvFreq59);
        numberFormatOnTyping.setNumberFormatOnTyping(amount60, tvFreq60);
        numberFormatOnTyping.setNumberFormatOnTyping(amount61, tvFreq61);
        numberFormatOnTyping.setNumberFormatOnTyping(amount62, tvFreq62);
        numberFormatOnTyping.setNumberFormatOnTyping(amount63, tvFreq63);
        numberFormatOnTyping.setNumberFormatOnTyping(amount64, tvFreq64);
        numberFormatOnTyping.setNumberFormatOnTyping(amount65, tvFreq65);
        numberFormatOnTyping.setNumberFormatOnTyping(amount66, tvFreq66);
        numberFormatOnTyping.setNumberFormatOnTyping(amount67, tvFreq67);
        numberFormatOnTyping.setNumberFormatOnTyping(amount68, tvFreq68);
        numberFormatOnTyping.setNumberFormatOnTyping(amount69, tvFreq69);
        numberFormatOnTyping.setNumberFormatOnTyping(amount70, tvFreq70);
        numberFormatOnTyping.setNumberFormatOnTyping(amount71, tvFreq71);
        numberFormatOnTyping.setNumberFormatOnTyping(amount72, tvFreq72);
        numberFormatOnTyping.setNumberFormatOnTyping(amount73, tvFreq73);
        numberFormatOnTyping.setNumberFormatOnTyping(amount74, tvFreq74);
        numberFormatOnTyping.setNumberFormatOnTyping(amount75, tvFreq75);
        numberFormatOnTyping.setNumberFormatOnTyping(amount76, tvFreq76);
        numberFormatOnTyping.setNumberFormatOnTyping(amount77, tvFreq77);
    }

    private void assign() {
        amount1 = findViewById(R.id.amount1);
        amount2 = findViewById(R.id.amount2);
        amount3 = findViewById(R.id.amount3);
        amount4 = findViewById(R.id.amount4);
        amount5 = findViewById(R.id.amount5);
        amount6 = findViewById(R.id.amount6);
        amount7 = findViewById(R.id.amount7);
        amount8 = findViewById(R.id.amount8);
        amount9 = findViewById(R.id.amount9);
        amount10 = findViewById(R.id.amount10);
        amount11 = findViewById(R.id.amount11);
        amount12 = findViewById(R.id.amount12);
        amount13 = findViewById(R.id.amount13);
        amount14 = findViewById(R.id.amount14);
        amount15 = findViewById(R.id.amount15);
        amount16 = findViewById(R.id.amount16);
        amount17 = findViewById(R.id.amount17);
        amount18 = findViewById(R.id.amount18);
        amount19 = findViewById(R.id.amount19);
        amount20 = findViewById(R.id.amount20);
        amount21 = findViewById(R.id.amount21);
        amount22 = findViewById(R.id.amount22);
        amount23 = findViewById(R.id.amount23);
        amount24 = findViewById(R.id.amount24);
        amount25 = findViewById(R.id.amount25);
        amount26 = findViewById(R.id.amount26);
        amount27 = findViewById(R.id.amount27);
        amount28 = findViewById(R.id.amount28);
        amount29 = findViewById(R.id.amount29);
        amount30 = findViewById(R.id.amount30);
        amount31 = findViewById(R.id.amount31);
        amount32 = findViewById(R.id.amount32);
        amount33 = findViewById(R.id.amount33);
        amount34 = findViewById(R.id.amount34);
        amount35 = findViewById(R.id.amount35);
        amount36 = findViewById(R.id.amount36);
        amount37 = findViewById(R.id.amount37);
        amount38 = findViewById(R.id.amount38);
        amount39 = findViewById(R.id.amount39);
        amount40 = findViewById(R.id.amount40);
        amount41 = findViewById(R.id.amount41);
        amount42 = findViewById(R.id.amount42);
        amount43 = findViewById(R.id.amount43);
        amount44 = findViewById(R.id.amount44);
        amount45 = findViewById(R.id.amount45);
        amount46 = findViewById(R.id.amount46);
        amount47 = findViewById(R.id.amount47);
        amount48 = findViewById(R.id.amount48);
        amount49 = findViewById(R.id.amount49);
        amount50 = findViewById(R.id.amount50);
        amount51 = findViewById(R.id.amount51);
        amount52 = findViewById(R.id.amount52);
        amount53 = findViewById(R.id.amount53);
        amount54 = findViewById(R.id.amount54);
        amount55 = findViewById(R.id.amount55);
        amount56 = findViewById(R.id.amount56);
        amount57 = findViewById(R.id.amount57);
        amount58 = findViewById(R.id.amount58);
        amount59 = findViewById(R.id.amount59);
        amount60 = findViewById(R.id.amount60);
        amount61 = findViewById(R.id.amount61);
        amount62 = findViewById(R.id.amount62);
        amount63 = findViewById(R.id.amount63);
        amount64 = findViewById(R.id.amount64);
        amount65 = findViewById(R.id.amount65);
        amount66 = findViewById(R.id.amount66);
        amount67 = findViewById(R.id.amount67);
        amount68 = findViewById(R.id.amount68);
        amount69 = findViewById(R.id.amount69);
        amount70 = findViewById(R.id.amount70);
        amount71 = findViewById(R.id.amount71);
        amount72 = findViewById(R.id.amount72);
        amount73 = findViewById(R.id.amount73);
        amount74 = findViewById(R.id.amount74);
        amount75 = findViewById(R.id.amount75);
        amount76 = findViewById(R.id.amount76);
        amount77 = findViewById(R.id.amount77);

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
        tvFreq55 = findViewById(R.id.tvFreq55);
        tvFreq56 = findViewById(R.id.tvFreq56);
        tvFreq57 = findViewById(R.id.tvFreq57);
        tvFreq58 = findViewById(R.id.tvFreq58);
        tvFreq59 = findViewById(R.id.tvFreq59);
        tvFreq60 = findViewById(R.id.tvFreq60);
        tvFreq61 = findViewById(R.id.tvFreq61);
        tvFreq62 = findViewById(R.id.tvFreq62);
        tvFreq63 = findViewById(R.id.tvFreq63);
        tvFreq64 = findViewById(R.id.tvFreq64);
        tvFreq65 = findViewById(R.id.tvFreq65);
        tvFreq66 = findViewById(R.id.tvFreq66);
        tvFreq67 = findViewById(R.id.tvFreq67);
        tvFreq68 = findViewById(R.id.tvFreq68);
        tvFreq69 = findViewById(R.id.tvFreq69);
        tvFreq70 = findViewById(R.id.tvFreq70);
        tvFreq71 = findViewById(R.id.tvFreq71);
        tvFreq72 = findViewById(R.id.tvFreq72);
        tvFreq73 = findViewById(R.id.tvFreq73);
        tvFreq74 = findViewById(R.id.tvFreq74);
        tvFreq75 = findViewById(R.id.tvFreq75);
        tvFreq76 = findViewById(R.id.tvFreq76);
        tvFreq77 = findViewById(R.id.tvFreq77);

//
//        imFreq1 = findViewById(R.id.imFreq1);
//        imFreq2 = findViewById(R.id.imFreq2);
//        imFreq3 = findViewById(R.id.imFreq3);
//        imFreq4 = findViewById(R.id.imFreq4);
//        imFreq5 = findViewById(R.id.imFreq5);
//        imFreq6 = findViewById(R.id.imFreq6);
//        imFreq7 = findViewById(R.id.imFreq7);
//        imFreq8 = findViewById(R.id.imFreq8);
//        imFreq9 = findViewById(R.id.imFreq9);
//        imFreq10 = findViewById(R.id.imFreq10);
//        imFreq11 = findViewById(R.id.imFreq11);
//        imFreq12 = findViewById(R.id.imFreq12);
//        imFreq13 = findViewById(R.id.imFreq13);
//        imFreq14 = findViewById(R.id.imFreq14);
//        imFreq15 = findViewById(R.id.imFreq15);
//        imFreq16 = findViewById(R.id.imFreq16);
//        imFreq17 = findViewById(R.id.imFreq17);
//        imFreq18 = findViewById(R.id.imFreq18);
//        imFreq19 = findViewById(R.id.imFreq19);
//        imFreq20 = findViewById(R.id.imFreq20);
//        imFreq21 = findViewById(R.id.imFreq21);
//        imFreq22 = findViewById(R.id.imFreq22);
//        imFreq23 = findViewById(R.id.imFreq23);
//        imFreq24 = findViewById(R.id.imFreq24);
//        imFreq25 = findViewById(R.id.imFreq25);
//        imFreq26 = findViewById(R.id.imFreq26);
//        imFreq27 = findViewById(R.id.imFreq27);
//        imFreq28 = findViewById(R.id.imFreq28);
//        imFreq29 = findViewById(R.id.imFreq29);
//        imFreq30 = findViewById(R.id.imFreq30);
//        imFreq31 = findViewById(R.id.imFreq31);
//        imFreq32 = findViewById(R.id.imFreq32);
//        imFreq33 = findViewById(R.id.imFreq33);
//        imFreq34 = findViewById(R.id.imFreq34);
//        imFreq35 = findViewById(R.id.imFreq35);
//        imFreq36 = findViewById(R.id.imFreq36);
//        imFreq37 = findViewById(R.id.imFreq37);
//        imFreq38 = findViewById(R.id.imFreq38);
//        imFreq39 = findViewById(R.id.imFreq39);
//        imFreq40 = findViewById(R.id.imFreq40);
//        imFreq41 = findViewById(R.id.imFreq41);
//        imFreq42 = findViewById(R.id.imFreq42);
//        imFreq43 = findViewById(R.id.imFreq43);
//        imFreq44 = findViewById(R.id.imFreq44);
//        imFreq45 = findViewById(R.id.imFreq45);
//        imFreq46 = findViewById(R.id.imFreq46);
//        imFreq47 = findViewById(R.id.imFreq47);
//        imFreq48 = findViewById(R.id.imFreq48);
//        imFreq49 = findViewById(R.id.imFreq49);
//        imFreq50 = findViewById(R.id.imFreq50);
//        imFreq51 = findViewById(R.id.imFreq51);
//        imFreq52 = findViewById(R.id.imFreq52);
//        imFreq53 = findViewById(R.id.imFreq53);
//        imFreq54 = findViewById(R.id.imFreq54);
//        imFreq55 = findViewById(R.id.imFreq55);
//        imFreq56 = findViewById(R.id.imFreq56);
//        imFreq57 = findViewById(R.id.imFreq57);
//        imFreq58 = findViewById(R.id.imFreq58);
//        imFreq59 = findViewById(R.id.imFreq59);
//        imFreq60 = findViewById(R.id.imFreq60);
//        imFreq61 = findViewById(R.id.imFreq61);
//        imFreq62 = findViewById(R.id.imFreq62);
//        imFreq63 = findViewById(R.id.imFreq63);
//        imFreq64 = findViewById(R.id.imFreq64);
//        imFreq65 = findViewById(R.id.imFreq65);
//        imFreq66 = findViewById(R.id.imFreq66);
//        imFreq67 = findViewById(R.id.imFreq67);
//        imFreq68 = findViewById(R.id.imFreq68);
//        imFreq69 = findViewById(R.id.imFreq69);

    }

    private void addInflow() {

        pDialog.setMessage("Adding outflows ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        // url to post our data
        String url = "https://mwalimubiashara.com/app/post_outflow.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(AddOutflowActivity.this);

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
                    Toast.makeText(AddOutflowActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    finish();
                } catch (JSONException e) {
                    pDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                pDialog.dismiss();
                Toast.makeText(AddOutflowActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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


                params.put("email", email);
                params.put("outflow_cat1", "A. Day to Day");
                params.put("outflow_line1", "Shelter/Rent ");
                params.put("amount1", amount1.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency1", tvFreq1.getText().toString());
                params.put("outflow_cat2", "A. Day to Day");
                params.put("outflow_line2", "Utilities (elec water gas wi-fi) ");
                params.put("amount2", amount2.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency2", tvFreq2.getText().toString());
                params.put("outflow_cat3", "A. Day to Day");
                params.put("outflow_line3", "Groceries/Shopping - needs");
                params.put("amount3", amount3.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency3", tvFreq3.getText().toString());
                params.put("outflow_cat4", "A. Day to Day");
                params.put("outflow_line4", "Groceries/Shopping - wants ");
                params.put("amount4", amount4.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency4", tvFreq4.getText().toString());
                params.put("outflow_cat5", "A. Day to Day");
                params.put("outflow_line5", "Groceries/Shopping - weekly supplies");
                params.put("amount5", amount5.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency5", tvFreq5.getText().toString());
                params.put("outflow_cat6", "A. Day to Day");
                params.put("outflow_line6", "Health (outside insurance)");
                params.put("amount6", amount6.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency6", tvFreq6.getText().toString());
                params.put("outflow_cat7", "A. Day to Day");
                params.put("outflow_line7", "Househelp");
                params.put("amount7", amount7.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency7", tvFreq7.getText().toString());
                params.put("outflow_cat8", "A. Day to Day");
                params.put("outflow_line8", "Laundry");
                params.put("amount8", amount8.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency8", tvFreq8.getText().toString());
                params.put("outflow_cat9", "A. Day to Day");
                params.put("outflow_line9", "Bank/Mpesa Charges");
                params.put("amount9", amount9.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency9", tvFreq9.getText().toString());
                params.put("outflow_cat10", "B. Faith, Giving and Others");
                params.put("outflow_line10", "Faith giving");
                params.put("amount10", amount10.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency10", tvFreq10.getText().toString());
                params.put("outflow_cat11", "B. Faith, Giving and Others");
                params.put("outflow_line11", "My parents and siblings ");
                params.put("amount11", amount11.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency11", tvFreq11.getText().toString());
                params.put("outflow_cat12", "B. Faith, Giving and Others");
                params.put("outflow_line12", "Spouses parents and siblings ");
                params.put("amount12", amount12.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency12", tvFreq12.getText().toString());
                params.put("outflow_cat13", "B. Faith, Giving and Others");
                params.put("outflow_line13", "Friends ");
                params.put("amount13", amount13.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency13", tvFreq13.getText().toString());
                params.put("outflow_cat14", "C. Saving(Pay yourself first)");
                params.put("outflow_line14", "Emergency Fund ");
                params.put("amount14", amount14.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency14", tvFreq14.getText().toString());
                params.put("outflow_cat15", "C. Saving(Pay yourself first)");
                params.put("outflow_line15", "SACCO");
                params.put("amount15", amount15.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency15", tvFreq15.getText().toString());
                params.put("outflow_cat16", "C. Saving(Pay yourself first)");
                params.put("outflow_line16", "Merry-go-round ");
                params.put("amount16", amount16.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency16", tvFreq16.getText().toString());
                params.put("outflow_cat17", "C. Saving(Pay yourself first)");
                params.put("outflow_line17", "Chamaas/Investment groups ");
                params.put("amount17", amount17.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency17", tvFreq17.getText().toString());
                params.put("outflow_cat18", "C. Saving(Pay yourself first)");
                params.put("outflow_line18", "Family Inheritance");
                params.put("amount18", amount18.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency18", tvFreq18.getText().toString());
                params.put("outflow_cat19", "C. Saving(Pay yourself first)");
                params.put("outflow_line19", "Pension (Employee) ");
                params.put("amount19", amount19.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency19", tvFreq19.getText().toString());
                params.put("outflow_cat20", "C. Saving(Pay yourself first)");
                params.put("outflow_line20", "Pension (Employee) - Spouse ");
                params.put("amount20", amount20.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency20", tvFreq20.getText().toString());
                params.put("outflow_cat21", "D. Self");
                params.put("outflow_line21", "Personal allowance");
                params.put("amount21", amount21.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency21", tvFreq21.getText().toString());
                params.put("outflow_cat22", "D. Self");
                params.put("outflow_line22", "Exposure");
                params.put("amount22", amount22.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency22", tvFreq22.getText().toString());
                params.put("outflow_cat23", "D. Self");
                params.put("outflow_line23", "Entertainment");
                params.put("amount23", amount23.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency23", tvFreq23.getText().toString());
                params.put("outflow_cat24", "D. Self");
                params.put("outflow_line24", "Gym and Fitness");
                params.put("amount24", amount24.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency24", tvFreq24.getText().toString());
                params.put("outflow_cat25", "D. Self");
                params.put("outflow_line25", "Clubs ");
                params.put("amount25", amount25.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency25", tvFreq25.getText().toString());
                params.put("outflow_cat26", "D. Self");
                params.put("outflow_line26", "Grooming");
                params.put("amount26", amount26.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency26", tvFreq26.getText().toString());
                params.put("outflow_cat27", "D. Self");
                params.put("outflow_line27", "Holiday/Travel");
                params.put("amount27", amount27.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency27", tvFreq27.getText().toString());
                params.put("outflow_cat28", "D. Self");
                params.put("outflow_line28", "Other Self");
                params.put("amount28", amount28.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency28", tvFreq28.getText().toString());
                params.put("outflow_cat29", "E. Investment");
                params.put("outflow_line29", "ST- Fixed deposits");
                params.put("amount29", amount29.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency29", tvFreq29.getText().toString());
                params.put("outflow_cat30", "E. Investment");
                params.put("outflow_line30", "ST- Unit trusts - money market fund");
                params.put("amount30", amount30.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency30", tvFreq30.getText().toString());
                params.put("outflow_cat31", "E. Investment");
                params.put("outflow_line31", "ST- Unit trusts - fixed income fund");
                params.put("amount31", amount31.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency31", tvFreq31.getText().toString());
                params.put("outflow_cat32", "E. Investment");
                params.put("outflow_line32", "MT- Unit trusts - balanced fund");
                params.put("amount32", amount32.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency32", tvFreq32.getText().toString());
                params.put("outflow_cat33", "E. Investment");
                params.put("outflow_line33", "LT- Unit trusts - equity fund");
                params.put("amount33", amount33.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency33", tvFreq33.getText().toString());
                params.put("outflow_cat34", "E. Investment");
                params.put("outflow_line34", "ST- Fixed Income - Treasury Bills");
                params.put("amount34", amount34.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency34", tvFreq34.getText().toString());
                params.put("outflow_cat35", "E. Investment");
                params.put("outflow_line35", "MT- Fixed Income - Treasury Bonds");
                params.put("amount35", amount35.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency35", tvFreq35.getText().toString());
                params.put("outflow_cat36", "E. Investment");
                params.put("outflow_line36", "LT- Fixed Income - Infrastructure Bonds");
                params.put("amount36", amount36.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency36", tvFreq36.getText().toString());
                params.put("outflow_cat37", "E. Investment");
                params.put("outflow_line37", "ST- Fixed Income - Commercial Paper");
                params.put("amount37", amount37.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency37", tvFreq37.getText().toString());
                params.put("outflow_cat38", "E. Investment");
                params.put("outflow_line38", "MT- Fixed Income - Corporate Bonds");
                params.put("amount38", amount38.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency38", tvFreq38.getText().toString());
                params.put("outflow_cat39", "E. Investment");
                params.put("outflow_line39", "LT- Properties - Residential");
                params.put("amount39", amount39.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency39", tvFreq39.getText().toString());
                params.put("outflow_cat40", "E. Investment");
                params.put("outflow_line40", "LT- Properties - Commercial");
                params.put("amount40", amount40.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency40", tvFreq40.getText().toString());
                params.put("outflow_cat41", "E. Investment");
                params.put("outflow_line41", "LT- REIT - Development");
                params.put("amount41", amount41.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency41", tvFreq41.getText().toString());
                params.put("outflow_cat42", "E. Investment");
                params.put("outflow_line42", "MT- REIT - Rental");
                params.put("amount42", amount42.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency42", tvFreq42.getText().toString());
                params.put("outflow_cat43", "E. Investment");
                params.put("outflow_line43", "LT- Listed businesses - equities");
                params.put("amount43", amount43.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency43", tvFreq43.getText().toString());
                params.put("outflow_cat44", "E. Investment");
                params.put("outflow_line44", "MT- Unlisted businesses - SME");
                params.put("amount44", amount44.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency44", tvFreq44.getText().toString());
                params.put("outflow_cat45", "E. Investment");
                params.put("outflow_line45", "MT- Unlisted businesses - Micro ");
                params.put("amount45", amount45.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency45", tvFreq45.getText().toString());
                params.put("outflow_cat46", "E. Investment");
                params.put("outflow_line46", "MT- Unlisted businesses - Start ups");
                params.put("amount46", amount46.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency46", tvFreq46.getText().toString());
                params.put("outflow_cat47", "E. Investment");
                params.put("outflow_line47", "MT- Blockchain /De-fi");
                params.put("amount47", amount47.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency47", tvFreq47.getText().toString());
                params.put("outflow_cat48", "E. Investment");
                params.put("outflow_line48", "MT- Forex trading");
                params.put("amount48", amount48.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency48", tvFreq48.getText().toString());
                params.put("outflow_cat49", "E. Investment");
                params.put("outflow_line49", "MT- Other investments");
                params.put("amount49", amount49.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency49", tvFreq49.getText().toString());
                params.put("outflow_cat50", "F. Dependents");
                params.put("outflow_line50", "School fees(Tution) ");
                params.put("amount50", amount50.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency50", tvFreq50.getText().toString());
                params.put("outflow_cat51", "F. Dependents");
                params.put("outflow_line51", "Others (uniform, books, trips)");
                params.put("amount51", amount51.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency51", tvFreq51.getText().toString());
                params.put("outflow_cat52", "F. Dependents");
                params.put("outflow_line52", "Dependents expenses");
                params.put("amount52", amount52.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency52", tvFreq52.getText().toString());
                params.put("outflow_cat53", "F. Dependents");
                params.put("outflow_line53", "Health");
                params.put("amount53", amount53.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency53", tvFreq53.getText().toString());
                params.put("outflow_cat54", "F. Dependents");
                params.put("outflow_line54", "Talent Exposure ");
                params.put("amount54", amount54.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency54", tvFreq54.getText().toString());
                params.put("outflow_cat55", "F. Dependents");
                params.put("outflow_line55", "Other dependents expenses");
                params.put("amount55", amount55.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency55", tvFreq55.getText().toString());
                params.put("outflow_cat56", "G. Mobility");
                params.put("outflow_line56", "Car fuel");
                params.put("amount56", amount56.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency56", tvFreq56.getText().toString());
                params.put("outflow_cat57", "G. Mobility");
                params.put("outflow_line57", "Car maintenance");
                params.put("amount57", amount57.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency57", tvFreq57.getText().toString());
                params.put("outflow_cat58", "G. Mobility");
                params.put("outflow_line58", "Car insurance");
                params.put("amount58", amount58.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency58", tvFreq58.getText().toString());
                params.put("outflow_cat59", "G. Mobility");
                params.put("outflow_line59", "Taxi/matatu/motorbike");
                params.put("amount59", amount59.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency59", tvFreq59.getText().toString());
                params.put("outflow_cat60", "G. Mobility");
                params.put("outflow_line60", "Vehicle class");
                params.put("amount60", amount60.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency60", tvFreq60.getText().toString());
                params.put("outflow_cat61", "G. Mobility");
                params.put("outflow_line61", "Other mobility ");
                params.put("amount61", amount61.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency61", tvFreq61.getText().toString());
                params.put("outflow_cat62", "H. Protection");
                params.put("outflow_line62", "Insurance - life");
                params.put("amount62", amount62.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency62", tvFreq62.getText().toString());
                params.put("outflow_cat63", "H. Protection");
                params.put("outflow_line63", "Insurance - health ");
                params.put("amount63", amount63.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency63", tvFreq63.getText().toString());
                params.put("outflow_cat64", "H. Protection");
                params.put("outflow_line64", "Insurance - education (over 10 yrs)");
                params.put("amount64", amount64.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency64", tvFreq64.getText().toString());
                params.put("outflow_cat65", "H. Protection");
                params.put("outflow_line65", "Insurance - education (below 10 yrs)");
                params.put("amount65", amount65.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency65", tvFreq65.getText().toString());
                params.put("outflow_cat66", "H. Protection");
                params.put("outflow_line66", "Insurance - domestic package ");
                params.put("amount66", amount66.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency66", tvFreq66.getText().toString());
                params.put("outflow_cat67", "H. Protection");
                params.put("outflow_line67", "Estate planning");
                params.put("amount67", amount67.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency67", tvFreq67.getText().toString());
                params.put("outflow_cat68", "H. Protection");
                params.put("outflow_line68", "Other protection");
                params.put("amount68", amount68.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency68", tvFreq68.getText().toString());
                params.put("outflow_cat69", "I. Other Outflows");
                params.put("outflow_line69", "Other outflows");
                params.put("amount69", amount69.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency69", tvFreq69.getText().toString());
                params.put("outflow_cat70", "A. Day to Day");
                params.put("outflow_line70", "Miscellaneous");
                params.put("amount70", amount70.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency70", tvFreq70.getText().toString());
                params.put("outflow_cat71", "B. Faith, Giving and Others");
                params.put("outflow_line71", "Social obligations (michango)");
                params.put("amount71", amount71.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency71", tvFreq71.getText().toString());
                params.put("outflow_cat72", "C. Saving(Pay yourself first)");
                params.put("outflow_line72", "Pension Extra (IPP)");
                params.put("amount72", amount72.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency72", tvFreq72.getText().toString());
                params.put("outflow_cat73", "C. Saving(Pay yourself first)");
                params.put("outflow_line73", "Pension Extra (IPP) - Spouse");
                params.put("amount73", amount73.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency73", tvFreq73.getText().toString());
                params.put("outflow_cat74", "C. Saving(Pay yourself first)");
                params.put("outflow_line74", "Post retirement medical cover");
                params.put("amount74", amount74.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency74", tvFreq74.getText().toString());
                params.put("outflow_cat75", "C. Saving(Pay yourself first)");
                params.put("outflow_line75", "Post retirement medical cover - Spouse");
                params.put("amount75", amount75.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency75", tvFreq75.getText().toString());
                params.put("outflow_cat76", "C. Saving(Pay yourself first)");
                params.put("outflow_line76", "Insurance (with savings)");
                params.put("amount76", amount76.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency76", tvFreq76.getText().toString());
                params.put("outflow_cat77", "C. Saving(Pay yourself first)");
                params.put("outflow_line77", "Insurance (with savings) - Spouse");
                params.put("amount77", amount77.getText().toString().replaceAll(",", ""));
                params.put("outflow_frequency77", tvFreq77.getText().toString());


                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}