package com.mb.kibeti.tap_tracking

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TapTrackerApiService {

    @POST("user_action_tracker.php")
    suspend fun postUserAction(@Body userAction: UserAction): Response<TrackResponse>
}