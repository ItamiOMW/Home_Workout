package com.example.homeworkout.data.database.room.converters

import androidx.room.TypeConverter
import com.example.homeworkout.domain.models.PlannedWorkoutModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class PlannedWorkoutConverter {

    @TypeConverter
    fun fromPlannedWorkout(value: PlannedWorkoutModel): String {
        val gson = Gson()
        val type = object : TypeToken<PlannedWorkoutModel>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toPlannedWorkout(value: String): PlannedWorkoutModel {
        val gson = Gson()
        val type = object : TypeToken<PlannedWorkoutModel>() {}.type
        return gson.fromJson(value, type)
    }
}
