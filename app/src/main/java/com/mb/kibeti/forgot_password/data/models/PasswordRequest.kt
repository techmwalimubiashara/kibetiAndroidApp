package com.mb.kibeti.forgot_password.data.models

data class PasswordRequest(
    val email: String,
    val action: String = "set_password",
    val new_password: String
)
