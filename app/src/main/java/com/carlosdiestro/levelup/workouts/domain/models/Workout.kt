package com.carlosdiestro.levelup.workouts.domain.models

data class Workout(
    val id: Int,
    val name: String,
    val exercises: List<WorkoutExercise>
)
