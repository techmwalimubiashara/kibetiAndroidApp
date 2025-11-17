package com.mb.kibeti.budget.request

data class CurrentBudgetRequestBody(
    val action: String,
    val email: String,
    val month: Int,
    val year: Int
)