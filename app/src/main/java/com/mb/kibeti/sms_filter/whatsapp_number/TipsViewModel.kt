package com.mb.kibeti.sms_filter.whatsapp_number

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class TipsViewModel : ViewModel() {
    private val _apiResponse = MutableLiveData<Resource<JoinCommunityResponse>>()
    val apiResponse: LiveData<Resource<JoinCommunityResponse>> = _apiResponse
    private var currentRequest: Job? = null

    fun joinCommunity(email:String?,phoneNumber: String) {
        currentRequest?.cancel()
        currentRequest = viewModelScope.launch {
            _apiResponse.value = Resource.Loading()
            try {
                val response = withTimeout(30_000) {
                    RetrofitClient.instance.joinCommunity(
                        JoinCommunityRequest(email = email, phone = phoneNumber)
                    )
                }

                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        when {
                            body.isSuccessfulOperation() -> {
                                _apiResponse.value = Resource.Success(body)
                            }
                            body.isAlreadyRegistered() -> {
                                // Treat as success but with special flag
                                _apiResponse.value = Resource.Success(
                                    body.copy(message = "Already registered")
                                )
                            }
                            else -> {
                                _apiResponse.value = Resource.Error(
                                    body.message ?: "Operation completed with warnings"
                                )
                            }
                        }
                    } ?: run {
                        _apiResponse.value = Resource.Error("Empty response from server")
                    }
                } else {
                    _apiResponse.value = Resource.Error(
                        "Server error: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                _apiResponse.value = Resource.Error(
                    "Network error: ${e.localizedMessage ?: "Unknown error"}"
                )
            }
        }
    }
}