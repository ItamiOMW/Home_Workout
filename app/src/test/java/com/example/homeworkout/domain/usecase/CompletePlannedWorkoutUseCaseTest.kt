package com.example.homeworkout.domain.usecase

import com.example.homeworkout.data.repository_impl.FakeWorkoutRepository
import com.example.homeworkout.domain.usecase.workout_repository_usecases.CompletePlannedWorkoutUseCase
import com.example.homeworkout.formatDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class CompletePlannedWorkoutUseCaseTest {

    private lateinit var completeWorkoutUseCase: CompletePlannedWorkoutUseCase

    private lateinit var fakeRepository: FakeWorkoutRepository

    private lateinit var testedWorkout: PlannedWorkoutModel


    @BeforeEach
    fun setUp() {

        val fakeDate = formatDate(5, 5, 2025)

        fakeRepository = FakeWorkoutRepository()

        completeWorkoutUseCase = CompletePlannedWorkoutUseCase(fakeRepository)

        testedWorkout = PlannedWorkoutModel(
            1,
            date = fakeDate,
            workoutModel = WorkoutModel(title = "fake title",
                imagePath = 0,
                duration = 0f,
                listExercises = listOf(ExerciseModel(
                    "fake title", 5, "123", 123)
                )
            ), isCompleted = false)

        runTest {
            fakeRepository.addPlannedWorkout(testedWorkout)
        }

    }

    @Test
    fun `must turn flag of PlannedWorkoutModel isCompleted to true`() {

        assertEquals(0, fakeRepository.countOfCompletedWorkouts)
        assertEquals(1, fakeRepository.plannedWorkoutList.size)
        assertEquals(false, fakeRepository.plannedWorkoutList.first().isCompleted)
        runTest {
            completeWorkoutUseCase.invoke(testedWorkout)
        }
        assertEquals(1, fakeRepository.plannedWorkoutList.size)
        assertEquals(true, fakeRepository.plannedWorkoutList.first().isCompleted)
        assertEquals(1, fakeRepository.countOfCompletedWorkouts)
    }
}