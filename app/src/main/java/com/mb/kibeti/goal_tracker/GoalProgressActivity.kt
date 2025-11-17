package com.mb.kibeti.goal_tracker

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.mb.kibeti.R
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mb.kibeti.CelebrationPopActivity
import com.mb.kibeti.LoginActivity
import com.mb.kibeti.LoginActivity.MY_PREFERENCES
import com.mb.kibeti.Variables
import com.mb.kibeti.tap_tracking.TapTracker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class GoalProgressActivity : AppCompatActivity() {
    private lateinit var progressBarGoal1: CircularProgressIndicator
    private lateinit var progressBarGoal2: CircularProgressIndicator
    private lateinit var tvGoal1Title: TextView
    private lateinit var tvGoal2Title: TextView
    private lateinit var tvProgressGoal1: TextView
    private lateinit var tvProgressGoal2: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var cardGoal1: CardView
    private lateinit var cardGoal2: CardView
    private lateinit var btnComplete: Button

    private lateinit var goal1: GoalDetails
    private lateinit var goal2: GoalDetails
    private var progressGoal1 = 0
    private var progressGoal2 = 0
    private var goal1Id: String? = null
    private var goal2Id: String? = null
    private lateinit var userEmail: String
    private lateinit var tapTracker: TapTracker
    private lateinit var sharedPrefs_old: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_progress)
        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        sharedPrefs_old = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE)
        val savedGoals = getSavedGoals()
        tapTracker=TapTracker(this);

        val email: String? = sharedPrefs_old . getString (LoginActivity.EMAIL, "");
        val nonNullableStr: String = email!!

        if (savedGoals.size < 2) {
            finish()
            return
        }



        goal1 = savedGoals[0]
        goal2 = savedGoals[1]
        userEmail = Constants.USER_EMAIL

        progressBarGoal1 = findViewById(R.id.progressBarGoal1)
        progressBarGoal2 = findViewById(R.id.progressBarGoal2)
        tvGoal1Title = findViewById(R.id.tvGoal1Title)
        tvGoal2Title = findViewById(R.id.tvGoal2Title)
        tvProgressGoal1 = findViewById(R.id.tvProgressGoal1)
        tvProgressGoal2 = findViewById(R.id.tvProgressGoal2)
        cardGoal1 = findViewById(R.id.cardGoal1)
        cardGoal2 = findViewById(R.id.cardGoal2)
        btnComplete = findViewById(R.id.btnComplete)


        tvGoal1Title.text = goal1.name
        tvGoal2Title.text = goal2.name

        applyAnimations()
        fetchGoalProgress() //update from api
        loadSavedProgress()
        cardGoal1.setOnClickListener { openGoalSteps(goal1, goal1Id) }
        cardGoal2.setOnClickListener { openGoalSteps(goal2, goal2Id) }
        btnComplete.setOnClickListener{ celebrationActivty(email)}
    }

    private fun getSavedGoals(): List<GoalDetails> {
        val gson = Gson()
        val json = sharedPreferences.getString("selected_goals", null)
        return if (json != null) {
            val type = object : TypeToken<List<GoalDetails>>() {}.type
            gson.fromJson(json, type)
        } else {  
            emptyList()
        }
    }

    private fun loadSavedProgress() {
        progressGoal1 = sharedPreferences.getInt("progress_${goal1.name}", 0)
        progressGoal2 = sharedPreferences.getInt("progress_${goal2.name}", 0)

        updateUI(progressGoal1, progressGoal2)
    }

    private fun fetchGoalProgress() {
        val request = GetGoalsRequest(email = userEmail)

        Retrofit.instance.getAllGoals(request).enqueue(object : Callback<GetAllGoalsResponse> {
            override fun onResponse(call: Call<GetAllGoalsResponse>, response: Response<GetAllGoalsResponse>) {
                if (response.isSuccessful && response.body()?.error == false) {
                    val goalDataList = response.body()?.data ?: emptyList()
                    updateProgressFromAPI(goalDataList)
                } else {
                    Log.e("GoalProgressActivity", "Failed to fetch goals: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<GetAllGoalsResponse>, t: Throwable) {
                Log.e("GoalProgressActivity", "Error fetching goals: ${t.message}")
            }
        })
    }


    private fun updateProgressFromAPI(goalDataList: List<GoalProgressDetails>) {
        val progressMap = goalDataList.associateBy { it.goal_name }

        goal1Id = progressMap[goal1.name]?.goal_id ?: ""
        goal2Id = progressMap[goal2.name]?.goal_id ?: ""

        val newProgressGoal1 = progressMap[goal1.name]?.percent?.toIntOrNull() ?: progressGoal1
        val newProgressGoal2 = progressMap[goal2.name]?.percent?.toIntOrNull() ?: progressGoal2

        Log.d("GoalProgressActivity", "API Progress - ${goal1.name}: $newProgressGoal1, ${goal2.name}: $newProgressGoal2")

        if (newProgressGoal1 != progressGoal1 || newProgressGoal2 != progressGoal2) {
            progressGoal1 = newProgressGoal1
            progressGoal2 = newProgressGoal2
            updateUI(progressGoal1, progressGoal2)
            saveProgressToPreferences()

            runOnUiThread {
                animateProgress(progressBarGoal1, progressGoal1)
                animateProgress(progressBarGoal2, progressGoal2)
            }
        }
    }



    private fun updateUI(progress1: Int, progress2: Int) {
        animateProgress(progressBarGoal1, progress1)
        animateProgress(progressBarGoal2, progress2)

        tvProgressGoal1.text = "$progress1%"
        tvProgressGoal2.text = "$progress2%"
    }



    private fun animateProgress(progressBar: CircularProgressIndicator, progress: Int) {
        val animator = ObjectAnimator.ofInt(progressBar, "progress", progressBar.progress, progress)
        animator.duration = 1000
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener {
            progressBar.setProgressCompat(it.animatedValue as Int, true)
        }
        animator.start()
    }



    private fun saveProgressToPreferences() {
        sharedPreferences.edit().apply {
            putInt("progress_${goal1.name}", progressGoal1)
            putInt("progress_${goal2.name}", progressGoal2)
            apply()
        }
    }

    private fun applyAnimations() {
        val rootLayout = findViewById<View>(android.R.id.content)
        rootLayout.alpha = 0f
        rootLayout.animate().alpha(1f).setDuration(800).start()

        applyScaleAnimation(cardGoal1)
        applyScaleAnimation(cardGoal2)
    }

    private fun applyScaleAnimation(view: View) {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.8f, 1f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.8f, 1f)
        ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY).apply {
            duration = 700
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    private fun openGoalSteps(goal: GoalDetails, goalId: String?) {
        Log.d("GoalProgressActivity", "Opening GoalStepsActivity for goal: ${goal.name}, goalId: $goalId")
        val intent = Intent(this, GoalStepsActivity::class.java)
        intent.putExtra("goal", Gson().toJson(goal))
        intent.putExtra("goalId", goalId)
        startActivity(intent)
    }

    private fun celebrationActivty(email:String) {

        tapTracker.postTrack(email, "Goal Tracker")

        Log.d("Celebration Activity", "Celebration created")
        val intent = Intent(this, CelebrationPopActivity::class.java)
        startActivity(intent)
        finish()

    }
}