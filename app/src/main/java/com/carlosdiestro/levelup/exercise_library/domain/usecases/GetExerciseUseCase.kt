package com.carlosdiestro.levelup.exercise_library.domain.usecases

import com.carlosdiestro.levelup.exercise_library.domain.repositories.ExerciseRepository
import com.carlosdiestro.levelup.exercise_library.mappers.toPLO
import com.carlosdiestro.levelup.exercise_library.ui.models.ExercisePLO
import javax.inject.Inject

class GetExerciseUseCase @Inject constructor(
    private val repository: ExerciseRepository
) {

    suspend operator fun invoke(id: Int): ExercisePLO {
        return repository.getById(id).toPLO()
    }
}