package com.carlosdiestro.levelup.workouts.framework

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "completed_workout_exercise",
    indices = [
        Index(value = ["workoutId"], unique = false)
    ],
    foreignKeys = [ForeignKey(
        entity = WorkoutEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("workoutId"),
        onDelete = CASCADE
    )]
)
data class CompletedWorkoutExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val workoutId: Int,
    val date: Long,
    val exerciseOrder: Int
)
