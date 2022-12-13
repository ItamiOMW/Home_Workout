package com.example.homeworkout.domain.usecase.workout_repository_usecases

import app.cash.turbine.test
import com.example.homeworkout.data.fake_repository_impl.FakeRepositoryImpl
import com.example.homeworkout.domain.models.PlannedWorkoutModel
import com.example.homeworkout.domain.repository.WorkoutRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AddPlannedWorkoutUseCaseTest {

    private lateinit var fakeRepository: WorkoutRepository

    private lateinit var addPlannedWorkoutUseCase: AddPlannedWorkoutUseCase

    @Before
    fun setUp() {

        fakeRepository = FakeRepositoryImpl()

        addPlannedWorkoutUseCase = AddPlannedWorkoutUseCase(fakeRepository)

    }

    @Test
    fun `addPlannedWorkoutUseCase, should add 5 plannedWorkouts to the fakeRepository list`() {
        runTest {
            (1..5).forEach {
                addPlannedWorkoutUseCase.invoke(PlannedWorkoutModel(it)).test {

                }
            }
        }
    }
}