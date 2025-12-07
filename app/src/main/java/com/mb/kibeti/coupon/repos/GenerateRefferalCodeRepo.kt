package com.mb.kibeti.coupon.repos

import com.mb.kibeti.coupon.requests.FetchReferralCodeRequestBody
import com.mb.kibeti.coupon.requests.GenerateReferralCodeRequestBody
import com.mb.kibeti.coupon.responses.GetRefferalCodeResponse
import com.mb.kibeti.retrofit_package.RetrofitClient
import retrofit2.Response

class GenerateRefferalCodeRepo(val email : String) {
    val retrofit = RetrofitClient.apiService_dev
    suspend fun generateCode(): Response<GetRefferalCodeResponse> {
        val requestBody = GenerateReferralCodeRequestBody(email)

        return retrofit.generateRefferalCode(requestBody)
    }
}