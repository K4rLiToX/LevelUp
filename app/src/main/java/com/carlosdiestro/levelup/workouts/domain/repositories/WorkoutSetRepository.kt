package com.carlosdiestro.levelup.workouts.domain.repositories

import com.carlosdiestro.levelup.workouts.domain.models.WorkoutSet
import com.carlosdiestro.levelup.workouts.framework.middle_tables.SetWithDropSets
import kotlinx.coroutines.flow.Flow

interface WorkoutSetRepository {

    fun getExerciseSetsWithDropSets(exerciseId: Int): Flow<List<SetWithDropSets>>
    suspend fun insert(list: List<WorkoutSet>)
    suspend fun insert(workoutSet: WorkoutSet)
    suspend fun update(list: List<WorkoutSet>)
    suspend fun update(workoutSet: WorkoutSet)
    suspend fun delete(list: List<WorkoutSet>)
    suspend fun delete(workoutSet: WorkoutSet)
}