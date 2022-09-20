package com.carlosdiestro.levelup.workouts.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.workouts.domain.usecases.DeleteWorkoutUseCase
import com.carlosdiestro.levelup.workouts.domain.usecases.GetWorkoutListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutsViewModel @Inject constructor(
    private val getWorkoutListUseCase: GetWorkoutListUseCase,
    private val deleteWorkoutUseCase: DeleteWorkoutUseCase
) : ViewModel() {

    private var _state: MutableStateFlow<WorkoutsState> = MutableStateFlow(WorkoutsState())
    val state = _state.asStateFlow()

    init {
        fetchWorkouts()
    }

    fun onEvent(event: WorkoutEvent) {
        when (event) {
            is WorkoutEvent.DeleteWorkout -> deleteWorkout(event.id)
        }
    }

    private fun fetchWorkouts() {
        launchAndCollect(getWorkoutListUseCase()) { response ->
            _state.update {
                it.copy(
                    noData = response.isEmpty(),
                    workouts = response
                )
            }
        }
    }

    private fun deleteWorkout(id: Int) {
        val workoutToDelete = state.value.workouts.find { it.id == id }!!
        viewModelScope.launch {
            deleteWorkoutUseCase(workoutToDelete)
        }
    }
}