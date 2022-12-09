package com.example.homeworkout.presentation.adapters.planned_workouts_adapter

import android.app.Application
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
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
            Glide.with(application).asDrawable().load(plannedWorkoutModel.workoutModel.image)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?,
                    ) {
                        llWorkout.background = resource
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
            tvWorkoutName.text = plannedWorkoutModel.workoutModel.title
            tvWorkoutDuration.text = plannedWorkoutModel.workoutModel.duration.toString()
            if (plannedWorkoutModel.completed) {
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