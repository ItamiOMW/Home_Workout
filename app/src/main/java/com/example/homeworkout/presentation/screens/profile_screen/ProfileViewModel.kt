package com.example.homeworkout.presentation.screens.profile_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeworkout.domain.usecase.auth_repository_usecases.GetCurrentUserUseCase
import com.example.homeworkout.domain.usecase.auth_repository_usecases.SignOutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<ProfileViewModelState>()
    val state: LiveData<ProfileViewModelState>
        get() = _state

    init {
        getCurrentUser()
    }


    fun signOut() {

        viewModelScope.launch(Dispatchers.IO) {
            val isSignedOut = signOutUseCase.invoke()
            _state.postValue(SignedOutSuccessfully(isSignedOut))
        }

    }

    private fun getCurrentUser() {

        viewModelScope.launch(Dispatchers.IO) {
            val user = getCurrentUserUseCase.invoke()
            if (user != null) {
                _state.postValue(CurrentUser(user))
            }
        }

    }
}