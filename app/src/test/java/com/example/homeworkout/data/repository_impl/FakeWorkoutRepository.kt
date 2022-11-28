package com.example.homeworkout.data.repository_impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.homeworkout.domain.repository.WorkoutRepository

class FakeWorkoutRepository : WorkoutRepository {

    val plannedWorkoutList = mutableListOf<PlannedWorkoutModel>()

    val workoutList = mutableListOf<WorkoutModel>()

    val userInfoList = mutableListOf<UserInfoModel>()

    var countOfCompletedWorkouts = 0

    override suspend fun getPlannedWorkoutsByDate(date: String): List<PlannedWorkoutModel> {
        return plannedWorkoutList.filter { it.date == date }
    }

    override suspend fun addPlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel) {
        plannedWorkoutList.add(plannedWorkoutModel)
    }

    override suspend fun deletePlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel) {
        plannedWorkoutList.remove(plannedWorkoutModel)
    }

    override suspend fun completePlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel) {
        val index = plannedWorkoutList.indexOf(plannedWorkoutModel)
        plannedWorkoutList.removeAt(index)
        plannedWorkoutList.add(index, PlannedWorkoutModel(
            plannedWorkoutModel.id,
            plannedWorkoutModel.date,
            plannedWorkoutModel.workoutModel,
            true
        ))
        completeWorkout(plannedWorkoutModel.workoutModel)
    }

    override fun getAllWorkouts(): LiveData<List<WorkoutModel>> {
        return MutableLiveData(workoutList)
    }

    override suspend fun addWorkout(workoutModel: WorkoutModel) {
        workoutList.add(workoutModel)
    }

    override suspend fun completeWorkout(workoutModel: WorkoutModel) {
        countOfCompletedWorkouts++
    }

    override fun getListUserInfo(): LiveData<List<UserInfoModel>> {
        return MutableLiveData(userInfoList)
    }

    override suspend fun getUserInfoByDate(date: String): UserInfoModel {
        return userInfoList.first { it.date == date }
    }

    override suspend fun updateUserInfo(userInfoModel: UserInfoModel) {
        userInfoList.map {
            if (it.date == userInfoModel.date) {
                UserInfoModel(userInfoModel.date, userInfoModel.weight, userInfoModel.photo)
            }
        }
    }

    override suspend fun addUserInfo(userInfoModel: UserInfoModel) {
        userInfoList.map {
            if (it.date == userInfoModel.date) {
                UserInfoModel(userInfoModel.date, userInfoModel.weight, userInfoModel.photo)
            } else {
                userInfoList.add(userInfoModel)
            }
        }
    }

    override suspend fun deleteUserInfo(userInfoModel: UserInfoModel) {
        userInfoList.remove(userInfoModel)
    }

    override suspend fun getCountOfCompletedWorkouts(): Int {
        return countOfCompletedWorkouts
    }

}

