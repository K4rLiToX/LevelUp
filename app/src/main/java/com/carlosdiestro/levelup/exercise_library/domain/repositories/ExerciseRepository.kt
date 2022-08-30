package com.carlosdiestro.levelup.exercise_library.domain.repositories

import com.carlosdiestro.levelup.exercise_library.domain.models.Exercise
import com.carlosdiestro.levelup.exercise_library.domain.models.ExerciseGroup
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    suspend fun insert(exercise: Exercise)
    suspend fun update(exercise: Exercise)
    suspend fun delete(exercise: Exercise)
    fun getAll(): Flow<List<Exercise>>
    fun getByGroup(group: ExerciseGroup): Flow<List<Exercise>>
}