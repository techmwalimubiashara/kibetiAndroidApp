package com.mb.kibeti.coupon.repos

import com.mb.kibeti.coupon.requests.SampleLoginRequest
import com.mb.kibeti.coupon.responses.SampleLoginApiResponse
import com.mb.kibeti.retrofit_package.RetrofitClient
import retrofit2.Response

class SampleLoginRepo(
    private val email: String,
    private val password: String
) {
    val retrofit = RetrofitClient.apiService_dev
    suspend fun login(): Response<SampleLoginApiResponse> {
        val requestBody = SampleLoginRequest(email, password)
        return retrofit.sampleLogin(requestBody)
    }
}