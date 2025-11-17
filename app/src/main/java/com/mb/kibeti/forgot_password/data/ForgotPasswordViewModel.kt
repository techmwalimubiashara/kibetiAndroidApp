package com.mb.kibeti.forgot_password.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mb.kibeti.forgot_password.data.models.PasswordRequest
import com.mb.kibeti.forgot_password.data.models.PasswordUiState
import com.mb.kibeti.forgot_password.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class ForgotPasswordViewModel(private val repository: ForgotPasswordRepository) : ViewModel() {

    private val _emailCheckResult = MutableLiveData<Result<String>>()
    val emailCheckResult: LiveData<Result<String>> get() = _emailCheckResult

    private val _resetCodeVerificationResult = MutableStateFlow<Resource<String>>(Resource.Empty())
    val resetCodeVerificationResult: StateFlow<Resource<String>> get() = _resetCodeVerificationResult

    private val _uiState = MutableStateFlow<PasswordUiState>(PasswordUiState.Idle)
    val uiState: StateFlow<PasswordUiState> = _uiState

    // Function to request password reset by checking email and reset code
    fun sendResetCode(email: String, resetCode: String) {
        viewModelScope.launch {
            try {
                val result = repository.sendResetCode(email, resetCode)

                result.fold(
                    onSuccess = {
                        _emailCheckResult.postValue(Result.success(it))
                    },
                    onFailure = {
                        _emailCheckResult.postValue(Result.failure(it))
                    }
                )
            } catch (e: Exception) {
                Log.e("ForgotPasswordViewModel", "Error sending reset code", e)
                _emailCheckResult.postValue(Result.failure(e))
            }
        }
    }

    fun setPassword(email: String, newPassword: String) {
        Log.d("ForgotPasswordViewModel", "Setting password for email: $email")
        val request = PasswordRequest(email = email, new_password = newPassword)
        _uiState.value = PasswordUiState.Loading

        viewModelScope.launch {
            try {
                val response = repository.setPassword(request)
                Log.d("ForgotPasswordViewModel", "Response: ${response.body()}")

                if (response.isSuccessful && response.body()?.error == false) {
                    Log.d("ForgotPasswordViewModel", "Password set successfully")
                    _uiState.value = PasswordUiState.Success(response.body()!!)
                } else {
                    Log.e("ForgotPasswordViewModel", "Error: ${response.body()?.message}")
                    _uiState.value = PasswordUiState.Error(response.body()?.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                Log.e("ForgotPasswordViewModel", "Exception: ${e.message}")
                _uiState.value = PasswordUiState.Error(e.message ?: "Network error")
            }
        }
    }




}

class ForgotPasswordViewModelFactory(
    private val repository: ForgotPasswordRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java)) {
            return ForgotPasswordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
