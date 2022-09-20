package com.carlosdiestro.levelup.workouts.ui.workout_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.workouts.domain.usecases.GetCompletedWorkoutExerciseListUseCase
import com.carlosdiestro.levelup.workouts.ui.workout_on_going.WorkoutDetailsProgressState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WorkoutDetailsProgressViewModel @Inject constructor(
    private val getCompletedWorkoutExerciseListUseCase: GetCompletedWorkoutExerciseListUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _state: MutableStateFlow<WorkoutDetailsProgressState> =
        MutableStateFlow(WorkoutDetailsProgressState())
    val state = _state.asStateFlow()

    private val workoutId =
        savedStateHandle.get<Int>(WorkoutDetailsProgressFragment.WORKOUT_ID_KEY)!!

    init {
        fetchWorkout()
    }

    private fun fetchWorkout() {
        launchAndCollect(getCompletedWorkoutExerciseListUseCase(workoutId)) { response ->
            _state.update {
                it.copy(exercises = response)
            }
        }
    }
}