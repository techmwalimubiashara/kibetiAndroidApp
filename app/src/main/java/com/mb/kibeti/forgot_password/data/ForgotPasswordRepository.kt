package com.mb.kibeti.forgot_password.data

import android.util.Log
import com.mb.kibeti.forgot_password.data.models.PasswordRequest
import com.mb.kibeti.forgot_password.data.models.PasswordResetRequest
import com.mb.kibeti.forgot_password.data.models.PasswordResponse
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import retrofit2.Response

class ForgotPasswordRepository(private val apiService: ApiService) {

    suspend fun sendResetCode(email: String, resetCode: String): Result<String> {
        val request = PasswordResetRequest(email, resetCode)

        return try {
            val response = apiService.sendResetCode(request)

            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d("RAW_RESPONSE", responseBody.toString())

                if (responseBody?.response == "success") {
                    Result.success("Reset code sent successfully")
                } else {
                    Result.failure(Exception("Failed to send reset code."))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("Repository", "Error Body: $errorBody")
                Result.failure(Exception("HTTP Error: ${response.code()}, Message: $errorBody"))
            }
        } catch (e: JsonSyntaxException) {
            Log.e("Repository", "JSON Parsing Error: ${e.message}", e)
            Result.failure(Exception("JSON Parsing Error: ${e.message}"))
        } catch (e: HttpException) {
            Log.e("Repository", "HTTP Exception: ${e.message}", e)
            Result.failure(Exception("HTTP Exception: ${e.message}"))
        } catch (e: Exception) {
            Log.e("Repository", "Unexpected Error: ${e.message}", e)
            Result.failure(Exception("Unexpected Error: ${e.message}"))
        }
    }


    suspend fun setPassword(request: PasswordRequest): Response<PasswordResponse> {
        Log.d("ForgotPasswordRepository", "Requesting setPassword with $request")
        return apiService.setPassword(request)
    }
}