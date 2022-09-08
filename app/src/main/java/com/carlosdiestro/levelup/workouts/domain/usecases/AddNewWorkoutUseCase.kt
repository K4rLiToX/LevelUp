package com.carlosdiestro.levelup.workouts.domain.usecases

import com.carlosdiestro.levelup.workouts.domain.models.Workout
import com.carlosdiestro.levelup.workouts.domain.models.WorkoutExercise
import com.carlosdiestro.levelup.workouts.domain.models.WorkoutSet
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutRepository
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutSetPLO
import javax.inject.Inject

class AddNewWorkoutUseCase @Inject constructor(
    private val workoutRepository: WorkoutRepository
) {

    suspend operator fun invoke(name: String, list: List<WorkoutExercisePLO>) {
        workoutRepository.insert(
            Workout(
                id = -1,
                name = name,
                exercises = list.toDomain()
            )
        )
    }
}

fun WorkoutExercisePLO.toDomain(workoutId: Int? = null): WorkoutExercise = WorkoutExercise(
    id = if(id != -1) id else -1,
    workoutId = workoutId ?: id,
    name = name,
    isUnilateral = isUnilateral,
    exerciseOrder = exerciseOrder,
    sets = sets.toDomain(id)
)

@JvmName("toDomainWorkoutExercisePLO")
fun List<WorkoutExercisePLO>.toDomain(workoutId: Int? = null): List<WorkoutExercise> =
    this.map { it.toDomain(workoutId) }

fun WorkoutSetPLO.toDomain(exerciseId: Int? = null): WorkoutSet = WorkoutSet(
    id = if(id != -1) id else -1,
    exerciseId = exerciseId ?: id,
    setOrder = setOrder,
    repRange = repRange
)

@JvmName("toDomainWorkoutSetPLO")
fun List<WorkoutSetPLO>.toDomain(exerciseId: Int? = null): List<WorkoutSet> =
    this.map { it.toDomain(exerciseId) }