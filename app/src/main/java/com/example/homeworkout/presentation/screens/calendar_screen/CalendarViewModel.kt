package com.example.homeworkout.presentation.screens.calendar_screen

import android.app.Application
import android.text.format.DateFormat
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

    private var _plannedWorkoutList = MutableLiveData<List<PlannedWorkoutModel>>()
    val plannedWorkoutList: LiveData<List<PlannedWorkoutModel>>
        get() = _plannedWorkoutList

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
        }
    }


    private fun updateList(date: String) {
        _plannedWorkoutList.value = getPlannedWorkoutsByDateUseCase.invoke(date).value
    }

    private fun getCurrentDate(): String {
        val date = Date()
        return DateFormat.format(application.getString(R.string.date_format), date.time).toString()
    }
}