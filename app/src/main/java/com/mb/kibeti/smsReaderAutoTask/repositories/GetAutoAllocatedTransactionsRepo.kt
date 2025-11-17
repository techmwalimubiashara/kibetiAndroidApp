package com.mb.kibeti.smsReaderAutoTask.repositories

import android.util.Log
import com.mb.kibeti.retrofit_package.RetrofitClient
import com.mb.kibeti.smsReaderAutoTask.requests.GetAutoAllocatedTransactionsRequestBody
import com.mb.kibeti.smsReaderAutoTask.responses.GetAutoAllocatedTransactionsApiResponse
import retrofit2.Response

class GetAutoAllocatedTransactionsRepo(private val action : String, private val date : String, private val email : String) {
    val retrofit = RetrofitClient.apiService
    suspend fun getAutoAllocatedTransactionsInsideRepo(): Response<GetAutoAllocatedTransactionsApiResponse>{
        Log.d("hittingEndpoint", "started to hit endpoint")
        val requestBody = GetAutoAllocatedTransactionsRequestBody(action, date,email)
        Log.d("requesting body", requestBody.toString())
        return retrofit.getAutoAllocatedTransactions(requestBody)


    }

}