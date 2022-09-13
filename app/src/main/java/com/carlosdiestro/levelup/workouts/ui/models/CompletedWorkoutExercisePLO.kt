package com.carlosdiestro.levelup.workouts.ui.models

data class CompletedWorkoutExercisePLO(
    val name: String,
    val isUnilateral: Boolean,
    val exerciseOrder: Int,
    val completedSets: List<CompletedWorkoutSetPLO> = mutableListOf()
)