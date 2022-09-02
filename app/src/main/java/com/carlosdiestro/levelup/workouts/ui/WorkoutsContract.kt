package com.carlosdiestro.levelup.workouts.ui

import com.carlosdiestro.levelup.workouts.ui.models.WorkoutPLO

data class WorkoutsState(
    val noData: Boolean = false,
    val workoutList: List<WorkoutPLO> = emptyList()
)