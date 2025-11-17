package com.mb.kibeti.sms_filter.whatsapp_number

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonDeserializationContext
import java.lang.reflect.Type
import com.mb.kibeti.Constants
//import com.mb.kibeti.BuildConfig

object RetrofitClient {
        private const val TIMEOUT = 30L

        private val gson = GsonBuilder()
            .registerTypeAdapter(JoinCommunityResponse::class.java, JoinCommunityResponseDeserializer())
            .create()

        val instance: ApiService by lazy {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
//                    level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                    level = if (true) HttpLoggingInterceptor.Level.BODY
                    else HttpLoggingInterceptor.Level.NONE
                })
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build()

            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(ApiService::class.java)
        }
    }

    class JoinCommunityResponseDeserializer : JsonDeserializer<JoinCommunityResponse> {
        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): JoinCommunityResponse {
            return try {
                when {
                    json.isJsonObject -> {
                        val jsonObject = json.asJsonObject
                        JoinCommunityResponse(
                            status = jsonObject.get("status")?.asInt ?: 200,
                            success = jsonObject.get("success")?.asString ?: "1",
                            message = jsonObject.get("message")?.asString ?: "Operation completed"
                        )
                    }
                    json.isJsonPrimitive && json.asJsonPrimitive.isString -> {
                        // Handle case where response is just a string
                        JoinCommunityResponse(message = json.asString)
                    }
                    else -> {
                        JoinCommunityResponse()
                    }
                }
            } catch (e: Exception) {
                JoinCommunityResponse()
            }
        }
    }