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

internal class DeletePlannedWorkoutUseCaseTest {

    private lateinit var deletePlannedWorkoutUseCase: DeletePlannedWorkoutUseCase

    private lateinit var fakeRepository: FakeWorkoutRepository

    private lateinit var plannedWorkout: PlannedWorkoutModel


    @BeforeEach
    fun setUp() {

        fakeRepository = FakeWorkoutRepository()

        deletePlannedWorkoutUseCase = DeletePlannedWorkoutUseCase(fakeRepository)

        val fakeDate = formatDate(14, 10, 2022)
        val listPlannedWorkouts = mutableListOf<PlannedWorkoutModel>()
        //ALPHABET FROM FIRST TO TENTH LETTER
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
            listPlannedWorkouts.map { fakeRepository.addPlannedWorkout(it) }
        }

    }

    @Test
    fun `should delete planned workout from list`() {
        assertEquals(10, fakeRepository.plannedWorkoutList.size)
        runTest {
            deletePlannedWorkoutUseCase.invoke(PlannedWorkoutModel(
                id = 1,
                date = formatDate(14, 10, 2022),
                workoutModel = WorkoutModel(
                    title = "fake title + 1",
                    imagePath = 1,
                    duration = 1f,
                    listExercises = listOf(ExerciseModel(
                        "fake title", 5, "123", 123)
                    )
                ), isCompleted = false
            ))
        }
        assertEquals(9, fakeRepository.plannedWorkoutList.size)
    }


}