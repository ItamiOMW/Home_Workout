package com.example.homeworkout.data.database.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.homeworkout.domain.models.PlannedWorkoutModel
import com.example.homeworkout.domain.models.UserInfoModel
import com.example.homeworkout.domain.models.WorkoutModel
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface WorkoutDao {

    //METHODS FOR WORK WITH PLANNED WORKOUT MODEL
    @Query("SELECT * FROM planned_workout_models WHERE date=:date")
    fun getPlannedWorkoutsByDate(date: Long): Flow<List<PlannedWorkoutModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel)

    @Query("DELETE FROM planned_workout_models WHERE id=:id")
    suspend fun deletePlannedWorkout(id: Int)

    //METHODS FOR WORK WITH WORKOUT MODEL
    @Query("SELECT * FROM workout_models")
    fun getAllWorkouts(): Flow<List<WorkoutModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWorkout(workoutModel: WorkoutModel)

    @Insert
    suspend fun addWorkouts(listWorkoutModel: List<WorkoutModel>)


    //METHODS FOR WORK WITH USER INFO
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUserInfo(userInfoModel: UserInfoModel)

    @Query("SELECT * FROM user_info")
    fun getListUserInfo(): Flow<List<UserInfoModel>>

    @Query("SELECT * FROM user_info WHERE date=:date")
    suspend fun getUserInfoByDate(date: Long): UserInfoModel

    @Query("DELETE FROM user_info WHERE date=:date")
    suspend fun deleteUserInfo(date: Long)

}