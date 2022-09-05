package com.carlosdiestro.levelup.workouts.framework

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "workout_set_table",
    indices = [
        Index(value = ["exerciseId"], unique = false)
    ],
    foreignKeys = [ForeignKey(
        entity = WorkoutExerciseEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("exerciseId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class WorkoutSetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val exerciseId: Int,
    val setOrder: Int,
    val repRange: String,
)
