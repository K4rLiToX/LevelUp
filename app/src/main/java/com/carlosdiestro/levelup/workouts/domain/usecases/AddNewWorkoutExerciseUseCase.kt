package com.carlosdiestro.levelup.workouts.domain.usecases

import com.carlosdiestro.levelup.core.data.DefaultDispatcher
import com.carlosdiestro.levelup.exercise_library.ui.models.ExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddNewWorkoutExerciseUseCase @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {

    operator fun invoke(
        newExercise: ExercisePLO,
        list: List<WorkoutExercisePLO>
    ): Flow<List<WorkoutExercisePLO>> = flow {
        val isAlreadyAdded = list.find { it.id == newExercise.id } != null
        if (!isAlreadyAdded) emit(
            list.plus(
                WorkoutExercisePLO(
                    id = newExercise.id,
                    name = newExercise.name,
                    exerciseOrder = list.size + 1,
                    isUnilateral = newExercise.isUnilateral
                )
            )
        )
    }.flowOn(defaultDispatcher)
}