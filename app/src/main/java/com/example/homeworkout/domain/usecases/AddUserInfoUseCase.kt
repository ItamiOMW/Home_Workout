package com.example.homeworkout.domain.usecases

import com.example.homeworkout.domain.models.UserInfoModel
import com.example.homeworkout.domain.repository.WorkoutRepository
import javax.inject.Inject

class AddUserInfoUseCase @Inject constructor(private val repository: WorkoutRepository) {

    suspend operator fun invoke(userInfo: UserInfoModel) = repository.addUserInfo(userInfo)
}