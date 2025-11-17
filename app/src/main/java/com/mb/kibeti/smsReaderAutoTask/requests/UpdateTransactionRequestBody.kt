package com.mb.kibeti.smsReaderAutoTask.requests

data class UpdateTransactionRequestBody(
    val action: String,
    val amount: Int,
    val date: String,
    val outflow_id: Int,
    val trans_code: String
)