package com.example.homeworkout.data.database.room.converters

import androidx.room.TypeConverter
import com.example.homeworkout.domain.models.WorkoutModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

class WorkoutConverter : Serializable {

    @TypeConverter
    fun fromWorkout(value: WorkoutModel): String {
        val gson = Gson()
        val type = object : TypeToken<WorkoutModel>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toWorkoutList(value: String): WorkoutModel {
        val gson = Gson()
        val type = object : TypeToken<WorkoutModel>() {}.type
        return gson.fromJson(value, type)
    }


}