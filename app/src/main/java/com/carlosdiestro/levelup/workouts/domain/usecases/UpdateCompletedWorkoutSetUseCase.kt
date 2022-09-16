package com.carlosdiestro.levelup.workouts.domain.usecases

import com.carlosdiestro.levelup.core.data.DefaultDispatcher
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutSetPLO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UpdateCompletedWorkoutSetUseCase @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {

    operator fun invoke(
        newSet: CompletedWorkoutSetPLO,
        exerciseOrder: Int,
        list: MutableList<CompletedWorkoutExercisePLO>
    ): Flow<List<CompletedWorkoutExercisePLO>> = flow {
        list[exerciseOrder - 1] = list[exerciseOrder - 1].copy(
            completedSets = list[exerciseOrder - 1].completedSets.map {
                if (it.setOrder == newSet.setOrder) newSet
                else it
            }
        )
        emit(list.toList())
    }.flowOn(defaultDispatcher)
}