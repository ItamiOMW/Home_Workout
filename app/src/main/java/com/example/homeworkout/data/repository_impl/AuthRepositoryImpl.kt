package com.example.homeworkout.data.repository_impl

import com.example.homeworkout.data.mapper.UserMapper
import com.example.homeworkout.domain.models.Response
import com.example.homeworkout.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firestoreAuth: FirebaseAuth,
    private val mapper: UserMapper,
) : AuthRepository {

    override fun signIn(credential: AuthCredential) = flow {
        emit(Response.loading())

        firestoreAuth.signInWithCredential(credential)

        emit(Response.success(true))

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override fun signOut() = flow {
        emit(Response.loading())

        firestoreAuth.signOut()

        emit(Response.success(true))

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override fun checkSignedIn() = flow {
        emit(Response.loading())

        val isSignedIn = firestoreAuth.currentUser != null

        emit(Response.success(isSignedIn))

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override fun getCurrentUser() = flow {
        emit(Response.loading())

        val currentFirebaseUser = firestoreAuth.currentUser
        val userModel = currentFirebaseUser?.let { mapper.mapFireBaseUserToUserModel(it) }

        emit(Response.success(userModel))

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

}