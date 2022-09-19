package com.carlosdiestro.levelup.workouts.domain.usecases

import com.carlosdiestro.levelup.core.data.DefaultDispatcher
import com.carlosdiestro.levelup.exercise_library.ui.models.ExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ReplaceWorkoutExerciseUseCase @Inject constructor(
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) {

    operator fun invoke(
        id: Int,
        newExercise: ExercisePLO,
        list: List<WorkoutExercisePLO>
    ): Flow<List<WorkoutExercisePLO>> = flow {
        val isAlreadyAdded = list.find { it.name == newExercise.name } != null
        if (!isAlreadyAdded) {
            val exerciseToReplace = list.find { it.id == id }!!
            val newExerciseTransformed = WorkoutExercisePLO(
                id = -1,
                name = newExercise.name,
                exerciseOrder = exerciseToReplace.exerciseOrder,
                isUnilateral = newExercise.isUnilateral
            )
            emit(
                list
                    .toMutableList()
                    .map {
                        if (it.id == id) newExerciseTransformed
                        else it
                    }
            )
        }

    }.flowOn(dispatcher)
}