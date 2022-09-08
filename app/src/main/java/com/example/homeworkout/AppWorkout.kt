package com.example.homeworkout

import android.app.Application
import com.example.homeworkout.di.component.DaggerWorkoutComponent

class AppWorkout: Application() {

    val component by lazy {
        DaggerWorkoutComponent.factory().create(this)
    }
}