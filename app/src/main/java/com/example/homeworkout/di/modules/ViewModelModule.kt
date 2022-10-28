package com.example.homeworkout.di.modules

import androidx.lifecycle.ViewModel
import com.example.homeworkout.di.annotations.ViewModelKey
import com.example.homeworkout.presentation.screens.calendar_screen.CalendarViewModel
import com.example.homeworkout.presentation.screens.choose_workout_screen.ChooseWorkoutViewModel
import com.example.homeworkout.presentation.screens.login_screen.LoginViewModel
import com.example.homeworkout.presentation.screens.parent_screen.MainViewModel
import com.example.homeworkout.presentation.screens.plan_workout_screen.PlanWorkoutViewModel
import com.example.homeworkout.presentation.screens.progress_screen.ProgressViewModel
import com.example.homeworkout.presentation.screens.training_screen.TrainingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ChooseWorkoutViewModel::class)
    fun bindChooseWorkoutViewModel(viewModel: ChooseWorkoutViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CalendarViewModel::class)
    fun bindCalendarViewModel(viewModel: CalendarViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlanWorkoutViewModel::class)
    fun bindPlanWorkoutViewModel(viewModel: PlanWorkoutViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TrainingViewModel::class)
    fun bindTrainingViewModel(viewModel: TrainingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProgressViewModel::class)
    fun bindProgressViewModel(viewModel: ProgressViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}