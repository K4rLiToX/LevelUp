package com.carlosdiestro.levelup.exercise_library.domain.usecases

import com.carlosdiestro.levelup.exercise_library.domain.models.Exercise
import com.carlosdiestro.levelup.exercise_library.domain.models.ExerciseCategory
import com.carlosdiestro.levelup.exercise_library.domain.repositories.ExerciseRepository
import javax.inject.Inject

class InsertExerciseUseCase @Inject constructor(
    private val repository: ExerciseRepository
) {

    suspend operator fun invoke(name: String, category: ExerciseCategory, isUnilateral: Boolean) {
        repository.insert(
            Exercise(
                id = -1,
                name = name,
                isUnilateral = isUnilateral,
                category = category
            )
        )
    }
}