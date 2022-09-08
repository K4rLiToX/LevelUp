package com.carlosdiestro.levelup.workouts.domain.repositories

import com.carlosdiestro.levelup.workouts.domain.models.Workout
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {

    fun getAll(): Flow<List<Workout>>
    fun getById(id: Int): Flow<Workout>
    suspend fun insert(workout: Workout)
    suspend fun update(workout: Workout)
    suspend fun delete(workout: Workout)
}