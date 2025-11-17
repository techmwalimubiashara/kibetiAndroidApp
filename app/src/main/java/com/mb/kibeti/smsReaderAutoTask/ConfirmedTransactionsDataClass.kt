package com.mb.kibeti.smsReaderAutoTask

data class PendingTransaction(
    val transactionId: String, // Unique ID of the transaction
    val amount: String,        // Amount of money involved in the transaction
    val recipient: String,     // Person or entity receiving the money
    val time: String,
    val category: String       // Category of the transaction (e.g., groceries, rent)
)
