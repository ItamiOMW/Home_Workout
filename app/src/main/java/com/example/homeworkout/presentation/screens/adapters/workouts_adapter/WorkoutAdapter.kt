package com.example.homeworkout.presentation.screens.adapters.workouts_adapter

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.ListAdapter
import com.example.homeworkout.databinding.WorkoutItemBinding
import com.example.homeworkout.domain.models.WorkoutModel
import javax.inject.Inject

class WorkoutAdapter @Inject constructor(private val application: Application) :
    ListAdapter<WorkoutModel, WorkoutViewHolder>(DiffUtilItemCallbackWorkout()) {

    var onItemClicked: ((WorkoutModel) -> Unit)? = null
    var onItemLongClicked: ((WorkoutModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val binding = WorkoutItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workoutModel = currentList[position]
        with(holder.binding) {
            llWorkout.background = AppCompatResources.getDrawable(application, workoutModel.imagePath)
            tvWorkoutName.text = workoutModel.title
            tvWorkoutDuration.text = workoutModel.duration.toString()
        }
        holder.itemView.setOnClickListener {
            onItemClicked?.invoke(workoutModel)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClicked?.invoke(workoutModel)
            true
        }
    }

}