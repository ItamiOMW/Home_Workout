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

internal class AddPlannedWorkoutUseCaseTest {

    private lateinit var addPlannedWorkoutUseCase: AddPlannedWorkoutUseCase

    private lateinit var fakeRepository: FakeWorkoutRepository


    @BeforeEach
    fun setUp() {

        fakeRepository = FakeWorkoutRepository()

        addPlannedWorkoutUseCase = AddPlannedWorkoutUseCase(fakeRepository)

    }

    @Test
    fun `after adding 10 PlannedWorkoutModel size of list must be 10`() {
        val fakeDate = formatDate(14, 10, 2022)
        val listPlannedWorkouts = mutableListOf<PlannedWorkoutModel>()
        (1..10).forEachIndexed { index, i ->
            listPlannedWorkouts.add(
                PlannedWorkoutModel(
                    id = index,
                    date = fakeDate,
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
            listPlannedWorkouts.forEach { addPlannedWorkoutUseCase.invoke(it) }
        }
        assertEquals(10, fakeRepository.plannedWorkoutList.size)
    }
}