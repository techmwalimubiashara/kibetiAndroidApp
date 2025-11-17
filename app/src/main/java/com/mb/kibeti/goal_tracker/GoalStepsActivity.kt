package com.mb.kibeti.goal_tracker
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mb.kibeti.R
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoalStepsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GoalStepsAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var goal: GoalDetails
    private  var userEmail: String?=""
    private var goalId: String? = null
    private lateinit var btnCompleteProcess: Button // New button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_steps)
        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val goalJson = intent.getStringExtra("goal")
        goal = Gson().fromJson(goalJson, GoalDetails::class.java)
        userEmail = sharedPreferences.getString("selected_goals", "")

        // Set the title to the goal name
        findViewById<TextView>(R.id.tvTitle).text = goal.name


        recyclerView = findViewById(R.id.recyclerViewSteps)
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnCompleteProcess = findViewById(R.id.btnCompleteProcess) // Initialize the button
        btnCompleteProcess.setOnClickListener { completeProcess() }

        goalId = sharedPreferences.getString("${goal.name}_goalId", null)

        if (goalId.isNullOrBlank() || goalId == "200 OK") {
            fetchGoalsFromApi()
        }

        adapter = GoalStepsAdapter(
            steps = goal.steps,
            onStepChecked = { step -> handleStepCompletion(step) },
            onInfoClick = { step -> showStepDetails(step) }
        )
        recyclerView.adapter = adapter
        fetchGoalsFromApi()
    }

    private fun completeProcess() {
        // Save current progress to SharedPreferences
        val progress = goal.steps.filter { it.completed }.sumOf { it.percentage }
        sharedPreferences.edit().putInt("progress_${goal.name}", progress).apply()

        // Navigate back to GoalProgressActivity
        val intent = Intent(this, GoalProgressActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish() // Close this activity
    }

    private fun fetchGoalsFromApi() {
//        val request = GetGoalsRequest(email = userEmail)
        val request = userEmail?.let { GetGoalsRequest(email = it) }

        if (request != null) {

            Retrofit.instance.getAllGoals(request).enqueue(object : Callback<GetAllGoalsResponse> {
                override fun onResponse(
                    call: Call<GetAllGoalsResponse>,
                    response: Response<GetAllGoalsResponse>
                ) {
                    if (response.isSuccessful && response.body()?.error == false) {
                        val allGoalsList = response.body()?.data ?: emptyList()

                        val goalData = allGoalsList.firstOrNull { it.goal_name == goal.name }
                        val progress = goalData?.percent?.toIntOrNull() ?: 0

                        Log.d(
                            "GoalStepsActivity",
                            "API Response: ${response.body()}"
                        ) // Debugging API response
                        Log.d(
                            "GoalStepsActivity",
                            "Fetched progress: $progress% for goal: ${goal.name}"
                        )

                        if (goalData?.goal_id?.isNotBlank() == true) {
                            goalId = goalData.goal_id
                            saveGoalIdToSharedPreferences(goalId!!)
                            updateStepCompletion(progress) // Pre-check steps
                            updateRecyclerView()
                        } else {
                            Log.w("GoalStepsActivity", "Goal ID not found in API response")
                        }
                    } else {
                        Log.e(
                            "GoalStepsActivity",
                            "Failed to fetch goals: ${response.errorBody()?.string()}"
                        )
                    }
                }

                override fun onFailure(call: Call<GetAllGoalsResponse>, t: Throwable) {
                    Log.e("GoalStepsActivity", "Error fetching goals: ${t.message}")
                }
            })
        }
    }



    private fun handleStepCompletion(step: Step) {
        val isChecking = !step.completed // Determine if checking or unchecking

        val message = if (isChecking) {
            "Are you sure you want to mark this step as completed?"
        } else {
            "Are you sure you want to uncheck this step? This will reduce your goal progress."
        }

        AlertDialog.Builder(this)
            .setTitle("Confirm Action")
            .setMessage(message)
            .setPositiveButton("Yes") { dialog, _ ->
                step.completed = isChecking // Toggle completion status

                // Recalculate progress based on checked steps
                val currentProgress = goal.steps.filter { it.completed }.sumOf { it.percentage }

                adapter.notifyItemChanged(goal.steps.indexOf(step))

                if (goalId.isNullOrBlank() || goalId == "200 OK") {
                    addGoal(step, currentProgress) // First-time goal addition
                } else {
                    updateGoal(step, currentProgress) // Update existing goal
                }

                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                adapter.notifyItemChanged(goal.steps.indexOf(step)) // Reset checkbox UI
                dialog.dismiss()
            }
            .show()
    }



    private fun addGoal(step: Step, progress: Int) {
        val request = userEmail?.let {
            AddGoalRequest(
                email = it,
                goal = goal.name,
                step = (goal.steps.indexOf(step) + 1).toString(),
                percent = progress.toString()
            )
        }

        if (request != null) {
            Retrofit.instance.addGoal(request).enqueue(object : Callback<AddGoalResponse> {
                override fun onResponse(call: Call<AddGoalResponse>, response: Response<AddGoalResponse>) {
                    if (response.isSuccessful && response.body()?.error == false) {
                        response.body()?.goal_id?.let {
                            goalId = it.takeIf { it.isNotBlank() }
                            if (!goalId.isNullOrBlank()) {
                                saveGoalIdToSharedPreferences(goalId!!)
                                Log.d("GoalStepsActivity", "Goal added successfully: $goalId")
                            }
                        }
                    } else {
                        Log.e("GoalStepsActivity", "Failed to add goal: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<AddGoalResponse>, t: Throwable) {
                    Log.e("GoalStepsActivity", "Error adding goal: ${t.message}")
                }
            })
        }
    }

    private fun updateGoal(step: Step, progress: Int) {
        if (goalId.isNullOrBlank() || goalId == "200 OK") {
            Log.e("GoalStepsActivity", "Invalid goalId: Cannot update goal")
            return
        }

        val request = userEmail?.let {
            UpdateGoalRequest(
                email = it,
                goal_id = goalId!!,
                step = (goal.steps.indexOf(step) + 1).toString(),
                percent = progress.toString()
            )
        }

        if (request != null) {
            Retrofit.instance.updateGoal(request).enqueue(object : Callback<UpdateGoalResponse> {
                override fun onResponse(call: Call<UpdateGoalResponse>, response: Response<UpdateGoalResponse>) {
                    if (response.isSuccessful && response.body()?.error == false) {
                        Log.d("GoalStepsActivity", "Goal updated successfully: ${step.title}")
                    } else {
                        Log.e("GoalStepsActivity", "Failed to update goal: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<UpdateGoalResponse>, t: Throwable) {
                    Log.e("GoalStepsActivity", "Error updating goal: ${t.message}")
                }
            })
        }
    }


    private fun saveGoalIdToSharedPreferences(goalId: String) {
        sharedPreferences.edit().putString("${goal.name}_goalId", goalId).apply()
    }
    private fun updateStepCompletion(progress: Int) {
        var accumulatedProgress = 0

        for (step in goal.steps) {
            accumulatedProgress += step.percentage
            step.completed = accumulatedProgress <= progress
        }

        Log.d("GoalStepsActivity", "Updated Steps: ${goal.steps}") // Debugging steps
        adapter.notifyDataSetChanged() // Refresh UI
    }

    private fun updateRecyclerView() {
        adapter = GoalStepsAdapter(goal.steps,
            onStepChecked = { step -> handleStepCompletion(step) },
            onInfoClick = { step -> showStepDetails(step) })
        recyclerView.adapter = adapter
    }




    private fun showStepDetails(step: Step) {
        AlertDialog.Builder(this)
            .setTitle(step.title)
            .setMessage(step.description)
            .setPositiveButton("Mark as Completed") { dialog, _ ->
                handleStepCompletion(step)
                dialog.dismiss()
            }
            .setNegativeButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
