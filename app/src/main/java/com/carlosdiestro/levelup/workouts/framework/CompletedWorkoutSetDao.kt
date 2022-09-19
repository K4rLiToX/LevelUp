package com.carlosdiestro.levelup.workouts.framework

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface CompletedWorkoutSetDao {

    @Insert
    suspend fun insert(completedWorkoutSetEntity: CompletedWorkoutSetEntity)

    @Insert
    suspend fun insert(list: List<CompletedWorkoutSetEntity>)
}