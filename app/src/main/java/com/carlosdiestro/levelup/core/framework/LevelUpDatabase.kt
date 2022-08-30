package com.carlosdiestro.levelup.core.framework

import androidx.room.Database
import androidx.room.RoomDatabase
import com.carlosdiestro.levelup.bodyweight_progress.framework.BodyWeightDAO
import com.carlosdiestro.levelup.bodyweight_progress.framework.BodyWeightEntity

@Database(
    entities = [BodyWeightEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LevelUpDatabase : RoomDatabase() {
    abstract fun bodyWeightDao(): BodyWeightDAO
}