package com.mb.kibeti.room

import android.content.Context
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GoalRepository(private val goalDao: GoalDao, context: Context) {

    private val firestore = FirebaseFirestore.getInstance()
    private val goalsCollection = firestore.collection("goals")

    // Local database LiveData
    val allGoals: LiveData<List<Goal>> = goalDao.getAllGoals()

    // Insert a goal into the local database and sync with Firestore
    suspend fun insert(goal: Goal) {
        withContext(Dispatchers.IO) {
            val id = goalDao.insert(goal).toInt()
            syncWithFirestore(goal.copy(id = id))
        }
    }

    // Update a goal in the local database and sync with Firestore
    suspend fun update(goal: Goal) {
        withContext(Dispatchers.IO) {
            goalDao.update(goal)
            syncWithFirestore(goal)
        }
    }

    // Get a goal by ID from the local database
    suspend fun getGoalById(id: Long) = withContext(Dispatchers.IO) {
        goalDao.getGoalById(id)
    }

    // Delete a goal from the local database and Firestore
    suspend fun deleteGoalById(id: Long) {
        withContext(Dispatchers.IO) {
            goalDao.deleteGoalById(id)
            deleteFromFirestore(id)
        }
    }

    // Sync the goal with Firestore
    private suspend fun syncWithFirestore(goal: Goal) {
        withContext(Dispatchers.IO) {
            goalsCollection.document(goal.id.toString()).set(goal)
                .addOnSuccessListener {
                    // Successfully synced with Firestore
                }
                .addOnFailureListener { e ->
                    // Handle failure to sync
                    e.printStackTrace()
                }
        }
    }

    // Delete the goal from Firestore
    private suspend fun deleteFromFirestore(id: Long) {
        withContext(Dispatchers.IO) {
            goalsCollection.document(id.toString()).delete()
                .addOnSuccessListener {
                    // Successfully deleted from Firestore
                }
                .addOnFailureListener { e ->
                    // Handle failure to delete
                    e.printStackTrace()
                }
        }
    }

    // Fetch all goals from Firestore and update the local database
    suspend fun fetchAllGoalsFromFirestore() {
        withContext(Dispatchers.IO) {
            try {
                val result = Tasks.await(goalsCollection.get())
                val goals = mutableListOf<Goal>()
                for (document in result) {
                    val goal = document.toObject<Goal>()
                    goals.add(goal)
                }
                // Update local database with fetched goals
                updateLocalDatabase(goals)
            } catch (e: Exception) {
                // Handle failure to fetch from Firestore
                e.printStackTrace()
            }
        }
    }

    // Helper function to clear and update the local database
    private suspend fun updateLocalDatabase(goals: List<Goal>) {
        withContext(Dispatchers.IO) {
            goalDao.clearAllGoals()
            goalDao.insertAll(goals)
        }
    }
}
