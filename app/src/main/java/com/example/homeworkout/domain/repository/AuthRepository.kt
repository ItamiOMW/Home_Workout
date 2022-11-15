package com.example.homeworkout.domain.repository

import com.example.homeworkout.domain.models.Response
import com.example.homeworkout.domain.models.UserModel
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.Flow

//HERE THE METHODS THAT USED WITH AUTHENTICATION
interface AuthRepository {

    fun signIn(credential: AuthCredential): Flow<Response<Boolean>>

    fun signOut(): Flow<Response<Boolean>>

    fun checkSignedIn(): Flow<Response<Boolean>>

    fun getCurrentUser(): Flow<Response<UserModel?>>

}