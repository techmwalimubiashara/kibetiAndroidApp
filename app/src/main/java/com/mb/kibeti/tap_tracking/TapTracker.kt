package com.mb.kibeti.tap_tracking

import android.content.Context
import android.util.Log
import com.mb.kibeti.feedback.utils.BASE_URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TapTracker(private val context: Context) {

    private val apiService: TapTrackerApiService
    private val TAG = "TapTracker"

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(TapTrackerApiService::class.java)
    }

    // Public method to post track
    fun postTrack(email: String, activity: String) {
        val userAction = UserAction(email = email, activity = activity)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.postUserAction(userAction)
                if (response.isSuccessful) {
                    val trackResponse = response.body()
                    Log.d(TAG, "Response: ${trackResponse?.message}, Error: ${trackResponse?.error}")
                } else {
                    Log.e(TAG, "Unsuccessful response: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception during postTrack: ${e.message}", e)
            }
        }
    }
}
