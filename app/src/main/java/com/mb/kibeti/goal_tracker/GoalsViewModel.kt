package com.mb.kibeti.goal_tracker
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


data class GoalResponse(
    val goals: List<GoalDetails>
)

data class GoalDetails(
    val name: String,
    val steps: List<Step>
)

data class Step(
    val title: String,
    val description: String,
    var completed: Boolean = false,
    val percentage: Int
)



class GoalsViewModel : ViewModel() {
    private val _goals = MutableLiveData<Map<String, GoalDetails>>()
    val goals: LiveData<Map<String, GoalDetails>> get() = _goals

    init {
        fetchGoals()
    }

    private fun fetchGoals() {
        RetrofitClient.instance.getGoals().enqueue(object : Callback<GoalResponse> {
            override fun onResponse(call: Call<GoalResponse>, response: Response<GoalResponse>) {
                if (response.isSuccessful) {
                    response.body()?.goals?.let { goalList ->
                        Log.d("GoalsViewModel", "Fetched Goals: $goalList") // Log the fetched data

                        _goals.value = goalList.associateBy { it.name }
                    } ?: run {
                        Log.d("GoalsViewModel", "Empty goals list received")
                        _goals.value = emptyMap()
                    }
                } else {
                    Log.d("GoalsViewModel", "API Response Failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<GoalResponse>, t: Throwable) {
                Log.d("GoalsViewModel", "Error fetching goals: ${t.message}")
            }
        })
    }

}
