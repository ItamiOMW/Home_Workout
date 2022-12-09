package com.example.homeworkout.domain.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.homeworkout.data.database.room.converters.ExerciseConverter
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "workout_models")
data class WorkoutModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "title")
    val title: String = "",
    @ColumnInfo(name = "image")
    val image: String = "",
    @ColumnInfo(name = "duration")
    val duration: Float = 0.0f,
    @TypeConverters(ExerciseConverter::class)
    @ColumnInfo(name = "listExercises")
    val listExercises: List<ExerciseModel> = mutableListOf(),
) : Parcelable