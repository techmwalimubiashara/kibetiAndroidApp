package com.mb.kibeti.reminder

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.RingtoneManager
import android.os.Build

import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.mb.kibeti.MpesaSpending
import com.mb.kibeti.MpesaUpdates
import com.mb.kibeti.R
import com.mb.kibeti.eod_dashboard.EODFeelingActivity

object NotificationHelper {
    private const val CHANNEL_ID = "mpesa_reminder_channel"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val channel = NotificationChannel(
                CHANNEL_ID,
                "MPESA Reminder",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 500, 500)
                setSound(soundUri, null)
                lightColor = ContextCompat.getColor(context, R.color.primary_orange) // Use your app color
            }

            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    fun showNotification(context: Context, count: Int, totalAmount: String, dailyGoalAmount: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                return
            }
        }

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // Message for unallocated transactions
        var notificationMessage = "Visit Kibeti - your trusted accountability partner"// have $count unallocated transactions totaling KES $totalAmount."
//        if (dailyGoalAmount > 0) {
//            notificationMessage += "\n🔔 Don't forget to update your goals! Your total daily savings target is KES $dailyGoalAmount."
//        }

        // Intent to open the app when notification is clicked
        val intent = Intent(context, MpesaSpending::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        intent.putExtra("cashflow", "outflow")


        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo) // Custom app icon
            .setContentTitle("Ready to track your wealth?")
            .setContentText(notificationMessage)
            .setStyle(NotificationCompat.BigTextStyle().bigText(notificationMessage)) // Expandable message
            .setColor(ContextCompat.getColor(context, R.color.primary_orange)) // Apply app color
            .setColorized(true) // Ensure full colorization
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(soundUri)
            .setVibrate(longArrayOf(0, 500, 500, 500))
            .setContentIntent(pendingIntent) // Open app when clicked
            .setAutoCancel(true) // Remove notification when clicked
            .build()

        NotificationManagerCompat.from(context).notify(1, notification)
    }
}


