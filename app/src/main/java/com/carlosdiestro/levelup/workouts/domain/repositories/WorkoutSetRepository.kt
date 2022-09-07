package com.carlosdiestro.levelup.workouts.domain.repositories

import com.carlosdiestro.levelup.workouts.domain.models.WorkoutSet
import kotlinx.coroutines.flow.Flow

interface WorkoutSetRepository {

    fun getExerciseSets(exerciseId: Int): Flow<List<WorkoutSet>>
    suspend fun insert(list: List<WorkoutSet>)
    suspend fun insert(workoutSet: WorkoutSet)
    suspend fun update(list: List<WorkoutSet>)
    suspend fun update(workoutSet: WorkoutSet)
    suspend fun delete(list: List<WorkoutSet>)
    suspend fun delete(workoutSet: WorkoutSet)
}