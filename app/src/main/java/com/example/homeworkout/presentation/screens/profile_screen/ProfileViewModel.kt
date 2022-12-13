package com.example.homeworkout.presentation.screens.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeworkout.domain.models.Response
import com.example.homeworkout.domain.usecase.auth_repository_usecases.GetCurrentUserUseCase
import com.example.homeworkout.domain.usecase.auth_repository_usecases.SignOutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUIState())
    val state = _state.asStateFlow()

    init {
        getCurrentUser()
    }


    fun signOut() {

        viewModelScope.launch {
            signOutUseCase.invoke().collect {
                when (it) {
                    is Response.Loading -> {
                        _state.value = Loading
                    }
                    is Response.Failed -> {
                        _state.value = Failure(it.message)
                    }
                    is Response.Success -> {
                        _state.value = SignedOutSuccessfully(it.data)
                    }
                }
            }

        }

    }

    private fun getCurrentUser() {

        viewModelScope.launch {
            getCurrentUserUseCase.invoke().collect {
                when (it) {
                    is Response.Loading -> {
                        _state.value = Loading
                    }
                    is Response.Failed -> {
                        _state.value = Failure(it.message)
                    }
                    is Response.Success -> {
                        if (it.data != null) {
                            _state.value = CurrentUser(it.data)
                        }
                    }
                }
            }
        }

    }
}