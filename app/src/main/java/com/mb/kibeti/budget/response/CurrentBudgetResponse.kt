package com.mb.kibeti.budget.response

data class CurrentBudgetResponse(
    val `data`: List<Data>,
    val inflow_success: String,
    val outflow: List<Outflow>,
    val outflow_success: String,
    val total_inflow: String,
    val total_outflow: String
)