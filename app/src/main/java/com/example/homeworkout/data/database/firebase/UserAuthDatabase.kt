package com.example.homeworkout.data.database.firebase

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserAuthDatabase @Inject constructor(

) : UserAuthHelper {

    companion object {
        private val fAuth = FirebaseAuth.getInstance()
    }

    override suspend fun signIn(credential: AuthCredential) {
        fAuth.signInWithCredential(credential).await()
    }

    override suspend fun signOut() {
        fAuth.signOut()
    }

    override suspend fun checkSignedIn() = fAuth.currentUser != null

    override suspend fun getCurrentUser(): FirebaseUser? = fAuth.currentUser

}