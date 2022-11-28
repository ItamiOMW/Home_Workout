package com.example.homeworkout.data.database.firebase.authentication

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser

interface UserAuthDatabaseHelper {

    suspend fun signIn(credential: AuthCredential)

    suspend fun signOut()

    suspend fun checkSignedIn(): Boolean

    suspend fun getCurrentUser(): FirebaseUser?

}