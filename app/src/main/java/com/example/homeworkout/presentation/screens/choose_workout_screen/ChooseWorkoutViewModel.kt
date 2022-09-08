package com.example.homeworkout.presentation.screens.choose_workout_screen

import androidx.lifecycle.ViewModel
import com.example.homeworkout.domain.usecases.GetAllWorkoutsUseCase
import javax.inject.Inject

class ChooseWorkoutViewModel @Inject constructor(
    private val getAllWorkoutsUseCase: GetAllWorkoutsUseCase
) : ViewModel() {

    val workoutList = getAllWorkoutsUseCase.invoke()
}