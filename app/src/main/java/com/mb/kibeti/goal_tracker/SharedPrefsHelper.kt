package com.mb.kibeti.goal_tracker

import android.content.Context
import android.content.SharedPreferences
import com.mb.kibeti.Variables

class SharedPrefsHelper(context: Context) {

    val utils:Variables = Variables()

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(utils.myPrefName, Context.MODE_PRIVATE)

    fun saveSelectedGoals(goals: List<String>) {
        sharedPreferences.edit().putStringSet("selected_goals", goals.toSet()).apply()
    }

    fun getSelectedGoals(): List<String> {
        return sharedPreferences.getStringSet("selected_goals", emptySet())?.toList() ?: emptyList()
    }

    fun saveCompletedGoal(goal: String) {
        val completedGoals = sharedPreferences.getStringSet("completed_goals", emptySet())?.toMutableSet() ?: mutableSetOf()
        completedGoals.add(goal)
        sharedPreferences.edit().putStringSet("completed_goals", completedGoals).apply()
    }

    fun getCompletedGoals(): Set<String> {
        return sharedPreferences.getStringSet("completed_goals", emptySet()) ?: emptySet()
    }
}