package com.carlosdiestro.levelup.bodyweight_progress.domain.usecases

import com.carlosdiestro.levelup.bodyweight_progress.domain.repositories.BodyWeightRepository
import com.carlosdiestro.levelup.bodyweight_progress.ui.models.BodyWeightPLO
import javax.inject.Inject

class UpdateBodyWeightUseCase @Inject constructor(
    private val repository: BodyWeightRepository
) {

    suspend operator fun invoke(bodyWeightPLO: BodyWeightPLO) {
        repository.update(bodyWeightPLO.toDomain())
    }
}