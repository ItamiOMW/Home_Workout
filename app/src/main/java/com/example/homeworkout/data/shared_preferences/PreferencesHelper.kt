package com.example.homeworkout.data.shared_preferences

interface PreferencesHelper {

    suspend fun increaseCountOfCompletedWorkouts()

    suspend fun getCountOfCompletedWorkouts(): Int

}