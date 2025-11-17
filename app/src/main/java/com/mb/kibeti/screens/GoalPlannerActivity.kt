package com.mb.kibeti.screens

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mb.kibeti.model.GoalPlan
import com.mb.kibeti.R
import com.mb.kibeti.databinding.ActivityGoalPlannerBinding
import com.mb.kibeti.model.Investment
import com.mb.kibeti.viewmodel.GoalPlannerViewModel

class GoalPlannerActivity : AppCompatActivity() {

    private val viewModel: GoalPlannerViewModel by viewModels()
    private lateinit var adapter: GoalPlanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityGoalPlannerBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_goal_planner
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        adapter = GoalPlanAdapter(emptyList())
        binding.rvResults.adapter = adapter

        binding.rvResults.layoutManager = LinearLayoutManager(this)
        binding.btnCalculate.setOnClickListener {
            val age = binding.etAge.text.toString().toIntOrNull() ?: 0
            val experience = binding.etExperience.text.toString().toIntOrNull() ?: 0
            val appetite = binding.etAppetite.text.toString().toIntOrNull() ?: 0
            val tolerance = binding.etTolerance.text.toString().toIntOrNull() ?: 0

            val investment = Investment(age, experience, appetite, tolerance)
            viewModel.deleteAllGoalPlans()
            viewModel.calculateGoals(investment)
        }

        viewModel.allGoalPlans.observe(this, Observer { plans ->
            adapter.updateData(plans.map { GoalPlan(it.name, it.percentage) })
        })
    }
}
