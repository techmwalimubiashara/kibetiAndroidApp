package com.mb.kibeti

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mb.kibeti.databinding.ItemGoalBinding
import com.mb.kibeti.room.Goal

class GoalAdapter(private val viewModel: GoalViewModel) :
    RecyclerView.Adapter<GoalAdapter.GoalViewHolder>() {

    private var goals: List<Goal> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val binding = ItemGoalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GoalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val goal = goals[position]
        holder.bind(goal)
    }

    override fun getItemCount(): Int = goals.size

    fun submitList(newGoals: List<Goal>) {
        goals = newGoals
        notifyDataSetChanged()
    }

    inner class GoalViewHolder(private val binding: ItemGoalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(goal: Goal) {
            binding.goal = goal
            binding.viewModel = viewModel

            binding.downloadButton.setOnClickListener {
                val savingsPlan = viewModel.calculateSavingsPlan(goal)
                PdfUtils.generatePdf(binding.root.context, goal, savingsPlan)
            }
            binding.executePendingBindings()
        }
    }
}
