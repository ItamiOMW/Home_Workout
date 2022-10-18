package com.example.homeworkout.presentation.screens.calendar_screen

import android.app.Application
import android.text.format.DateFormat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeworkout.R
import com.example.homeworkout.domain.models.PlannedWorkoutModel
import com.example.homeworkout.domain.usecases.DeletePlannedWorkoutUseCase
import com.example.homeworkout.domain.usecases.GetPlannedWorkoutsByDateUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class CalendarViewModel @Inject constructor(
    private val application: Application,
    private val getPlannedWorkoutsByDateUseCase: GetPlannedWorkoutsByDateUseCase,
    private val deletePlannedWorkoutUseCase: DeletePlannedWorkoutUseCase,
) : ViewModel() {

    private var _state = MutableLiveData<CalendarViewModelState>()
    val state: LiveData<CalendarViewModelState>
        get() = _state

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
        viewModelScope.launch(Dispatchers.IO) {
            deletePlannedWorkoutUseCase.invoke(plannedWorkoutModel)
            updateList(date)
        }
    }


    private fun updateList(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.postValue(PlannedWorkoutList(getPlannedWorkoutsByDateUseCase.invoke(date)))
        }
    }

    private fun getCurrentDate(): String {
        val date = Date()
        return DateFormat.format(application.getString(R.string.date_format), date.time).toString()
    }
}