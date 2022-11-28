package com.example.homeworkout.data.repository_impl

import com.example.homeworkout.data.database.room.WorkoutDao
import com.example.homeworkout.data.shared_preferences.PreferencesHelper
import com.example.homeworkout.domain.models.PlannedWorkoutModel
import com.example.homeworkout.domain.models.Response
import com.example.homeworkout.domain.models.UserInfoModel
import com.example.homeworkout.domain.models.WorkoutModel
import com.example.homeworkout.domain.repository.WorkoutRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class WorkoutLocalRepositoryImpl @Inject constructor(
    private val preferencesHelper: PreferencesHelper,
    private val dao: WorkoutDao,
) : WorkoutRepository {

    override fun getPlannedWorkoutsByDate(date: String) = flow {
        emit(Response.loading())

        val list = dao.getPlannedWorkoutsByDate(date)

        emit(Response.success(list))

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override fun addPlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel) = flow {

        emit(Response.loading())

        dao.addPlannedWorkout(plannedWorkoutModel)

        emit(Response.success(true))

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override fun deletePlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel) = flow {
        emit(Response.loading())

        dao.deletePlannedWorkout(plannedWorkoutModel.id)

        emit(Response.success(true))

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override fun completePlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel) = flow {
        emit(Response.loading())

        val plannedWorkout = plannedWorkoutModel.copy(isCompleted = true)
        dao.addPlannedWorkout(plannedWorkout)

        emit(Response.success(true))

    }.catch {
        emit(Response.failed(it.message.toString()))
    }


    override fun getAllWorkouts() = channelFlow {
        send(Response.loading())

        dao.getAllWorkouts().collectLatest {
            send(Response.success(it))
        }

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override fun addWorkout(workoutModel: WorkoutModel) = flow {
        emit(Response.loading())

        dao.addWorkout(workoutModel)

        emit(Response.success(true))

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override fun completeWorkout(workoutModel: WorkoutModel) = flow {
        emit(Response.loading())

        preferencesHelper.increaseCountOfCompletedWorkouts()

        emit(Response.success(true))

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override fun getListUserInfo() = channelFlow {
        send(Response.loading())

        dao.getListUserInfo().collectLatest {
            send(Response.success(it))
        }

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    override fun getUserInfoByDate(date: String) = flow<Response<UserInfoModel>> {
        emit(Response.loading())

        val userInfo = dao.getUserInfoByDate(date)

        emit(Response.success(userInfo))

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override fun updateUserInfo(userInfoModel: UserInfoModel) = flow {
        emit(Response.loading())

        //ON CONFLICT STRATEGY = REPLACE, SO NO NEED TO CREATE NEW DAO METHOD
        dao.addUserInfo(userInfoModel)

        emit(Response.success(true))

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override fun addUserInfo(userInfoModel: UserInfoModel) = flow {
        emit(Response.loading())

        dao.addUserInfo(userInfoModel)

        emit(Response.success(true))

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override fun deleteUserInfo(userInfoModel: UserInfoModel) = flow {
        emit(Response.loading())

        dao.deleteUserInfo(userInfoModel.date)

        emit(Response.success(true))

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    override fun getCountOfCompletedWorkouts() = flow {
        emit(Response.loading())

        val amountOfCompletedWorkouts = preferencesHelper.getCountOfCompletedWorkouts()

        emit(Response.success(amountOfCompletedWorkouts))

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

}