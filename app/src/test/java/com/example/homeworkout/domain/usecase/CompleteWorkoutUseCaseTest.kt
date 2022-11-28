package com.example.homeworkout.domain.usecase

import com.example.homeworkout.data.repository_impl.FakeWorkoutRepository
import com.example.homeworkout.domain.usecase.workout_repository_usecases.CompleteWorkoutUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class CompleteWorkoutUseCaseTest {


    private lateinit var completeWorkoutUseCase: CompleteWorkoutUseCase

    private lateinit var fakeRepository: FakeWorkoutRepository

    private lateinit var testedWorkout: WorkoutModel


    @BeforeEach
    fun setUp() {

        fakeRepository = FakeWorkoutRepository()

        completeWorkoutUseCase = CompleteWorkoutUseCase(fakeRepository)

        testedWorkout = WorkoutModel(
            title = "fake title",
            imagePath = 0,
            duration = 0f,
            listExercises = listOf(ExerciseModel(
                "fake title", 5, "123", 123)
            )
        )

    }

    @Test
    fun `must increase count of completed workout`() {

        assertEquals(0, fakeRepository.countOfCompletedWorkouts)
        runTest {
            completeWorkoutUseCase.invoke(testedWorkout)
        }
        assertEquals(1, fakeRepository.countOfCompletedWorkouts)
    }
}