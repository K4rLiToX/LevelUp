package com.carlosdiestro.levelup.workouts.domain.usecases

import com.carlosdiestro.levelup.workouts.domain.models.CompletedWorkoutSet
import com.carlosdiestro.levelup.workouts.domain.models.Repetition
import com.carlosdiestro.levelup.workouts.domain.models.Weight
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutExerciseRepository
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutRepository
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutSetPLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutSetPLO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllWorkoutCompletedExercisesUseCase @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val repository: WorkoutExerciseRepository
) {

    operator fun invoke(id: Int): Flow<List<Pair<String, List<CompletedWorkoutExercisePLO>>>> =
        flow {
            val workout = workoutRepository.getById(id).first()
            val completedExercises = repository.getCompletedExercisesWithSets(id).firstOrNull()

            val exercisesPLO = workout.exercises.toPLO()

            if (completedExercises == null) {
                emit(emptyList())
            } else {
                emit(
                    exercisesPLO
                        .zip(completedExercises)
                        .map { (exercise, pair) ->
                            Pair(
                                exercise.name,
                                pair.second.map {
                                    CompletedWorkoutExercisePLO(
                                        workoutId = id,
                                        name = exercise.name,
                                        date = it.date,
                                        isUnilateral = exercise.isUnilateral,
                                        exerciseOrder = exercise.exerciseOrder,
                                        completedSets = getCompletedExercisesSets(
                                            exercise.sets,
                                            it.completedSets
                                        )
                                    )
                                }
                            )
                        }
                )
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