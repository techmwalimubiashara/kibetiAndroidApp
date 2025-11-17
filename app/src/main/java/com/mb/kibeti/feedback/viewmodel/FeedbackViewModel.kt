package com.mb.kibeti.feedback.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mb.kibeti.feedback.models.FeedbackRequest
import com.mb.kibeti.feedback.models.FeedbackResponse
import com.mb.kibeti.feedback.repository.FeedbackRepository
import kotlinx.coroutines.launch

class FeedbackViewModel(private val repository: FeedbackRepository) : ViewModel() {

    fun postFeedback(
        email: String,
        experience: String,
        category: String,
        comment: String,
        onSuccess: (FeedbackResponse) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val feedbackRequest = FeedbackRequest(
            email = email,
            experience = experience,
            category = category,
            comment = comment
        )

        viewModelScope.launch {
            try {
                val response = repository.postFeedback(feedbackRequest)
                Log.d("API Response", response.toString())

                if (response.message.contains("successfully", ignoreCase = true)) {
                    onSuccess(response)
                } else {
                    onFailure(response.message)
                }

            } catch (e: Exception) {
                onFailure("Error: ${e.message}")
            }
        }
    }

}


class FeedbackViewModelFactory(private val repository: FeedbackRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FeedbackViewModel::class.java)) {
            return FeedbackViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}