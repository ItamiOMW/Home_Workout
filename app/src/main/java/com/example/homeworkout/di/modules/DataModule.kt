package com.example.homeworkout.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.homeworkout.data.database.firebase.authentication.UserAuthDatabase
import com.example.homeworkout.data.database.firebase.authentication.UserAuthDatabaseHelper
import com.example.homeworkout.data.database.room.WorkoutDao
import com.example.homeworkout.data.database.room.WorkoutDatabase
import com.example.homeworkout.data.repository_impl.AuthRepositoryImpl
import com.example.homeworkout.data.repository_impl.WorkoutLocalRepositoryImpl
import com.example.homeworkout.data.shared_preferences.AppSharedPreferences
import com.example.homeworkout.data.shared_preferences.PreferencesHelper
import com.example.homeworkout.di.annotations.AppScope
import com.example.homeworkout.domain.repository.AuthRepository
import com.example.homeworkout.domain.repository.WorkoutRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @AppScope
    @Binds
    fun bindWorkoutRepositoryImpl(
        repositoryImpl: WorkoutLocalRepositoryImpl,
    ): WorkoutRepository

    @AppScope
    @Binds
    fun bindAuthRepositoryImpl(
        repositoryImpl: AuthRepositoryImpl,
    ): AuthRepository

    @AppScope
    @Binds
    fun bindPreferencesHelper(
        appSharedPreferences: AppSharedPreferences,
    ): PreferencesHelper

    @AppScope
    @Binds
    fun bindUserAuthDatabase(
        userAuthDatabase: UserAuthDatabase,
    ): UserAuthDatabaseHelper


    companion object {

        @AppScope
        @Provides
        fun provideDao(
            application: Application,
        ): WorkoutDao {
            return WorkoutDatabase.getInstance(application).workoutDao()
        }

        @AppScope
        @Provides
        fun provideSharedPreferences(
            application: Application,
        ): SharedPreferences {
            return application.getSharedPreferences(
                AppSharedPreferences.APP_PREFERENCES,
                Context.MODE_PRIVATE
            )
        }
    }
}