package com.example.homeworkout.di.component

import android.app.Activity
import android.app.Application
import com.example.homeworkout.di.annotations.AppScope
import com.example.homeworkout.di.modules.DataModule
import com.example.homeworkout.di.modules.PresentationModule
import com.example.homeworkout.di.modules.ViewModelModule
import com.example.homeworkout.presentation.screens.calendar_screen.CalendarFragment
import com.example.homeworkout.presentation.screens.choose_workout_screen.ChooseWorkoutFragment
import com.example.homeworkout.presentation.screens.login_screen.LoginFragment
import com.example.homeworkout.presentation.screens.parent_screen.MainActivity
import com.example.homeworkout.presentation.screens.plan_workout_screen.PlanWorkoutFragment
import com.example.homeworkout.presentation.screens.profile_screen.ProfileFragment
import com.example.homeworkout.presentation.screens.progress_screen.ProgressFragment
import com.example.homeworkout.presentation.screens.tabs_screen.TabsFragment
import com.example.homeworkout.presentation.screens.training_screen.TrainingFragment
import com.example.homeworkout.presentation.screens.workout_detail_screen.WorkoutDetailFragment
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(modules = [ViewModelModule::class, DataModule::class, PresentationModule::class])
interface WorkoutComponent {

    fun inject(fragment: CalendarFragment)

    fun inject(fragment: ChooseWorkoutFragment)

    fun inject(fragment: PlanWorkoutFragment)

    fun inject(fragment: ProgressFragment)

    fun inject(fragment: TabsFragment)

    fun inject(fragment: TrainingFragment)

    fun inject(fragment: WorkoutDetailFragment)

    fun inject(fragment: LoginFragment)

    fun inject(fragment: ProfileFragment)

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): WorkoutComponent

    }
}