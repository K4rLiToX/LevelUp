package com.carlosdiestro.levelup.workouts.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosdiestro.levelup.workouts.domain.usecases.GetWorkoutListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutsViewModel @Inject constructor(
    private val getWorkoutListUseCase: GetWorkoutListUseCase
) : ViewModel() {

    private var _state: MutableStateFlow<WorkoutsState> = MutableStateFlow(WorkoutsState())
    val state = _state.asStateFlow()

    init {
        fetchWorkouts()
    }

    private fun fetchWorkouts() {
        viewModelScope.launch {
            getWorkoutListUseCase().collect { response ->
                _state.update {
                    it.copy(
                        noData = response.isEmpty(),
                        workoutList = response
                    )
                }
            }
        }
    }
}