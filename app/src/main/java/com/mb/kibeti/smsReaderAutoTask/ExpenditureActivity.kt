package com.mb.kibeti.smsReaderAutoTask

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Telephony
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mb.kibeti.R
import com.mb.kibeti.databinding.ActivityExpenditureBinding

class ExpenditureActivity : AppCompatActivity() {
    private lateinit var bind : ActivityExpenditureBinding
    private val SMS_PERMISSION_CODE = 101
    private lateinit var smsReceiver: SmsReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityExpenditureBinding.inflate(layoutInflater)
        setContentView(bind.root)

        showPendingTransactionsDialog()

        checkAndRequestPermissions()
        smsReceiver = SmsReceiver()
        val intentFilter = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
        registerReceiver(smsReceiver, intentFilter)
    }

    @SuppressLint("MissingInflatedId")
    private fun showPendingTransactionsDialog() {
        // Get the saved transactions from SharedPreferences
        val transactions = getUnconfirmedTransactions(this)  // Use getUnconfirmedTransactions to fetch unconfirmed transactions

        if (transactions.isNotEmpty()) {
            val dialogView = layoutInflater.inflate(R.layout.dialog_confirm_transaction, null)
            val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()

            val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recView)
            val confirmAllButton = dialogView.findViewById<Button>(R.id.confirmButton)

//            recyclerView.layoutManager = LinearLayoutManager(this)
//            recyclerView.adapter = ConfirmationTransactionListAdapter(transactions) { transaction ->
//                // Handle Edit button click in adapter
//                editTransaction(transaction)
//            }

            // Confirm All button click
            confirmAllButton.setOnClickListener {
                // Mark all transactions as confirmed
                for (transaction in transactions) {
                    confirmTransaction(transaction) // Confirm each transaction
                }
                dialog.dismiss() // Close the dialog after confirmation
                Toast.makeText(this, "All transactions confirmed", Toast.LENGTH_SHORT).show()
            }

            dialog.show()
        } else {
            // If no transactions, show a message
            Toast.makeText(this, "No pending transactions", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUnconfirmedTransactions(context: Context): List<PendingTransaction> {
        val sharedPreferences = context.getSharedPreferences("AutoAllocations", Context.MODE_PRIVATE)
        val transactionIds = sharedPreferences.all.keys // Get all saved transaction IDs (keys)

        // List to store the unconfirmed transactions
        val unconfirmedTransactions = mutableListOf<PendingTransaction>()

        // Get the list of confirmed recipients from SharedPreferences
        val confirmedRecipients = context.getSharedPreferences("ConfirmedRecipients", Context.MODE_PRIVATE)

        // Retrieve each transaction by its ID and map it to a PendingTransaction object
        for (transactionId in transactionIds) {
            val transactionData = sharedPreferences.getString(transactionId, null)
            if (transactionData != null) {
                val parts = transactionData.split("|")
                if (parts.size >= 4) {
                    val amount = parts[0]
                    val recipient = parts[1]
                    val time = parts[2]
                    val category = parts[3]

                    // If recipient is not confirmed yet, add it to unconfirmed list
                    if (!confirmedRecipients.contains(recipient)) {
                        unconfirmedTransactions.add(PendingTransaction(transactionId, amount, recipient, time, category))
                    }
                }
            }
        }

        return unconfirmedTransactions
    }

    private fun isRecipientConfirmed(recipient: String): Boolean {
        // Retrieve the confirmed transactions from SharedPreferences
        val sharedPreferences = getSharedPreferences("ConfirmedTransactions", Context.MODE_PRIVATE)

        // Check if the recipient is saved as confirmed
        return sharedPreferences.contains(recipient)
    }

    private fun confirmTransaction(transaction: PendingTransaction) {
        // Mark the transaction as confirmed by saving it in a separate SharedPreferences
        val confirmedPreferences = getSharedPreferences("ConfirmedRecipients", Context.MODE_PRIVATE)
        val editor = confirmedPreferences.edit()

        // Store recipient (for simplicity) as key and other data as value (you can customize this)
        editor.putString(transaction.recipient, "${transaction.amount}|${transaction.category}|${transaction.time}")
        editor.apply()

        // Optionally, you can also remove the transaction from "AutoAllocations" if you want
        // to delete the unconfirmed one after confirming
        val autoAllocationsPreferences = getSharedPreferences("AutoAllocations", Context.MODE_PRIVATE)
        val autoAllocationsEditor = autoAllocationsPreferences.edit()
        autoAllocationsEditor.remove(transaction.transactionId) // Remove confirmed transaction
        autoAllocationsEditor.apply()

        // Notify user about confirmation
        Toast.makeText(this, "Transaction confirmed for ${transaction.recipient}", Toast.LENGTH_SHORT).show()
    }

    private fun editTransaction(transaction: PendingTransaction) {
        // Allow user to manually edit the transaction
        Toast.makeText(this, "Edit the transaction for ${transaction.recipient}", Toast.LENGTH_SHORT).show()
    }

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECEIVE_SMS), SMS_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SMS_PERMISSION_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(smsReceiver)
    }}