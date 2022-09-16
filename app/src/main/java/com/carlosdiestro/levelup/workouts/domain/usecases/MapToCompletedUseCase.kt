package com.carlosdiestro.levelup.workouts.domain.usecases

import com.carlosdiestro.levelup.core.data.DefaultDispatcher
import com.carlosdiestro.levelup.workouts.domain.models.CompletedWorkoutExercise
import com.carlosdiestro.levelup.workouts.domain.models.CompletedWorkoutSet
import com.carlosdiestro.levelup.workouts.domain.models.Repetition
import com.carlosdiestro.levelup.workouts.domain.models.Weight
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutSetPLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutSetPLO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MapToCompletedUseCase @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(
        workoutExercises: List<WorkoutExercisePLO>,
        completedExercises: List<CompletedWorkoutExercise>
    ): List<CompletedWorkoutExercisePLO> = withContext(defaultDispatcher) {
        workoutExercises.sortedBy { it.exerciseOrder }
            .zip(completedExercises.sortedBy { it.exerciseOrder })
            .map { (exercise, completedExercise) ->
                CompletedWorkoutExercisePLO(
                    workoutId = -1,
                    name = exercise.name,
                    isUnilateral = exercise.isUnilateral,
                    exerciseOrder = exercise.exerciseOrder,
                    completedSets = getCompletedExercisesSets(
                        exercise.sets,
                        completedExercise.completedSets
                    )
                )
            }
    }

    private suspend fun getCompletedExercisesSets(
        sets: List<WorkoutSetPLO>,
        completedSets: List<CompletedWorkoutSet>
    ): List<CompletedWorkoutSetPLO> = withContext(defaultDispatcher) {
        sets.sortedBy { it.setOrder }
            .zip(completedSets.sortedBy { it.setOrder })
            .map { (set, completedSet) ->
                CompletedWorkoutSetPLO(
                    exerciseId = -1,
                    repRange = set.repRange,
                    setOrder = set.setOrder,
                    lastReps = completedSet.repetitions,
                    lastWeight = completedSet.weights,
                    currentReps = Repetition(),
                    currentWeight = Weight(),
                    status = completedSet.status
                )
            }
    }
}