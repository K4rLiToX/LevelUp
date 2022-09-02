package com.carlosdiestro.levelup.workouts.domain.models

data class WorkoutSet(
    val id: Int,
    val exerciseId: Int,
    val setOrder: Int,
    val repRange: RepRange,
    val dropSets: List<WorkoutDropSet>
)
