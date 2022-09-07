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

fun WorkoutExercisePLO.toDomain(): WorkoutExercise = WorkoutExercise(
    id = id,
    workoutId = id,
    name = name,
    isUnilateral = isUnilateral,
    exerciseOrder = exerciseOrder,
    sets = sets.toDomain()
)

@JvmName("toDomainWorkoutExercisePLO")
fun List<WorkoutExercisePLO>.toDomain(): List<WorkoutExercise> = this.map { it.toDomain() }

fun WorkoutSetPLO.toDomain(): WorkoutSet = WorkoutSet(
    id = id,
    exerciseId = id,
    setOrder = setOrder,
    repRange = repRange
)

@JvmName("toDomainWorkoutSetPLO")
fun List<WorkoutSetPLO>.toDomain(): List<WorkoutSet> = this.map { it.toDomain() }