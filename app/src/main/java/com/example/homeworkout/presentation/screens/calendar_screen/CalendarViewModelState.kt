package com.example.homeworkout.presentation.screens.calendar_screen

import com.example.homeworkout.domain.models.PlannedWorkoutModel

open class CalendarViewModelState

class PlannedWorkoutList(val list: List<PlannedWorkoutModel>): CalendarViewModelState()