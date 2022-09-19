package com.carlosdiestro.levelup.workouts.ui.workout_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutExercisePLO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class WorkoutDetailsProgressExerciseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val exercises = savedStateHandle.get<List<CompletedWorkoutExercisePLO>>(
        WorkoutDetailsProgressExerciseFragment.EXERCISES
    )!!

    private var _state: MutableStateFlow<WorkoutDetailsProgressExerciseState> =
        MutableStateFlow(WorkoutDetailsProgressExerciseState(exercises))
    val state = _state.asStateFlow()
}