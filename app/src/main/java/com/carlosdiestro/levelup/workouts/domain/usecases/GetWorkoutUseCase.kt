package com.carlosdiestro.levelup.workouts.domain.usecases

import com.carlosdiestro.levelup.workouts.domain.models.WorkoutExercise
import com.carlosdiestro.levelup.workouts.domain.models.WorkoutSet
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutRepository
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutPLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutSetPLO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetWorkoutUseCase @Inject constructor(
    private val repository: WorkoutRepository
) {

    operator fun invoke(workoutId: Int): Flow<Pair<WorkoutPLO, List<WorkoutExercisePLO>>> =
        repository.getById(workoutId).map { Pair(it.toPLO(), it.exercises.toPLO()) }
}

fun WorkoutExercise.toPLO(): WorkoutExercisePLO = WorkoutExercisePLO(
    id = id,
    name = name,
    isUnilateral = isUnilateral,
    exerciseOrder = exerciseOrder,
    sets = sets.toPLO()
)

@JvmName("toPLOWorkoutExercise")
fun List<WorkoutExercise>.toPLO(): List<WorkoutExercisePLO> = this.map { it.toPLO() }

fun WorkoutSet.toPLO(): WorkoutSetPLO = WorkoutSetPLO(
    id = id,
    setOrder = setOrder,
    repRange = repRange
)

@JvmName("toPLOWorkoutSet")
fun List<WorkoutSet>.toPLO(): List<WorkoutSetPLO> = this.map { it.toPLO() }