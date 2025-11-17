package com.mb.kibeti.reminder

data class GoalRequestBody(
    val action: String = "get_goal",
    val email: String
)