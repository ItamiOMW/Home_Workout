package com.example.homeworkout.data.database.room.converters

import androidx.room.TypeConverter
import com.example.homeworkout.data.database.room.room_db_models.ExerciseDbModel
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