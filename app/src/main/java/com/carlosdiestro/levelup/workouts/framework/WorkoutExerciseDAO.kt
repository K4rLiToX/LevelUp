package com.carlosdiestro.levelup.workouts.framework

import androidx.room.*
import com.carlosdiestro.levelup.workouts.framework.middle_tables.ExerciseWithSets
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutExerciseDAO {

    @Insert
    suspend fun insert(list: List<WorkoutExerciseEntity>): List<Long>

    @Insert
    suspend fun insert(workoutExerciseEntity: WorkoutExerciseEntity): Long

    @Update
    suspend fun update(list: List<WorkoutExerciseEntity>)

    @Update
    suspend fun update(workoutExerciseEntity: WorkoutExerciseEntity)

    @Delete
    suspend fun delete(list: List<WorkoutExerciseEntity>)

    @Delete
    suspend fun delete(workoutExerciseEntity: WorkoutExerciseEntity)

    @Query("DELETE FROM workout_exercise_table WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM workout_exercise_table WHERE workoutId = :id ORDER BY orderPosition DESC")
    fun getWorkoutExercises(id: Int): Flow<List<WorkoutExerciseEntity>>

    @Transaction
    @Query("SELECT * FROM workout_exercise_table WHERE workoutId = :id ORDER BY orderPosition DESC")
    fun getWorkoutExercisesWithSets(id: Int): Flow<List<ExerciseWithSets>>
}