package com.example.homeworkout.domain.usecase.auth_repository_usecases

import com.example.homeworkout.domain.repository.AuthRepository
import javax.inject.Inject

class CheckSignedInUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend operator fun invoke() = repository.checkSignedIn()
}