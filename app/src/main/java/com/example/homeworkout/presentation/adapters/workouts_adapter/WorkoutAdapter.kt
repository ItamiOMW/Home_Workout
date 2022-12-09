package com.example.homeworkout.presentation.adapters.workouts_adapter

import android.app.Application
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
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
            Glide.with(application).asDrawable().load(workoutModel.image).into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?,
                ) {
                    llWorkout.background = resource
                }
                override fun onLoadCleared(placeholder: Drawable?) {}
            })
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