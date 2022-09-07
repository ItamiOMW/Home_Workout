package com.example.homeworkout.domain.usecases

import com.example.homeworkout.domain.models.WorkoutModel
import com.example.homeworkout.domain.repository.WorkoutRepository
import javax.inject.Inject

class CreateWorkoutUseCase @Inject constructor (private val repository: WorkoutRepository) {

    suspend operator fun invoke(workoutModel: WorkoutModel) =
        repository.addWorkout(workoutModel)

}