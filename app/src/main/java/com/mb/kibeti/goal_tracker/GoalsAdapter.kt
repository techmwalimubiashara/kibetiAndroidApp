package com.mb.kibeti.goal_tracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mb.kibeti.R

class GoalsAdapter(
    private val goals: Map<String, GoalDetails>,
    private val onSelectionChanged: (Set<GoalDetails>) -> Unit
) : RecyclerView.Adapter<GoalsAdapter.GoalViewHolder>() {

    private val selectedGoals = mutableSetOf<GoalDetails>()

    fun getSelectedGoals(): List<GoalDetails> {
        return selectedGoals.toList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_goal_card, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val goal = goals.values.toList()[position] // Get GoalDetails
        holder.bind(goal)
    }

    override fun getItemCount(): Int = goals.size

    inner class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvGoalTitle: TextView = itemView.findViewById(R.id.tvGoalTitle)
        private val checkGoal: CheckBox = itemView.findViewById(R.id.checkGoal)

        fun bind(goal: GoalDetails) {
            tvGoalTitle.text = goal.name
            checkGoal.setOnCheckedChangeListener(null) // Prevent unwanted triggers
            checkGoal.isChecked = selectedGoals.contains(goal)

            checkGoal.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (selectedGoals.size < 2) {
                        selectedGoals.add(goal)
                    } else {
                        checkGoal.isChecked = false // Prevent selecting more than 2
                    }
                } else {
                    selectedGoals.remove(goal)
                }
                onSelectionChanged(selectedGoals)
            }
        }
    }
}
