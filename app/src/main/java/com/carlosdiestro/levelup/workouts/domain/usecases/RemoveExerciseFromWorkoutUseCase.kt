package com.carlosdiestro.levelup.workouts.domain.usecases

import com.carlosdiestro.levelup.core.data.DefaultDispatcher
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoveExerciseFromWorkoutUseCase @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {

    operator fun invoke(id: Int, list: List<WorkoutExercisePLO>): Flow<List<WorkoutExercisePLO>> = flow {
        val exerciseToDelete = list.find { it.id == id }
        if(exerciseToDelete != null) {
            emit(
                (list subtract listOf(exerciseToDelete).toSet())
                    .toList()
                    .mapIndexed { i, e ->
                        if(e.exerciseOrder == i + 1) e
                        else e.copy(exerciseOrder = i + 1)
                    }
            )
        }

    }.flowOn(defaultDispatcher)
}