package com.mb.kibeti.reminder

data class Transaction(
    val trans_code: String,
    val amount: String,
    val trans_name: String,
    val trans_date: String,
    val trans_time: String,
    val trans_type: String,
    val update_trans: String
)