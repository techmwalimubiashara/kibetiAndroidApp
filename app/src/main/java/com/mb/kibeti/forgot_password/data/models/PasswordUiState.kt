package com.mb.kibeti.forgot_password.data.models

sealed class PasswordUiState {
    object Idle : PasswordUiState()
    object Loading : PasswordUiState()
    data class Success(val response: PasswordResponse) : PasswordUiState()
    data class Error(val message: String) : PasswordUiState()
}