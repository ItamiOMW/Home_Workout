package com.example.homeworkout.data.database.room

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.homeworkout.AppWorkout
import com.example.homeworkout.data.database.room.converters.BitmapConverter
import com.example.homeworkout.data.database.room.converters.ExerciseConverter
import com.example.homeworkout.data.database.room.converters.PlannedWorkoutConverter
import com.example.homeworkout.data.database.room.converters.WorkoutConverter
import com.example.homeworkout.domain.models.PlannedWorkoutModel
import com.example.homeworkout.domain.models.UserInfoModel
import com.example.homeworkout.domain.models.WorkoutModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@Database(
    entities = [PlannedWorkoutModel::class, WorkoutModel::class, UserInfoModel::class],
    version = 6,
    exportSchema = false
)
@TypeConverters(
    WorkoutConverter::class,
    PlannedWorkoutConverter::class,
    ExerciseConverter::class,
)
abstract class WorkoutDatabase : RoomDatabase() {

    abstract fun workoutDao(): WorkoutDao

    companion object {

        private const val DB_NAME = "workout.db"
        private var instance: WorkoutDatabase? = null


        fun getInstance(application: Application): WorkoutDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(application).also { instance = it }
            }
        }

        private fun buildDatabase(application: Application): WorkoutDatabase {
            return Room.databaseBuilder(application, WorkoutDatabase::class.java, DB_NAME)
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        instance?.let {
                            CoroutineScope(Dispatchers.IO).launch {
                                it.workoutDao().addWorkouts(DataGenerator()
                                    .getWorkouts(application)
                                )
                            }
                        }
                    }
                })
                .build()
        }
    }

}