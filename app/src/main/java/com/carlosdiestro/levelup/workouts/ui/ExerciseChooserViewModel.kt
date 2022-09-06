package com.carlosdiestro.levelup.workouts.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosdiestro.levelup.exercise_library.domain.models.ExerciseCategory
import com.carlosdiestro.levelup.exercise_library.domain.usecases.GetExercisesUseCase
import com.carlosdiestro.levelup.exercise_library.ui.models.ExercisePLO
import com.carlosdiestro.levelup.workouts.domain.usecases.FilterExerciseListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseChooserViewModel @Inject constructor(
    private val getExerciseListUseCase: GetExercisesUseCase,
    private val filterExerciseListUseCase: FilterExerciseListUseCase
) : ViewModel() {

    private var _state: MutableStateFlow<ExerciseChooserState> =
        MutableStateFlow(ExerciseChooserState())
    val state = _state.asStateFlow()

    private lateinit var originalList: List<ExercisePLO>

    init {
        fetchExercises()
    }

    fun onEvent(event: ExerciseChooserEvent) {
        when (event) {
            is ExerciseChooserEvent.OnFilterChanged -> updateExerciseCategoryFilter(event.newFilter)
        }
    }

    private fun fetchExercises() {
        viewModelScope.launch {
            getExerciseListUseCase(ExerciseCategory.ALL).collect { response ->
                originalList = response.toMutableList()
                _state.update {
                    it.copy(
                        noData = response.isEmpty(),
                        exerciseList = response,
                        filter = ExerciseCategory.ALL
                    )
                }
            }
        }
    }

    private fun updateExerciseCategoryFilter(newFilter: ExerciseCategory) {
        viewModelScope.launch {
            filterExerciseListUseCase(newFilter, originalList).collect { newList ->
                _state.update {
                    it.copy(
                        filter = newFilter,
                        exerciseList = newList
                    )
                }
            }
        }
    }
}