package com.mb.kibeti.smsReaderAutoTask

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast

class TransactionCleanupReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Clear the saved transactions at midnight
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("AutoAllocations", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()


        Toast.makeText(context, "Transactions cleared for the day", Toast.LENGTH_SHORT).show()
    }
}