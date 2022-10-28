package com.example.homeworkout.domain.usecase.workout_repository_usecases

import com.example.homeworkout.domain.repository.WorkoutRepository
import javax.inject.Inject

class GetUserInfoByDateUseCase @Inject constructor(private val repository: WorkoutRepository) {

    suspend operator fun invoke(date: String) = repository.getUserInfoByDate(date)
}