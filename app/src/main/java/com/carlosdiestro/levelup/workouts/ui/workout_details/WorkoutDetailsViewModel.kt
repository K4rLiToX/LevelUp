package com.carlosdiestro.levelup.workouts.ui.workout_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WorkoutDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _state: MutableStateFlow<WorkoutDetailsState> =
        MutableStateFlow(WorkoutDetailsState())
    val state = _state.asStateFlow()

    private val toolbarTitle = savedStateHandle.get<String>("workoutName")!!
    private val workoutId = savedStateHandle.get<Int>("workoutId")!!

    init {
        _state.update {
            it.copy(title = toolbarTitle)
        }
    }

    fun getWorkoutId(): Int = workoutId
    fun getWorkoutName(): String = toolbarTitle
}