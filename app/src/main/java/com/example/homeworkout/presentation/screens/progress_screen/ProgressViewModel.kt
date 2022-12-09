package com.example.homeworkout.presentation.screens.progress_screen

import android.app.Application
import android.net.Uri
import android.text.format.DateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeworkout.R
import com.example.homeworkout.domain.models.Response
import com.example.homeworkout.domain.models.UserInfoModel
import com.example.homeworkout.domain.usecase.workout_repository_usecases.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class ProgressViewModel @Inject constructor(
    private val getListUserInfoUseCase: GetListUserInfoUseCase,
    private val application: Application,
    private val addUserInfoUseCase: AddUserInfoUseCase,
    private val updateUserInfoUseCase: UpdateUserInfoUseCase,
    private val getCountOfCompletedWorkoutsUseCase: GetCountOfCompletedWorkoutsUseCase,
    private val deleteUserInfoUseCase: DeleteUserInfoUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ProgressUIState())
    val state = _state.asStateFlow()

    val listUserInfo = getListUserInfoUseCase.invoke()

    init {
        getCountOfCompletedWorkouts()
        updateDate(getCurrentDate())
    }

    fun updateImageUri(uri: Uri) {
        _state.value = ImageUri(uri)
    }

    fun updateDate(date: String) {
        _state.value = DateForProgressScreen(date)
    }


    fun getCountOfCompletedWorkouts() {

        viewModelScope.launch {
            getCountOfCompletedWorkoutsUseCase.invoke().collect {
                when (it) {
                    is Response.Loading -> {
                        _state.value = Loading
                    }
                    is Response.Failed -> {
                        _state.value = Failure(it.message)
                    }
                    is Response.Success -> {
                        _state.value = CompletedWorkouts(it.data)
                    }
                }
            }
        }

    }

    fun addUserInfo(date: Long, weight: String, photoUri: String) {
        if (checkDate(date) && checkWeight(weight)) {
            viewModelScope.launch {
                addUserInfoUseCase.invoke(UserInfoModel(date, weight, photoUri)).collect {
                    when (it) {
                        is Response.Loading -> {
                            _state.value = Loading
                        }
                        is Response.Failed -> {
                            _state.value = Failure(it.message)
                        }
                        is Response.Success -> {
                            _state.value = AddedUserInfo(it.data)
                        }
                    }
                }
            }
        }
        updateDate(getCurrentDate())
    }

    fun updateUserInfo(date: Long, weight: String, photoUri: String, firebaseId: String) {
        if (checkDate(date) && checkWeight(weight)) {
            viewModelScope.launch {
                updateUserInfoUseCase.invoke(UserInfoModel(date, weight, photoUri, firebaseId))
                    .collect {
                        when (it) {
                            is Response.Loading -> {
                                _state.value = Loading
                            }
                            is Response.Failed -> {
                                _state.value = Failure(it.message)
                            }
                            is Response.Success -> {
                                _state.value = UpdatedUserInfo(it.data)
                            }
                        }
                    }
            }
        }
        updateDate(getCurrentDate())
    }


    fun deleteUserInfo(userInfo: UserInfoModel) {
        viewModelScope.launch {
            deleteUserInfoUseCase.invoke(userInfo).collect {
                when (it) {
                    is Response.Loading -> {
                        _state.value = Loading
                    }
                    is Response.Failed -> {
                        _state.value = Failure(it.message)
                    }
                    is Response.Success -> {
                        _state.value = DeletedUserInfo(it.data)
                    }
                }
            }
        }
    }

    private fun getCurrentDate(): String {
        val date = Date()
        return DateFormat.format(application.getString(R.string.date_format), date.time).toString()
    }

    private fun checkDate(date: Long): Boolean {
        if (date == 0L) {
            _state.value = Failure(application.getString(R.string.date_wasnt_selected))
            return false
        }
        return true
    }

    private fun checkWeight(weight: String): Boolean {
        if (weight.isEmpty()) {
            _state.value = Failure(application.getString(R.string.weight_wasnt_entered))
            return false
        }
        return true
    }


}