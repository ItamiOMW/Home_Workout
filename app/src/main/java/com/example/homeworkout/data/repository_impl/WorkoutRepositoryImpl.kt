package com.example.homeworkout.data.repository_impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.homeworkout.data.database.WorkoutDao
import com.example.homeworkout.data.mapper.WorkoutMapper
import com.example.homeworkout.domain.models.PlannedWorkoutModel
import com.example.homeworkout.domain.models.WorkoutModel
import com.example.homeworkout.domain.repository.WorkoutRepository
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val dao: WorkoutDao,
    private val mapper: WorkoutMapper
) : WorkoutRepository {

    override fun getPlannedWorkoutsByDate(date: String): LiveData<List<PlannedWorkoutModel>> =
        Transformations.map(dao.getPlannedWorkoutsByDate(date)) {
            it.map { mapper.mapPlannedWorkoutDbToEntity(it) }
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
        //TODO IMPLEMENT AFTER PROGRESS SCREEN
    }

    override fun getAllWorkouts(): LiveData<List<WorkoutModel>> =
        Transformations.map(dao.getAllWorkouts()) {
            it.map { mapper.mapWorkoutDbToWorkoutEntity(it) }
        }

    override suspend fun addWorkout(workoutModel: WorkoutModel) {
        dao.addWorkout(mapper.mapWorkoutEntityToWorkoutDb(workoutModel))
    }

    override suspend fun completeWorkout(workoutModel: WorkoutModel) {
        //TODO IMPLEMENT AFTER PROGRESS SCREEN
    }

}