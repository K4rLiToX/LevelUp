package com.carlosdiestro.levelup.workouts.domain.usecases

import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutExerciseRepository
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutRepository
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutPLO
import javax.inject.Inject

class UpdateWorkoutUseCase @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val workoutExerciseRepository: WorkoutExerciseRepository
) {

    suspend operator fun invoke(
        isNameChanged: Boolean,
        workout: WorkoutPLO,
        list: List<WorkoutExercisePLO>
    ) {
        if (isNameChanged) workoutRepository.update(workout.toDomain())
        workoutExerciseRepository.update(workout.id, list.toDomain(workout.id))
    }
}