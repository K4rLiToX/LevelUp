package com.carlosdiestro.levelup.workouts.framework

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDropSetDAO {

    @Insert
    suspend fun insert(list: List<WorkoutDropSetEntity>)

    @Insert
    suspend fun insert(workoutDropSetEntity: WorkoutDropSetEntity)

    @Update
    suspend fun update(list: List<WorkoutDropSetEntity>)

    @Update
    suspend fun update(workoutDropSetEntity: WorkoutDropSetEntity)

    @Delete
    suspend fun delete(list: List<WorkoutDropSetEntity>)

    @Delete
    suspend fun delete(workoutDropSetEntity: WorkoutDropSetEntity)

    @Query("SELECT * FROM workout_drop_set_table WHERE setId = :id ORDER BY dropSetOrder DESC")
    fun getSetDropSets(id: Int): Flow<List<WorkoutDropSetEntity>?>
}