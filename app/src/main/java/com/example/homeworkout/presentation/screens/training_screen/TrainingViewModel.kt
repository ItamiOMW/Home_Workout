package com.example.homeworkout.presentation.screens.training_screen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeworkout.MILLIS_IN_SECOND
import com.example.homeworkout.R
import com.example.homeworkout.SECONDS_IN_HOUR
import com.example.homeworkout.SECONDS_IN_MINUTE
import com.example.homeworkout.domain.models.PlannedWorkoutModel
import com.example.homeworkout.domain.models.Response
import com.example.homeworkout.domain.models.WorkoutModel
import com.example.homeworkout.domain.usecase.workout_repository_usecases.CompletePlannedWorkoutUseCase
import com.example.homeworkout.domain.usecase.workout_repository_usecases.CompleteWorkoutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrainingViewModel @Inject constructor(
    private val application: Application,
    private val completePlannedWorkoutUseCase: CompletePlannedWorkoutUseCase,
    private val completeWorkoutUseCase: CompleteWorkoutUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(TrainingUIState())
    val state = _state.asStateFlow()

    private lateinit var workoutModel: WorkoutModel

    private var plannedWorkoutModel: PlannedWorkoutModel? = null

    private var currentExercisePosition = FIRST_EXERCISE_POSITION

    private var isWorkoutCompleted: Any? = null

    //MUST BE CALLED TO START WORKOUT
    fun start(workoutModel: WorkoutModel, plannedWorkoutModel: PlannedWorkoutModel?) {
        this.workoutModel = workoutModel
        this.plannedWorkoutModel = plannedWorkoutModel
        startStopwatch()
        startWorkout()
    }

    fun goToNextExercise() {
        if (currentExercisePosition == FIRST_EXERCISE_POSITION && workoutModel.listExercises.size != 1) {
            updateExercise(FIRST_EXERCISE_POSITION)
            formatCurrentPositionAndAmountExercises()
            return
        }
        if (currentExercisePosition + 1 == workoutModel.listExercises.size) {
            completeWorkout()
        } else {
            updateExercise(currentExercisePosition++)
            formatCurrentPositionAndAmountExercises()
        }
    }

    fun goToPreviousExercise() {
        if (currentExercisePosition != FIRST_EXERCISE_POSITION) {
            updateExercise(--currentExercisePosition)
            formatCurrentPositionAndAmountExercises()
        }
    }

    private fun startWorkout() {
        updateExercise(FIRST_EXERCISE_POSITION)
        formatCurrentPositionAndAmountExercises()
    }

    private fun updateExercise(positionInList: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            val exercise = Exercise(workoutModel.listExercises[positionInList])
            _state.value = exercise
        }
    }

    private fun formatCurrentPositionAndAmountExercises() {
        _state.value = CurrentExercisePositionAndAmountOfExercises(String.format(
            application.getString(R.string.exercise_count_format),
            currentExercisePosition + 1, workoutModel.listExercises.size)
        )
    }

    private fun completeWorkout() {

        viewModelScope.launch {
            if (plannedWorkoutModel != null) {
                plannedWorkoutModel?.let {
                    completePlannedWorkoutUseCase.invoke(it).collect {
                        when (it) {
                            is Response.Loading -> {
                                _state.value = Loading
                            }
                            is Response.Failed -> {
                                _state.value = Failure(it.message)
                            }
                            is Response.Success -> {
                                _state.value = IsPlannedWorkoutCompleted
                            }
                        }
                    }
                }
            }
            viewModelScope.launch {
                completeWorkoutUseCase.invoke(workoutModel).collect {
                    when (it) {
                        is Response.Loading -> {
                            _state.value = Loading
                        }
                        is Response.Failed -> {
                            _state.value = Failure(it.message)
                        }
                        is Response.Success -> {
                            _state.value = IsWorkoutCompleted
                        }
                    }
                }
            }
            isWorkoutCompleted = Any()
        }

    }

    private fun startStopwatch() {
        var timeSeconds = 0L
        viewModelScope.launch(Dispatchers.Default) {
            while (true) {
                if (isWorkoutCompleted != null) {
                    this.cancel()
                }
                delay(MILLIS_IN_SECOND)
                _state.value = TimerTime(formatStopwatchTime(++timeSeconds))
            }
        }
    }

    private fun formatStopwatchTime(timeSeconds: Long): String {
        val hours = timeSeconds / SECONDS_IN_HOUR
        val minutes = (timeSeconds % SECONDS_IN_HOUR) / SECONDS_IN_MINUTE
        val seconds = (timeSeconds % SECONDS_IN_HOUR) % SECONDS_IN_MINUTE
        return String.format(application.getString(R.string.time_format), hours, minutes, seconds)
    }

    companion object {

        private const val FIRST_EXERCISE_POSITION = 0

    }

}