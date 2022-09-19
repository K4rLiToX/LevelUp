package com.carlosdiestro.levelup.workouts.framework

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutSetDao {

    @Insert
    suspend fun insert(list: List<WorkoutSetEntity>)

    @Insert
    suspend fun insert(workoutSetEntity: WorkoutSetEntity)

    @Update
    suspend fun update(list: List<WorkoutSetEntity>)

    @Update
    suspend fun update(workoutSetEntity: WorkoutSetEntity)

    @Delete
    suspend fun delete(list: List<WorkoutSetEntity>)

    @Delete
    suspend fun delete(workoutSetEntity: WorkoutSetEntity)

    @Transaction
    @Query("SELECT * FROM workout_set_table WHERE exerciseId = :id ORDER BY setOrder ASC")
    fun getExerciseSets(id: Int): Flow<List<WorkoutSetEntity>>
}