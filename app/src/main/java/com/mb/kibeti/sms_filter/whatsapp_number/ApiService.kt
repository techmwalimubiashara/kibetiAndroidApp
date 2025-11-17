package com.mb.kibeti.sms_filter.whatsapp_number

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.mb.kibeti.Constants

interface ApiService {
    @POST("join_community.php")
    suspend fun joinCommunity(
        @Body request: JoinCommunityRequest
    ): Response<JoinCommunityResponse>
}

data class JoinCommunityRequest(
    val action: String = "join_community",
    val email: String?,
    val phone: String
)

data class JoinCommunityResponse(
    val status: Int = 200,
    val success: String = "1",
    val message: String = "Operation completed"
) {
    fun isSuccessfulOperation(): Boolean {
        return success == "1" && (message.contains("success", true)
                || message.contains("updated", true)
                || message.contains("sent", true))
    }

    fun isAlreadyRegistered(): Boolean {
        return success == "1" && message.contains("no records", true)
    }
}