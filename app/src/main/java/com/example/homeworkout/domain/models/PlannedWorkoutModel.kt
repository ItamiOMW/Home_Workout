package com.example.homeworkout.domain.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "planned_workout_models")
data class PlannedWorkoutModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "workoutDbModel")
    val workoutModel: WorkoutModel,
    @ColumnInfo(name = "isCompleted")
    val isCompleted: Boolean,
    val firebaseId: String = "",
): Parcelable