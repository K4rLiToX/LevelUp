package com.carlosdiestro.levelup.exercise_library.framework

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_table")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val isUnilateral: Boolean,
    val group: Int
)
