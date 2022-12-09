package com.example.homeworkout.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.homeworkout.FIRESTORE_DATABASE
import com.example.homeworkout.ROOM_DATABASE
import com.example.homeworkout.data.database.room.WorkoutDao
import com.example.homeworkout.data.database.room.WorkoutDatabase
import com.example.homeworkout.data.repository_impl.AuthRepositoryImpl
import com.example.homeworkout.data.repository_impl.WorkoutLocalRepositoryImpl
import com.example.homeworkout.data.repository_impl.WorkoutRemoteRepositoryImpl
import com.example.homeworkout.data.shared_preferences.AppSharedPreferences
import com.example.homeworkout.data.shared_preferences.PreferencesHelper
import com.example.homeworkout.DATABASE_TO_USE
import com.example.homeworkout.di.annotations.AppScope
import com.example.homeworkout.domain.repository.AuthRepository
import com.example.homeworkout.domain.repository.WorkoutRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

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


    companion object {

        @Provides
        fun bindWorkoutLocalRepositoryImpl(
            repositoryImplLocal: WorkoutLocalRepositoryImpl,
            repositoryImplRemote: WorkoutRemoteRepositoryImpl,
        ): WorkoutRepository {
            return when (DATABASE_TO_USE) {
                ROOM_DATABASE -> repositoryImplLocal
                FIRESTORE_DATABASE -> repositoryImplRemote
                else -> throw Exception("UNKNOWN DATABASE TO INJECT")
            }
        }

        @AppScope
        @Provides
        fun provideFirebaseUser(): FirebaseUser? {
            return FirebaseAuth.getInstance().currentUser
        }

        @AppScope
        @Provides
        fun provideFirestoreDatabase(): FirebaseFirestore {
            return Firebase.firestore
        }

        @AppScope
        @Provides
        fun provideFirestoreAuth(): FirebaseAuth {
            return FirebaseAuth.getInstance()
        }

        @AppScope
        @Provides
        fun provideFirestoreStorage(): FirebaseStorage {
            return FirebaseStorage.getInstance()
        }

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