package com.carlosdiestro.levelup.exercise_library.ui

import androidx.lifecycle.viewModelScope
import com.carlosdiestro.levelup.core.ui.base.BaseViewModel
import com.carlosdiestro.levelup.exercise_library.domain.models.ExerciseCategory
import com.carlosdiestro.levelup.exercise_library.domain.models.toExerciseGroup
import com.carlosdiestro.levelup.exercise_library.domain.usecases.GetExercisesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseCategoryViewModel @Inject constructor(
    private val getExercisesUseCase: GetExercisesUseCase
) : BaseViewModel() {

    private var _state: MutableStateFlow<ExerciseCategoryState> =
        MutableStateFlow(ExerciseCategoryState())
    val state = _state.asStateFlow()

    private var exerciseCategory: ExerciseCategory = ExerciseCategory.ALL

    fun getExercises() {
        viewModelScope.launch {
            getExercisesUseCase(exerciseCategory).collect { response ->
                _state.update {
                    it.copy(
                        noData = response.isEmpty(),
                        exerciseList = response
                    )
                }
            }
        }
    }

    fun setExerciseCategory(value: Int) {
        exerciseCategory = value.toExerciseGroup()
    }

    data class ExerciseCategoryState(
        val noData: Boolean = false,
        val exerciseList: List<ExercisePLO> = emptyList()
    )
}