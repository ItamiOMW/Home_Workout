package com.example.homeworkout.domain.repository

import androidx.lifecycle.LiveData
import com.example.homeworkout.domain.models.Response
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser

//HERE THE METHODS THAT USED WITH AUTHENTICATION
interface AuthRepository {

    suspend fun signIn(credential: AuthCredential): Boolean

    suspend fun signOut(): Boolean

    suspend fun checkSignedIn(): Boolean

    suspend fun getCurrentUser(): FirebaseUser?

}