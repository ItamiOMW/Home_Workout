package com.example.homeworkout.data.repository_impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.homeworkout.data.database.room.WorkoutDao
import com.example.homeworkout.data.mapper.WorkoutMapper
import com.example.homeworkout.data.shared_preferences.PreferencesHelper
import com.example.homeworkout.domain.models.PlannedWorkoutModel
import com.example.homeworkout.domain.models.UserInfoModel
import com.example.homeworkout.domain.models.WorkoutModel
import com.example.homeworkout.domain.repository.WorkoutRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val preferencesHelper: PreferencesHelper,
    private val dao: WorkoutDao,
    private val mapper: WorkoutMapper,
) : WorkoutRepository {

    override suspend fun getPlannedWorkoutsByDate(date: String): List<PlannedWorkoutModel> {
        return dao.getPlannedWorkoutsByDate(date).map { mapper.mapPlannedWorkoutDbToEntity(it) }
    }

    override suspend fun addPlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel) {
        dao.addPlannedWorkout(mapper.mapPlannedWorkoutEntityToDb(plannedWorkoutModel))
    }

    override suspend fun deletePlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel) {
        dao.deletePlannedWorkout(plannedWorkoutModel.id)
    }

    override suspend fun completePlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel) {
        val plannedWorkout = plannedWorkoutModel.copy(isCompleted = true)
        dao.addPlannedWorkout(mapper.mapPlannedWorkoutEntityToDb(plannedWorkout))
    }

    override fun getAllWorkouts(): LiveData<List<WorkoutModel>> =
        Transformations.map(dao.getAllWorkouts()) {
        it.map { mapper.mapWorkoutDbToWorkoutEntity(it) }
    }


    override suspend fun addWorkout(workoutModel: WorkoutModel) {
        dao.addWorkout(mapper.mapWorkoutEntityToWorkoutDb(workoutModel))
    }

    override suspend fun completeWorkout(workoutModel: WorkoutModel) {
        preferencesHelper.increaseCountOfCompletedWorkouts()
    }

    override fun getListUserInfo(): LiveData<List<UserInfoModel>> =
        Transformations.map(dao.getListUserInfo()) {
            it.map { mapper.mapDbModelToUserInfo(it) }
        }

    override suspend fun getUserInfoByDate(date: String): UserInfoModel {
        return mapper.mapDbModelToUserInfo(dao.getUserInfoByDate(date))
    }

    override suspend fun updateUserInfo(userInfoModel: UserInfoModel) {
        //ON CONFLICT STRATEGY = REPLACE, SO NO NEED TO CREATE NEW DAO METHOD
        dao.addUserInfo(mapper.mapUserInfoToDbModel(userInfoModel))
    }

    override suspend fun addUserInfo(userInfoModel: UserInfoModel) {
        dao.addUserInfo(mapper.mapUserInfoToDbModel(userInfoModel))
    }

    override suspend fun deleteUserInfo(userInfoModel: UserInfoModel) {
        dao.deleteUserInfo(userInfoModel.date)
    }

    override suspend fun getCountOfCompletedWorkouts(): Int {
        return preferencesHelper.getCountOfCompletedWorkouts()
    }
}