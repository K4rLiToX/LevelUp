package com.carlosdiestro.levelup.exercise_library.domain.repositories

import com.carlosdiestro.levelup.exercise_library.domain.models.Exercise
import com.carlosdiestro.levelup.exercise_library.domain.models.ExerciseCategory
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    suspend fun insert(exercise: Exercise)
    suspend fun update(exercise: Exercise)
    suspend fun delete(exercise: Exercise)
    suspend fun getById(id: Int): Exercise
    fun getAll(): Flow<List<Exercise>>
    fun getByGroup(group: ExerciseCategory): Flow<List<Exercise>>
}