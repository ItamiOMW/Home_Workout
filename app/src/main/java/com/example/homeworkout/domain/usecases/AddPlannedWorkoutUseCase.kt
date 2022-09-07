package com.example.homeworkout.domain.usecases

import com.example.homeworkout.domain.models.PlannedWorkoutModel
import com.example.homeworkout.domain.repository.WorkoutRepository
import javax.inject.Inject

class AddPlannedWorkoutUseCase @Inject constructor (private val repository: WorkoutRepository) {

    suspend operator fun invoke(plannedWorkoutModel: PlannedWorkoutModel) =
        repository.addPlannedWorkout(plannedWorkoutModel)

}