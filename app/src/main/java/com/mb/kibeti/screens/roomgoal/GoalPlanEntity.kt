package com.mb.kibeti.screens.roomgoal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goal_plan_table")
data class GoalPlanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val percentage: Int
)