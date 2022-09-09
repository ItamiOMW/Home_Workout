package com.example.homeworkout.di.modules

import androidx.lifecycle.ViewModel
import com.example.homeworkout.di.annotations.ViewModelKey
import com.example.homeworkout.presentation.screens.calendar_screen.CalendarViewModel
import com.example.homeworkout.presentation.screens.choose_workout_screen.ChooseWorkoutViewModel
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
}