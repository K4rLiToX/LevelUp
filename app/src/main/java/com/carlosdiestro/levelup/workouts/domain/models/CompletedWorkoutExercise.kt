package com.carlosdiestro.levelup.workouts.domain.models

data class CompletedWorkoutExercise(
    val id: Int,
    val workoutId: Int,
    val date: String,
    val exerciseOrder: Int,
    val completedSets: List<CompletedWorkoutSet>
)
