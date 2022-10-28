package com.example.homeworkout.presentation.screens.parent_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeworkout.domain.usecase.auth_repository_usecases.CheckSignedInUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val checkSignedInUseCase: CheckSignedInUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<MainViewModelState>()
    val state: LiveData<MainViewModelState>
        get() = _state

    fun checkSignedIn() {
        viewModelScope.launch {
            _state.postValue(IsSignedIn(checkSignedInUseCase.invoke()))
        }
    }
}