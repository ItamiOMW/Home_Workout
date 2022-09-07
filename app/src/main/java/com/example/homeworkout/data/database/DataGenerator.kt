package com.example.homeworkout.data.database

import com.example.homeworkout.R
import com.example.homeworkout.data.database.db_models.ExerciseDbModel
import com.example.homeworkout.data.database.db_models.WorkoutDbModel
import com.example.homeworkout.domain.models.WorkoutModel

class DataGenerator {

    companion object {

        fun getWorkouts() = listOf(
            WorkoutDbModel(
                id = WorkoutModel.UNKNOWN_ID,
                title = "FULL BODY WORKOUT",
                imagePath = R.drawable.fullbody,
                duration = 10f,
                listExercises = listOf(
                    ExerciseDbModel(
                        "PUSH UP",
                        10,
                        "HAVEN'T ADDED GIFS YET"
                    )
                )
            )
        )
    }
}