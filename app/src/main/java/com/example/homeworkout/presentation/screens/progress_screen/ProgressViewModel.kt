package com.example.homeworkout.presentation.screens.progress_screen

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.text.format.DateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeworkout.R
import com.example.homeworkout.domain.models.UserInfoModel
import com.example.homeworkout.domain.usecase.workout_repository_usecases.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class ProgressViewModel @Inject constructor(
    getListUserInfoUseCase: GetListUserInfoUseCase,
    private val application: Application,
    private val addUserInfoUseCase: AddUserInfoUseCase,
    private val updateUserInfoUseCase: UpdateUserInfoUseCase,
    private val getUserInfoByDateUseCase: GetUserInfoByDateUseCase,
    private val getCountOfCompletedWorkoutsUseCase: GetCountOfCompletedWorkoutsUseCase,
    private val deleteUserInfoUseCase: DeleteUserInfoUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<ProgressViewModelState>()
    val state: LiveData<ProgressViewModelState>
        get() = _state

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
        viewModelScope.launch(Dispatchers.IO) {
            _state.postValue(CompletedWorkouts(getCountOfCompletedWorkoutsUseCase.invoke()))
        }
    }

    fun addUserInfo(date: String, weight: String, photo: Bitmap) {
        if (checkDate(date) && checkWeight(weight)) {
            viewModelScope.launch(Dispatchers.IO) {
                addUserInfoUseCase.invoke(UserInfoModel(date, weight, photo))
            }
        } else {
            resetFailures()
        }
        updateDate(getCurrentDate())
    }

    fun updateUserInfo(date: String, weight: String, photo: Bitmap) {
        if (checkDate(date) && checkWeight(weight)) {
            viewModelScope.launch(Dispatchers.IO) {
                updateUserInfoUseCase.invoke(UserInfoModel(date, weight, photo))
            }
        } else {
            resetFailures()
        }
        updateDate(getCurrentDate())
    }

    fun getUserInfoByDate(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getUserInfoByDateUseCase.invoke(date)
        }
    }

    fun deleteUserInfo(userInfo: UserInfoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteUserInfoUseCase.invoke(userInfo)
        }
    }

    private fun getCurrentDate(): String {
        val date = Date()
        return DateFormat.format(application.getString(R.string.date_format), date.time).toString()
    }

    private fun checkDate(date: String): Boolean {
        if (date.isEmpty()) {
            _state.value = DateFailure(true)
            return false
        }
        return true
    }

    private fun checkWeight(weight: String): Boolean {
        if (weight.isEmpty()) {
            _state.value = WeightFailure(true)
            return false
        }
        return true
    }

    private fun resetFailures() {
        _state.value = DateFailure(false)
        _state.value = WeightFailure(false)
    }

}