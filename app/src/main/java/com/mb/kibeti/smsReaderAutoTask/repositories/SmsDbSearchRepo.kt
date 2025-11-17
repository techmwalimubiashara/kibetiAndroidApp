package com.mb.kibeti.smsReaderAutoTask.repositories

import android.util.Log
import com.mb.kibeti.retrofit_package.RetrofitClient
import com.mb.kibeti.smsReaderAutoTask.requests.DataBaseSearchRequestBody
import com.mb.kibeti.smsReaderAutoTask.responses.DbSearchApiResponse
import retrofit2.Response

class SmsDbSearchRepo(val action : String, val search_cat : String, val search_name: String, val email : String) {
    val retrofit  = RetrofitClient.apiService
    suspend fun performSmsDbSearch(action : String, search_cat: String,search_name: String,email: String): Response<DbSearchApiResponse>{

        val requestBody = DataBaseSearchRequestBody(action, search_cat, search_name, email)
        Log.d("APIRequest", "Request Body: $requestBody")

        return retrofit.performDbSearch(requestBody)
    }
}