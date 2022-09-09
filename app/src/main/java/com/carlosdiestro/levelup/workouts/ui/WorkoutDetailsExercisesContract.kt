package com.carlosdiestro.levelup.workouts.ui

import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO

data class WorkoutDetailsExercisesState(
    val exerciseList: List<WorkoutExercisePLO> = emptyList()
)