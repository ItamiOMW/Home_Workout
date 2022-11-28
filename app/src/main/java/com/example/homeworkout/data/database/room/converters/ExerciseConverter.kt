package com.example.homeworkout.data.database.room.converters

import androidx.room.TypeConverter
import com.example.homeworkout.domain.models.ExerciseModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ExerciseConverter {

    @TypeConverter
    fun fromExerciseList(value: List<ExerciseModel>): String {
        val gson = Gson()
        val type = object : TypeToken<List<ExerciseModel>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toExerciseList(value: String): List<ExerciseModel> {
        val gson = Gson()
        val type = object : TypeToken<List<ExerciseModel>>() {}.type
        return gson.fromJson(value, type)
    }

}