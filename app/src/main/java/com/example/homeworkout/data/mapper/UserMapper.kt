package com.example.homeworkout.data.mapper

import com.example.homeworkout.domain.models.UserModel
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class UserMapper @Inject constructor() {

    fun mapFireBaseUserToUserModel(user: FirebaseUser) = UserModel(
        user.displayName.toString(),
        user.photoUrl.toString()
    )

}