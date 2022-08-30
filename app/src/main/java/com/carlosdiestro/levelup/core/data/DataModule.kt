package com.carlosdiestro.levelup.core.data

import com.carlosdiestro.levelup.bodyweight_progress.data.repositories.BodyWeightRepositoryImpl
import com.carlosdiestro.levelup.bodyweight_progress.domain.repositories.BodyWeightRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindBodyWeightRepository(bodyWeightRepositoryImpl: BodyWeightRepositoryImpl): BodyWeightRepository
}