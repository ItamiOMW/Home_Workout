package com.example.homeworkout.data.database.converters

import androidx.room.TypeConverter
import com.example.homeworkout.data.database.db_models.WorkoutDbModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

class WorkoutConverter : Serializable {

    @TypeConverter
    fun fromWorkout(value: WorkoutDbModel): String {
        val gson = Gson()
        val type = object : TypeToken<WorkoutDbModel>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toWorkoutList(value: String): WorkoutDbModel {
        val gson = Gson()
        val type = object : TypeToken<WorkoutDbModel>() {}.type
        return gson.fromJson(value, type)
    }


}