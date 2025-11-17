package com.mb.kibeti.smsReaderAutoTask.repositories

import com.mb.kibeti.retrofit_package.RetrofitClient
import com.mb.kibeti.smsReaderAutoTask.requests.ConfirmAllocatedTransactionsRequestBody
import com.mb.kibeti.smsReaderAutoTask.responses.ConfirmAllocatedTransactionsResponse
import retrofit2.Response

class ConfirmAllocatedTransactionsRepo(val action: String, val date: String, val email: String) {
    val retrofit = RetrofitClient.apiService
    suspend fun confirmAutoAllocatedTransactions():Response<ConfirmAllocatedTransactionsResponse>{
        val requestBody = ConfirmAllocatedTransactionsRequestBody(action, date, email)
        return retrofit.confirmAllocatedTransactions(requestBody )
    }
}