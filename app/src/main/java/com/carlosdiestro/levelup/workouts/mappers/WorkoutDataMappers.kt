package com.carlosdiestro.levelup.workouts.mappers

import com.carlosdiestro.levelup.core.ui.managers.TimeManager
import com.carlosdiestro.levelup.workouts.domain.models.*
import com.carlosdiestro.levelup.workouts.framework.*
import com.carlosdiestro.levelup.workouts.framework.middle_tables.CompletedWorkoutExercisesWithSets
import com.carlosdiestro.levelup.workouts.framework.middle_tables.ExerciseWithSets
import com.carlosdiestro.levelup.workouts.framework.middle_tables.WorkoutWithExercises

fun WorkoutExercise.toEntityInsert(realWorkoutId: Int): WorkoutExerciseEntity =
    WorkoutExerciseEntity(
        workoutId = realWorkoutId,
        name = name,
        isUnilateral = isUnilateral,
        orderPosition = exerciseOrder
    )

fun List<WorkoutExercise>.toEntityInsert(workoutId: Int): List<WorkoutExerciseEntity> =
    this.map { it.toEntityInsert(workoutId) }

fun WorkoutExercise.toEntity(): WorkoutExerciseEntity = WorkoutExerciseEntity(
    id = id,
    workoutId = workoutId,
    name = name,
    isUnilateral = isUnilateral,
    orderPosition = exerciseOrder
)

fun List<WorkoutExercise>.toEntity(): List<WorkoutExerciseEntity> = this.map { it.toEntity() }

fun CompletedWorkoutExercisesWithSets.toDomain(): CompletedWorkoutExercise =
    CompletedWorkoutExercise(
        id = this.exercise!!.id!!,
        workoutId = this.exercise.workoutId,
        date = TimeManager.toText(this.exercise.date),
        exerciseOrder = this.exercise.exerciseOrder,
        completedSets = this.sets.toDomain()
    )

@JvmName("toDomainCompletedWorkoutExercisesWithSets")
fun List<CompletedWorkoutExercisesWithSets>.toDomain(): List<CompletedWorkoutExercise> =
    this.map { it.toDomain() }

fun CompletedWorkoutSetEntity.toDomain(): CompletedWorkoutSet = CompletedWorkoutSet(
    id = id!!,
    exerciseId = exerciseId,
    setOrder = setOrder,
    date = TimeManager.toText(date),
    repetitions = repetitions.toRepetition(),
    weights = weights.toWeight(),
    status = status.toSetStatus()
)

@JvmName("toDomainCompletedWorkoutSet")
fun List<CompletedWorkoutSetEntity>.toDomain(): List<CompletedWorkoutSet> =
    this.map { it.toDomain() }

fun CompletedWorkoutExercise.toEntity(): CompletedWorkoutExerciseEntity =
    CompletedWorkoutExerciseEntity(
        workoutId = workoutId,
        exerciseOrder = exerciseOrder,
        date = TimeManager.toMillis(date)
    )

@JvmName("toEntityCompletedWorkoutExercise")
fun List<CompletedWorkoutExercise>.toEntity(): List<CompletedWorkoutExerciseEntity> =
    this.map { it.toEntity() }

@JvmName("toDomainWorkoutWithExercises")
fun List<WorkoutWithExercises>.toDomain(): List<Workout> = this.map { it.toDomain() }

fun ExerciseWithSets.toDomain(): WorkoutExercise = WorkoutExercise(
    id = this.exercise!!.id!!,
    workoutId = this.exercise.workoutId,
    name = this.exercise.name,
    isUnilateral = this.exercise.isUnilateral,
    exerciseOrder = this.exercise.orderPosition,
    sets = this.sets.toDomain()
)

@JvmName("toDomainExerciseWithSets")
fun List<ExerciseWithSets>.toDomain(): List<WorkoutExercise> = this.map { it.toDomain() }

fun Workout.toEntity(): WorkoutEntity = WorkoutEntity(
    id = if (id == -1) null else id,
    name = name
)

fun WorkoutWithExercises.toDomain(): Workout = Workout(
    id = this.workout!!.id!!,
    name = this.workout.name,
    exercises = this.exercises.toDomain()
)

fun WorkoutSetEntity.toDomain(): WorkoutSet = WorkoutSet(
    id = id!!,
    exerciseId = exerciseId,
    setOrder = setOrder,
    repRange = repRange.toRepRange()
)

fun List<WorkoutSetEntity>.toDomain(): List<WorkoutSet> = this.map { it.toDomain() }

fun WorkoutSet.toEntity(): WorkoutSetEntity = WorkoutSetEntity(
    id = if (id == -1) null else id,
    exerciseId = exerciseId,
    setOrder = setOrder,
    repRange = repRange.toStringValue()
)

@JvmName("toEntityWorkoutSet")
fun List<WorkoutSet>.toEntity(): List<WorkoutSetEntity> = this.map { it.toEntity() }

fun CompletedWorkoutSet.toEntity(): CompletedWorkoutSetEntity = CompletedWorkoutSetEntity(
    exerciseId = exerciseId,
    setOrder = setOrder,
    date = TimeManager.toMillis(date),
    repetitions = repetitions.toStringValue(),
    weights = weights.toStringValue(),
    status = status.toInt()
)

@JvmName("toEntityCompletedWorkoutSet")
fun List<CompletedWorkoutSet>.toEntity(): List<CompletedWorkoutSetEntity> =
    this.map { it.toEntity() }