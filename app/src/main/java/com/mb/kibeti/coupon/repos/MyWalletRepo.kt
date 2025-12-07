package com.mb.kibeti.coupon.repos

import com.mb.kibeti.coupon.requests.GenerateReferralCodeRequestBody
import com.mb.kibeti.coupon.requests.MyWalletRequestBody
import com.mb.kibeti.coupon.responses.MyWalletApiResponse
import com.mb.kibeti.retrofit_package.RetrofitClient
import retrofit2.Response

class MyWalletRepo(val email : String) {
    val retrofit = RetrofitClient.apiService_dev
    suspend fun getMyWallet(): Response<MyWalletApiResponse> {
        val requestBody = MyWalletRequestBody(email)

        return retrofit.getMyWallet(requestBody)

    }
}