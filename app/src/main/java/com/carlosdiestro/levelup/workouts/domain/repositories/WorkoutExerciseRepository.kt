package com.carlosdiestro.levelup.workouts.domain.repositories

import com.carlosdiestro.levelup.workouts.domain.models.WorkoutExercise
import com.carlosdiestro.levelup.workouts.framework.middle_tables.ExerciseWithSets
import kotlinx.coroutines.flow.Flow

interface WorkoutExerciseRepository {

    fun getWorkoutExercisesWithSets(workoutId: Int): Flow<List<ExerciseWithSets>>
    suspend fun insert(workoutId: Int, list: List<WorkoutExercise>)
    suspend fun insert(workoutId: Int, workoutExercise: WorkoutExercise)
    suspend fun update(workoutId: Int, newExercises: List<WorkoutExercise>)
    suspend fun update(workoutExercise: WorkoutExercise)
    suspend fun delete(list: List<WorkoutExercise>)
    suspend fun delete(workoutExercise: WorkoutExercise)
    suspend fun deleteById(id: Int)
}