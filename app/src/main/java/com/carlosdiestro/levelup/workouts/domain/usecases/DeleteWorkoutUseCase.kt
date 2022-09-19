package com.carlosdiestro.levelup.workouts.domain.usecases

import com.carlosdiestro.levelup.workouts.domain.models.Workout
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutRepository
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutPLO
import javax.inject.Inject

class DeleteWorkoutUseCase @Inject constructor(
    private val repository: WorkoutRepository
) {

    suspend operator fun invoke(workout: WorkoutPLO) {
        repository.delete(workout.toDomain())
    }
}

fun WorkoutPLO.toDomain(): Workout = Workout(
    id = id,
    name = name,
    emptyList()
)