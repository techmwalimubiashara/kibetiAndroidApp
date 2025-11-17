package com.mb.kibeti.feedback.models

data class FeedbackRequest(
    val email: String,
    val experience: String,
    val category: String,
    val comment: String
)
