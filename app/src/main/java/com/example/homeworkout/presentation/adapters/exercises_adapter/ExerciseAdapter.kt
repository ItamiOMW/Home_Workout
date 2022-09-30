package com.example.homeworkout.presentation.adapters.exercises_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.homeworkout.presentation.adapters.user_info_adapter.UserInfoViewHolder
import com.example.homeworkout.databinding.ExerciseItemBinding
import com.example.homeworkout.domain.models.ExerciseModel
import javax.inject.Inject

class ExerciseAdapter @Inject constructor (): ListAdapter<ExerciseModel, ExerciseViewHolder>(
    DiffUtilItemCallbackExercise()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding = ExerciseItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val item = currentList[position]
        with(holder.binding) {
            tvTitle.text = item.title
            tvReps.text = item.reps.toString()
            //TODO ADD GIFS FOR ivExerciseGif
        }
    }

}