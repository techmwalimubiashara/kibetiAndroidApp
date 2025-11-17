package com.mb.kibeti;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("/app/actual_cashflow.php")
    Call<List<DataModel>> getData();

    @GET("app/inputs_count.php")
    Call<Integer> getInputCount();

    @POST("app/inputs_submit.php")
    Call<Void> submitInputs(@Body List<String> values);
}