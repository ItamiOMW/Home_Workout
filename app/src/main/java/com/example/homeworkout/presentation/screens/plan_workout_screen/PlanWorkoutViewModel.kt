package com.example.homeworkout.presentation.screens.plan_workout_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeworkout.UNKNOWN_ID
import com.example.homeworkout.domain.models.PlannedWorkoutModel
import com.example.homeworkout.domain.models.WorkoutModel
import com.example.homeworkout.domain.usecases.AddPlannedWorkoutUseCase
import com.example.homeworkout.domain.usecases.GetAllWorkoutsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlanWorkoutViewModel @Inject constructor(
    private val addPlannedWorkoutUseCase: AddPlannedWorkoutUseCase,
    private val getAllWorkoutsUseCase: GetAllWorkoutsUseCase,
) : ViewModel() {

    private var _state = MutableLiveData<PlanWorkoutViewModelState>()
    val state: LiveData<PlanWorkoutViewModelState>
        get() = _state

    val list = getAllWorkoutsUseCase.invoke()

    fun planWorkout(date: String, workoutModel: WorkoutModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addPlannedWorkoutUseCase.invoke(
                PlannedWorkoutModel(
                    id = UNKNOWN_ID,
                    date = date,
                    workoutModel = workoutModel,
                    isCompleted = PlannedWorkoutModel.UNKNOWN_IF_COMPLETED
                )
            )
        }
    }
}