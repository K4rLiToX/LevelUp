package com.carlosdiestro.levelup.workouts.domain.usecases

import com.carlosdiestro.levelup.workouts.domain.models.Workout
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutRepository
import com.carlosdiestro.levelup.workouts.mappers.toDomain
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO
import javax.inject.Inject

class AddNewWorkoutUseCase @Inject constructor(
    private val repository: WorkoutRepository
) {

    suspend operator fun invoke(name: String, list: List<WorkoutExercisePLO>) {
        repository.insert(
            Workout(
                id = -1,
                name = name,
                exercises = list.toDomain()
            )
        )
    }
}