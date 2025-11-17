package com.mb.kibeti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class InvestmentRecommendationCalculator extends AppCompatActivity {
    private LinearLayout linearLayoutstep1, linearLayoutstep2, linearLayoutstep3, linearLayoutstep4;
    Button btnNextStep2, btnNextStep3, btnNextStep1, btnNextStep4;
    RadioGroup groupradio, groupradioRisk, groupradioExperience, groupradioTorelance;
    String age_grp, experience, tolerance, appetite;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment_recommendation_calculator);
        initializing();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        groupradio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                btnNextStep1.setEnabled(true);
                btnNextStep1.setBackground(ContextCompat.getDrawable(InvestmentRecommendationCalculator.this, R.drawable.button_rectangle_line));

                            if (checkedId == R.id.radio_18_22) {
                                age_grp = "1";
                            } else if (checkedId == R.id.radio_23_29) {

                                age_grp = "2";
                            } else if (checkedId == R.id.radio_30_34) {

                                age_grp = "3";
                            }
                            else if (checkedId == R.id.radio_35_39) {

                                age_grp = "4";
                            }
                            else if (checkedId == R.id.radio_40_44) {

                                age_grp = "5";
                            }else if (checkedId == R.id.radio_45_49) {

                                age_grp = "6";
                            }
                            else if (checkedId == R.id.radio_50_54) {

                                age_grp = "7";
                            }
                            else if (checkedId == R.id.radio_55_59) {

                                age_grp = "8";
                            }else if (checkedId == R.id.radio_60_64) {

                                age_grp = "9";
                            }else if (checkedId == R.id.radio_over65) {

                                age_grp = "10";
                            }


                Log.e("TAG", "onCheckedChanged: Age group " + age_grp);
            }
        });
        groupradioRisk.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                btnNextStep2.setEnabled(true);
                btnNextStep2.setBackground(ContextCompat.getDrawable(InvestmentRecommendationCalculator.this, R.drawable.button_rectangle_line));


                if (checkedId == R.id.radio_Never) {
                    appetite = "1";
                } else  if (checkedId == R.id.radio_Rarely) {
                    appetite = "2";
                } else  if (checkedId == R.id.radio_Sometimes) {
                    appetite = "3";
                } else  if (checkedId == R.id.radio_Often) {
                    appetite = "4";
                } else  if (checkedId == R.id.radio_Always) {
                    appetite = "5";
                }

                Log.e("TAG", "onCheckedChanged: Appetite " + appetite);
            }
        });
        groupradioTorelance.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                btnNextStep3.setEnabled(true);
                btnNextStep3.setBackground(ContextCompat.getDrawable(InvestmentRecommendationCalculator.this, R.drawable.button_rectangle_line));

                if (checkedId == R.id.radio_never_1) {
                    tolerance = "1";
                } else  if (checkedId == R.id.radio_rarely) {
                    tolerance = "2";
                } else  if (checkedId == R.id.radio_sometimes) {
                    tolerance = "3";
                } else  if (checkedId == R.id.radio_often) {
                    tolerance = "4";
                } else  if (checkedId == R.id.radio_always) {
                    tolerance = "5";
                }

                Log.e("TAG", "onCheckedChanged: Tolerance " + tolerance);
            }
        });
        groupradioExperience.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                btnNextStep4.setEnabled(true);
                btnNextStep4.setBackground(ContextCompat.getDrawable(InvestmentRecommendationCalculator.this, R.drawable.button_rectangle_line));


                if (checkedId == R.id.radio_never_invested) {
                    experience = "1";
                } else  if (checkedId == R.id.radio_beginner) {
                    experience = "2";
                }else if (checkedId == R.id.radio_intermediate) {
                    experience = "3";
                } else  if (checkedId == R.id.radio_advanced) {
                    experience = "4";
                } else if (checkedId == R.id.radio_expert) {
                    experience = "5";
                }

                Log.e("TAG", "onCheckedChanged: Experience " + experience);
            }
        });

    }

    private void initializing() {
        linearLayoutstep1 = findViewById(R.id.layoutStep1);
        linearLayoutstep2 = findViewById(R.id.layoutStep2);
        linearLayoutstep3 = findViewById(R.id.layoutStep3);
        linearLayoutstep4 = findViewById(R.id.layoutStep4);
        backButton = findViewById(R.id.backButton);
        btnNextStep1 = findViewById(R.id.btnNextStep1);
        btnNextStep3 = findViewById(R.id.btnNextStep3);
        btnNextStep2 = findViewById(R.id.btnNextStep2);
        btnNextStep4 = findViewById(R.id.btnNextStep4);
        groupradio = findViewById(R.id.groupradio);
        groupradioRisk = findViewById(R.id.groupradioRisk);
        groupradioTorelance = findViewById(R.id.groupradioTorelance);
        groupradioExperience = findViewById(R.id.groupradioExperience);
    }

    public void startLayoutStep1(View view) {
        linearLayoutstep1.setVisibility(View.VISIBLE);
        linearLayoutstep3.setVisibility(View.GONE);
        linearLayoutstep4.setVisibility(View.GONE);
        linearLayoutstep2.setVisibility(View.GONE);
    }

    public void startLayoutStep2(View view) {
        linearLayoutstep1.setVisibility(View.GONE);
        linearLayoutstep3.setVisibility(View.GONE);
        linearLayoutstep4.setVisibility(View.GONE);
        linearLayoutstep2.setVisibility(View.VISIBLE);
    }

    public void startLayoutStep3(View view) {
        linearLayoutstep1.setVisibility(View.GONE);
        linearLayoutstep3.setVisibility(View.VISIBLE);
        linearLayoutstep4.setVisibility(View.GONE);
        linearLayoutstep2.setVisibility(View.GONE);
    }

    public void startLayoutStep4(View view) {
        linearLayoutstep1.setVisibility(View.GONE);
        linearLayoutstep3.setVisibility(View.GONE);
        linearLayoutstep4.setVisibility(View.VISIBLE);
        linearLayoutstep2.setVisibility(View.GONE);
    }

    public void getInvestmentView(View view) {
        getInvestment();
    }

    public void getInvestment() {
//        Toast.makeText(this, "Workig on it right away", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(InvestmentRecommendationCalculator.this, InvestmentProfile.class);
        intent.putExtra("age_group", age_grp);
        intent.putExtra("appetite", appetite);
        intent.putExtra("experience", experience);
        intent.putExtra("tolerance", tolerance);
        startActivity(intent);
    }
}