package com.mb.kibeti.reminder

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class MpesaViewModel(private val context: Context) : ViewModel() {

    // Fetch Unallocated Transactions
    fun fetchTransactions(onResult: (Int, String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userEmail = "dominickiplimo2017@gmail.com"
//                val userEmail = UserPreferences.getUserEmail(context)
                if (userEmail.isNullOrEmpty()) {
                    Log.e("MpesaViewModel", "User email is null or empty")
                    return@launch
                }

                val transactionResponse = RetrofitClient.apiService.getTransactions(
                    MpesaRequestBody(email = userEmail)
                ).awaitResponse()

                var transactionCount = 0
                var totalTransactionAmount = "0.00"

                if (transactionResponse.isSuccessful) {
                    transactionResponse.body()?.let {
                        transactionCount = it.count
                        totalTransactionAmount = it.total_amount ?: "0.00"
                    }
                }

                Log.d("MpesaViewModel", "Transactions: $transactionCount, Amount: $totalTransactionAmount")
                onResult(transactionCount, totalTransactionAmount)

            } catch (e: Exception) {
                Log.e("MpesaViewModel", "Transaction API call failed: ${e.message}")
            }
        }
    }

    // Fetch Financial Goals
    fun fetchGoals(onResult: (Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
//                val userEmail = UserPreferences.getUserEmail(context)
                val userEmail = "dev@kibeti.com"

                if (userEmail.isNullOrEmpty()) {
                    Log.e("MpesaViewModel", "User email is null or empty")
                    return@launch
                }

                val goalResponse = RetrofitClient.apiService.getGoals(
                    GoalRequestBody(email = userEmail)
                ).awaitResponse()

                var totalDailyGoalAmount = 0

                if (goalResponse.isSuccessful) {
                    goalResponse.body()?.let { goalData ->
                        goalData.data?.let { goals ->
                            totalDailyGoalAmount = goals.sumOf { it.daily_amount.toInt() }
                        }
                    }
                }

                Log.d("MpesaViewModel", "Total Daily Goal Amount: $totalDailyGoalAmount")
                onResult(totalDailyGoalAmount)

            } catch (e: Exception) {
                Log.e("MpesaViewModel", "Goal API call failed: ${e.message}")
            }
        }
    }
}

//
//import android.content.Context
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import retrofit2.awaitResponse
//
//class MpesaViewModel(private val context: Context) : ViewModel() {
//
//
//    fun fetchTransactions(onResult: (Int, String) -> Unit) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                //retrieve from shared preferences
////                val userEmail = UserPreferences.getUserEmail(context) ?: return@launch // Exit if email is null
//                val userEmail = "dominickiplimo2017@gmail.com" //use retrieved one instead
//                val requestBody = MpesaRequestBody(email = userEmail)
//
//                val response = RetrofitClient.apiService.getTransactions(requestBody).awaitResponse()
//
//                if (response.isSuccessful) {
//                    response.body()?.let {
//                        Log.d("MpesaViewModel", "API Success: Count = ${it.count}, Amount = ${it.total_amount}")
//                        onResult(it.count, it.total_amount)
//                    } ?: Log.e("MpesaViewModel", "Response body is null")
//                } else {
//                    Log.e("MpesaViewModel", "API Error: ${response.code()} ${response.message()}")
//                }
//            } catch (e: Exception) {
//                Log.e("MpesaViewModel", "API call failed: ${e.message}")
//            }
//        }
//    }
//
//    fun fetchGoals(onResult: (Int) -> Unit) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
////                val userEmail = UserPreferences.getUserEmail(context)
//                val userEmail = "dev@kibeti.com"
//                if (userEmail.isNullOrEmpty()) {
//                    Log.e("MpesaViewModel", "User email is null or empty")
//                    return@launch
//                }
//
//                val goalResponse = RetrofitClient.apiService.getGoals(
//                    GoalRequestBody(email = userEmail)
//                ).awaitResponse()
//
//                var totalDailyGoalAmount = 0
//
//                if (goalResponse.isSuccessful) {
//                    goalResponse.body()?.let { goalData ->
//                        goalData.data?.let { goals ->
//                            totalDailyGoalAmount = goals.sumOf { it.daily_amount.toInt() }
//                        }
//                    }
//                }
//
//                Log.d("MpesaViewModel", "Total Daily Goal Amount: $totalDailyGoalAmount")
//                onResult(totalDailyGoalAmount)
//
//            } catch (e: Exception) {
//                Log.e("MpesaViewModel", "Goal API call failed: ${e.message}")
//            }
//        }
//    }
//}
