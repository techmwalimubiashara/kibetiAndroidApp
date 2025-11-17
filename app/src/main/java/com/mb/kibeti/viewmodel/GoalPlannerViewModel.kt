package com.mb.kibeti.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.mb.kibeti.screens.roomgoal.GoalPlanEntity
//import com.example.plannerapp.room.GoalPlanDao
//import com.example.plannerapp.room.GoalPlannerDatabase
import com.mb.kibeti.model.Investment
import com.mb.kibeti.screens.roomgoal.GoalPlanDao
import com.mb.kibeti.screens.roomgoal.GoalPlannerDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GoalPlannerViewModel(application: Application) : AndroidViewModel(application) {

    private val goalPlanDao: GoalPlanDao = GoalPlannerDatabase.getDatabase(application).goalPlanDao()
    val allGoalPlans: LiveData<List<GoalPlanEntity>> = goalPlanDao.getAllGoalPlans()

    fun calculateGoals(investment: Investment) {
        val funds = listOf("Money Market", "Fixed Income", "Balanced", "Equity","Properties - Commercial")

        funds.forEach { fund ->
            val score = calculateScore(fund, investment)
            val goalPlan = GoalPlanEntity(name = fund, percentage = score * 25)
            insertGoalPlan(goalPlan)
        }
    }

    private fun calculateScore(fund: String, investment: Investment): Int {
        val ageScore = getAgeScore(fund, investment.age)
        val experienceScore = getExperienceScore(fund, investment.experience)
        val appetiteScore = getAppetiteScore(fund, investment.appetite)
        val toleranceScore = getToleranceScore(fund, investment.tolerance)

        return ageScore + experienceScore + appetiteScore + toleranceScore
    }

    private fun getAgeScore(fund: String, age: Int): Int {
        return when (fund) {
            "Money Market" -> 1
            "Fixed Income" -> 1
            "Properties - Commercial" -> if (age <= 60) 1 else 0
            "Balanced" -> if (age <= 60) 1 else 0
            "Equity" -> if (age <= 55) 1 else 0
            else -> 0
        }
    }

    private fun getExperienceScore(fund: String, experience: Int): Int {
        return when (fund) {
            "Money Market" -> 1
            "Fixed Income" -> if (experience >= 2) 1 else 0
            "Balanced" -> if (experience >= 3) 1 else 0
            "Equity" -> if (experience >= 3) 1 else 0
            "Properties - Commercial" -> if (experience <= 1) 1 else 0
            else -> 0
        }
    }

    private fun getAppetiteScore(fund: String, appetite: Int): Int {
        return when (fund) {
            "Money Market" -> 1
            "Fixed Income" -> if (appetite >= 2) 1 else 0
            "Balanced" -> if (appetite >= 3) 1 else 0
            "Equity" -> if (appetite >= 3) 1 else 0
            "Properties - Commercial" -> if (appetite <= 5) 1 else 0
            else -> 0
        }
    }

    private fun getToleranceScore(fund: String, tolerance: Int): Int {
        return when (fund) {
            "Money Market" -> 1
            "Fixed Income" -> if (tolerance >= 2) 1 else 0
            "Balanced" -> if (tolerance >= 3) 1 else 0
            "Equity" -> if (tolerance >= 4) 1 else 0
            "Properties - Commercial" -> if (tolerance <= 5) 1 else 0
            else -> 0
        }
    }

    private fun insertGoalPlan(goalPlan: GoalPlanEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            goalPlanDao.insert(goalPlan)
        }
    }

    fun deleteAllGoalPlans() {
        viewModelScope.launch(Dispatchers.IO) {
            goalPlanDao.deleteAll()
        }
    }
}