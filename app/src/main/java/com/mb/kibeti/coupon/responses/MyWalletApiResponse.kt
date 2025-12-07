package com.mb.kibeti.coupon.responses

data class MyWalletApiResponse(
    val balance: String,
    val transactions: List<Transaction>
)