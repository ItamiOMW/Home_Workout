package com.example.homeworkout.domain.usecase

import com.example.homeworkout.data.repository_impl.FakeWorkoutRepository
import com.example.homeworkout.domain.usecase.workout_repository_usecases.GetCountOfCompletedWorkoutsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class GetCountOfCompletedWorkoutsUseCaseTest {

    private lateinit var getCountOfCompletedWorkoutsUseCase: GetCountOfCompletedWorkoutsUseCase

    private lateinit var fakeRepository: FakeWorkoutRepository


    @BeforeEach
    fun setUp() {

        fakeRepository = FakeWorkoutRepository()

        getCountOfCompletedWorkoutsUseCase = GetCountOfCompletedWorkoutsUseCase(fakeRepository)

        fakeRepository.countOfCompletedWorkouts = 10
    }

    @Test
    fun `should return count of completed workouts (instance = 10) `() {
        runTest {
            val actual = getCountOfCompletedWorkoutsUseCase.invoke()
            assertEquals(10, actual)
        }
    }
}