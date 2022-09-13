package com.carlosdiestro.levelup.workouts.ui.workout_on_going

import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutExercisePLO

data class OnGoingWorkoutState(
    val workoutName: String = "",
    val completedExercises: List<CompletedWorkoutExercisePLO> = mutableListOf(),
    val currentExercise: CompletedWorkoutExercisePLO? = null
)