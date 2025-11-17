package com.mb.kibeti

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mb.kibeti.room.AppDatabase
import com.mb.kibeti.room.Goal
import com.mb.kibeti.room.GoalRepository

import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class GoalViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GoalRepository = GoalRepository(
        AppDatabase.getDatabase(application).goalDao(),
        application.applicationContext
    )

    val allGoals: LiveData<List<Goal>> = repository.allGoals

    private val _dailySavings = MutableLiveData<Double>()
    val dailySavings: LiveData<Double> get() = _dailySavings

    private val _weeklySavings = MutableLiveData<Double>()
    val weeklySavings: LiveData<Double> get() = _weeklySavings

    private val _monthlySavings = MutableLiveData<Double>()
    val monthlySavings: LiveData<Double> get() = _monthlySavings

    private val _annualSavings = MutableLiveData<Double>()
    val annualSavings: LiveData<Double> get() = _annualSavings

    fun insertGoal(goal: Goal) = viewModelScope.launch {
        repository.insert(goal)
    }

    fun updateGoal(goal: Goal) = viewModelScope.launch {
        repository.update(goal)
    }

    fun deleteGoalById(id: Long) = viewModelScope.launch {
        repository.deleteGoalById(id)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateSavingsPlan(goal: Goal): Map<String, String> {
        val days = daysBetween(goal.startDate, goal.endDate)
        val amountNeeded = goal.amountNeeded ?: return mapOf(
            "daily" to "N/A",
            "weekly" to "N/A",
            "monthly" to "N/A",
            "annual" to "N/A"
        )
        if (days <= 0) return mapOf(
            "daily" to "N/A",
            "weekly" to "N/A",
            "monthly" to "N/A",
            "annual" to "N/A"
        )

        val dailySavings = amountNeeded / days
        val weeklySavings = dailySavings * 7
        val monthlySavings = dailySavings * 30
        val annualSavings = dailySavings * 365

        return mapOf(
            "daily" to "%.2f".format(dailySavings),
            "weekly" to "%.2f".format(weeklySavings),
            "monthly" to "%.2f".format(monthlySavings),
            "annual" to "%.2f".format(annualSavings)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun daysBetween(startDate: String, endDate: String): Long {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return try {
            val start = LocalDate.parse(startDate, formatter)
            val end = LocalDate.parse(endDate, formatter)
            Duration.between(start.atStartOfDay(), end.atStartOfDay()).toDays()
        } catch (e: DateTimeParseException) {
            e.printStackTrace()
            0 // Return 0 if there's an error parsing the dates
        }
    }

    fun fetchGoalsFromOnline() = viewModelScope.launch {
        repository.fetchAllGoalsFromFirestore()
    }
}
