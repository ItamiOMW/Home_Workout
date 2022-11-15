package com.example.homeworkout.presentation.screens.plan_workout_screen

import com.example.homeworkout.domain.models.WorkoutModel

open class PlanWorkoutUIState

object Loading: PlanWorkoutUIState()

class Failure(val message: String): PlanWorkoutUIState()

class ListWorkouts(val list: List<WorkoutModel>): PlanWorkoutUIState()

class WorkoutPlanned(val boolean: Boolean): PlanWorkoutUIState()

