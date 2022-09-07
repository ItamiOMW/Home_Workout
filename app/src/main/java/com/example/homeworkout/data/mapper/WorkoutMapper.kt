package com.example.homeworkout.data.mapper

import com.example.homeworkout.data.database.db_models.ExerciseDbModel
import com.example.homeworkout.data.database.db_models.PlannedWorkoutDbModel
import com.example.homeworkout.data.database.db_models.WorkoutDbModel
import com.example.homeworkout.domain.models.ExerciseModel
import com.example.homeworkout.domain.models.PlannedWorkoutModel
import com.example.homeworkout.domain.models.WorkoutModel

class WorkoutMapper {

    fun mapWorkoutDbToWorkoutEntity(model: WorkoutDbModel) = WorkoutModel(
        id = model.id,
        title = model.title,
        imagePath = model.imagePath,
        duration = model.duration,
        listExercises = mapListDbExercisesToListEntityExercises(model.listExercises),
    )

    private fun mapListDbExercisesToListEntityExercises(list: List<ExerciseDbModel>) =
        list.map {
            ExerciseModel(it.title, it.reps, it.exerciseGif)
        }


    fun mapWorkoutEntityToWorkoutDb(model: WorkoutModel) = WorkoutDbModel(
        id = model.id,
        title = model.title,
        imagePath = model.imagePath,
        duration = model.duration,
        listExercises = mapListEntityExercisesToListDbExercises(model.listExercises),
    )

    private fun mapListEntityExercisesToListDbExercises(list: List<ExerciseModel>) =
        list.map {
            ExerciseDbModel(it.title, it.reps, it.exerciseGif)
        }


    fun mapPlannedWorkoutDbToEntity(model: PlannedWorkoutDbModel) = PlannedWorkoutModel(
        id = model.id,
        date = model.date,
        workoutModel = mapWorkoutDbToWorkoutEntity(model.workoutDbModel),
        isCompleted = model.isCompleted
    )


    fun mapPlannedWorkoutEntityToDb(model: PlannedWorkoutModel) = PlannedWorkoutDbModel(
        id = model.id,
        date = model.date,
        workoutDbModel = mapWorkoutEntityToWorkoutDb(model.workoutModel),
        isCompleted = model.isCompleted
    )
}