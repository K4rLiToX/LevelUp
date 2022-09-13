package com.carlosdiestro.levelup.workouts.ui.workout_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.workouts.domain.usecases.GetWorkoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WorkoutDetailsExercisesViewModel @Inject constructor(
    private val getWorkoutUseCase: GetWorkoutUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _state: MutableStateFlow<WorkoutDetailsExercisesState> =
        MutableStateFlow(WorkoutDetailsExercisesState())
    val state = _state.asStateFlow()

    private val workoutId =
        savedStateHandle.get<Int>(WorkoutDetailsExercisesFragment.WORKOUT_ID_KEY)!!

    init {
        fetchWorkout()
    }

    private fun fetchWorkout() {
        launchAndCollect(getWorkoutUseCase(workoutId)) { (_, exercises) ->
            _state.update {
                it.copy(
                    exerciseList = exercises
                )
            }
        }
    }
}