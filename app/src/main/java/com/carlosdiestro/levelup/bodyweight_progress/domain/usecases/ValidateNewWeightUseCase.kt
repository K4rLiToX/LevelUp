package com.carlosdiestro.levelup.bodyweight_progress.domain.usecases

import com.carlosdiestro.levelup.bodyweight_progress.domain.repositories.BodyWeightRepository
import com.carlosdiestro.levelup.core.domain.ValidationResult
import com.carlosdiestro.levelup.core.ui.resources.StringResource
import com.carlosdiestro.levelup.core.ui.managers.TimeManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ValidateNewWeightUseCase @Inject constructor(
    private val repository: BodyWeightRepository
) {

    operator fun invoke(input: String): Flow<ValidationResult> = flow {
        val weightAlreadyExists = repository.exists(TimeManager.now())
        when {
            input.isBlank() -> emit(ValidationResult(false, StringResource.BlankInput))
            input.startsWith("0") -> emit(ValidationResult(false, StringResource.ZeroValue))
            weightAlreadyExists -> emit(ValidationResult(false, StringResource.WeightExists))
            else -> emit(ValidationResult(true))
        }
    }
}