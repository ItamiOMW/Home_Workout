package com.example.homeworkout.data.database.room

import android.app.Application
import com.example.homeworkout.R
import com.example.homeworkout.utils.UNKNOWN_ID
import com.example.homeworkout.domain.models.ExerciseModel
import com.example.homeworkout.domain.models.WorkoutModel
import com.example.homeworkout.utils.UriFromDrawableUtil


class DataGenerator {

    fun getWorkouts(application: Application) = listOf(
        WorkoutModel(
            id = UNKNOWN_ID,
            title = "TEST WORKOUT",
            image = UriFromDrawableUtil.getUriFromDrawable(application, R.drawable.fullbody),
            duration = 0f,
            listExercises = listOf(
                ExerciseModel(
                    title = "PUSH UP",
                    reps = 10,
                    description = "PERFORM THE EXERCISE KEEPING THE CORE MUSCLES IN TENSION",
                    exerciseGif = UriFromDrawableUtil.getUriFromDrawable(application, R.drawable.pushup)
                )
            )
        )
    )


}