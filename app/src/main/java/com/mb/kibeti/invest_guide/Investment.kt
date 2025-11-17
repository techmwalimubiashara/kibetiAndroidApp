package com.mb.kibeti.invest_guide

import java.io.Serializable

data class Investment(
    val name: String,
    val info: String,
    var amount: Float? = null
)
data class InvestmentData(
    val name: String,
    val amount: Float,
    val frequency: Int = 0 // Default value is 0
): Serializable