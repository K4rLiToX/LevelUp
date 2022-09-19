package com.carlosdiestro.levelup.workouts.framework

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_table")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String
)
