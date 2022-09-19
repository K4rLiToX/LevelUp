package com.carlosdiestro.levelup.workouts.domain.usecases

import com.carlosdiestro.levelup.workouts.domain.models.Workout
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutRepository
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutPLO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetWorkoutListUseCase @Inject constructor(
    private val repository: WorkoutRepository
) {
    operator fun invoke(): Flow<List<WorkoutPLO>> =
        repository.getAll().map { it.toPLO() }
}

fun Workout.toPLO(): WorkoutPLO = WorkoutPLO(
    id = id,
    name = name,
    numberOfExercises = exercises.size.toString()
)

fun List<Workout>.toPLO(): List<WorkoutPLO> = this.map { it.toPLO() }
