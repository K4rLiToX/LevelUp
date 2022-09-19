package com.carlosdiestro.levelup.exercise_library.domain.usecases

import com.carlosdiestro.levelup.core.data.DefaultDispatcher
import com.carlosdiestro.levelup.core.domain.ValidationResult
import com.carlosdiestro.levelup.core.ui.resources.StringResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BlankStringValidatorUseCase @Inject constructor(
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(input: String): ValidationResult = withContext(dispatcher) {
        when {
            input.isBlank() -> ValidationResult(false, StringResource.BlankInput)
            else -> ValidationResult(true, null)
        }
    }
}