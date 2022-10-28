package com.example.homeworkout.domain.usecase.auth_repository_usecases

import com.example.homeworkout.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential
import javax.inject.Inject

class SignInUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend operator fun invoke(credential: AuthCredential) = repository.signIn(credential)
}