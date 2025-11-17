package com.mb.kibeti.retrofit_package

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // Define the base URL of the API
    private const val BASE_URL = "https://mwalimubiashara.com/"

    // Create the Retrofit instance
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            // To handle JSON
            .build()
    }

    // Lazy initialization of the API service
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java) // Create the service instance
    }
}
