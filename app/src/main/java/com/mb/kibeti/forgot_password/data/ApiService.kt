package com.mb.kibeti.forgot_password.data

import com.mb.kibeti.forgot_password.data.models.PasswordRequest
import com.mb.kibeti.forgot_password.data.models.PasswordResetRequest
import com.mb.kibeti.forgot_password.data.models.PasswordResetResponse
import com.mb.kibeti.forgot_password.data.models.PasswordResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {


    @POST("app_developer/forgot_password.php")
    suspend fun sendResetCode(@Body request: PasswordResetRequest): Response<PasswordResetResponse>


    @POST("app_developer/forgot_password.php")
    suspend fun setPassword(@Body request: PasswordRequest): Response<PasswordResponse>
}
