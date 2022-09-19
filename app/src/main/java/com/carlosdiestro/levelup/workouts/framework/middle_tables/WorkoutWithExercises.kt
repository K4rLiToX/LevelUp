package com.carlosdiestro.levelup.workouts.framework.middle_tables

import androidx.room.Embedded
import androidx.room.Relation
import com.carlosdiestro.levelup.workouts.framework.WorkoutEntity
import com.carlosdiestro.levelup.workouts.framework.WorkoutExerciseEntity

data class WorkoutWithExercises(
    @Embedded
    val workout: WorkoutEntity? = null,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutId",
        entity = WorkoutExerciseEntity::class
    )
    val exercises: List<ExerciseWithSets> = emptyList()
)
