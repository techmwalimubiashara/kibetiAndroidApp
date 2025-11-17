package com.mb.kibeti.reminder

data class Goal(
    val goal_id: String,
    val email: String,
    val goal_name: String,
    val goal_amount: String,
    val daily_amount: String,
    val goal_date: String
)