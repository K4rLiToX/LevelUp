package com.carlosdiestro.levelup.workouts.domain.usecases

import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutRepository
import com.carlosdiestro.levelup.workouts.mappers.toPLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutPLO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetWorkoutUseCase @Inject constructor(
    private val repository: WorkoutRepository
) {

    operator fun invoke(workoutId: Int): Flow<Pair<WorkoutPLO, List<WorkoutExercisePLO>>> =
        repository.getById(workoutId).map { Pair(it.toPLO(), it.exercises.toPLO()) }
}