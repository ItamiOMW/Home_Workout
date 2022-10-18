package com.example.homeworkout.presentation.screens.training_screen

import com.example.homeworkout.domain.models.ExerciseModel

open class TrainingViewModelState()

class TimerTime(val time: String): TrainingViewModelState()

class Exercise(val exerciseModel: ExerciseModel): TrainingViewModelState()

class CurrentExercisePositionAndAmountOfExercises(val position: String): TrainingViewModelState()

class IsWorkoutCompleted(val any: Any): TrainingViewModelState()
