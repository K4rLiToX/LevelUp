package com.carlosdiestro.levelup.workouts.framework.middle_tables

import androidx.room.Embedded
import androidx.room.Relation
import com.carlosdiestro.levelup.workouts.framework.WorkoutExerciseEntity
import com.carlosdiestro.levelup.workouts.framework.WorkoutSetEntity

data class ExerciseWithSets(
    @Embedded
    val exercise: WorkoutExerciseEntity? = null,
    @Relation(
        parentColumn = "id",
        entityColumn = "exerciseId",
        entity = WorkoutSetEntity::class
    )
    val sets: List<WorkoutSetEntity> = emptyList()
)
