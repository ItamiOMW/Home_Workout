package com.example.homeworkout.domain.usecase

import com.example.homeworkout.data.repository_impl.FakeWorkoutRepository
import com.example.homeworkout.domain.usecase.workout_repository_usecases.DeletePlannedWorkoutUseCase
import com.example.homeworkout.formatDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class DeletePlannedWorkoutUseCaseTest {

    private lateinit var deletePlannedWorkoutUseCase: DeletePlannedWorkoutUseCase

    private lateinit var fakeRepository: FakeWorkoutRepository


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