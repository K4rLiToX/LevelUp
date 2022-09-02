package com.carlosdiestro.levelup.workouts.framework.middle_tables

import androidx.room.Embedded
import androidx.room.Relation
import com.carlosdiestro.levelup.workouts.framework.WorkoutDropSetEntity
import com.carlosdiestro.levelup.workouts.framework.WorkoutSetEntity

data class SetWithDropSets(
    @Embedded
    val set: WorkoutSetEntity? = null,
    @Relation(
        parentColumn = "id",
        entityColumn = "setId",
        entity = WorkoutDropSetEntity::class
    )
    val dropSets: List<WorkoutDropSetEntity> = emptyList()
)
