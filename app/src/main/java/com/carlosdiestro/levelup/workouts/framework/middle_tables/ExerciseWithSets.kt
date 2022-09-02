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
        entityColumn = "id",
        entity = SetWithDropSets::class
    )
    val sets: List<SetWithDropSets> = emptyList()
)
