package com.mb.kibeti.screens.roomgoal

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GoalPlanEntity::class], version = 1, exportSchema = false)
abstract class GoalPlannerDatabase : RoomDatabase() {

    abstract fun goalPlanDao(): GoalPlanDao

    companion object {
        @Volatile
        private var INSTANCE: GoalPlannerDatabase? = null

        fun getDatabase(context: Context): GoalPlannerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GoalPlannerDatabase::class.java,
                    "goal_planner_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}