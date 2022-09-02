package com.carlosdiestro.levelup.workouts.framework

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "workout_drop_set_table",
    foreignKeys = [ForeignKey(
        entity = WorkoutSetEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("setId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class WorkoutDropSetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val setId: Int,
    val dropSetOrder: Int,
    val repRange: String
)
