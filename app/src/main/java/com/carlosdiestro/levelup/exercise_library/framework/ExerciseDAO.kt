package com.carlosdiestro.levelup.exercise_library.framework

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDAO {

    @Insert
    suspend fun insert(exerciseEntity: ExerciseEntity)

    @Update
    suspend fun update(exerciseEntity: ExerciseEntity)

    @Delete
    suspend fun delete(exerciseEntity: ExerciseEntity)

    @Query("SELECT * FROM exercise_table ORDER BY name ASC")
    fun getAll(): Flow<List<ExerciseEntity>?>

    @Query("SELECT * FROM exercise_table WHERE `group` = :group ORDER BY name ASC")
    fun getByGroup(group: Int): Flow<List<ExerciseEntity>?>
}