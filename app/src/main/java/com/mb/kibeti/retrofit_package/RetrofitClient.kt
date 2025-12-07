package com.mb.kibeti.retrofit_package

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

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


    private const val BASE_URL_DEV = "https://dev.mwalimubiashara.com/api/"

    // This token should be assigned after successful login
    var accessToken: String? = null

    // Interceptor to add headers including Authorization if token is available
    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Accept", "application/json")

        accessToken?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        val request = requestBuilder.build()
        chain.proceed(request)
    }

    // Logging interceptor for network request/response logs
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttpClient with interceptors and timeouts
    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    // Retrofit instance
    private val retrofit_dev: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_DEV)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Your API service interface
//    val apiService: ApiService = retrofit_dev.create(ApiService::class.java)
    val apiService_dev: ApiService = retrofit_dev.create(ApiService::class.java)

}
