package com.carlosdiestro.levelup.workouts.ui

import com.carlosdiestro.levelup.workouts.ui.models.WorkoutPLO

sealed class WorkoutEvent {
    class OnDeleteWorkout(val id: Int) : WorkoutEvent()
}

data class WorkoutsState(
    val noData: Boolean = false,
    val workoutList: List<WorkoutPLO> = emptyList()
)