package com.mb.kibeti.forgot_password.data.models

data class PasswordResponse(
    val message: String,
    val pass_reset_confirm: String,
    val error: Boolean
)
