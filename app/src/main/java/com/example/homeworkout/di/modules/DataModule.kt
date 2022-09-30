package com.example.homeworkout.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.homeworkout.data.database.WorkoutDao
import com.example.homeworkout.data.database.WorkoutDatabase
import com.example.homeworkout.data.repository_impl.WorkoutRepositoryImpl
import com.example.homeworkout.data.shared_preferences.AppSharedPreferences
import com.example.homeworkout.data.shared_preferences.PreferencesHelper
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
        repositoryImpl: WorkoutRepositoryImpl,
    ): WorkoutRepository

    @AppScope
    @Binds
    fun bindPreferencesHelper(
        appSharedPreferences: AppSharedPreferences,
    ): PreferencesHelper

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