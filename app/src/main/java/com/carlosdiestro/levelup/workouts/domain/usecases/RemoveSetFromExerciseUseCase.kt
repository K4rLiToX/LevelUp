package com.carlosdiestro.levelup.workouts.domain.usecases

import com.carlosdiestro.levelup.core.data.DefaultDispatcher
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutSetPLO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoveSetFromExerciseUseCase @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {

    operator fun invoke(
        exercise: WorkoutExercisePLO,
        set: WorkoutSetPLO,
        list: List<WorkoutExercisePLO>
    ): Flow<List<WorkoutExercisePLO>> = flow {
        emit(
            list.toMutableList().apply {
                this[exercise.exerciseOrder - 1] = this[exercise.exerciseOrder - 1].copy(
                    sets = removeSetAndRearrangeList(set, this[exercise.exerciseOrder - 1].sets)
                )
            }
        )
    }.flowOn(defaultDispatcher)

    private fun removeSetAndRearrangeList(
        set: WorkoutSetPLO,
        list: List<WorkoutSetPLO>
    ): List<WorkoutSetPLO> {
        return when (set.setOrder) {
            1 -> list.filter { it.setOrder != 1 }.map { it.copy(setOrder = it.setOrder - 1) }
            list.size -> list.take(list.size - 1)
            else -> {
                val firstHalf = list.take(set.setOrder - 1)
                val secondHalf = list.takeLastWhile { it.setOrder > set.setOrder }
                    .map { it.copy(setOrder = it.setOrder - 1) }
                firstHalf.plus(secondHalf)
            }
        }
    }
}