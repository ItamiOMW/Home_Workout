package com.example.homeworkout.presentation.screens.training_screen

import com.example.homeworkout.domain.models.ExerciseModel

open class TrainingUIState

object Loading: TrainingUIState()

class Failure(val message: String): TrainingUIState()

class TimerTime(val time: String): TrainingUIState()

class Exercise(val exerciseModel: ExerciseModel): TrainingUIState()

class CurrentExercisePositionAndAmountOfExercises(val position: String): TrainingUIState()

object IsWorkoutCompleted: TrainingUIState()

object IsPlannedWorkoutCompleted: TrainingUIState()
