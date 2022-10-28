package com.example.homeworkout.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.homeworkout.R
import com.example.homeworkout.SIGN_IN_REQUEST
import com.example.homeworkout.SIGN_UP_REQUEST
import com.example.homeworkout.data.database.firebase.UserAuthDatabase
import com.example.homeworkout.data.database.firebase.UserAuthHelper
import com.example.homeworkout.data.database.room.WorkoutDao
import com.example.homeworkout.data.database.room.WorkoutDatabase
import com.example.homeworkout.data.repository_impl.AuthRepositoryImpl
import com.example.homeworkout.data.repository_impl.WorkoutRepositoryImpl
import com.example.homeworkout.data.shared_preferences.AppSharedPreferences
import com.example.homeworkout.data.shared_preferences.PreferencesHelper
import com.example.homeworkout.di.annotations.AppScope
import com.example.homeworkout.domain.repository.AuthRepository
import com.example.homeworkout.domain.repository.WorkoutRepository
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
interface DataModule {

    @AppScope
    @Binds
    fun bindWorkoutRepositoryImpl(
        repositoryImpl: WorkoutRepositoryImpl,
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
    ): UserAuthHelper


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