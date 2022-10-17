package com.example.homeworkout.presentation.screens.training_screen

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeworkout.MILLIS_IN_SECOND
import com.example.homeworkout.R
import com.example.homeworkout.SECONDS_IN_HOUR
import com.example.homeworkout.SECONDS_IN_MINUTE
import com.example.homeworkout.domain.models.ExerciseModel
import com.example.homeworkout.domain.models.PlannedWorkoutModel
import com.example.homeworkout.domain.models.WorkoutModel
import com.example.homeworkout.domain.usecases.CompletePlannedWorkoutUseCase
import com.example.homeworkout.domain.usecases.CompleteWorkoutUseCase
import kotlinx.coroutines.*
import javax.inject.Inject

class TrainingViewModel @Inject constructor(
    private val application: Application,
    private val completePlannedWorkoutUseCase: CompletePlannedWorkoutUseCase,
    private val completeWorkoutUseCase: CompleteWorkoutUseCase,
) : ViewModel() {

    private val _timerTime = MutableLiveData<String>()
    val timerTime: LiveData<String>
        get() = _timerTime

    private val _exercise = MutableLiveData<ExerciseModel>()
    val exercise: LiveData<ExerciseModel>
        get() = _exercise

    private val _currentExercisePositionAndAmountOfExercises = MutableLiveData<String>()
    val currentExercisePositionAndAmountOfExercises: LiveData<String>
        get() = _currentExercisePositionAndAmountOfExercises

    private val _isWorkoutCompleted = MutableLiveData<Any>()
    val isWorkoutCompleted: LiveData<Any>
        get() = _isWorkoutCompleted

    private lateinit var workoutModel: WorkoutModel

    private var plannedWorkoutModel: PlannedWorkoutModel? = null

    private var currentExercisePosition = FIRST_EXERCISE_POSITION


    //SHOULD BE CALLED TO START WORKOUT
    fun start(workoutModel: WorkoutModel, plannedWorkoutModel: PlannedWorkoutModel?) {
        this.workoutModel = workoutModel
        this.plannedWorkoutModel = plannedWorkoutModel
        startStopwatch()
        startWorkout()
    }

    fun goToNextExercise() {
        if (currentExercisePosition == FIRST_EXERCISE_POSITION && workoutModel.listExercises.size != 1) {
            _exercise.value = workoutModel.listExercises[FIRST_EXERCISE_POSITION]
            formatCurrentPositionAndAmountExercises()
            return
        }
        if (currentExercisePosition + 1 == workoutModel.listExercises.size) {
            completeWorkout()
        } else  {
            _exercise.value = workoutModel.listExercises[++currentExercisePosition]
            formatCurrentPositionAndAmountExercises()
        }
    }

    fun goToPreviousExercise() {
        if (currentExercisePosition != FIRST_EXERCISE_POSITION) {
            _exercise.value = workoutModel.listExercises[--currentExercisePosition]
            formatCurrentPositionAndAmountExercises()
        }
    }

    private fun startWorkout() {
        _exercise.value = workoutModel.listExercises[FIRST_EXERCISE_POSITION]
        formatCurrentPositionAndAmountExercises()
    }

    private fun formatCurrentPositionAndAmountExercises() {
        _currentExercisePositionAndAmountOfExercises.value = String.format(
            application.getString(R.string.exercise_count_format),
            currentExercisePosition + 1, workoutModel.listExercises.size
        )
    }

    private fun completeWorkout() {

        viewModelScope.launch(Dispatchers.IO) {
            if (plannedWorkoutModel != null) {
                plannedWorkoutModel?.let { completePlannedWorkoutUseCase.invoke(it) }
            }
            completeWorkoutUseCase.invoke(workoutModel)
            _isWorkoutCompleted.postValue(Any())
        }

    }

    private fun startStopwatch() {
        var timeSeconds = 0L
        viewModelScope.launch(Dispatchers.Default) {
            while (true) {
                delay(MILLIS_IN_SECOND)
                _timerTime.postValue(formatStopwatchTime(++timeSeconds))
                if (isWorkoutCompleted.value != null) {
                    this.cancel()
                }
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