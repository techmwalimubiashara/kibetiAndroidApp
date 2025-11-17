package com.mb.kibeti.tap_tracking

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class TapTrackerViewModel(application: Application) : AndroidViewModel(application) {

    private val tapTracker: TapTracker = TapTracker(application.applicationContext)

    // Public method to expose postTrack functionality
    fun postTrack(email: String, activity: String) {
        tapTracker.postTrack(email, activity)
    }
}