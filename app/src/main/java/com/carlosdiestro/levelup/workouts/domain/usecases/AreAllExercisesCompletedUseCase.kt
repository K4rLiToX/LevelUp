package com.carlosdiestro.levelup.workouts.domain.usecases

import com.carlosdiestro.levelup.core.data.DefaultDispatcher
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutExercisePLO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AreAllExercisesCompletedUseCase @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {

    operator fun invoke(list: List<CompletedWorkoutExercisePLO>): Flow<Boolean> = flow {
        val sets = list.flatMap { it.completedSets }
        emit(
            sets.find { !it.isCompleted } != null
        )
    }.flowOn(defaultDispatcher)
}