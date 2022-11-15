package com.example.homeworkout.presentation.screens.choose_workout_screen

import com.example.homeworkout.domain.models.WorkoutModel

open class ChooseWorkoutUIState

object Loading: ChooseWorkoutUIState()

class Failure(val message: String): ChooseWorkoutUIState()

class WorkoutList(val list: List<WorkoutModel>): ChooseWorkoutUIState()