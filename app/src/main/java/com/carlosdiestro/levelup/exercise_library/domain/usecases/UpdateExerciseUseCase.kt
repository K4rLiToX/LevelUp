package com.carlosdiestro.levelup.exercise_library.domain.usecases

import com.carlosdiestro.levelup.exercise_library.domain.repositories.ExerciseRepository
import com.carlosdiestro.levelup.exercise_library.mappers.toDomain
import com.carlosdiestro.levelup.exercise_library.ui.models.ExercisePLO
import javax.inject.Inject

class UpdateExerciseUseCase @Inject constructor(
    private val repository: ExerciseRepository
) {

    suspend operator fun invoke(exercise: ExercisePLO) {
        repository.update(exercise.toDomain())
    }
}