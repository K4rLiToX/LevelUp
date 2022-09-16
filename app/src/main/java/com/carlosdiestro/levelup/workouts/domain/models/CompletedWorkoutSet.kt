package com.carlosdiestro.levelup.workouts.domain.models

data class CompletedWorkoutSet(
    val id: Int,
    val exerciseId: Int,
    val setOrder: Int,
    val date: String,
    val repetitions: Repetition,
    val weights: Weight,
    val status: SetStatus
)
