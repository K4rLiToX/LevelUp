package com.carlosdiestro.levelup.core.framework

import androidx.room.Database
import androidx.room.RoomDatabase
import com.carlosdiestro.levelup.bodyweight_progress.framework.BodyWeightDao
import com.carlosdiestro.levelup.bodyweight_progress.framework.BodyWeightEntity
import com.carlosdiestro.levelup.exercise_library.framework.ExerciseDao
import com.carlosdiestro.levelup.exercise_library.framework.ExerciseEntity
import com.carlosdiestro.levelup.workouts.framework.*

@Database(
    entities = [
        BodyWeightEntity::class,
        ExerciseEntity::class,
        WorkoutEntity::class,
        WorkoutExerciseEntity::class,
        WorkoutSetEntity::class,
        CompletedWorkoutExerciseEntity::class,
        CompletedWorkoutSetEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class LevelUpDatabase : RoomDatabase() {
    abstract fun bodyWeightDao(): BodyWeightDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutExerciseDao(): WorkoutExerciseDao
    abstract fun workoutSetDao(): WorkoutSetDao
    abstract fun completedExerciseDao(): CompletedWorkoutExerciseDao
    abstract fun completedSetDao(): CompletedWorkoutSetDao
}