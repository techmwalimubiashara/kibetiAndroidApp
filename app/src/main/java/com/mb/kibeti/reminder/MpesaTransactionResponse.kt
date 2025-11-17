package com.mb.kibeti.reminder

data class MpesaTransactionResponse(
    val error: Boolean,
    val response: String,
    val total_amount: String,
    val count: Int,
    val data: List<Transaction>
)