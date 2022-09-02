package com.carlosdiestro.levelup.workouts.domain.repositories

import com.carlosdiestro.levelup.workouts.domain.models.WorkoutDropSet
import kotlinx.coroutines.flow.Flow

interface WorkoutDropSetRepository {

    fun getSetDropSets(setId: Int): Flow<List<WorkoutDropSet>>
    suspend fun insert(list: List<WorkoutDropSet>)
    suspend fun insert(workoutDropSet: WorkoutDropSet)
    suspend fun update(list: List<WorkoutDropSet>)
    suspend fun update(workoutDropSet: WorkoutDropSet)
    suspend fun delete(list: List<WorkoutDropSet>)
    suspend fun delete(workoutDropSet: WorkoutDropSet)
}