package com.carlosdiestro.levelup.workouts.framework

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.carlosdiestro.levelup.workouts.framework.middle_tables.CompletedWorkoutExercisesWithSets
import kotlinx.coroutines.flow.Flow

@Dao
interface CompletedWorkoutExerciseDao {

    @Insert
    suspend fun insert(completedWorkoutExercise: CompletedWorkoutExerciseEntity): Long

    @Insert
    suspend fun insert(list: List<CompletedWorkoutExerciseEntity>): List<Long>

    @Transaction
    @Query("SELECT * FROM completed_workout_exercise WHERE workoutId = :id ORDER BY date DESC")
    suspend fun getCompletedExercisesWithSets(id: Int): List<CompletedWorkoutExercisesWithSets>?

    @Transaction
    @Query("SELECT * FROM completed_workout_exercise WHERE workoutId = :id ORDER BY date DESC")
    fun getCompletedExercisesWithSetsFlow(id: Int): Flow<List<CompletedWorkoutExercisesWithSets>?>
}