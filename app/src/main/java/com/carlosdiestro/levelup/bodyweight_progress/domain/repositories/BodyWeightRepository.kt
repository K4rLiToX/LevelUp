package com.carlosdiestro.levelup.bodyweight_progress.domain.repositories

import com.carlosdiestro.levelup.bodyweight_progress.domain.models.BodyWeight
import kotlinx.coroutines.flow.Flow

interface BodyWeightRepository {
    fun getAll() : Flow<List<BodyWeight>>
}