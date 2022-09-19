package com.carlosdiestro.levelup.workouts.framework.middle_tables

import androidx.room.Embedded
import androidx.room.Relation
import com.carlosdiestro.levelup.workouts.framework.CompletedWorkoutExerciseEntity
import com.carlosdiestro.levelup.workouts.framework.CompletedWorkoutSetEntity

data class CompletedWorkoutExercisesWithSets(
    @Embedded
    val exercise: CompletedWorkoutExerciseEntity? = null,
    @Relation(
        parentColumn = "id",
        entityColumn = "exerciseId",
        entity = CompletedWorkoutSetEntity::class
    )
    val sets: List<CompletedWorkoutSetEntity>
)