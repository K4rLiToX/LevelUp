package com.carlosdiestro.levelup.workouts.framework

import androidx.room.*
import com.carlosdiestro.levelup.workouts.framework.middle_tables.WorkoutWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDAO {

    @Insert
    suspend fun insert(workoutEntity: WorkoutEntity)

    @Update
    suspend fun update(workoutEntity: WorkoutEntity)

    @Delete
    suspend fun delete(workoutEntity: WorkoutEntity)

    @Transaction
    @Query("SELECT * FROM workout_table ORDER BY name DESC")
    fun getAll(): Flow<List<WorkoutWithExercises>?>
}