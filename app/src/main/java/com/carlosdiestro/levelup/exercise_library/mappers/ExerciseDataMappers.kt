package com.carlosdiestro.levelup.exercise_library.mappers

import com.carlosdiestro.levelup.exercise_library.domain.models.Exercise
import com.carlosdiestro.levelup.exercise_library.domain.models.toExerciseCategory
import com.carlosdiestro.levelup.exercise_library.domain.models.toValue
import com.carlosdiestro.levelup.exercise_library.framework.ExerciseEntity

fun Exercise.toEntity(): ExerciseEntity = ExerciseEntity(
    id = if (id != -1) id else null,
    name = name,
    isUnilateral = isUnilateral,
    group = category.toValue(),
)

fun ExerciseEntity.toDomain(): Exercise = Exercise(
    id = id!!,
    name = name,
    isUnilateral = isUnilateral,
    category = group.toExerciseCategory()
)

fun List<ExerciseEntity>.toDomain(): List<Exercise> =
    this.map { it.toDomain() }