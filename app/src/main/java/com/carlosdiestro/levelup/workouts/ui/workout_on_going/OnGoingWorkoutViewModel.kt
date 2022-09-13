package com.carlosdiestro.levelup.workouts.ui.workout_on_going

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.workouts.domain.usecases.GetWorkoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OnGoingWorkoutViewModel @Inject constructor(
    private val getWorkoutUseCase: GetWorkoutUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _state: MutableStateFlow<OnGoingWorkoutState> =
        MutableStateFlow(OnGoingWorkoutState())
    val state = _state.asStateFlow()

    private val workoutId = savedStateHandle.get<Int>("workoutId")!!

    init {
        fetchWorkout()
    }

    private fun fetchWorkout() {
        launchAndCollect(getWorkoutUseCase(workoutId)) { response ->

        }
    }
}