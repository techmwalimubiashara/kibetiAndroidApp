package com.mb.kibeti.screens

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mb.kibeti.databinding.ItemGoalPlanBinding
import com.mb.kibeti.model.GoalPlan

class GoalPlanAdapter(private var plans: List<GoalPlan>) :
    RecyclerView.Adapter<GoalPlanAdapter.GoalPlanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalPlanViewHolder {
        val binding = ItemGoalPlanBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GoalPlanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GoalPlanViewHolder, position: Int) {
        holder.bind(plans[position])
    }

    override fun getItemCount(): Int = plans.size

    fun updateData(newPlans: List<GoalPlan>) {
        plans = newPlans
        notifyDataSetChanged()
    }

    class GoalPlanViewHolder(private val binding: ItemGoalPlanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(goalPlan: GoalPlan) {
            binding.goalPlan = goalPlan
            binding.executePendingBindings()
        }
    }
}
