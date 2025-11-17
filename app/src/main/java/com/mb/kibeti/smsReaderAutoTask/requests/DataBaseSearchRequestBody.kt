package com.mb.kibeti.smsReaderAutoTask.requests

data class DataBaseSearchRequestBody(
    val action: String,
    val search_cat: String,
    val search_name: String,
    val email: String
)