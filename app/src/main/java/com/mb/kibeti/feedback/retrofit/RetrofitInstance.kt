package com.mb.kibeti.feedback.retrofit

import com.mb.kibeti.feedback.utils.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import com.mb.kibeti.currency_picker.ApiService


object RetrofitInstance {
    val gson = GsonBuilder()
        .setLenient()
        .create()
//    val client = OkHttpClient.Builder()
//        .addInterceptor(JsonPreprocessingInterceptor())
//        .build()

    val api: FeedbackApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
//            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(FeedbackApi::class.java)
    }

    val currencyApi: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
//            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}