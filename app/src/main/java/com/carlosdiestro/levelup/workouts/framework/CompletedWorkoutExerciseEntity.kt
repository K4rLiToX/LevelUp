package com.carlosdiestro.levelup.workouts.framework

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "completed_workout_exercise")
data class CompletedWorkoutExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val workoutId: Int,
    val date: Long,
    val exerciseOrder: Int
)
