package com.example.homeworkout.presentation.adapters.exercises_adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.homeworkout.domain.models.ExerciseModel

class DiffUtilItemCallbackExercise: DiffUtil.ItemCallback<ExerciseModel>() {

    override fun areItemsTheSame(oldItem: ExerciseModel, newItem: ExerciseModel): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: ExerciseModel, newItem: ExerciseModel): Boolean {
        return oldItem == newItem
    }

}