package com.carlosdiestro.levelup.workouts.domain.repositories

import com.carlosdiestro.levelup.workouts.domain.models.WorkoutExercise
import com.carlosdiestro.levelup.workouts.framework.middle_tables.ExerciseWithSets
import kotlinx.coroutines.flow.Flow

interface WorkoutExerciseRepository {

    fun getWorkoutExercisesWithSets(workoutId: Int): Flow<List<ExerciseWithSets>>
    suspend fun insert(list: List<WorkoutExercise>)
    suspend fun insert(workoutExercise: WorkoutExercise)
    suspend fun update(list: List<WorkoutExercise>)
    suspend fun update(workoutExercise: WorkoutExercise)
    suspend fun delete(list: List<WorkoutExercise>)
    suspend fun delete(workoutExercise: WorkoutExercise)
}