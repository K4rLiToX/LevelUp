package com.carlosdiestro.levelup.bodyweight_progress.domain.usecases

import com.carlosdiestro.levelup.bodyweight_progress.domain.models.BodyWeight
import com.carlosdiestro.levelup.bodyweight_progress.domain.repositories.BodyWeightRepository
import com.carlosdiestro.levelup.core.ui.managers.TimeManager
import javax.inject.Inject

class InsertBodyWeightUseCase @Inject constructor(
    private val repository: BodyWeightRepository
) {

    suspend operator fun invoke(weight: Double) {
        repository.insert(
            BodyWeight(
                date = TimeManager.now(),
                weight = weight
            )
        )
    }
}