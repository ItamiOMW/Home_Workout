package com.example.homeworkout.presentation.screens.calendar_screen

import com.example.homeworkout.domain.models.PlannedWorkoutModel

open class CalendarUIState

object Loading: CalendarUIState()

class PlannedWorkoutList(val list: List<PlannedWorkoutModel>): CalendarUIState()

class Failure(val message: String): CalendarUIState()

object WorkoutDeleted: CalendarUIState()