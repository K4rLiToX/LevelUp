package com.carlosdiestro.levelup.exercise_library.domain.models

enum class ExerciseGroup() {
    PUSH,
    PULL,
    LEGS,
    CORE
}

fun ExerciseGroup.toValue(): Int {
    return when(this) {
        ExerciseGroup.PUSH -> ExerciseGroup.PUSH.ordinal
        ExerciseGroup.PULL -> ExerciseGroup.PULL.ordinal
        ExerciseGroup.LEGS -> ExerciseGroup.LEGS.ordinal
        ExerciseGroup.CORE -> ExerciseGroup.CORE.ordinal
    }
}

fun Int.toExerciseGroup() : ExerciseGroup {
    return when(this) {
        ExerciseGroup.PUSH.ordinal -> ExerciseGroup.PUSH
        ExerciseGroup.PULL.ordinal -> ExerciseGroup.PULL
        ExerciseGroup.LEGS.ordinal -> ExerciseGroup.LEGS
        else -> ExerciseGroup.CORE
    }
}