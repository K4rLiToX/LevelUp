package com.carlosdiestro.levelup.bodyweight_progress.domain.usecases

import com.carlosdiestro.levelup.bodyweight_progress.domain.repositories.BodyWeightRepository
import com.carlosdiestro.levelup.core.data.DefaultDispatcher
import com.carlosdiestro.levelup.core.domain.ValidationResult
import com.carlosdiestro.levelup.core.ui.managers.TimeManager
import com.carlosdiestro.levelup.core.ui.resources.StringResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ValidateNewWeightUseCase @Inject constructor(
    private val repository: BodyWeightRepository,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(input: String, isUpdate: Boolean = false): ValidationResult =
        withContext(dispatcher) {
            val weightAlreadyExists = repository.exists(TimeManager.now())
            when {
                input.isBlank() -> ValidationResult(false, StringResource.BlankInput)
                input.startsWith("0") -> ValidationResult(false, StringResource.ZeroValue)
                weightAlreadyExists && !isUpdate -> ValidationResult(
                    false,
                    StringResource.WeightExists
                )
                else -> ValidationResult(true)
            }
        }
}