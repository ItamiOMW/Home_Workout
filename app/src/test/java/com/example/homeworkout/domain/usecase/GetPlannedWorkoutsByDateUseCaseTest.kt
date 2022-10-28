package com.example.homeworkout.domain.usecase

import com.example.homeworkout.data.repository_impl.FakeWorkoutRepository
import com.example.homeworkout.domain.models.ExerciseModel
import com.example.homeworkout.domain.models.PlannedWorkoutModel
import com.example.homeworkout.domain.models.WorkoutModel
import com.example.homeworkout.domain.usecase.workout_repository_usecases.GetPlannedWorkoutsByDateUseCase
import com.example.homeworkout.formatDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class GetPlannedWorkoutsByDateUseCaseTest {

    private lateinit var getPlannedWorkoutsByDateUseCase: GetPlannedWorkoutsByDateUseCase

    private lateinit var fakeRepository: FakeWorkoutRepository


    @BeforeEach
    fun setUp() {

        fakeRepository = FakeWorkoutRepository()

        getPlannedWorkoutsByDateUseCase = GetPlannedWorkoutsByDateUseCase(fakeRepository)

        val listPlannedWorkouts = mutableListOf<PlannedWorkoutModel>()
        //ALPHABET FROM FIRST TO TENTH LETTER
        (1..10).forEachIndexed { index, i ->
            listPlannedWorkouts.add(
                PlannedWorkoutModel(
                    id = index,
                    date = formatDate(index, index, index),
                    workoutModel = WorkoutModel(
                        title = "fake title + $index",
                        imagePath = index,
                        duration = index.toFloat(),
                        listExercises = listOf(ExerciseModel(
                            "fake title", 5, "123", 123)
                        )
                    ), isCompleted = false
                )
            )
        }
        runTest {
            listPlannedWorkouts.map { fakeRepository.addPlannedWorkout(it) }
        }

    }

    @Test
    fun `should return list of PlannedWorkoutModel with the same date`() {
        val expected = listOf(PlannedWorkoutModel(
            id = 1,
            date = formatDate(1, 1, 1),
            workoutModel = WorkoutModel(
                title = "fake title + 1",
                imagePath = 1,
                duration = 1.toFloat(),
                listExercises = listOf(ExerciseModel(
                    "fake title", 5, "123", 123)
                )
            ), isCompleted = false
        ))
        runTest {
            assertEquals(expected, getPlannedWorkoutsByDateUseCase.invoke(formatDate(1, 1, 1)))
        }
    }
}