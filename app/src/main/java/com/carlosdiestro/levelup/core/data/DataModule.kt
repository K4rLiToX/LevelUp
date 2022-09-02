package com.carlosdiestro.levelup.core.data

import com.carlosdiestro.levelup.bodyweight_progress.data.repositories.BodyWeightRepositoryImpl
import com.carlosdiestro.levelup.bodyweight_progress.domain.repositories.BodyWeightRepository
import com.carlosdiestro.levelup.exercise_library.data.repositories.ExerciseRepositoryImpl
import com.carlosdiestro.levelup.exercise_library.domain.repositories.ExerciseRepository
import com.carlosdiestro.levelup.workouts.data.WorkoutDropSetRepositoryImpl
import com.carlosdiestro.levelup.workouts.data.WorkoutExerciseRepositoryImpl
import com.carlosdiestro.levelup.workouts.data.WorkoutRepositoryImpl
import com.carlosdiestro.levelup.workouts.data.WorkoutSetRepositoryImpl
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutDropSetRepository
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutExerciseRepository
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutRepository
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutSetRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindBodyWeightRepository(bodyWeightRepositoryImpl: BodyWeightRepositoryImpl): BodyWeightRepository

    @Binds
    abstract fun bindExerciseRepository(exerciseRepositoryImpl: ExerciseRepositoryImpl): ExerciseRepository

    @Binds
    abstract fun bindWorkoutRepository(workoutRepositoryImpl: WorkoutRepositoryImpl): WorkoutRepository

    @Binds
    abstract fun bindWorkoutExerciseRepository(workoutExerciseRepositoryImpl: WorkoutExerciseRepositoryImpl): WorkoutExerciseRepository

    @Binds
    abstract fun bindWorkoutSetRepository(workoutSetRepositoryImpl: WorkoutSetRepositoryImpl): WorkoutSetRepository

    @Binds
    abstract fun bindWorkoutDropSetRepository(workoutDropSetRepositoryImpl: WorkoutDropSetRepositoryImpl): WorkoutDropSetRepository
}