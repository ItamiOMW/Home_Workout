package com.example.homeworkout.presentation.screens.plan_workout_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeworkout.UNKNOWN_ID
import com.example.homeworkout.WORKOUT_NOT_COMPLETED
import com.example.homeworkout.domain.models.PlannedWorkoutModel
import com.example.homeworkout.domain.models.Response
import com.example.homeworkout.domain.models.WorkoutModel
import com.example.homeworkout.domain.usecase.workout_repository_usecases.AddPlannedWorkoutUseCase
import com.example.homeworkout.domain.usecase.workout_repository_usecases.GetAllWorkoutsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlanWorkoutViewModel @Inject constructor(
    private val addPlannedWorkoutUseCase: AddPlannedWorkoutUseCase,
    private val getAllWorkoutsUseCase: GetAllWorkoutsUseCase,
) : ViewModel() {

    private var _state = MutableStateFlow(PlanWorkoutUIState())
    val state = _state.asStateFlow()

    init {
        getAllWorkouts()
    }

    fun getAllWorkouts() {
        viewModelScope.launch {
            getAllWorkoutsUseCase.invoke().collect {
                when (it) {
                    is Response.Success -> {
                        _state.value = ListWorkouts(it.data)
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

    fun planWorkout(date: Long, workoutModel: WorkoutModel) {
        viewModelScope.launch {
            addPlannedWorkoutUseCase.invoke(
                PlannedWorkoutModel(
                    id = UNKNOWN_ID,
                    date = date,
                    workoutModel = workoutModel,
                    completed = WORKOUT_NOT_COMPLETED
                )
            ).collect {
                when (it) {
                    is Response.Loading -> {
                        _state.value = Loading
                    }
                    is Response.Success -> {
                        _state.value = WorkoutPlanned(it.data)
                    }
                    is Response.Failed -> {
                        _state.value = Failure(it.message)
                    }
                }
            }
        }
    }
}