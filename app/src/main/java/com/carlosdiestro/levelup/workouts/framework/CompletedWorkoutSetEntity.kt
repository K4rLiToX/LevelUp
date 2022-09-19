package com.carlosdiestro.levelup.workouts.framework

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "completed_workout_set",
    indices = [
        Index(value = ["exerciseId"], unique = false)
    ],
    foreignKeys = [ForeignKey(
        entity = CompletedWorkoutExerciseEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("exerciseId"),
        onDelete = CASCADE
    )]
)
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
