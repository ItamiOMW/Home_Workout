package com.example.homeworkout.data.database.firebase

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserAuthDatabase @Inject constructor() : UserAuthHelper {

    override suspend fun signIn(credential: AuthCredential): Boolean {
        var ifSuccessful = false
        fAuth
        fAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                ifSuccessful = it.isSuccessful
                Log.d("testing", it.isSuccessful.toString())
            }.await()
        return ifSuccessful
    }

    override suspend fun signOut(): Boolean {
        try {
            fAuth.signOut()
        } catch (e: Exception) {
            return false
        }
        return true
    }

    override suspend fun checkSignedIn(): Boolean {
        return fAuth.currentUser != null
    }

    override suspend fun getCurrentUser(): FirebaseUser? {
        return fAuth.currentUser
    }

    companion object {
        private val fAuth = FirebaseAuth.getInstance()
    }

}