package com.mb.kibeti.sms_filter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.extras
        val pdus = bundle?.get("pdus") as? Array<*> ?: return

        val messages = pdus.mapNotNull { pdu ->
            SmsMessage.createFromPdu(pdu as ByteArray)
        }

        for (message in messages) {
            val messageBody = message.messageBody
            if (messageBody.contains("MPESA", ignoreCase = true)) {
                parseMpesaMessage(messageBody)?.let { transaction ->
                    val timestamp = message.timestampMillis
                    val sixMonthsAgo = System.currentTimeMillis() - (6L * 30 * 24 * 60 * 60 * 1000)

                    if (timestamp >= sixMonthsAgo) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val db = AppDatabase.getDatabase(context)
                            // Check if this SMS was already processed
                            val smsId = message.timestampMillis // Using timestamp as unique ID
                            val isProcessed = db.transactionDao().isSmsProcessed(smsId) > 0

                            if (!isProcessed) {
                                db.transactionDao().insertTransaction(
                                    transaction.copy(
                                        timestamp = timestamp,
                                        smsId = smsId
                                    )
                                )
                            }
                        }
                    }
                }
            }
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
}

private val SmsMessage.timestampMillis: Long
    get() = try {
        timestampMillis
    } catch (e: Exception) {
        System.currentTimeMillis()
    }