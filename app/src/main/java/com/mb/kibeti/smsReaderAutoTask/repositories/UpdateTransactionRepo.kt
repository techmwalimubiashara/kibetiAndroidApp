package com.mb.kibeti.smsReaderAutoTask.repositories

import com.mb.kibeti.retrofit_package.RetrofitClient
import com.mb.kibeti.smsReaderAutoTask.requests.UpdateTransactionRequestBody
import com.mb.kibeti.smsReaderAutoTask.responses.UpdateTransactionApiResponse
import retrofit2.Response

class UpdateTransactionRepo(val action : String, val amount : Int, val date:String, val outflow_id: Int, val tran_code: String) {
    val retrofit = RetrofitClient.apiService
    suspend fun updateTransaction( action : String, amount : Int,  date:String,  outflow_id: Int,  tran_code: String): Response<UpdateTransactionApiResponse>{

        val requestBody = UpdateTransactionRequestBody(action, amount,date,outflow_id, tran_code)
        return retrofit.updateTransaction(requestBody)
    }
}