package com.mb.kibeti.budget.request

data class EditBudgetRequestBody(
    val email: String,
    val action: String,
    val month: Int,
    val year: Int,
    val day: Int,
    val dependents: Int,
    val savings: Int,
    val investments: Int,
    val mobility: Int,
    val giving: Int,
    val self: Int,
    val loan: Int,
    val protection: Int,
    val others: Int


)