package com.mb.kibeti.smsReaderAutoTask.requests

data class ConfirmAllocatedTransactionsRequestBody (
    val action : String,
    val date : String,
    val email : String
)
