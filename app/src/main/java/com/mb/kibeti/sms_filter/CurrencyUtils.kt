package com.mb.kibeti.sms_filter

import android.content.Context
import java.text.NumberFormat
import java.util.*

object CurrencyUtils {
    fun formatAmount(context: Context, amount: Double): String {
        // Retrieve currency from SharedPreferences
        val sharedPrefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val currencySymbol = sharedPrefs.getString("currency", "Ksh") ?: "Ksh"

        // Format the amount without decimals
        val format = NumberFormat.getNumberInstance(Locale("en", "KE"))
        format.maximumFractionDigits = 0
        return "$currencySymbol ${format.format(amount)}"
    }
}