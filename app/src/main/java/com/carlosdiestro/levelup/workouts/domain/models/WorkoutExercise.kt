package com.carlosdiestro.levelup.workouts.domain.models

data class WorkoutExercise(
    val id: Int,
    val workoutId: Int,
    val name: String,
    val isUnilateral: Boolean,
    val exerciseOrder: Int,
    val sets: List<WorkoutSet>
)
