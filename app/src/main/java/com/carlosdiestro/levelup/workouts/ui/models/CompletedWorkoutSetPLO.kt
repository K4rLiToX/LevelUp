package com.carlosdiestro.levelup.workouts.ui.models

import com.carlosdiestro.levelup.workouts.domain.models.RepRange
import com.carlosdiestro.levelup.workouts.domain.models.Repetition
import com.carlosdiestro.levelup.workouts.domain.models.Weight

data class CompletedWorkoutSetPLO(
    val setOrder: Int,
    val repRange: RepRange,
    val lastReps: Repetition,
    val lastWeight: Weight,
    val currentReps: Repetition,
    val currentWeight: Weight
)
