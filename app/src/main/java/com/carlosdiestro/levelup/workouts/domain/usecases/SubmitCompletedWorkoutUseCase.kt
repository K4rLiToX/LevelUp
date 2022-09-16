package com.carlosdiestro.levelup.workouts.domain.usecases

import com.carlosdiestro.levelup.core.ui.managers.TimeManager
import com.carlosdiestro.levelup.workouts.domain.models.CompletedWorkoutExercise
import com.carlosdiestro.levelup.workouts.domain.models.CompletedWorkoutSet
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutExerciseRepository
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutSetPLO
import javax.inject.Inject

class SubmitCompletedWorkoutUseCase @Inject constructor(
    private val repository: WorkoutExerciseRepository
) {

    suspend operator fun invoke(list: List<CompletedWorkoutExercisePLO>) {
        repository.insert(list.toDomain(TimeManager.now()))
    }
}

fun CompletedWorkoutExercisePLO.toDomain(date: String): CompletedWorkoutExercise =
    CompletedWorkoutExercise(
        id = -1,
        workoutId = workoutId,
        date = date,
        exerciseOrder = exerciseOrder,
        completedSets = completedSets.toDomain(date)
    )

@JvmName("toDomainCompletedWorkoutExercisePLO")
fun List<CompletedWorkoutExercisePLO>.toDomain(date: String): List<CompletedWorkoutExercise> =
    this.map { it.toDomain(date) }

fun CompletedWorkoutSetPLO.toDomain(date: String): CompletedWorkoutSet = CompletedWorkoutSet(
    id = -1,
    exerciseId = -1,
    setOrder = setOrder,
    date = date,
    repetitions = currentReps,
    weights = currentWeight,
    status = status
)

@JvmName("toDomainCompletedWorkoutSetPLO")
fun List<CompletedWorkoutSetPLO>.toDomain(date: String): List<CompletedWorkoutSet> =
    this.map { it.toDomain(date) }