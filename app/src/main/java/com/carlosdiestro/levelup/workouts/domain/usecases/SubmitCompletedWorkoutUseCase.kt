package com.carlosdiestro.levelup.workouts.domain.usecases

import com.carlosdiestro.levelup.core.ui.managers.TimeManager
import com.carlosdiestro.levelup.workouts.domain.models.CompletedWorkoutExercise
import com.carlosdiestro.levelup.workouts.domain.models.CompletedWorkoutSet
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutExerciseRepository
import com.carlosdiestro.levelup.workouts.mappers.toDomain
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