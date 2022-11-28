package com.example.homeworkout.presentation.screens.parent_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeworkout.domain.models.Response
import com.example.homeworkout.domain.usecase.auth_repository_usecases.CheckSignedInUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val checkSignedInUseCase: CheckSignedInUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MainViewModelState())
    val state = _state.asStateFlow()

    fun checkSignedIn() {
        viewModelScope.launch {
            checkSignedInUseCase.invoke().collectLatest {
                when (it) {
                    is Response.Loading -> {
                        _state.value = Loading
                    }
                    is Response.Failed -> {
                        _state.value = Failure(it.message)
                    }
                    is Response.Success -> {
                        _state.value = IsSignedIn(it.data)
                    }
                }
            }
        }
    }
}