package com.example.homeworkout.presentation.screens.choose_workout_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeworkout.domain.models.Response
import com.example.homeworkout.domain.usecase.workout_repository_usecases.GetAllWorkoutsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChooseWorkoutViewModel @Inject constructor(
    private val getAllWorkoutsUseCase: GetAllWorkoutsUseCase,
) : ViewModel() {

    private var _state = MutableStateFlow(ChooseWorkoutUIState())
    val state = _state.asStateFlow()

    init {
        getAllWorkouts()
    }

    fun getAllWorkouts() {
        viewModelScope.launch {
            getAllWorkoutsUseCase.invoke().collectLatest {
                when (it) {
                    is Response.Success -> {
                        _state.value = WorkoutList(it.data)
                    }
                    is Response.Failed -> {
                        _state.value = Failure(it.message)
                    }
                    is Response.Loading -> {
                        _state.value = Loading
                    }
                }
            }
        }
    }

}