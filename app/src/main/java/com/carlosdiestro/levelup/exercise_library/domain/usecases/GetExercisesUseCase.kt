package com.carlosdiestro.levelup.exercise_library.domain.usecases

import com.carlosdiestro.levelup.exercise_library.domain.models.Exercise
import com.carlosdiestro.levelup.exercise_library.domain.models.ExerciseCategory
import com.carlosdiestro.levelup.exercise_library.domain.repositories.ExerciseRepository
import com.carlosdiestro.levelup.exercise_library.ui.ExercisePLO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetExercisesUseCase @Inject constructor(
    private val repository: ExerciseRepository
) {

    operator fun invoke(category: ExerciseCategory): Flow<List<ExercisePLO>> = flow {
        when (category) {
            ExerciseCategory.ALL -> repository.getAll().collect { list -> emit(list.toPLO()) }
            else -> repository.getByGroup(category).collect { list -> emit(list.toPLO()) }
        }
    }
}

fun Exercise.toPLO(): ExercisePLO = ExercisePLO(
    id = id,
    name = name,
    isUnilateral = isUnilateral,
    category = category
)

fun ExercisePLO.toDomain(): Exercise = Exercise(
    id = id,
    name = name,
    isUnilateral = isUnilateral,
    category = category
)

fun List<Exercise>.toPLO(): List<ExercisePLO> = this.map { it.toPLO() }