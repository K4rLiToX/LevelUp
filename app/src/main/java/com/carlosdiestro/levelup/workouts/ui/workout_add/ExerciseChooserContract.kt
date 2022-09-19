package com.carlosdiestro.levelup.workouts.ui.workout_add

import com.carlosdiestro.levelup.exercise_library.domain.models.ExerciseCategory
import com.carlosdiestro.levelup.exercise_library.ui.models.ExercisePLO

sealed interface ExerciseChooserEvent {
    class OnFilterChanged(val newFilter: ExerciseCategory) : ExerciseChooserEvent
}

data class ExerciseChooserState(
    val noData: Boolean = false,
    val exercises: List<ExercisePLO> = emptyList(),
    val filter: ExerciseCategory = ExerciseCategory.ALL
)