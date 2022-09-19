package com.carlosdiestro.levelup.core.framework

import android.content.Context
import androidx.room.Room
import com.carlosdiestro.levelup.bodyweight_progress.framework.BodyWeightDao
import com.carlosdiestro.levelup.exercise_library.framework.ExerciseDao
import com.carlosdiestro.levelup.workouts.framework.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FrameworkModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): LevelUpDatabase {
        return Room.databaseBuilder(
            context,
            LevelUpDatabase::class.java,
            "levelupdb"
        )
            .build()
    }

    @Provides
    @Singleton
    fun provideBodyWeightDAO(db: LevelUpDatabase): BodyWeightDao {
        return db.bodyWeightDao()
    }

    @Provides
    @Singleton
    fun provideExerciseDAO(db: LevelUpDatabase): ExerciseDao {
        return db.exerciseDao()
    }

    @Provides
    @Singleton
    fun provideWorkoutDAO(db: LevelUpDatabase): WorkoutDao {
        return db.workoutDao()
    }

    @Provides
    @Singleton
    fun provideWorkoutExerciseDAO(db: LevelUpDatabase): WorkoutExerciseDao {
        return db.workoutExerciseDao()
    }

    @Provides
    @Singleton
    fun provideWorkoutSetDAO(db: LevelUpDatabase): WorkoutSetDao {
        return db.workoutSetDao()
    }

    @Provides
    @Singleton
    fun provideCompletedWorkoutExerciseDAO(db: LevelUpDatabase): CompletedWorkoutExerciseDao {
        return db.completedExerciseDao()
    }

    @Provides
    @Singleton
    fun provideCompletedWorkoutSetDAO(db: LevelUpDatabase): CompletedWorkoutSetDao {
        return db.completedSetDao()
    }
}