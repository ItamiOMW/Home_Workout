package com.example.homeworkout.domain.usecases

import com.example.homeworkout.data.repository_impl.FakeWorkoutRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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