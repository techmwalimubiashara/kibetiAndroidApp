package com.mb.kibeti.reminder

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call

interface MpesaApiService {
    @POST("get_mpesa_status.php")
    fun getTransactions(@Body requestBody: MpesaRequestBody): Call<MpesaTransactionResponse>

    @POST("goals.php")
    fun getGoals(@Body requestBody: GoalRequestBody): Call<GoalResponse>

}