package com.mb.kibeti.reminder

data class MpesaRequestBody(
    val action: String = "get_transactions",
    val email: String,
    val cashflow: String = "outflow"
)