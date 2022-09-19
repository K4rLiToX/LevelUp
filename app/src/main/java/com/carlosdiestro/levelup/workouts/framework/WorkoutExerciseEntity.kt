package com.carlosdiestro.levelup.workouts.framework

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "workout_exercise_table",
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
data class WorkoutExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val workoutId: Int,
    val name: String,
    val isUnilateral: Boolean,
    val orderPosition: Int
)
