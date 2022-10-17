package com.example.homeworkout.domain.usecases

import com.example.homeworkout.data.repository_impl.FakeWorkoutRepository
import com.example.homeworkout.domain.models.ExerciseModel
import com.example.homeworkout.domain.models.PlannedWorkoutModel
import com.example.homeworkout.domain.models.WorkoutModel
import com.example.homeworkout.formatDate
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CreateWorkoutUseCaseTest {

    private lateinit var createWorkoutUseCase: CreateWorkoutUseCase

    private lateinit var fakeRepository: FakeWorkoutRepository


    @BeforeEach
    fun setUp() {

        fakeRepository = FakeWorkoutRepository()

        createWorkoutUseCase = CreateWorkoutUseCase(fakeRepository)

    }

    @Test
    fun `should add workout to list workouts`() {
        assertEquals(0, fakeRepository.workoutList.size)
        runTest {
            createWorkoutUseCase.invoke(WorkoutModel(
                title = "fake title",
                imagePath = 0,
                duration = 0f,
                listExercises = listOf(ExerciseModel(
                    "fake title", 5, "123", 123)
                )
            ))
        }
        assertEquals(1, fakeRepository.workoutList.size)
    }

}