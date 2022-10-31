package com.example.homeworkout.presentation.screens.profile_screen

import com.example.homeworkout.domain.models.UserModel

open class ProfileViewModelState

class SignedOutSuccessfully(val boolean: Boolean): ProfileViewModelState()

class CurrentUser(val user: UserModel): ProfileViewModelState()

