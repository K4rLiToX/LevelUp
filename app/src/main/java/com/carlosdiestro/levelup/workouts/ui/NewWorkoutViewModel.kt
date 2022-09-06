package com.carlosdiestro.levelup.workouts.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosdiestro.levelup.exercise_library.ui.models.ExercisePLO
import com.carlosdiestro.levelup.workouts.domain.usecases.AddNewWorkoutExerciseUseCase
import com.carlosdiestro.levelup.workouts.domain.usecases.AddSetToWorkoutExerciseUseCase
import com.carlosdiestro.levelup.workouts.domain.usecases.RemoveSetFromExerciseUseCase
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutSetPLO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewWorkoutViewModel @Inject constructor(
    private val addNewWorkoutExerciseUseCase: AddNewWorkoutExerciseUseCase,
    private val addSetToWorkoutExerciseUseCase: AddSetToWorkoutExerciseUseCase,
    private val removeSetFromExerciseUseCase: RemoveSetFromExerciseUseCase
) : ViewModel() {

    private var exerciseList: MutableList<WorkoutExercisePLO> = mutableListOf()

    private var _state: MutableStateFlow<NewWorkoutState> = MutableStateFlow(NewWorkoutState())
    val state = _state.asStateFlow()

    fun onEvent(event: NewWorkoutEvent) {
        when (event) {
            is NewWorkoutEvent.OnExerciseClicked -> handleExercise(event.exercisePLO)
            is NewWorkoutEvent.OnNewSetClicked -> addNewSetToExercise(
                event.newSet,
                event.exercisePosition
            )
            is NewWorkoutEvent.OnSetRemoved -> removeSetFromExercise(
                event.exercise,
                event.set
            )
        }
    }

    private fun handleExercise(exercisePLO: ExercisePLO) {
        viewModelScope.launch {
            addNewWorkoutExerciseUseCase(
                exercisePLO,
                exerciseList.toList()
            ).collect { response ->
                exerciseList = response.toMutableList()
                _state.update {
                    it.copy(
                        noData = response.isEmpty(),
                        exerciseList = response
                    )
                }
            }
        }
    }

    private fun addNewSetToExercise(newSet: WorkoutSetPLO, position: Int) {
        viewModelScope.launch {
            addSetToWorkoutExerciseUseCase(
                newSet,
                position,
                exerciseList.toList()
            ).collect { response ->
                exerciseList = response.toMutableList()
                _state.update {
                    it.copy(
                        exerciseList = response
                    )
                }
            }
        }
    }

    private fun removeSetFromExercise(exercise: WorkoutExercisePLO, set: WorkoutSetPLO) {
        viewModelScope.launch {
            removeSetFromExerciseUseCase(
                exercise,
                set,
                exerciseList.toList()
            ).collect { response ->
                exerciseList = response.toMutableList()
                _state.update {
                    it.copy(
                        exerciseList = response
                    )
                }
            }
        }
    }
}