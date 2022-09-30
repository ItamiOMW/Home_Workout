package com.example.homeworkout.domain.usecases

import com.example.homeworkout.domain.repository.WorkoutRepository
import javax.inject.Inject

class GetCountOfCompletedWorkoutsUseCase @Inject constructor(
    private val repository: WorkoutRepository,
) {
    suspend operator fun invoke() = repository.getCountOfCompletedWorkouts()
}