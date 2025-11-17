package com.mb.kibeti.smsReaderAutoTask

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage
import android.util.Log
import com.mb.kibeti.smsReaderAutoTask.repositories.SmsDbSearchRepo
import com.mb.kibeti.smsReaderAutoTask.repositories.UpdateTransactionRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Check if the received broadcast is an SMS message
//        if (intent.action != null && intent.action == "android.provider.Telephony.SMS_RECEIVED") {
//            val bundle = intent.extras
//            if (bundle != null) {
//                val pdus = bundle["pdus"] as Array<Any>?
//                if (pdus != null) {
//                    Log.d("SmsReceiverBundle", "Received message bundle: $bundle")
//
//                    // Iterate through received message parts
//                    for (pdu in pdus) {
//                        Log.d("SmsPDU", "Received PDU: $pdu")
//
//                        val currentMessage: SmsMessage =
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                val format = bundle.getString("format")
//                                SmsMessage.createFromPdu(pdu as ByteArray, format)
//                            } else {
//                                SmsMessage.createFromPdu(pdu as ByteArray)
//                            }
//
//                        val senderNumber = currentMessage.displayOriginatingAddress
//                        val message = currentMessage.displayMessageBody
//
//                        Log.d("senderNumber", "Received message from $senderNumber: $message")
//
//                        // Check if the message is from M-Pesa
//                        if (senderNumber == "MPESA") {
////                            mpesaSorting(context, message)
//                        }
//                    }
//                }
//            }
//        }
    }

    // Process M-Pesa message and categorize it
