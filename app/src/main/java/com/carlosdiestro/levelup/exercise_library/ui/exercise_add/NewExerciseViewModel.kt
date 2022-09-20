package com.carlosdiestro.levelup.exercise_library.ui.exercise_add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosdiestro.levelup.exercise_library.domain.models.ExerciseCategory
import com.carlosdiestro.levelup.exercise_library.domain.usecases.BlankStringValidatorUseCase
import com.carlosdiestro.levelup.exercise_library.domain.usecases.InsertExerciseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewExerciseViewModel @Inject constructor(
    private val blankStringValidatorUseCase: BlankStringValidatorUseCase,
    private val insertExerciseUseCase: InsertExerciseUseCase
) : ViewModel() {

    private var _state: MutableStateFlow<NewExerciseState> = MutableStateFlow(NewExerciseState())
    val state = _state.asStateFlow()

    fun onEvent(event: NewExerciseEvent) {
        when (event) {
            is NewExerciseEvent.Save -> submitExercise(
                event.name,
                event.category,
                event.isUnilateral
            )
            NewExerciseEvent.ResetState -> _state.update {
                it.copy(
                    exerciseName = "",
                    exerciseNameError = null,
                    isSubmitSuccessful = false
                )
            }
        }
    }

    private fun submitExercise(name: String, category: ExerciseCategory, isUnilateral: Boolean) {
        viewModelScope.launch {
            val response = blankStringValidatorUseCase(name)
            if (!response.isSuccessful) _state.update {
                it.copy(
                    exerciseName = name,
                    exerciseNameError = response.errorMessage,
                    isSubmitSuccessful = false
                )
            }
            else {
                insertExerciseUseCase(name, category, isUnilateral)
                _state.update { it.copy(isSubmitSuccessful = true) }
            }
        }
    }
}