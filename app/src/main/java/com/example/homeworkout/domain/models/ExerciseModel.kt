package com.example.homeworkout.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExerciseModel(
    val title: String,
    val reps: Int,
    val description: String,
    val exerciseGif: Int
): Parcelable {
}