package com.carlosdiestro.levelup.workouts.ui.workout_details

import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutExercisePLO

data class WorkoutDetailsProgressExerciseState(
    val exercises: List<CompletedWorkoutExercisePLO> = emptyList()
)