package com.carlosdiestro.levelup.bodyweight_progress.data.repositories

import com.carlosdiestro.levelup.bodyweight_progress.domain.models.BodyWeight
import com.carlosdiestro.levelup.bodyweight_progress.domain.repositories.BodyWeightRepository
import com.carlosdiestro.levelup.bodyweight_progress.framework.BodyWeightDAO
import com.carlosdiestro.levelup.bodyweight_progress.framework.BodyWeightEntity
import com.carlosdiestro.levelup.core.data.IoDispatcher
import com.carlosdiestro.levelup.core.ui.TimeManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BodyWeightRepositoryImpl @Inject constructor(
    private val dao: BodyWeightDAO,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BodyWeightRepository {

    override fun getAll(): Flow<List<BodyWeight>> = dao.getAll().map { it?.toDomain() ?: emptyList() }.flowOn(ioDispatcher)

    override suspend fun exists(date: String): Boolean = withContext(ioDispatcher) {
        dao.getByDate(TimeManager.toMillis(date)) != null
    }

    override suspend fun insert(bodyWeight: BodyWeight) = withContext(ioDispatcher) {
        dao.insert(bodyWeight.toEntity())
    }
}

fun List<BodyWeightEntity>.toDomain(): List<BodyWeight> = this.map { it.toDomain() }
fun BodyWeightEntity.toDomain(): BodyWeight =
    BodyWeight(date = TimeManager.toText(date), weight = weight)

fun BodyWeight.toEntity(): BodyWeightEntity =
    BodyWeightEntity(date = TimeManager.toMillis(date), weight = weight)