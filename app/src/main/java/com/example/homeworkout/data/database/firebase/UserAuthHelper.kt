package com.example.homeworkout.data.database.firebase

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser

interface UserAuthHelper {

    suspend fun signIn(credential: AuthCredential): Boolean

    suspend fun signOut(): Boolean

    suspend fun checkSignedIn(): Boolean

    suspend fun getCurrentUser(): FirebaseUser?

}