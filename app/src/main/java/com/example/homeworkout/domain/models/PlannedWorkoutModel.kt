package com.example.homeworkout.domain.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "planned_workout_models")
data class PlannedWorkoutModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "date")
    val date: Long = 0,
    @ColumnInfo(name = "workoutDbModel")
    val workoutModel: WorkoutModel = WorkoutModel(),
    @ColumnInfo(name = "isCompleted")
    val completed: Boolean = false,
    var firebaseId: String = "",
): Parcelable