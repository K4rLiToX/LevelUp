package com.carlosdiestro.levelup.exercise_library.domain.models

enum class ExerciseCategory {
    PUSH,
    PULL,
    LEGS,
    CORE,
    ALL
}

fun ExerciseCategory.toValue(): Int {
    return when (this) {
        ExerciseCategory.PUSH -> ordinal
        ExerciseCategory.PULL -> ordinal
        ExerciseCategory.LEGS -> ordinal
        ExerciseCategory.CORE -> ordinal
        ExerciseCategory.ALL -> ordinal
    }
}

fun Int.toExerciseCategory(): ExerciseCategory {
    return when (this) {
        ExerciseCategory.PUSH.ordinal -> ExerciseCategory.PUSH
        ExerciseCategory.PULL.ordinal -> ExerciseCategory.PULL
        ExerciseCategory.LEGS.ordinal -> ExerciseCategory.LEGS
        ExerciseCategory.CORE.ordinal -> ExerciseCategory.CORE
        else -> ExerciseCategory.ALL
    }
}