package com.mb.kibeti.screens.roomgoal

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GoalPlanDao {
//
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goalPlan: GoalPlanEntity)

    @Query("SELECT * FROM goal_plan_table")
    fun getAllGoalPlans(): LiveData<List<GoalPlanEntity>>

    @Query("DELETE FROM goal_plan_table")
    suspend fun deleteAll()
}