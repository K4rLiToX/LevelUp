package com.carlosdiestro.levelup.core.framework

import androidx.room.Database
import androidx.room.RoomDatabase
import com.carlosdiestro.levelup.bodyweight_progress.framework.BodyWeightDAO
import com.carlosdiestro.levelup.bodyweight_progress.framework.BodyWeightEntity
import com.carlosdiestro.levelup.exercise_library.framework.ExerciseDAO
import com.carlosdiestro.levelup.exercise_library.framework.ExerciseEntity

@Database(
    entities = [BodyWeightEntity::class, ExerciseEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LevelUpDatabase : RoomDatabase() {
    abstract fun bodyWeightDao(): BodyWeightDAO
    abstract fun exerciseDao(): ExerciseDAO
}