//    fun mpesaSorting(context: Context, message: String) {
//        val parts = message.split(" ")
//
//        var recipient = ""
//        var amount = ""
//        var type = "unknown"
//        var search_cat = "universal"
//        var userEmail = "swaafiyah9@gmail.com"
//        var transactionCode = ""
//        var transactionDate = ""
//        var transactionTime = ""
//
//        // Function to format date from M-Pesa format to standard YYYY-MM-DD format
//        fun formatDate(inputDate: String): String {
//            return try {
//                val inputFormat = SimpleDateFormat("d/M/yy", Locale.US) // Original format
//                val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US) // Desired format
//                val date = inputFormat.parse(inputDate)
//                outputFormat.format(date ?: return inputDate) // Convert and return formatted date
//            } catch (e: Exception) {
//                inputDate // Return original date if parsing fails
//            }
//        }
//
//
//        fun saveAutoAllocatedTransactionData(
//            context: Context,
//            transactionId: String,
//            amount: String,
//            recipient: String,
//            time: String,
//            category: String
//        ) {
//            val sharedPreferences = context.getSharedPreferences("AutoAllocations", Context.MODE_PRIVATE)
//            val editor = sharedPreferences.edit()
//
//            // Create a string with only the required details
//            val transactionData = "$amount|$recipient|$time|$category"
//
//            // Save the transaction data under the transactionId
//            editor.putString(transactionId, transactionData)
//            editor.apply()
//        }
//
//
//
//        // Check if the message indicates a 'Send Money' transaction
//        if (message.contains("sent to") && Regex("(\\+?254|0)\\d{9}").containsMatchIn(message)) {
//            Log.d("sentmessage", "SENT MONEY MESSAGE")
//
//            // Extract phone number from the message using regex
//            val phoneRegex = "(\\+?254|0)\\d{9}".toRegex()
//            val match = phoneRegex.find(message)
//            recipient = match?.value ?: ""
//
//            // Regex pattern to extract transaction details
//            val sendMoneyRegex =
//                Regex("""(\w{10}) Confirmed\. Ksh([\d,]+\.\d{2}) .*on (\d{1,2}/\d{1,2}/\d{2}) at (\d{1,2}:\d{2} [APM]{2})""")
//            val sendMatch = sendMoneyRegex.find(message)
//
//            if (sendMatch != null) {
//                transactionCode = sendMatch.groupValues[1]
//                Log.d("sendTrancode", transactionCode)
//                amount = sendMatch.groupValues[2].replace(",", "").split(".")[0]
//                Log.d("sendTranAmount", amount)
//                transactionDate = formatDate(sendMatch.groupValues[3])
//                Log.d("sendTranDate", transactionDate)// Format date
//                transactionTime = sendMatch.groupValues[4]
//            } else {
//                Log.d("Error", "Failed to extract details for Send Money message")
//            }
//            type = "send money"
//            search_cat = "personal"
//
//        } else if (message.contains("sent to") && message.contains("for account")) {
//            Log.d("paidmessage", "PAID MESSAGE")
//
//            // Extract recipient name from the message
//            val recipientStart = parts.indexOf("to") + 1
//            val recipientEnd = parts.indexOf("for")
//            recipient = parts.subList(recipientStart, recipientEnd)
//                .joinToString(" ") // Extract recipient name
//            amount = parts[1].replace("Ksh", "") // Extract amount
//            type = "Paybill"
//            search_cat = "universal"
//
//            // Regex pattern to extract transaction details
//            val paidRegex =
//                Regex("""(\w{10}) Confirmed\. Ksh([\d,]+\.\d{2}) .*on (\d{1,2}/\d{1,2}/\d{2}) at (\d{1,2}:\d{2} [APM]{2})""")
//            val paidMatch = paidRegex.find(message)
//
//            if (paidMatch != null) {
//                transactionCode = paidMatch.groupValues[1]
//                amount = paidMatch.groupValues[2].replace(",", "")
//                    .split(".")[0]// remove comas and decimals in the extracted amount to fit the amount format in update transaction request body
//                transactionDate = formatDate(paidMatch.groupValues[3]) // Format date
//                transactionTime = paidMatch.groupValues[4]
//            } else {
//                Log.d("Error", "Failed to extract details for Paid message")
//            }
//        } else if (message.contains("sent to") && !message.contains(Regex("""\d{10}"""))) {
//            Log.d("PochiLaBiashara", "Detected Pochi La Biashara Transaction")
//
//            // Extract recipient's name
//            recipient = message.substringAfter("sent to ").substringBefore(" on")
//
//            // Regex pattern to extract transaction details for Pochi La Biashara messages
//            val pochiRegex = Regex("""(\w{10}) Confirmed\. Ksh([\d,]+\.\d{2}) .*on (\d{1,2}/\d{1,2}/\d{2}) at (\d{1,2}:\d{2} [APM]{2})""")
//            val pochiMatch = pochiRegex.find(message)
//
//            if (pochiMatch != null) {
//                transactionCode = pochiMatch.groupValues[1]
//                amount = pochiMatch.groupValues[2].replace(",", "").split(".")[0]  // Remove commas and decimals
//                transactionDate = formatDate(pochiMatch.groupValues[3])  // Format date
//                transactionTime = pochiMatch.groupValues[4]
//
//                type = "Pochi La Biashara"
//                search_cat = "universal"
//
//                // Log extracted details
//                Log.d("pochiTranCode", transactionCode)
//                Log.d("pochiTranAmount", amount)
//                Log.d("pochiTranDate", transactionDate)
//                Log.d("pochiTranTime", transactionTime)
//                Log.d("pochiRecipient", recipient)
//            } else {
//                Log.d("Error", "Failed to extract details for Pochi La Biashara message")
//            }
//        } else if (message.contains("paid to")) {
//            Log.d("TillPayment", "Detected Till Payment")
//
//            // Extract recipient's name
//            recipient = message.substringAfter("paid to ").substringBefore(".")
//            type = "Till Payment"
//            search_cat = "universal"
//
//            // Regex pattern to extract transaction details for Till Payment messages
//            val tillRegex = Regex("""(\w{10}) Confirmed\. Ksh([\d,]+\.\d{2}) .*on (\d{1,2}/\d{1,2}/\d{2}) at (\d{1,2}:\d{2} [APM]{2})""")
//            val tillMatch = tillRegex.find(message)
//
//            if (tillMatch != null) {
//                transactionCode = tillMatch.groupValues[1]
//                amount = tillMatch.groupValues[2].replace(",", "").split(".")[0]  // Remove commas and decimals
//                transactionDate = formatDate(tillMatch.groupValues[3])  // Format date
//                transactionTime = tillMatch.groupValues[4]
//
//                // Log extracted details
//                Log.d("tillTranCode", transactionCode)
//                Log.d("tillTranAmount", amount)
//                Log.d("tillTranDate", transactionDate)
//                Log.d("tillTranTime", transactionTime)
//                Log.d("tillRecipient", recipient)
//            } else {
//                Log.d("Error", "Failed to extract details for Till Payment message")
//            }
//        } else {
//            Log.d("messageError", "Cannot understand message")
//        }
//
//        // Log extracted transaction details
//        Log.d("Transaction Code", transactionCode)
//        Log.d("Amounttt", amount)
//        Log.d("Transaction Date", transactionDate)
//        Log.d("Transaction Time", transactionTime)
//
//        val search_name = recipient
//        val email = userEmail
//        val repo = SmsDbSearchRepo("search", search_cat, search_name, email)
//        Log.d("Searchcat", search_cat)
//        Log.d("action", "search")
//        Log.d("email", userEmail)
//        Log.d("recpientrepo", recipient)
//
//        // Ensure recipient is not empty before making a database search request
//        if (recipient.isEmpty()) {
//            Log.d("error", "Empty recipient")
//        } else {
//            try {
//                CoroutineScope(Dispatchers.IO).launch {
//                    val response =
//                        repo.performSmsDbSearch("search", search_cat, search_name, email)
//                    if (response.isSuccessful) {
//                        Log.d("API_SUCCESS", "First API call successful: ${response.body()}")
//                        var category = ""
//                        val time = transactionTime
//                        val transactionId = transactionCode
//                        if (!response.body()?.data.isNullOrEmpty()) {
//                            category = response.body()?.data?.get(0)?.outflow_line.toString()
//                        }
//
//                        saveAutoAllocatedTransactionData(
//                            context,
//                            transactionId,
//                            amount,
//                            recipient,
//                            time,
//                            category
//                        )
//
//
//                        if (response.body()?.outflow_id.isNullOrEmpty()) {
//                            Log.d("null outflow_id", "outflow id null")
//                        } else {
//                            val outflow_id = response.body()?.outflow_id!!.toInt()
//                            Log.d("out_flow", outflow_id.toString())
//
//                            if (outflow_id == 0) {
//                                Log.d("0outflow", "no budget for this")
//                            } else {
//                                val action = "update"
//                                val amount = amount!!.toInt()
//                                Log.d("updateAmount", amount.toString())
//
//                                val date = transactionDate
//                                Log.d("updateDate", date)
//
//                                val tran_code = transactionCode
//                                Log.d("updateCode", tran_code)
//                                Log.d(
//                                    "update repo params",
//                                    "$date, $tran_code, $outflow_id, $amount,$action"
//                                )
//
//
//                                val repo =
//                                    UpdateTransactionRepo(
//                                        action,
//                                        amount,
//                                        date,
//                                        outflow_id,
//                                        tran_code
//                                    )
//
//                                val updateResponse = repo.updateTransaction(
//                                    action,
//                                    amount,
//                                    date,
//                                    outflow_id,
//                                    tran_code
//                                )
//                                if (updateResponse.isSuccessful) {
//                                    Log.d("updates", "transaction updated successifully")
//                                } else {
//                                    Log.d("updatesError", "transaction update Error")
//                                }
//                            }
//                        }
//
//
//                    } else {
//                        Log.d("Error", "${response.code()} ${response.message()}")
//                    }
//                }
//            } catch (e: Exception) {
//                Log.e("SmsReceiverError", "Error performing database search", e)
//            }
//        }
//    }
}