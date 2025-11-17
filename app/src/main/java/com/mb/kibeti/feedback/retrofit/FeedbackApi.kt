package com.mb.kibeti.feedback.retrofit

import com.mb.kibeti.feedback.models.FeedbackRequest
import com.mb.kibeti.feedback.models.FeedbackResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FeedbackApi {

    @POST("feedback.php")
    suspend fun postFeedback(
        @Body feedbackRequest: FeedbackRequest
    ): Response<FeedbackResponse>


}