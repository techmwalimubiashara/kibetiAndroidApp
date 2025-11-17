package com.mb.kibeti;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PieChartActivity extends AppCompatActivity {

    private PieChart pieChart;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outflow_chart);

        pieChart = findViewById(R.id.idPieChart);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://mwalimubiashara.com") // Replace with your base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        loadPieChartData();
    }

    private void loadPieChartData() {
        Call<List<DataModel>> call = apiService.getData();
        call.enqueue(new Callback<List<DataModel>>() {
            @Override
            public void onResponse(Call<List<DataModel>> call, Response<List<DataModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DataModel> dataModels = response.body();
                    setupPieChart(dataModels);
                } else {
                    Toast.makeText(PieChartActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DataModel>> call, Throwable t) {
                Toast.makeText(PieChartActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupPieChart(List<DataModel> dataModels) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (DataModel dataModel : dataModels) {
            entries.add(new PieEntry(dataModel.getValue(), dataModel.getLabel()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Data Set");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate(); // refresh
    }
}