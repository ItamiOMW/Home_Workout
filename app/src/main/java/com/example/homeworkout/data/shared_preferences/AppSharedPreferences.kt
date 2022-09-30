package com.example.homeworkout.data.shared_preferences

import android.content.SharedPreferences
import javax.inject.Inject

class AppSharedPreferences @Inject constructor(
    private val prefs: SharedPreferences,
) : PreferencesHelper {

    override suspend fun increaseCountOfCompletedWorkouts() {
        var currentCount = prefs.getInt(COUNT_OF_COMPLETED_WORKOUTS_KEY, ZERO_WORKOUTS_COMPLETED)
        prefs.edit().putInt(COUNT_OF_COMPLETED_WORKOUTS_KEY, ++currentCount).apply()

    }

    override suspend fun getCountOfCompletedWorkouts(): Int {
        return prefs.getInt(COUNT_OF_COMPLETED_WORKOUTS_KEY, ZERO_WORKOUTS_COMPLETED)
    }

    companion object {

        private const val COUNT_OF_COMPLETED_WORKOUTS_KEY = "COUNT_OF_COMPLETED_WORKOUTS_KEY"

        private const val ZERO_WORKOUTS_COMPLETED = 0

        const val APP_PREFERENCES = "APP_PREFERENCES"
    }

}