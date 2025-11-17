package com.mb.kibeti.budget.repository

import com.mb.kibeti.budget.request.CurrentBudgetRequestBody
import com.mb.kibeti.budget.response.CurrentBudgetResponse
import com.mb.kibeti.retrofit_package.RetrofitClient
import retrofit2.Response

class CurrentBudgetRepo(private val action : String, private val email: String, private val month: Int, private val year: Int) {
    val retrofit = RetrofitClient.apiService
    suspend fun getCurrentBudget():Response<CurrentBudgetResponse>{
        val requestBody = CurrentBudgetRequestBody(action,email,month,year)

        return retrofit.getBudget(requestBody)
    }
}