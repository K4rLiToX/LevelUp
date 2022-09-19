package com.carlosdiestro.levelup.workouts.ui.workout_details

import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO

data class WorkoutDetailsExercisesState(
    val exercises: List<WorkoutExercisePLO> = emptyList()
)