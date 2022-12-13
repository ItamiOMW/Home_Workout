package com.example.homeworkout.data.fake_repository_impl

import com.example.homeworkout.domain.models.PlannedWorkoutModel
import com.example.homeworkout.domain.models.Response
import com.example.homeworkout.domain.models.UserInfoModel
import com.example.homeworkout.domain.models.WorkoutModel
import com.example.homeworkout.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepositoryImpl : WorkoutRepository {

    private val plannedWorkouts = mutableListOf<PlannedWorkoutModel>()

    private val workouts = mutableListOf<WorkoutModel>()

    private val usersInfo = mutableListOf<UserInfoModel>()

    private var countOfCompletedWorkouts = 0

    override fun getPlannedWorkoutsByDate(date: Long): Flow<Response<List<PlannedWorkoutModel>>> {
        return flow {
            emit(Response.success(plannedWorkouts.filter { it.date == date }))
        }
    }

    override fun addPlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel): Flow<Response<Boolean>> {
        return flow {
            plannedWorkouts.add(plannedWorkoutModel)
            emit(Response.success(true))
        }
    }

    override fun deletePlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel): Flow<Response<Boolean>> {
        return flow {
            plannedWorkouts.remove(plannedWorkoutModel)
            emit(Response.success(true))
        }
    }

    override fun completePlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel): Flow<Response<Boolean>> {
        return flow {
            val index = plannedWorkouts.indexOf(plannedWorkoutModel)
            plannedWorkouts.removeAt(index)
            plannedWorkouts.add(index, plannedWorkoutModel.copy(completed = true))
            emit(Response.success(true))
        }
    }

    override fun getAllWorkouts(): Flow<Response<List<WorkoutModel>>> {
        return flow {
            emit(Response.success(workouts))
        }
    }

    override fun addWorkout(workoutModel: WorkoutModel): Flow<Response<Boolean>> {
        return flow {
            workouts.add(workoutModel.id, workoutModel)
            emit(Response.success(true))
        }
    }

    override fun completeWorkout(workoutModel: WorkoutModel): Flow<Response<Boolean>> {
        return flow {
            countOfCompletedWorkouts++
            emit(Response.success(true))
        }
    }

    override fun getListUserInfo(): Flow<Response<List<UserInfoModel>>> {
        return flow {
            emit(Response.success(usersInfo))
        }
    }

    override fun updateUserInfo(userInfoModel: UserInfoModel): Flow<Response<Boolean>> {
        return flow {
            val oldObject = usersInfo[userInfoModel.id]
            if (userInfoModel == oldObject) {
                emit(Response.failed("NO CHANGES FOUND"))
            } else {
                usersInfo.removeAt(userInfoModel.id)
                usersInfo.add(userInfoModel.id, userInfoModel)
                emit(Response.success(true))
            }
        }
    }

    override fun addUserInfo(userInfoModel: UserInfoModel): Flow<Response<Boolean>> {
        return flow {
            usersInfo.add(userInfoModel.id, userInfoModel)
            emit(Response.success(true))
        }
    }

    override fun deleteUserInfo(userInfoModel: UserInfoModel): Flow<Response<Boolean>> {
        return flow {
            usersInfo.removeAt(userInfoModel.id)
            emit(Response.success(true))
        }
    }

    override fun getCountOfCompletedWorkouts(): Flow<Response<Int>> {
        return flow {
            emit(Response.success(countOfCompletedWorkouts))
        }
    }

}