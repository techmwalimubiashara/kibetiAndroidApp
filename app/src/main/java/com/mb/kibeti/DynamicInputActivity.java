package com.mb.kibeti;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DynamicInputActivity extends AppCompatActivity {

    ApiService api;
    LinearLayout container;
    Button btnSubmit;
    List<EditText> dynamicEdits = new ArrayList<>();
    ProgressDialog dialog; // loading indicator
    PipeLines util = new PipeLines();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_input);

        container = findViewById(R.id.editContainer);
        btnSubmit = findViewById(R.id.btnSubmit);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(util.PARENT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(ApiService.class);

        loadInputCount();

        btnSubmit.setOnClickListener(v -> submitToBackend());
    }

    private void loadInputCount() {
        dialog.show();

        api.getInputCount().enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    int count = response.body();
                    createDynamicInputs(count);
                } else {
                    Toast.makeText(DynamicInputActivity.this, "Failed to get input count", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(DynamicInputActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createDynamicInputs(int count) {
        for (int i = 0; i < count; i++) {
            EditText et = new EditText(this);
            et.setHint("Value " + (i + 1));
            container.addView(et);
            dynamicEdits.add(et);
        }
    }

    private void submitToBackend() {
        List<String> values = new ArrayList<>();

        // VALIDATION
        for (int i = 0; i < dynamicEdits.size(); i++) {
            String txt = dynamicEdits.get(i).getText().toString().trim();
            if (txt.isEmpty()) {
                dynamicEdits.get(i).setError("Required");
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return; // stop submit
            }
            values.add(txt);
        }

        dialog.show();

        api.submitInputs(values).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(DynamicInputActivity.this, "Submitted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DynamicInputActivity.this, "Submit error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(DynamicInputActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
