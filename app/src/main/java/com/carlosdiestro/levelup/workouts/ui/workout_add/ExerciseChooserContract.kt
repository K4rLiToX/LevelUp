package com.carlosdiestro.levelup.workouts.ui.workout_add

sealed interface ExerciseChooserResponse {
    object ExerciseSelected : ExerciseChooserResponse
}