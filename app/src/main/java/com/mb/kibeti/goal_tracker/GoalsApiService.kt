package com.mb.kibeti.goal_tracker

import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface GoalsApiService {

    @GET("goals_data.php")
    fun getGoals(): Call<GoalResponse>

    @POST("goals.php")
    fun addGoal(@Body request: AddGoalRequest): Call<AddGoalResponse>

    @POST("goals.php")
    fun updateGoal(@Body request: UpdateGoalRequest): Call<UpdateGoalResponse>

    @POST("goals.php")
    fun getAllGoals(@Body request: GetGoalsRequest): Call<GetAllGoalsResponse>

}
data class AddGoalRequest(
    val action: String = "add_goal",
    val email: String,
    val goal: String,
    val step: String,
    val percent: String
)

data class AddGoalResponse(
    val success: String,
    val error: Boolean,
    val message: String,
    val goal_id: String?
)

data class UpdateGoalRequest(
    val action: String = "update_goal",
    val email: String,
    val goal_id: String,
    val step: String,
    val percent: String
)

data class UpdateGoalResponse(
    val message: String,
    val confirm: Boolean,
    val error: Boolean
)

data class GetGoalsRequest(
    val action: String = "get_all_goal",
    val email: String
)

data class GetAllGoalsResponse(
    val success: String,
    val error: Boolean,
    val data: List<GoalProgressDetails>
)

data class GoalProgressDetails(
    val goal_id: String,
    val goal_name: String,
    val step: String,
    val percent: String
)
//
//data class AddGoalResponse(
//    val success: String,
//    val error: Boolean,
//    val message: String,
//    val goal_id: String?
//)
//
//data class UpdateGoalRequest(
//    val action: String = "update_goal",
//    val email: String,
//    val goal_id: String,
//    val step: String,
//    val percent: String
//)

//data class UpdateGoalResponse(
//    val message: String,
//    val confirm: Boolean,
//    val error: Boolean
//)
//
//data class GetGoalsRequest(
//    val action: String = "get_all_goal",
//    val email: String
//)

//data class GetAllGoalsResponse(
//    val success: String,
//    val error: Boolean,
//    val data: List<GoalProgressDetails>
//)

//data class GoalProgressDetails(
//    val goal_id: String,
//    val goal_name: String,
//    val step: String,
//    val percent: String
//)

