package com.mb.kibeti.goal_tracker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mb.kibeti.R
import com.google.gson.Gson
import com.mb.kibeti.Variables
import com.mb.kibeti.goal_tracker.GoalsAdapter

class GoalTrackerActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnContinue: Button
    private val viewModel: GoalsViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var adapter: GoalsAdapter
    val utils: Variables = Variables()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_tracker)
        supportActionBar?.hide()

        recyclerView = findViewById(R.id.recyclerGoals)
        btnContinue = findViewById(R.id.btnContinue)
        sharedPreferences = getSharedPreferences(utils.myPrefName, Context.MODE_PRIVATE)

        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.goals.observe(this) { goalMap ->
            adapter = GoalsAdapter(goalMap) { selectedGoals ->
                btnContinue.isEnabled = selectedGoals.size == 2
            }
            recyclerView.adapter = adapter
        }

        btnContinue.setOnClickListener {
            val selectedGoals = adapter.getSelectedGoals()
            saveSelectedGoals(selectedGoals)
            navigateToNextScreen()
        }
    }

    private fun saveSelectedGoals(selectedGoals: List<GoalDetails>) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val jsonGoals = gson.toJson(selectedGoals)
        editor.putString("selected_goals", jsonGoals)
        editor.apply()
    }

    private fun navigateToNextScreen() {
        val intent = Intent(this, GoalProgressActivity::class.java)
        startActivity(intent)
        finish()
    }
}