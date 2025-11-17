package com.mb.kibeti.reminder

import android.content.Context
import android.content.SharedPreferences

object ReminderPreferences {
    private const val PREF_NAME = "reminder_prefs"
    private const val KEY_HOUR = "reminder_hour"
    private const val KEY_MINUTE = "reminder_minute"

    fun saveReminderTime(context: Context, hour: Int, minute: Int) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putInt(KEY_HOUR, hour)
            putInt(KEY_MINUTE, minute)
            apply()
        }
    }

    fun getReminderTime(context: Context): Pair<Int, Int> {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val hour = sharedPreferences.getInt(KEY_HOUR, 18) // Default: 6 PM
        val minute = sharedPreferences.getInt(KEY_MINUTE, 0)
        return Pair(hour, minute)
    }
}
