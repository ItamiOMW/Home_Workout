package com.example.homeworkout.domain.usecase.workout_repository_usecases

import com.example.homeworkout.domain.models.PlannedWorkoutModel
import com.example.homeworkout.domain.repository.WorkoutRepository
import javax.inject.Inject

class CompletePlannedWorkoutUseCase @Inject constructor(private val repository: WorkoutRepository) {

    suspend operator fun invoke(plannedWorkoutModel: PlannedWorkoutModel) =
        repository.completePlannedWorkout(plannedWorkoutModel)

}