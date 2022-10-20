package com.example.homeworkout.data.database.db_models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "planned_workout_models")
data class PlannedWorkoutDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "workoutDbModel")
    val workoutDbModel: WorkoutDbModel,
    @ColumnInfo(name = "isCompleted")
    val isCompleted: Boolean
)