package com.mb.kibeti.coupon.repos

import com.mb.kibeti.coupon.requests.MyReferralsRequestBody
import com.mb.kibeti.coupon.requests.MyWalletRequestBody
import com.mb.kibeti.coupon.responses.MyReferralsApiResponse
import com.mb.kibeti.retrofit_package.RetrofitClient
import retrofit2.Response

class ReferralsRepo(val email : String) {
    val retrofit = RetrofitClient.apiService_dev
    suspend fun getMyReferrals() : Response<MyReferralsApiResponse> {


        return retrofit.getMyReferrals(email)
    }
}