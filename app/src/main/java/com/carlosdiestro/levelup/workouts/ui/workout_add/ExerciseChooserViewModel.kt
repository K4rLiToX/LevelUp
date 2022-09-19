package com.carlosdiestro.levelup.workouts.ui.workout_add

import androidx.lifecycle.ViewModel
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.exercise_library.domain.models.ExerciseCategory
import com.carlosdiestro.levelup.exercise_library.domain.usecases.GetExercisesUseCase
import com.carlosdiestro.levelup.exercise_library.ui.models.ExercisePLO
import com.carlosdiestro.levelup.workouts.domain.usecases.FilterExerciseListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
        launchAndCollect(getExerciseListUseCase(ExerciseCategory.ALL)) { response ->
            originalList = response.toMutableList()
            _state.update {
                it.copy(
                    noData = response.isEmpty(),
                    exercises = response,
                    filter = ExerciseCategory.ALL
                )
            }
        }
    }

    private fun updateExerciseCategoryFilter(newFilter: ExerciseCategory) {
        launchAndCollect(filterExerciseListUseCase(newFilter, originalList)) { response ->
            _state.update {
                it.copy(
                    filter = newFilter,
                    exercises = response
                )
            }
        }
    }
}