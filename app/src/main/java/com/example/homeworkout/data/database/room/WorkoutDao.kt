package com.example.homeworkout.data.database.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.homeworkout.data.database.room.room_db_models.PlannedWorkoutDbModel
import com.example.homeworkout.data.database.room.room_db_models.UserInfoDbModel
import com.example.homeworkout.data.database.room.room_db_models.WorkoutDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    //METHODS FOR WORK WITH PLANNED WORKOUT MODEL
    @Query("SELECT * FROM planned_workout_models WHERE date=:date")
    suspend fun getPlannedWorkoutsByDate(date: String): List<PlannedWorkoutDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlannedWorkout(plannedWorkoutModel: PlannedWorkoutDbModel)

    @Query("DELETE FROM planned_workout_models WHERE id=:id")
    suspend fun deletePlannedWorkout(id: Int)

    //METHODS FOR WORK WITH WORKOUT MODEL
    @Query("SELECT * FROM workout_models")
    fun getAllWorkouts(): List<WorkoutDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWorkout(workoutModel: WorkoutDbModel)

    @Insert
    suspend fun addWorkouts(listWorkoutModel: List<WorkoutDbModel>)


    //METHODS FOR WORK WITH USER INFO
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUserInfo(userInfoDbModel: UserInfoDbModel)

    @Query("SELECT * FROM user_info")
    fun getListUserInfo(): List<UserInfoDbModel>

    @Query("SELECT * FROM user_info WHERE date=:date")
    suspend fun getUserInfoByDate(date: String): UserInfoDbModel

    @Query("DELETE FROM user_info WHERE date=:date")
    suspend fun deleteUserInfo(date: String)

}