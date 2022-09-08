package com.example.homeworkout.di.modules

import android.app.Application
import com.example.homeworkout.data.database.WorkoutDao
import com.example.homeworkout.data.database.WorkoutDatabase
import com.example.homeworkout.data.repository_impl.WorkoutRepositoryImpl
import com.example.homeworkout.di.annotations.AppScope
import com.example.homeworkout.domain.repository.WorkoutRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @AppScope
    @Binds
    fun bindRepositoryImpl(
        repositoryImpl: WorkoutRepositoryImpl
    ): WorkoutRepository

    companion object {

        @AppScope
        @Provides
        fun provideDao(
            application: Application
        ): WorkoutDao {
            return WorkoutDatabase.getInstance(application).workoutDao()
        }
    }
}