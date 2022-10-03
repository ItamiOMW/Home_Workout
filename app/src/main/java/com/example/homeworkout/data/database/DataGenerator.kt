package com.example.homeworkout.data.database

import com.example.homeworkout.R
import com.example.homeworkout.UNKNOWN_ID
import com.example.homeworkout.data.database.db_models.ExerciseDbModel
import com.example.homeworkout.data.database.db_models.WorkoutDbModel

class DataGenerator {

    companion object {

        fun getWorkouts() = listOf(
            WorkoutDbModel(
                id = UNKNOWN_ID,
                title = "FULL BODY WORKOUT",
                imagePath = R.drawable.fullbody,
                duration = 10f,
                listExercises = listOf(
                    ExerciseDbModel(
                        "PUSH UP",
                        10,
                        "PERFORM THE EXERCISE KEEPING THE CORE MUSCLES IN TENSION",
                        "HAVEN'T ADDED YET"
                    )
                )
            )
        )
    }
}