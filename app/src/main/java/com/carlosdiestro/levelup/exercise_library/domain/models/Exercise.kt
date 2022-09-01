package com.carlosdiestro.levelup.exercise_library.domain.models

data class Exercise(
    val id: Int,
    val name: String,
    val isUnilateral: Boolean,
    val category: ExerciseCategory
)
