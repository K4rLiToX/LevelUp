package com.carlosdiestro.levelup.exercise_library.ui.exercise_add

import com.carlosdiestro.levelup.core.ui.resources.StringResource
import com.carlosdiestro.levelup.exercise_library.domain.models.ExerciseCategory

data class NewExerciseState(
    val exerciseName: String = "",
    val isUnilateral: Boolean = false,
    val exerciseCategory: ExerciseCategory = ExerciseCategory.PUSH,
    val exerciseNameError: StringResource? = null,
    val isSubmitSuccessful: Boolean = false
)

sealed interface NewExerciseEvent {
    class Save(
        val name: String,
        val isUnilateral: Boolean,
        val category: ExerciseCategory
    ) : NewExerciseEvent

    object ResetState : NewExerciseEvent
}