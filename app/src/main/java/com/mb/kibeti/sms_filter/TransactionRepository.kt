package com.mb.kibeti.sms_filter

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.sqlite.db.SimpleSQLiteQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TransactionRepository(private val dao: TransactionDao) {

    fun getRecentTransactions(): LiveData<List<TransactionEntity>> {
        val sixMonthsAgo = Calendar.getInstance().apply {
            add(Calendar.MONTH, -6)
        }.timeInMillis
        return dao.getRecentTransactions(sixMonthsAgo)
    }

    fun getTopRecipients(): LiveData<List<RecipientSummary>> {
        val sixMonthsAgo = Calendar.getInstance().apply {
            add(Calendar.MONTH, -6)
        }.timeInMillis
        return dao.getTopRecipients(sixMonthsAgo)
    }

    suspend fun fetchExistingMpesaTransactions(context: Context) {
        try {
            // Get all already processed SMS IDs
            val alreadyProcessedIds = dao.getAllProcessedSmsIds().toSet()

            val cursor = context.contentResolver.query(
                Uri.parse("content://sms/inbox"),
                arrayOf("_id", "body", "date"),  // Include _id in projection
                "body LIKE ? AND date >= ?",
                arrayOf(
                    "%MPESA%",
                    (System.currentTimeMillis() - (6L * 30 * 24 * 60 * 60 * 1000)).toString()
                ),
                "date DESC"
            ) ?: return

            cursor.use {
                while (it.moveToNext()) {
                    val smsId = it.getLong(it.getColumnIndexOrThrow("_id"))

                    // Skip already processed messages
                    if (smsId in alreadyProcessedIds) continue

                    val body = it.getString(it.getColumnIndexOrThrow("body"))
                    val date = it.getLong(it.getColumnIndexOrThrow("date"))

                    parseMpesaMessage(body)?.let { transaction ->
                        dao.insertTransaction(
                            transaction.copy(
                                timestamp = date,
                                smsId = smsId  // Store the SMS ID
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            // Handle error appropriately
            e.printStackTrace()
        }
    }

    private fun parseMpesaMessage(message: String): TransactionEntity? {
        return parseSendMoney(message) ?: parsePaybill(message) ?: parseTill(message)
    }

    private fun parseSendMoney(message: String): TransactionEntity? {
        val regex = """([A-Z0-9]+) Confirmed\. Ksh([\d,]+\.\d{2}) sent to ([A-Za-z ]+) (\d+) on (\d+/\d+/\d+) at (\d+:\d+ [APM]+)""".toRegex()
        return regex.find(message)?.let {
            val (code, amountStr, name, phone, date, time) = it.destructured
            val amount = amountStr.replace(",", "").toDoubleOrNull() ?: return null
            return TransactionEntity(
                id = 0,
                transactionCode = code,
                amount = amount,
                recipient = "$name $phone",
                transactionType = "Send Money",
                timestamp = parseDateTime(date, time) ?: return null
            )
        }
    }

    private fun parsePaybill(message: String): TransactionEntity? {
        val regex = """([A-Z0-9]+) Confirmed\. Ksh([\d,]+\.\d{2}) (?:sent|paid) to ([A-Za-z &]+) (?:Paybill Account|for account) ([\w-]+)\.? on (\d+/\d+/\d+) at (\d+:\d+ [APM]+)""".toRegex()
        return regex.find(message)?.let {
            val (code, amountStr, business, account, date, time) = it.destructured
            val amount = amountStr.replace(",", "").toDoubleOrNull() ?: return null
            return TransactionEntity(
                id = 0,
                transactionCode = code,
                amount = amount,
                recipient = business,
                transactionType = "Paybill",
                timestamp = parseDateTime(date, time) ?: return null
            )
        }
    }

    private fun parseTill(message: String): TransactionEntity? {
        val regex = """([A-Z0-9]+) Confirmed\. Ksh([\d,]+\.\d{2}) paid to ([A-Za-z &]+)\.? on (\d+/\d+/\d+) at (\d+:\d+ [APM]+)""".toRegex()
        return regex.find(message)?.let {
            val (code, amountStr, business, date, time) = it.destructured
            val amount = amountStr.replace(",", "").toDoubleOrNull() ?: return null
            return TransactionEntity(
                id = 0,
                transactionCode = code,
                amount = amount,
                recipient = business,
                transactionType = "Till",
                timestamp = parseDateTime(date, time) ?: return null
            )
        }
    }

    private fun parseDateTime(dateStr: String, timeStr: String): Long? {
        return try {
            SimpleDateFormat("d/M/yy h:mm a", Locale.getDefault()).parse("$dateStr $timeStr")?.time
        } catch (e: Exception) {
            null
        }
    }
    suspend fun clearAndReprocess(context: Context) {
        withContext(Dispatchers.IO) {
            dao.clearTransactions()
            fetchExistingMpesaTransactions(context)
        }
    }

    fun getTotalSpending(): LiveData<Double> {
        val sixMonthsAgo = Calendar.getInstance().apply { add(Calendar.MONTH, -6) }.timeInMillis
        return liveData {
            emit(dao.getTotalSpending(sixMonthsAgo))
        }
    }

    fun getLast10Transactions(): LiveData<List<TransactionEntity>> {
        val sixMonthsAgo = Calendar.getInstance().apply { add(Calendar.MONTH, -6) }.timeInMillis
        return dao.getLast10Transactions(sixMonthsAgo)
    }

    suspend fun getSupermarketTransactions(sixMonthsAgo: Long): List<RecipientSummary> {
        return withContext(Dispatchers.IO) {
            try {
                val transactions = dao.getSupermarketTransactions(sixMonthsAgo)
                if (transactions.isEmpty()) {
                    Log.d("Supermarkets", "No supermarket transactions found")
                } else {
                    Log.d("Supermarkets", "Found transactions: ${transactions.joinToString()}")
                }
                transactions
            } catch (e: Exception) {
                Log.e("Supermarkets", "Error getting transactions", e)
                emptyList()
            }
        }
    }
}



