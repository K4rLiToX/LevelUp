package com.carlosdiestro.levelup.workouts.data

import com.carlosdiestro.levelup.core.data.IoDispatcher
import com.carlosdiestro.levelup.workouts.domain.models.WorkoutSet
import com.carlosdiestro.levelup.workouts.domain.models.toRepRange
import com.carlosdiestro.levelup.workouts.domain.models.toStringValue
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutSetRepository
import com.carlosdiestro.levelup.workouts.framework.WorkoutSetDAO
import com.carlosdiestro.levelup.workouts.framework.WorkoutSetEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WorkoutSetRepositoryImpl @Inject constructor(
    private val dao: WorkoutSetDAO,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : WorkoutSetRepository {

    override fun getExerciseSets(exerciseId: Int): Flow<List<WorkoutSet>> =
        flow<List<WorkoutSet>> {
            dao.getExerciseSets(exerciseId).map { it.toDomain() }
        }.flowOn(ioDispatcher)

    override suspend fun insert(list: List<WorkoutSet>) = withContext(ioDispatcher) {
        dao.insert(list.toEntity())
    }

    override suspend fun insert(workoutSet: WorkoutSet) = withContext(ioDispatcher) {
        dao.insert(workoutSet.toEntity())
    }

    override suspend fun update(list: List<WorkoutSet>) = withContext(ioDispatcher) {
        dao.update(list.toEntity())
    }

    override suspend fun update(workoutSet: WorkoutSet) = withContext(ioDispatcher) {
        dao.update(workoutSet.toEntity())
    }

    override suspend fun delete(list: List<WorkoutSet>) = withContext(ioDispatcher) {
        dao.delete(list.toEntity())
    }

    override suspend fun delete(workoutSet: WorkoutSet) = withContext(ioDispatcher) {
        dao.delete(workoutSet.toEntity())
    }
}

fun WorkoutSetEntity.toDomain(): WorkoutSet = WorkoutSet(
    id = id!!,
    exerciseId = exerciseId,
    setOrder = setOrder,
    repRange = repRange.toRepRange()
)

fun List<WorkoutSetEntity>.toDomain(): List<WorkoutSet> = this.map { it.toDomain() }

fun WorkoutSet.toEntity(): WorkoutSetEntity = WorkoutSetEntity(
    id = id,
    exerciseId = exerciseId,
    setOrder = setOrder,
    repRange = repRange.toStringValue()
)

fun List<WorkoutSet>.toEntity(): List<WorkoutSetEntity> = this.map { it.toEntity() }