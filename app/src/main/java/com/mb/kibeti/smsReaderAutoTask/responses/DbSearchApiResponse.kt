package com.mb.kibeti.smsReaderAutoTask.responses

data class DbSearchApiResponse(
    val `data`: List<DataX>,
    val outflow_id: String,
    val response: String
)