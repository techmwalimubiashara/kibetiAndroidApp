package com.mb.kibeti.coupon.responses

data class User(
    val alias: String,
    val email: String,
    val name: String,
    val referral_code: String,
    val wallet_balance: String
)