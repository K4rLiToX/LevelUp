package com.carlosdiestro.levelup.bodyweight_progress.framework

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BodyWeightDAO {

    @Insert
    suspend fun insert(entity: BodyWeightEntity)

    @Update
    suspend fun update(entity: BodyWeightEntity)

    @Delete
    suspend fun delete(entity: BodyWeightEntity)

    @Query("SELECT * FROM body_weight_table ORDER BY date DESC")
    fun getAll(): Flow<List<BodyWeightEntity>?>

    @Query("SELECT * FROM body_weight_table WHERE date = :date")
    suspend fun getByDate(date: Long): BodyWeightEntity?
}