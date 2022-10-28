package com.example.homeworkout.data.database.room.room_db_models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.homeworkout.data.database.room.converters.ExerciseConverter

@Entity(tableName = "workout_models")
data class WorkoutDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "imagePath")
    val imagePath: Int,
    @ColumnInfo(name = "duration")
    val duration: Float,
    @TypeConverters(ExerciseConverter::class)
    @ColumnInfo(name = "listExercises")
    val listExercises: List<ExerciseDbModel>,
)