package com.carlosdiestro.levelup.workouts.domain.usecases

import com.carlosdiestro.levelup.workouts.domain.models.CompletedWorkoutSet
import com.carlosdiestro.levelup.workouts.domain.models.Repetition
import com.carlosdiestro.levelup.workouts.domain.models.SetStatus
import com.carlosdiestro.levelup.workouts.domain.models.Weight
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutExerciseRepository
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutRepository
import com.carlosdiestro.levelup.workouts.mappers.toPLO
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutSetPLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutSetPLO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLastCompletedWorkoutUseCase @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val workoutExerciseRepository: WorkoutExerciseRepository
) {

    operator fun invoke(id: Int): Flow<List<CompletedWorkoutExercisePLO>> = flow {
        val workout = workoutRepository.getById(id).first()
        val lastCompletedWorkout = workoutExerciseRepository.getLastCompletedExercisesWithSets(id)

        val exercisesPLO = workout.exercises.toPLO()

        if (lastCompletedWorkout.isEmpty()) {
            emit(
                exercisesPLO.map {
                    CompletedWorkoutExercisePLO(
                        workoutId = id,
                        name = it.name,
                        isUnilateral = it.isUnilateral,
                        exerciseOrder = it.exerciseOrder,
                        completedSets = it.sets.map { s ->
                            CompletedWorkoutSetPLO(
                                exerciseId = it.id,
                                repRange = s.repRange,
                                setOrder = s.setOrder,
                                lastReps = Repetition(),
                                lastWeight = Weight(),
                                currentReps = Repetition(),
                                currentWeight = Weight(),
                                status = SetStatus.KEEP_WORKING
                            )
                        }
                    )
                }
            )
        } else {
            emit(
                exercisesPLO
                    .zip(lastCompletedWorkout)
                    .map { (exercise, completedExercise) ->
                        CompletedWorkoutExercisePLO(
                            workoutId = id,
                            name = exercise.name,
                            isUnilateral = exercise.isUnilateral,
                            exerciseOrder = exercise.exerciseOrder,
                            completedSets = getCompletedExercisesSets(
                                exercise.sets,
                                completedExercise.completedSets
                            )
                        )
                    })
        }
    }

    private fun getCompletedExercisesSets(
        sets: List<WorkoutSetPLO>,
        completedSets: List<CompletedWorkoutSet>
    ): List<CompletedWorkoutSetPLO> {
        return sets
            .zip(completedSets)
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
