package com.mb.kibeti.smsReaderAutoTask.requests

data class GetAutoAllocatedTransactionsRequestBody(
    val action: String,
    val date: String,
    val email: String
)