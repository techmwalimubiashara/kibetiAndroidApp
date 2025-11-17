package com.mb.kibeti.reminder

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class ReminderWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        Log.d("ReminderWorker", "Worker started")

        val viewModel = MpesaViewModel(applicationContext)

        var transactionCount = 0
        var totalTransactionAmount = "0.00"
        var totalDailyGoalAmount = 0

        val transactionCallback: (Int, String) -> Unit = { count, amount ->
            transactionCount = count
            totalTransactionAmount = amount

            // Call notification after both transactions & goals are fetched
            NotificationHelper.showNotification(applicationContext, transactionCount, totalTransactionAmount, totalDailyGoalAmount)
        }

        val goalCallback: (Int) -> Unit = { dailyGoalAmount ->
            totalDailyGoalAmount = dailyGoalAmount

            // Call notification after both transactions & goals are fetched
            NotificationHelper.showNotification(applicationContext, transactionCount, totalTransactionAmount, totalDailyGoalAmount)
        }

        viewModel.fetchTransactions(transactionCallback)
        viewModel.fetchGoals(goalCallback)

        return Result.success()
    }
}
