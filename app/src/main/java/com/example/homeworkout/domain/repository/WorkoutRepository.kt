package com.example.homeworkout.domain.repository

import androidx.lifecycle.LiveData
import com.example.homeworkout.domain.models.PlannedWorkoutModel
import com.example.homeworkout.domain.models.WorkoutModel

interface WorkoutRepository {

    //METHODS FOR WORK WITH PLANNED WORKOUT MODEL
    fun getPlannedWorkoutsByDate(date: String): LiveData<List<PlannedWorkoutModel>>

    suspend fun addPlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel)

    suspend fun deletePlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel)

    suspend fun completePlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel)

    //METHODS FOR WORK WITH WORKOUT MODEL
    fun getAllWorkouts(): LiveData<List<WorkoutModel>>

    suspend fun addWorkout(workoutModel: WorkoutModel)

    suspend fun completeWorkout(workoutModel: WorkoutModel)
}