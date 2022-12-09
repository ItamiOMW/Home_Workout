package com.example.homeworkout.domain.repository

import com.example.homeworkout.domain.models.PlannedWorkoutModel
import com.example.homeworkout.domain.models.Response
import com.example.homeworkout.domain.models.UserInfoModel
import com.example.homeworkout.domain.models.WorkoutModel
import kotlinx.coroutines.flow.Flow
import java.util.*

//HERE THE METHODS THAT USED WITH WORKOUT MODEL, PLANNED WORKOUT MODEL, USER INFO MODEL
interface WorkoutRepository {

    //METHODS FOR WORK WITH PLANNED WORKOUT MODEL
    fun getPlannedWorkoutsByDate(date: Long): Flow<Response<List<PlannedWorkoutModel>>>

    fun addPlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel): Flow<Response<Boolean>>

    fun deletePlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel): Flow<Response<Boolean>>

    fun completePlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel): Flow<Response<Boolean>>

    //METHODS FOR WORK WITH WORKOUT MODEL
    fun getAllWorkouts(): Flow<Response<List<WorkoutModel>>>

    fun addWorkout(workoutModel: WorkoutModel): Flow<Response<Boolean>>

    fun completeWorkout(workoutModel: WorkoutModel): Flow<Response<Boolean>>


    //METHODS FOR WORK WITH USER INFO
    fun getListUserInfo(): Flow<Response<List<UserInfoModel>>>

    fun updateUserInfo(userInfoModel: UserInfoModel): Flow<Response<Boolean>>

    fun addUserInfo(userInfoModel: UserInfoModel): Flow<Response<Boolean>>

    fun deleteUserInfo(userInfoModel: UserInfoModel): Flow<Response<Boolean>>


    //SAVE COUNT OF COMPLETED WORKOUTS
    fun getCountOfCompletedWorkouts(): Flow<Response<Int>>

}