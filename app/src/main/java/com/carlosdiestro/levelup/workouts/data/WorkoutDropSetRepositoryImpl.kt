package com.carlosdiestro.levelup.workouts.data

import com.carlosdiestro.levelup.core.data.IoDispatcher
import com.carlosdiestro.levelup.workouts.domain.models.WorkoutDropSet
import com.carlosdiestro.levelup.workouts.domain.models.toStringValue
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutDropSetRepository
import com.carlosdiestro.levelup.workouts.framework.WorkoutDropSetDAO
import com.carlosdiestro.levelup.workouts.framework.WorkoutDropSetEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WorkoutDropSetRepositoryImpl @Inject constructor(
    private val dao: WorkoutDropSetDAO,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : WorkoutDropSetRepository {

    override fun getSetDropSets(setId: Int): Flow<List<WorkoutDropSet>> =
        flow<List<WorkoutDropSet>> {
            dao.getSetDropSets(setId).map { it?.map { ds -> ds.toDomain() } ?: emptyList() }
        }.flowOn(ioDispatcher)

    override suspend fun insert(list: List<WorkoutDropSet>) = withContext(ioDispatcher) {
        dao.insert(list.toEntity())
    }

    override suspend fun insert(workoutDropSet: WorkoutDropSet) = withContext(ioDispatcher) {
        dao.insert(workoutDropSet.toEntity())
    }

    override suspend fun update(list: List<WorkoutDropSet>) = withContext(ioDispatcher) {
        dao.update(list.toEntity())
    }

    override suspend fun update(workoutDropSet: WorkoutDropSet) = withContext(ioDispatcher) {
        dao.update(workoutDropSet.toEntity())
    }

    override suspend fun delete(list: List<WorkoutDropSet>) = withContext(ioDispatcher) {
        dao.delete(list.toEntity())
    }

    override suspend fun delete(workoutDropSet: WorkoutDropSet) = withContext(ioDispatcher) {
        dao.delete(workoutDropSet.toEntity())
    }
}

fun WorkoutDropSet.toEntity(): WorkoutDropSetEntity = WorkoutDropSetEntity(
    id = id,
    setId = setId,
    dropSetOrder = dropSetOrder,
    repRange = repRange.toStringValue()
)

fun List<WorkoutDropSet>.toEntity(): List<WorkoutDropSetEntity> = this.map { it.toEntity() }