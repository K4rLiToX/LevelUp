package com.carlosdiestro.levelup.exercise_library.ui.exercise_add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosdiestro.levelup.exercise_library.domain.models.ExerciseCategory
import com.carlosdiestro.levelup.exercise_library.domain.usecases.AddNewExerciseUseCase
import com.carlosdiestro.levelup.exercise_library.domain.usecases.ValidateExerciseNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewExerciseViewModel @Inject constructor(
    private val validateExerciseNameUseCase: ValidateExerciseNameUseCase,
    private val addNewExerciseUseCase: AddNewExerciseUseCase
) : ViewModel() {

    private var _state: MutableStateFlow<NewExerciseContract.NewExerciseState> =
        MutableStateFlow(NewExerciseContract.NewExerciseState())
    val state = _state.asStateFlow()

    fun onEvent(event: NewExerciseContract.NewExerciseEvent) {
        when (event) {
            is NewExerciseContract.NewExerciseEvent.SaveNewExercise -> submitNewExercise(
                event.name,
                event.category,
                event.isUnilateral
            )
            NewExerciseContract.NewExerciseEvent.ResetNewExerciseState -> _state.update {
                it.copy(
                    exerciseName = "",
                    exerciseNameError = null,
                    isSubmitSuccessful = false
                )
            }
        }
    }

    private fun submitNewExercise(name: String, category: ExerciseCategory, isUnilateral: Boolean) {
        viewModelScope.launch {
            val response = validateExerciseNameUseCase(name)
            if (!response.isSuccessful) _state.update {
                it.copy(
                    exerciseName = name,
                    exerciseNameError = response.errorMessage,
                    isSubmitSuccessful = false
                )
            }
            else {
                addNewExerciseUseCase(name, category, isUnilateral)
                _state.update { it.copy(isSubmitSuccessful = true) }
            }
        }
    }
}