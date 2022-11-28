package com.example.homeworkout.domain.usecase.auth_repository_usecases

import com.example.homeworkout.domain.repository.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val repository: AuthRepository) {

    operator fun invoke() = repository.signOut()
}