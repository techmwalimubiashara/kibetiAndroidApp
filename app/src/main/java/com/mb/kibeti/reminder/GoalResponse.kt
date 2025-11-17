package com.mb.kibeti.reminder

data class GoalResponse(
    val success: String,
    val error: Boolean,
    val data: List<Goal>?
)