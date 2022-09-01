package com.carlosdiestro.levelup.exercise_library.ui.exercise_add

import com.carlosdiestro.levelup.core.ui.resources.StringResource
import com.carlosdiestro.levelup.exercise_library.domain.models.ExerciseCategory

class NewExerciseContract {

    data class NewExerciseState(
        val exerciseName: String = "",
        val exerciseNameError: StringResource? = null,
        val isSubmitSuccessful: Boolean = false
    )

    sealed class NewExerciseEvent {
        data class SaveNewExercise(
            val name: String,
            val isUnilateral: Boolean,
            val category: ExerciseCategory
        ) : NewExerciseEvent()

        object ResetNewExerciseState : NewExerciseEvent()
    }
}