package com.carlosdiestro.levelup.core.framework

import android.content.Context
import androidx.room.Room
import com.carlosdiestro.levelup.bodyweight_progress.framework.BodyWeightDAO
import com.carlosdiestro.levelup.exercise_library.framework.ExerciseDAO
import com.carlosdiestro.levelup.workouts.framework.WorkoutDAO
import com.carlosdiestro.levelup.workouts.framework.WorkoutExerciseDAO
import com.carlosdiestro.levelup.workouts.framework.WorkoutSetDAO
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
    fun provideBodyWeightDAO(db: LevelUpDatabase): BodyWeightDAO {
        return db.bodyWeightDao()
    }

    @Provides
    @Singleton
    fun provideExerciseDAO(db: LevelUpDatabase): ExerciseDAO {
        return db.exerciseDao()
    }

    @Provides
    @Singleton
    fun provideWorkoutDAO(db: LevelUpDatabase): WorkoutDAO {
        return db.workoutDao()
    }

    @Provides
    @Singleton
    fun provideWorkoutExerciseDAO(db: LevelUpDatabase): WorkoutExerciseDAO {
        return db.workoutExerciseDao()
    }

    @Provides
    @Singleton
    fun provideWorkoutSetDAO(db: LevelUpDatabase): WorkoutSetDAO {
        return db.workoutSetDao()
    }
}