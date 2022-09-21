package com.carlosdiestro.levelup.exercise_library.ui.exercise_add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosdiestro.levelup.exercise_library.domain.models.ExerciseCategory
import com.carlosdiestro.levelup.exercise_library.domain.usecases.BlankStringValidatorUseCase
import com.carlosdiestro.levelup.exercise_library.domain.usecases.GetExerciseUseCase
import com.carlosdiestro.levelup.exercise_library.domain.usecases.InsertExerciseUseCase
import com.carlosdiestro.levelup.exercise_library.domain.usecases.UpdateExerciseUseCase
import com.carlosdiestro.levelup.exercise_library.ui.models.ExercisePLO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewExerciseViewModel @Inject constructor(
    private val blankStringValidatorUseCase: BlankStringValidatorUseCase,
    private val getExerciseUseCase: GetExerciseUseCase,
    private val insertExerciseUseCase: InsertExerciseUseCase,
    private val updateExerciseUseCase: UpdateExerciseUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _state: MutableStateFlow<NewExerciseState> = MutableStateFlow(NewExerciseState())
    val state = _state.asStateFlow()

    private val exerciseId: Int = savedStateHandle["exerciseId"]!!
    private lateinit var exercise: ExercisePLO
    val isUpdateMode: Boolean = exerciseId != -1

    init {
        fetchExercise()
    }

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

    private fun fetchExercise() {
        viewModelScope.launch {
            if (exerciseId != -1) {
                exercise = getExerciseUseCase(exerciseId)
                _state.update {
                    it.copy(
                        exerciseName = exercise.name,
                        isUnilateral = exercise.isUnilateral,
                        exerciseCategory = exercise.category
                    )
                }
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
                if (isUpdateMode) {
                    updateExerciseUseCase(exercise.copy(
                        name = name,
                        isUnilateral = isUnilateral,
                        category = category
                    ))
                }
                else insertExerciseUseCase(name, category, isUnilateral)
                _state.update { it.copy(isSubmitSuccessful = true) }
            }
        }
    }
}