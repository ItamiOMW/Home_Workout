package com.example.homeworkout.domain.usecase.workout_repository_usecases

import com.example.homeworkout.domain.models.WorkoutModel
import com.example.homeworkout.domain.repository.WorkoutRepository
import javax.inject.Inject

class CompleteWorkoutUseCase @Inject constructor(private val repository: WorkoutRepository) {

    operator fun invoke(workoutModel: WorkoutModel) =
        repository.completeWorkout(workoutModel)
}