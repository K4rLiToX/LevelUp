package com.carlosdiestro.levelup.workouts.mappers

import com.carlosdiestro.levelup.workouts.domain.models.*
import com.carlosdiestro.levelup.workouts.ui.models.*

fun WorkoutExercisePLO.toDomain(workoutId: Int? = null): WorkoutExercise = WorkoutExercise(
    id = if (id != -1) id else -1,
    workoutId = workoutId ?: id,
    name = name,
    isUnilateral = isUnilateral,
    exerciseOrder = exerciseOrder,
    sets = sets.toDomain(id)
)

@JvmName("toDomainWorkoutExercisePLO")
fun List<WorkoutExercisePLO>.toDomain(workoutId: Int? = null): List<WorkoutExercise> =
    this.map { it.toDomain(workoutId) }

fun WorkoutSetPLO.toDomain(exerciseId: Int? = null): WorkoutSet = WorkoutSet(
    id = if (id != -1) id else -1,
    exerciseId = exerciseId ?: id,
    setOrder = setOrder,
    repRange = repRange
)

@JvmName("toDomainWorkoutSetPLO")
fun List<WorkoutSetPLO>.toDomain(exerciseId: Int? = null): List<WorkoutSet> =
    this.map { it.toDomain(exerciseId) }

fun WorkoutPLO.toDomain(): Workout = Workout(
    id = id,
    name = name,
    emptyList()
)

fun Workout.toPLO(): WorkoutPLO = WorkoutPLO(
    id = id,
    name = name,
    numberOfExercises = exercises.size.toString()
)

fun List<Workout>.toPLO(): List<WorkoutPLO> = this.map { it.toPLO() }

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

fun CompletedWorkoutExercisePLO.toDomain(date: String): CompletedWorkoutExercise =
    CompletedWorkoutExercise(
        id = -1,
        workoutId = workoutId,
        date = date,
        exerciseOrder = exerciseOrder,
        completedSets = completedSets.toDomain(date)
    )

@JvmName("toDomainCompletedWorkoutExercisePLO")
fun List<CompletedWorkoutExercisePLO>.toDomain(date: String): List<CompletedWorkoutExercise> =
    this.map { it.toDomain(date) }

fun CompletedWorkoutSetPLO.toDomain(date: String): CompletedWorkoutSet = CompletedWorkoutSet(
    id = -1,
    exerciseId = -1,
    setOrder = setOrder,
    date = date,
    repetitions = currentReps,
    weights = currentWeight,
    status = status
)

@JvmName("toDomainCompletedWorkoutSetPLO")
fun List<CompletedWorkoutSetPLO>.toDomain(date: String): List<CompletedWorkoutSet> =
    this.map { it.toDomain(date) }