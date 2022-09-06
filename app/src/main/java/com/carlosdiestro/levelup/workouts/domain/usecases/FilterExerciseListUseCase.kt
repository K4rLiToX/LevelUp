package com.carlosdiestro.levelup.workouts.domain.usecases

import com.carlosdiestro.levelup.core.data.DefaultDispatcher
import com.carlosdiestro.levelup.exercise_library.domain.models.ExerciseCategory
import com.carlosdiestro.levelup.exercise_library.ui.models.ExercisePLO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FilterExerciseListUseCase @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {

    operator fun invoke(
        filter: ExerciseCategory,
        list: List<ExercisePLO>
    ): Flow<List<ExercisePLO>> = flow {
        when (filter) {
            ExerciseCategory.ALL -> emit(list)
            else -> emit(list.filter { it.category == filter })
        }
    }.flowOn(defaultDispatcher)
}