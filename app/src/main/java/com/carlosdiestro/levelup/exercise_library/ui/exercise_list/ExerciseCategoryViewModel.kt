package com.carlosdiestro.levelup.exercise_library.ui.exercise_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.exercise_library.domain.models.ExerciseCategory
import com.carlosdiestro.levelup.exercise_library.domain.models.toExerciseCategory
import com.carlosdiestro.levelup.exercise_library.domain.usecases.GetExerciseListUseCase
import com.carlosdiestro.levelup.exercise_library.ui.exercise_list.ExerciseCategoryFragment.Companion.EXERCISE_CATEGORY_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ExerciseCategoryViewModel @Inject constructor(
    private val getExerciseListUseCase: GetExerciseListUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _state: MutableStateFlow<ExerciseCategoryState> =
        MutableStateFlow(ExerciseCategoryState())
    val state = _state.asStateFlow()

    private var exerciseCategory: ExerciseCategory =
        savedStateHandle.get<Int>(EXERCISE_CATEGORY_KEY)!!.toExerciseCategory()

    init {
        fetchExercises()
    }

    private fun fetchExercises() {
        launchAndCollect(getExerciseListUseCase(exerciseCategory)) { response ->
            _state.update {
                it.copy(
                    noData = response.isEmpty(),
                    exercises = response
                )
            }
        }
    }
}