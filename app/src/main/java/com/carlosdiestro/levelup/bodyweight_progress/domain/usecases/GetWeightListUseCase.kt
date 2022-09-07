package com.carlosdiestro.levelup.bodyweight_progress.domain.usecases

import com.carlosdiestro.levelup.bodyweight_progress.domain.models.BodyWeight
import com.carlosdiestro.levelup.bodyweight_progress.domain.repositories.BodyWeightRepository
import com.carlosdiestro.levelup.bodyweight_progress.ui.models.BodyWeightPLO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetWeightListUseCase @Inject constructor(
    private val repository: BodyWeightRepository
) {
    operator fun invoke(): Flow<List<BodyWeightPLO>> = flow {
        repository.getAll().collect { list ->
            emit(list.toPLO())
        }
    }
}

fun List<BodyWeight>.toPLO(): List<BodyWeightPLO> = this.map { it.toPLO() }
fun BodyWeight.toPLO(): BodyWeightPLO = BodyWeightPLO(date = date, weight = weight)
fun BodyWeightPLO.toDomain(): BodyWeight = BodyWeight(date = date, weight = weight)