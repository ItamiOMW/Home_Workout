package com.example.homeworkout.domain.models

import android.os.Parcelable
import com.example.homeworkout.UNKNOWN_ID
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkoutModel(
    val id: Int = UNKNOWN_ID,
    val title: String,
    val imagePath: Int,
    val duration: Float,
    val listExercises: List<ExerciseModel>,
): Parcelable