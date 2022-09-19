package com.carlosdiestro.levelup.workouts.ui.workout_on_going

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutExercisePLO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OnGoingWorkoutExerciseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val exercise: CompletedWorkoutExercisePLO =
        savedStateHandle.get<CompletedWorkoutExercisePLO>(OnGoingWorkoutExerciseFragment.EXERCISE)!!

    private var _state: MutableStateFlow<OnGoingWorkoutExerciseState> =
        MutableStateFlow(OnGoingWorkoutExerciseState(exercise))
    val state = _state.asStateFlow()

    val exerciseOrder = exercise.exerciseOrder

    fun isUnilateral(): Boolean = exercise.isUnilateral

}