package com.carlosdiestro.levelup.bodyweight_progress.domain.usecases

import com.carlosdiestro.levelup.bodyweight_progress.domain.repositories.BodyWeightRepository
import com.carlosdiestro.levelup.bodyweight_progress.mappers.toPLO
import com.carlosdiestro.levelup.bodyweight_progress.ui.models.BodyWeightPLO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetBodyWeightListUseCase @Inject constructor(
    private val repository: BodyWeightRepository
) {
    operator fun invoke(): Flow<List<BodyWeightPLO>> = repository.getAll().map { it.toPLO() }
}

