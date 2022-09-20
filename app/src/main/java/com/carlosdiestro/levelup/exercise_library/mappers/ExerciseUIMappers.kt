package com.carlosdiestro.levelup.exercise_library.mappers

import com.carlosdiestro.levelup.exercise_library.domain.models.Exercise
import com.carlosdiestro.levelup.exercise_library.ui.models.ExercisePLO

fun Exercise.toPLO(): ExercisePLO = ExercisePLO(
    id = id,
    name = name,
    isUnilateral = isUnilateral,
    category = category
)

fun ExercisePLO.toDomain(): Exercise = Exercise(
    id = id,
    name = name,
    isUnilateral = isUnilateral,
    category = category
)

fun List<Exercise>.toPLO(): List<ExercisePLO> =
    this.map { it.toPLO() }