package com.example.homeworkout.domain.usecase

import com.example.homeworkout.data.repository_impl.FakeWorkoutRepository
import com.example.homeworkout.domain.models.ExerciseModel
import com.example.homeworkout.domain.models.WorkoutModel
import com.example.homeworkout.domain.usecase.workout_repository_usecases.GetAllWorkoutsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class GetAllWorkoutsUseCaseTest {

    private lateinit var getAllWorkoutsUseCase: GetAllWorkoutsUseCase

    private lateinit var fakeRepository: FakeWorkoutRepository

    private val listWorkout = mutableListOf<WorkoutModel>()


    @BeforeEach
    fun setUp() {

        fakeRepository = FakeWorkoutRepository()

        getAllWorkoutsUseCase = GetAllWorkoutsUseCase(fakeRepository)

        //ALPHABET FROM FIRST TO TENTH LETTER
        runTest {
            (1..10).forEachIndexed { index, i ->
                listWorkout.add(
                    WorkoutModel(
                        id = index,
                        title = "fake title + $index",
                        imagePath = index,
                        duration = index.toFloat(),
                        listExercises = listOf(ExerciseModel(
                            "fake title", 5, "123", 123)
                        )
                    )
                )
            }
        }
        runTest {
            listWorkout.map { fakeRepository.addWorkout(it) }
        }

    }

    @Test
    fun `should return list with all workouts (WorkoutModel only) `() {
        val list = getAllWorkoutsUseCase.invoke()
        assertEquals(listWorkout, list.value)
    }
}