package com.example.homeworkout.presentation.adapters.planned_workouts_adapter

import  android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.ListAdapter
import com.example.homeworkout.R
import com.example.homeworkout.databinding.PlannedWorkoutItemBinding
import com.example.homeworkout.domain.models.PlannedWorkoutModel
import javax.inject.Inject

class PlannedWorkoutAdapter @Inject constructor(private val application: Application) :
    ListAdapter<PlannedWorkoutModel, PlannedWorkoutViewHolder>(DiffUtilItemCallbackPlannedWorkout()) {

    var onItemClicked: ((PlannedWorkoutModel) -> Unit)? = null

    var onItemLongClicked: ((PlannedWorkoutModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlannedWorkoutViewHolder {
        val binding = PlannedWorkoutItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlannedWorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlannedWorkoutViewHolder, position: Int) {
        val plannedWorkoutModel = currentList[position]
        with(holder.binding) {
            llWorkout.background = AppCompatResources.getDrawable(
                application,
                plannedWorkoutModel.workoutModel.imagePath
            )
            tvWorkoutName.text = plannedWorkoutModel.workoutModel.title
            tvWorkoutDuration.text = plannedWorkoutModel.workoutModel.duration.toString()
            if (plannedWorkoutModel.isCompleted) {
                tvIsCompleted.text = application.getText(R.string.completed)
            } else {
                tvIsCompleted.text = application.getText(R.string.empty)
            }
        }
        holder.itemView.setOnClickListener {
            onItemClicked?.invoke(plannedWorkoutModel)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClicked?.invoke(plannedWorkoutModel)
            true
        }
    }

}