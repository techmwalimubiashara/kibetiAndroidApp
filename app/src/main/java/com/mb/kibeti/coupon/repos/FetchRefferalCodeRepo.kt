package com.mb.kibeti.coupon.repos

import com.mb.kibeti.coupon.requests.FetchReferralCodeRequestBody
import com.mb.kibeti.coupon.responses.FetchRefferalCodeResponse
import com.mb.kibeti.retrofit_package.RetrofitClient
import retrofit2.Response

class FetchRefferalCodeRepo(val email : String) {
    val retrofit = RetrofitClient.apiService_dev
    suspend fun fetchRefferalCode() : Response<FetchRefferalCodeResponse> {

        return  retrofit.fetchRefferalCode(email)
    }
}