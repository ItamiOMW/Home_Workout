package com.example.homeworkout.presentation.screens.choose_workout_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.homeworkout.domain.usecases.GetAllWorkoutsUseCase
import javax.inject.Inject

class ChooseWorkoutViewModel @Inject constructor(
    getAllWorkoutsUseCase: GetAllWorkoutsUseCase,
) : ViewModel() {

    private var _state = MutableLiveData<ChooseWorkoutViewModelState>()
    val state: LiveData<ChooseWorkoutViewModelState>
        get() = _state

    val list = getAllWorkoutsUseCase.invoke()

}