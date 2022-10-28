package com.example.homeworkout.presentation.screens.login_screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeworkout.domain.usecase.auth_repository_usecases.SignInUseCase
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<LoginViewModelState>()
    val state: LiveData<LoginViewModelState>
        get() = _state

    fun signIn(credential: AuthCredential) {

        viewModelScope.launch {
            val isSucceed = signInUseCase.invoke(credential)
            //JUST FOR TEST
            Log.d("TEST_BUG", isSucceed.toString())
            _state.postValue(AuthenticationSucceed(isSucceed))
        }

    }
}