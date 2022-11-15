package com.example.homeworkout.presentation.screens.calendar_screen

import android.app.Application
import android.text.format.DateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeworkout.R
import com.example.homeworkout.domain.models.PlannedWorkoutModel
import com.example.homeworkout.domain.models.Response
import com.example.homeworkout.domain.usecase.workout_repository_usecases.DeletePlannedWorkoutUseCase
import com.example.homeworkout.domain.usecase.workout_repository_usecases.GetPlannedWorkoutsByDateUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class CalendarViewModel @Inject constructor(
    private val application: Application,
    private val getPlannedWorkoutsByDateUseCase: GetPlannedWorkoutsByDateUseCase,
    private val deletePlannedWorkoutUseCase: DeletePlannedWorkoutUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CalendarUIState())
    val state = _state.asStateFlow()

    private var _date: String = getCurrentDate()
    val date: String = _date

    init {
        updateList(_date)
    }


    fun updateDate(date: String) {
        this._date = date
        updateList(date)
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
                    }
                    is Response.Failed -> {
                        _state.value = Failure(it.message)
                    }
                }
            }
            updateList(date)
        }
    }


    private fun updateList(date: String) {
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

    private fun getCurrentDate(): String {
        val date = Date()
        return DateFormat.format(application.getString(R.string.date_format), date.time).toString()
    }
}