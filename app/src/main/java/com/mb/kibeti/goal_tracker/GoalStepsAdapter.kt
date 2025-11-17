package com.mb.kibeti.goal_tracker

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mb.kibeti.R
import androidx.appcompat.app.AlertDialog

class GoalStepsAdapter(
    private val steps: List<Step>,
    private val onStepChecked: (Step) -> Unit, // Callback for checkbox click
    private val onInfoClick: (Step) -> Unit
) : RecyclerView.Adapter<GoalStepsAdapter.StepViewHolder>() {

    inner class StepViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val checkBox: CheckBox = view.findViewById(R.id.checkBoxStep)
        private val title: TextView = view.findViewById(R.id.tvStepTitle)
        private val percentage: TextView = view.findViewById(R.id.tvStepPercentage)
        private val infoIcon: ImageView = view.findViewById(R.id.ivInfoIcon)

        fun bind(step: Step) {
            Log.d("StepAdapter", "Binding step: ${step.title}, isCompleted: ${step.completed}")

            title.text = step.title
            percentage.text = "${step.percentage}%"
            checkBox.setOnCheckedChangeListener(null) // Prevent multiple triggers
            checkBox.isChecked = step.completed

            // Allow both checking and unchecking
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                Log.d("StepAdapter", "Checkbox clicked: ${step.title}, Checked: $isChecked")
                onStepChecked(step) // Trigger action in GoalStepsActivity
            }

            infoIcon.setOnClickListener {
                Log.d("StepAdapter", "Info icon clicked for: ${step.title}")
                showStepDetails(step)
            }
        }

        private fun showStepDetails(step: Step) {
            AlertDialog.Builder(itemView.context)
                .setTitle(step.title)
                .setMessage(step.description)
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_goal_step, parent, false)
        return StepViewHolder(view)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        holder.bind(steps[position])
    }

    override fun getItemCount(): Int = steps.size
}
