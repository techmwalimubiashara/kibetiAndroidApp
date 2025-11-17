package com.mb.kibeti.forgot_password.data.models

data class PasswordResetRequest(
    val email: String,
    val reset_code: String,
    val action: String = "send_mail"
)

data class PasswordResetResponse(
    val response: String,
    val data: List<EmailData>?
)

data class EmailData(
    val email: String,
    val reset_code: String,
    val pass_reset_confirm: String
)