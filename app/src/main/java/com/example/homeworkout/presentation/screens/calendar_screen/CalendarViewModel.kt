package com.example.homeworkout.presentation.screens.calendar_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeworkout.domain.models.PlannedWorkoutModel
import com.example.homeworkout.domain.models.Response
import com.example.homeworkout.domain.usecase.workout_repository_usecases.DeletePlannedWorkoutUseCase
import com.example.homeworkout.domain.usecase.workout_repository_usecases.GetPlannedWorkoutsByDateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class CalendarViewModel @Inject constructor(
    private val getPlannedWorkoutsByDateUseCase: GetPlannedWorkoutsByDateUseCase,
    private val deletePlannedWorkoutUseCase: DeletePlannedWorkoutUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CalendarUIState())
    val state = _state.asStateFlow()

    private var _date: Long = getCurrentDate()
    val date: Long
        get() = _date

    init {
        updateList(_date)
    }


    fun updateDate(newDate: Long) {
        _date = newDate
        updateList(newDate)
    }

    fun deletePlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel) {
        viewModelScope.launch {
            deletePlannedWorkoutUseCase.invoke(plannedWorkoutModel).collect {
                when (it) {
                    is Response.Loading -> {
                        _state.value = Loading
                    }
                    is Response.Success -> {
                        _state.value = WorkoutDeleted
                        updateList(date)
                    }
                    is Response.Failed -> {
                        _state.value = Failure(it.message)
                    }
                }
            }
        }
    }


    private fun updateList(date: Long) {
        viewModelScope.launch {
            getPlannedWorkoutsByDateUseCase.invoke(date).collect {
                when (it) {
                    is Response.Loading -> {
                        _state.value = Loading
                    }
                    is Response.Success -> {
                        _state.value = PlannedWorkoutList(it.data)
                    }
                    is Response.Failed -> {
                        _state.value = Failure(it.message)
                    }
                }
            }
        }
    }

    private fun getCurrentDate(): Long {
        return LocalDate.now().toEpochDay()
    }
}