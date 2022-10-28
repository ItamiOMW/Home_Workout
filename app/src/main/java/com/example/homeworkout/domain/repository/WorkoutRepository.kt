package com.example.homeworkout.domain.repository

import androidx.lifecycle.LiveData
import com.example.homeworkout.domain.models.PlannedWorkoutModel
import com.example.homeworkout.domain.models.UserInfoModel
import com.example.homeworkout.domain.models.WorkoutModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient

//HERE THE METHODS THAT USED WITH WORKOUT MODEL, PLANNED WORKOUT MODEL, USER INFO MODEL
interface WorkoutRepository {

    //METHODS FOR WORK WITH PLANNED WORKOUT MODEL
    suspend fun getPlannedWorkoutsByDate(date: String): List<PlannedWorkoutModel>

    suspend fun addPlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel)

    suspend fun deletePlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel)

    suspend fun completePlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel)

    //METHODS FOR WORK WITH WORKOUT MODEL
    fun getAllWorkouts(): LiveData<List<WorkoutModel>>

    suspend fun addWorkout(workoutModel: WorkoutModel)

    suspend fun completeWorkout(workoutModel: WorkoutModel)


    //METHODS FOR WORK WITH USER INFO
    fun getListUserInfo(): LiveData<List<UserInfoModel>>

    suspend fun getUserInfoByDate(date: String): UserInfoModel

    suspend fun updateUserInfo(userInfoModel: UserInfoModel)

    suspend fun addUserInfo(userInfoModel: UserInfoModel)

    suspend fun deleteUserInfo(userInfoModel: UserInfoModel)


    //FOR WORK WITH PREFERENCES
    suspend fun getCountOfCompletedWorkouts(): Int

}