package com.example.homeworkout.data.database.converters

import androidx.room.TypeConverter
import com.example.homeworkout.data.database.db_models.ExerciseDbModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ExerciseConverter {

    @TypeConverter
    fun fromExerciseList(value: List<ExerciseDbModel>): String {
        val gson = Gson()
        val type = object : TypeToken<List<ExerciseDbModel>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toExerciseList(value: String): List<ExerciseDbModel> {
        val gson = Gson()
        val type = object : TypeToken<List<ExerciseDbModel>>() {}.type
        return gson.fromJson(value, type)
    }

}