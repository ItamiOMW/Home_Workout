package com.example.homeworkout.presentation.screens.plane_workout_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeworkout.domain.models.PlannedWorkoutModel
import com.example.homeworkout.domain.models.WorkoutModel
import com.example.homeworkout.domain.usecases.AddPlannedWorkoutUseCase
import com.example.homeworkout.domain.usecases.GetAllWorkoutsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlaneWorkoutViewModel @Inject constructor(
    private val addPlannedWorkoutUseCase: AddPlannedWorkoutUseCase,
    private val getAllWorkoutsUseCase: GetAllWorkoutsUseCase
) : ViewModel() {

    val workoutList = getAllWorkoutsUseCase.invoke()

    fun planeWorkout(workout: WorkoutModel, date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            addPlannedWorkoutUseCase.invoke(
                PlannedWorkoutModel(
                    id = PlannedWorkoutModel.UNKNOWN_ID,
                    date = date,
                    workoutModel = workout,
                    isCompleted = PlannedWorkoutModel.UNKNOWN_IF_COMPLETED
                )
            )
        }
    }
}