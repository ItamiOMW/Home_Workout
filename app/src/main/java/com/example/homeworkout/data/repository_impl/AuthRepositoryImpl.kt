package com.example.homeworkout.data.repository_impl

import com.example.homeworkout.data.database.firebase.UserAuthHelper
import com.example.homeworkout.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authHelper: UserAuthHelper
) : AuthRepository {

    override suspend fun signIn(credential: AuthCredential): Boolean {
        return authHelper.signIn(credential)
    }

    override suspend fun signOut(): Boolean {
        return authHelper.signOut()
    }

    override suspend fun checkSignedIn(): Boolean {
        return authHelper.checkSignedIn()
    }

    override suspend fun getCurrentUser(): FirebaseUser? {
        return authHelper.getCurrentUser()
    }

}