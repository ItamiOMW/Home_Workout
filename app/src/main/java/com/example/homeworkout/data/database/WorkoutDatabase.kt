package com.example.homeworkout.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.homeworkout.data.database.converters.BitmapConverter
import com.example.homeworkout.data.database.converters.ExerciseConverter
import com.example.homeworkout.data.database.converters.PlannedWorkoutConverter
import com.example.homeworkout.data.database.converters.WorkoutConverter
import com.example.homeworkout.data.database.db_models.PlannedWorkoutDbModel
import com.example.homeworkout.data.database.db_models.UserInfoDbModel
import com.example.homeworkout.data.database.db_models.WorkoutDbModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [PlannedWorkoutDbModel::class, WorkoutDbModel::class, UserInfoDbModel::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(
    WorkoutConverter::class,
    PlannedWorkoutConverter::class,
    ExerciseConverter::class,
    BitmapConverter::class
)
abstract class WorkoutDatabase() : RoomDatabase() {

    abstract fun workoutDao(): WorkoutDao

    companion object {

        private const val DB_NAME = "workout.db"
        private var instance: WorkoutDatabase? = null


        fun getInstance(context: Context): WorkoutDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): WorkoutDatabase {
            return Room.databaseBuilder(context, WorkoutDatabase::class.java, DB_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        instance?.let {
                            CoroutineScope(Dispatchers.IO).launch {
                                it.workoutDao().addWorkouts(DataGenerator.getWorkouts())
                            }
                        }
                    }
                })
                .build()
        }
    }

}