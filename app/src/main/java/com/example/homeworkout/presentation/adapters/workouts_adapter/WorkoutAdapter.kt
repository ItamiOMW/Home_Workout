package com.example.homeworkout.presentation.adapters.workouts_adapter

import android.app.Application
import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.homeworkout.databinding.WorkoutItemBinding
import com.example.homeworkout.domain.models.WorkoutModel
import com.example.homeworkout.fromByteArrayToBitmap
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
            llWorkout.background = BitmapDrawable(Resources.getSystem(), fromByteArrayToBitmap(workoutModel.image))
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