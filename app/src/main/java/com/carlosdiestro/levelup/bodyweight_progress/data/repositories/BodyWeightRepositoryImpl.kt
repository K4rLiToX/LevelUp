package com.carlosdiestro.levelup.bodyweight_progress.data.repositories

import com.carlosdiestro.levelup.bodyweight_progress.domain.models.BodyWeight
import com.carlosdiestro.levelup.bodyweight_progress.domain.repositories.BodyWeightRepository
import com.carlosdiestro.levelup.bodyweight_progress.framework.BodyWeightDao
import com.carlosdiestro.levelup.bodyweight_progress.framework.BodyWeightEntity
import com.carlosdiestro.levelup.core.data.IoDispatcher
import com.carlosdiestro.levelup.core.ui.managers.TimeManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BodyWeightRepositoryImpl @Inject constructor(
    private val dao: BodyWeightDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BodyWeightRepository {

    override fun getAll(): Flow<List<BodyWeight>> =
        dao.getAll().map { it?.toDomain() ?: emptyList() }.flowOn(dispatcher)

    override suspend fun exists(date: String): Boolean = withContext(dispatcher) {
        dao.getByDate(TimeManager.toMillis(date)) != null
    }

    override suspend fun insert(bodyWeight: BodyWeight) = withContext(dispatcher) {
        dao.insert(bodyWeight.toEntity())
    }

    override suspend fun update(bodyWeight: BodyWeight) = withContext(dispatcher) {
        dao.update(bodyWeight.toEntity())
    }
}

fun List<BodyWeightEntity>.toDomain(): List<BodyWeight> = this.map { it.toDomain() }
fun BodyWeightEntity.toDomain(): BodyWeight =
    BodyWeight(date = TimeManager.toText(date), weight = weight)

fun BodyWeight.toEntity(): BodyWeightEntity =
    BodyWeightEntity(date = TimeManager.toMillis(date), weight = weight)