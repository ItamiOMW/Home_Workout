package com.example.homeworkout.presentation.screens.profile_screen

import com.example.homeworkout.domain.models.UserModel

open class ProfileUIState

object Loading: ProfileUIState()

class SignedOutSuccessfully(val boolean: Boolean): ProfileUIState()

class CurrentUser(val user: UserModel): ProfileUIState()

class Failure(val message: String): ProfileUIState()

