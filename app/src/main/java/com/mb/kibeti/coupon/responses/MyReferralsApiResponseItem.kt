package com.mb.kibeti.coupon.responses

data class MyReferralsApiResponseItem(
    val alias: String,
    val created_at: String,
    val email: String,
    val name: String,
    val referral_code: String,
    val referral_level: Int,
    val referred_by: String,
    val referred_users: List<Any>,
    val updated_at: String,
    val wallet_balance: String
)