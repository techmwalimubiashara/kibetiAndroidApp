package com.mb.kibeti;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PaymentApi {
    @GET("initiatePayment")
    Call<PaymentResponse> initiatePayment(@Query("amount") double amount);

    @GET("checkPaymentStatus")
    Call<PaymentStatusResponse> checkPaymentStatus(@Query("transactionId") String transactionId);
}