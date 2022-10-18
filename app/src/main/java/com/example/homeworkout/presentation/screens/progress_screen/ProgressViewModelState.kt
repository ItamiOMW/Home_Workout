package com.example.homeworkout.presentation.screens.progress_screen

import android.net.Uri

open class ProgressViewModelState()

class CompletedWorkouts(val count: Int): ProgressViewModelState()

class ImageUri(val uri: Uri): ProgressViewModelState()

class DateForProgressScreen(val date: String): ProgressViewModelState()

class DateFailure(val boolean: Boolean): ProgressViewModelState()

class WeightFailure(val boolean: Boolean): ProgressViewModelState()


