package com.mb.kibeti.feedback.repository

import com.mb.kibeti.feedback.models.FeedbackRequest
import com.mb.kibeti.feedback.models.FeedbackResponse
import com.mb.kibeti.feedback.retrofit.FeedbackApi

class FeedbackRepository(private val api: FeedbackApi) {

    suspend fun postFeedback(feedbackRequest: FeedbackRequest): FeedbackResponse {
        val response = api.postFeedback(feedbackRequest)

        return if (response.isSuccessful) {
            FeedbackResponse("Feedback recieved successfully")

        } else {
            FeedbackResponse("Error: Network Failure - ${response.message()}")
        }
    }
}

