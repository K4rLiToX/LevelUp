package com.carlosdiestro.levelup.bodyweight_progress.domain.usecases

import com.carlosdiestro.levelup.bodyweight_progress.domain.models.BodyWeight
import com.carlosdiestro.levelup.bodyweight_progress.domain.repositories.BodyWeightRepository
import com.carlosdiestro.levelup.bodyweight_progress.ui.BodyWeightPLO
import com.carlosdiestro.levelup.core.domain.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetWeightListUseCase @Inject constructor(
    private val repository: BodyWeightRepository
) {
    operator fun invoke(): Flow<Response<List<BodyWeightPLO>>> = flow {
        emit(Response.Loading())
        repository.getAll().collect { list ->
            emit(Response.Success(list.toPLO()))
        }
    }
}

fun List<BodyWeight>.toPLO(): List<BodyWeightPLO> = this.map { it.toPLO() }
fun List<BodyWeightPLO>.toDomain(): List<BodyWeight> = this.map { it.toDomain() }
fun BodyWeight.toPLO(): BodyWeightPLO = BodyWeightPLO(date = date, weight = weight)
fun BodyWeightPLO.toDomain(): BodyWeight = BodyWeight(date = date, weight = weight)