package com.example.homeworkout.presentation.screens.login_screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeworkout.domain.models.Response
import com.example.homeworkout.domain.usecase.auth_repository_usecases.SignInUseCase
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    fun signIn(credential: AuthCredential) {
        viewModelScope.launch {
            signInUseCase.invoke(credential).collect {
                when(it) {
                    is Response.Loading -> {
                        _state.value = Loading
                    }
                    is Response.Failed -> {
                        _state.value = Failure(it.message)
                    }
                    is Response.Success -> {
                        _state.value = AuthenticationSucceed()
                    }
                }
            }
        }
    }

}