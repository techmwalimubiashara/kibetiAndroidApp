package com.mb.kibeti.forgot_password.utils

import android.content.Context
import android.util.Log

object SharedPreferencesHelper {

    private const val PREF_NAME = "ForgotPasswordPrefs"
    private const val KEY_RESET_CODE = "resetCode"
    private const val KEY_EMAIL = "email"

    // Save the reset code to SharedPreferences
    fun saveResetCode(context: Context, resetCode: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_RESET_CODE, resetCode)
            .apply()
        Log.d("SharedPreferencesHelper", "Saved reset code: $resetCode")  // Debugging log
    }

    // Retrieve the reset code from SharedPreferences
    fun getResetCode(context: Context): String? {
        val resetCode = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_RESET_CODE, null)
        Log.d("SharedPreferencesHelper", "Retrieved reset code: $resetCode")  // Debugging log
        return resetCode
    }

    // Clear the reset code from SharedPreferences
    fun clearResetCode(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .remove(KEY_RESET_CODE)
            .apply()
        Log.d("SharedPreferencesHelper", "Reset code cleared")  // Debugging log
    }

    // Retrieve the email from SharedPreferences
    fun getEmail(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_EMAIL, null)
    }
}
