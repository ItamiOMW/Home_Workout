package com.example.homeworkout.presentation.adapters.planned_workouts_adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.homeworkout.domain.models.PlannedWorkoutModel

class DiffUtilItemCallbackPlannedWorkout: DiffUtil.ItemCallback<PlannedWorkoutModel>() {

    override fun areItemsTheSame(oldItem: PlannedWorkoutModel, newItem: PlannedWorkoutModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PlannedWorkoutModel, newItem: PlannedWorkoutModel): Boolean {
        return oldItem == newItem
    }

}