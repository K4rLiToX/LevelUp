package com.carlosdiestro.levelup.workouts.framework

import androidx.room.*
import com.carlosdiestro.levelup.workouts.framework.middle_tables.SetWithDropSets
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutSetDAO {

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
    @Query("SELECT * FROM workout_set_table WHERE exerciseId = :id ORDER BY setOrder DESC")
    fun getExerciseSetsWithDropSets(id: Int): Flow<List<SetWithDropSets>>
}