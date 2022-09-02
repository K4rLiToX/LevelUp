package com.carlosdiestro.levelup.workouts.framework.middle_tables

import androidx.room.Embedded
import androidx.room.Relation
import com.carlosdiestro.levelup.workouts.framework.WorkoutEntity

data class WorkoutWithExercises(
    @Embedded
    val workout: WorkoutEntity? = null,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutId",
        entity = ExerciseWithSets::class
    )
    val exercises: List<ExerciseWithSets> = emptyList()
)
