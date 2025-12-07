package com.mb.kibeti.coupon.responses

data class Transaction(
    val amount: String,
    val created_at: String,
    val from_user_id: Int,
    val id: Int,
    val level: Int,
    val updated_at: String,
    val user_id: Int
)