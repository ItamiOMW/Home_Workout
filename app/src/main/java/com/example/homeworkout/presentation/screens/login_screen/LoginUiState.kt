package com.example.homeworkout.presentation.screens.login_screen

open class LoginUiState

object Loading: LoginUiState()

class AuthenticationSucceed: LoginUiState()

class Failure(val message: String): LoginUiState()