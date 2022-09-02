package com.carlosdiestro.levelup.workouts.domain.models

data class WorkoutDropSet(
    val id: Int,
    val setId: Int,
    val dropSetOrder: Int,
    val repRange: RepRange
)
