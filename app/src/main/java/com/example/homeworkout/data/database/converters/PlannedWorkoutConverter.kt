package com.example.homeworkout.data.database.converters

import androidx.room.TypeConverter
import com.example.homeworkout.data.database.db_models.PlannedWorkoutDbModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlannedWorkoutConverter {

    @TypeConverter
    fun fromPlannedWorkout(value: PlannedWorkoutDbModel): String {
        val gson = Gson()
        val type = object : TypeToken<PlannedWorkoutDbModel>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toPlannedWorkout(value: String): PlannedWorkoutDbModel {
        val gson = Gson()
        val type = object : TypeToken<PlannedWorkoutDbModel>() {}.type
        return gson.fromJson(value, type)
    }
}