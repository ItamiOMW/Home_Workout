package com.example.homeworkout.presentation.screens.adapters.workouts_adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.homeworkout.domain.models.WorkoutModel

class DiffUtilItemCallbackWorkout: DiffUtil.ItemCallback<WorkoutModel>() {

    override fun areItemsTheSame(oldItem: WorkoutModel, newItem: WorkoutModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: WorkoutModel, newItem: WorkoutModel): Boolean {
        return oldItem == newItem
    }

}