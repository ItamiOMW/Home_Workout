package com.example.homeworkout.data.repository_impl

import com.example.homeworkout.data.database.room.WorkoutDao
import com.example.homeworkout.data.mapper.WorkoutMapper
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
    private val mapper: WorkoutMapper,
) : WorkoutRepository {

    override fun getPlannedWorkoutsByDate(date: String) = flow {
        emit(Response.loading())

        val plannedWorkoutsDbModel = dao.getPlannedWorkoutsByDate(date)

        val plannedWorkouts = plannedWorkoutsDbModel.map {
            mapper.mapPlannedWorkoutDbToEntity(it)
        }

        emit(Response.success(plannedWorkouts))

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override fun addPlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel) = flow {
        emit(Response.loading())

        val plannedWorkoutDbModel = mapper.mapPlannedWorkoutEntityToDb(plannedWorkoutModel)
        dao.addPlannedWorkout(plannedWorkoutDbModel)

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
        dao.addPlannedWorkout(mapper.mapPlannedWorkoutEntityToDb(plannedWorkout))

        emit(Response.success(true))

    }.catch {
        emit(Response.failed(it.message.toString()))
    }


    override fun getAllWorkouts() = flow {
        emit(Response.loading())

        val workoutsDbModel = dao.getAllWorkouts()
        val workouts = workoutsDbModel.map { mapper.mapWorkoutDbToWorkoutEntity(it) }

        emit(Response.success(workouts))

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override fun addWorkout(workoutModel: WorkoutModel) = flow {
        emit(Response.loading())

        dao.addWorkout(mapper.mapWorkoutEntityToWorkoutDb(workoutModel))

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


    override fun getListUserInfo() = flow {
        emit(Response.loading())

        val listUserInfoDbModel = dao.getListUserInfo()
        val listUserInfo = listUserInfoDbModel.map { mapper.mapDbModelToUserInfo(it) }

        emit(Response.success(listUserInfo))

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    override fun getUserInfoByDate(date: String) = flow<Response<UserInfoModel>> {
        emit(Response.loading())

        val userInfoDbModel = dao.getUserInfoByDate(date)
        val userInfo = mapper.mapDbModelToUserInfo(userInfoDbModel)

        emit(Response.success(userInfo))
    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override fun updateUserInfo(userInfoModel: UserInfoModel) = flow {
        emit(Response.loading())

        //ON CONFLICT STRATEGY = REPLACE, SO NO NEED TO CREATE NEW DAO METHOD
        dao.addUserInfo(mapper.mapUserInfoToDbModel(userInfoModel))

        emit(Response.success(true))

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override fun addUserInfo(userInfoModel: UserInfoModel) = flow<Response<Boolean>> {
        emit(Response.loading())

        dao.addUserInfo(mapper.mapUserInfoToDbModel(userInfoModel))

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