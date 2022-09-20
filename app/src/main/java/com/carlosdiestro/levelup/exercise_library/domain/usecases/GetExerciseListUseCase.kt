package com.carlosdiestro.levelup.exercise_library.domain.usecases

import com.carlosdiestro.levelup.exercise_library.domain.models.ExerciseCategory
import com.carlosdiestro.levelup.exercise_library.domain.repositories.ExerciseRepository
import com.carlosdiestro.levelup.exercise_library.mappers.toPLO
import com.carlosdiestro.levelup.exercise_library.ui.models.ExercisePLO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetExerciseListUseCase @Inject constructor(
    private val repository: ExerciseRepository
) {

    operator fun invoke(category: ExerciseCategory): Flow<List<ExercisePLO>> =
        when (category) {
            ExerciseCategory.ALL -> repository.getAll().map { it.toPLO() }
            else -> repository.getByGroup(category).map { it.toPLO() }
        }
}

