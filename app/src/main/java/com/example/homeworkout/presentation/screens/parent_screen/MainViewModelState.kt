package com.example.homeworkout.presentation.screens.parent_screen

open class MainViewModelState

object Loading: MainViewModelState()

class IsSignedIn(val isSignedIn: Boolean): MainViewModelState()

class Failure(val message: String): MainViewModelState()