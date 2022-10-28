package com.example.homeworkout.data.database.room.room_db_models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExerciseDbModel(
    val title: String,
    val reps: Int,
    val description: String,
    val exerciseGif: Int
): Parcelable
