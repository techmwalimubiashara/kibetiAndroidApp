package com.mb.kibeti.smsReaderAutoTask.responses

data class ConfirmAllocatedTransactionsResponse(
    val error: Boolean,
    val message: String,
    val response: String
)