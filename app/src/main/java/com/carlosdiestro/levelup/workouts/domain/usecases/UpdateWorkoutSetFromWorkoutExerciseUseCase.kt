package com.carlosdiestro.levelup.workouts.domain.usecases

import com.carlosdiestro.levelup.core.data.DefaultDispatcher
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutSetPLO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UpdateWorkoutSetFromWorkoutExerciseUseCase @Inject constructor(
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) {

    operator fun invoke(
        exercise: WorkoutExercisePLO,
        set: WorkoutSetPLO,
        list: List<WorkoutExercisePLO>
    ): Flow<List<WorkoutExercisePLO>> = flow {
        emit(
            list.toMutableList().apply {
                this[exercise.exerciseOrder - 1] = this[exercise.exerciseOrder - 1].copy(
                    sets = updateSet(set, this[exercise.exerciseOrder - 1].sets)
                )
            }
        )
    }.flowOn(dispatcher)

    private fun updateSet(set: WorkoutSetPLO, sets: List<WorkoutSetPLO>): List<WorkoutSetPLO> {
        return sets.map {
            if (it.id == set.id) it.copy(repRange = set.repRange)
            else it
        }
    }
}