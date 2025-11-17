package com.mb.kibeti.currency_picker


import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("currency.php")
    fun postCurrency(
        @Body data: PostCurrency): Call<PostCurrency>


}