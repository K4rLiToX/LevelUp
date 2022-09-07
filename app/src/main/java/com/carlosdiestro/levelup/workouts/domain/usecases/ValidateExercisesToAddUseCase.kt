package com.carlosdiestro.levelup.workouts.domain.usecases

import com.carlosdiestro.levelup.core.data.DefaultDispatcher
import com.carlosdiestro.levelup.core.domain.ValidationResult
import com.carlosdiestro.levelup.core.ui.resources.StringResource
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ValidateExercisesToAddUseCase @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(list: List<WorkoutExercisePLO>): ValidationResult = withContext(defaultDispatcher) {
        val noExercises = list.isEmpty()
        val noSetsAdded = list.find { it.sets.isEmpty() } != null

        if(noExercises) return@withContext ValidationResult(false, StringResource.NoExercisesAdded)
        if(noSetsAdded) return@withContext ValidationResult(false, StringResource.NoSetsAdded)

        return@withContext ValidationResult(true)
    }
}