package com.carlosdiestro.levelup.workouts.framework

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "completed_workout_set")
data class CompletedWorkoutSetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val exerciseId: Int,
    val setOrder: Int,
    val date: Long,
    val repetitions: String,
    val weights: String,
    val status: Int
)
