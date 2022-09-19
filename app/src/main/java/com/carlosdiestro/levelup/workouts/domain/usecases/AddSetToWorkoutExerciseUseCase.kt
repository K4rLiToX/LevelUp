package com.carlosdiestro.levelup.workouts.domain.usecases

import com.carlosdiestro.levelup.core.data.DefaultDispatcher
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutSetPLO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddSetToWorkoutExerciseUseCase @Inject constructor(
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) {

    operator fun invoke(
        set: WorkoutSetPLO,
        position: Int,
        list: List<WorkoutExercisePLO>
    ): Flow<List<WorkoutExercisePLO>> = flow {
        emit(list.toMutableList().apply {
            this[position] = this[position].copy(sets = this[position].sets.plus(set))
        })
    }.flowOn(dispatcher)

}