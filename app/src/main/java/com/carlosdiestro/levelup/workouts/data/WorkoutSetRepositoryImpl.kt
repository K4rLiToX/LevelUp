package com.carlosdiestro.levelup.workouts.data

import com.carlosdiestro.levelup.core.data.IoDispatcher
import com.carlosdiestro.levelup.workouts.domain.models.WorkoutSet
import com.carlosdiestro.levelup.workouts.domain.models.toStringValue
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutDropSetRepository
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutSetRepository
import com.carlosdiestro.levelup.workouts.framework.WorkoutSetDAO
import com.carlosdiestro.levelup.workouts.framework.WorkoutSetEntity
import com.carlosdiestro.levelup.workouts.framework.middle_tables.SetWithDropSets
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WorkoutSetRepositoryImpl @Inject constructor(
    private val dao: WorkoutSetDAO,
    private val workoutDropSetRepository: WorkoutDropSetRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : WorkoutSetRepository {

    override fun getExerciseSetsWithDropSets(exerciseId: Int): Flow<List<SetWithDropSets>> =
        flow<List<SetWithDropSets>> {
            dao.getExerciseSetsWithDropSets(exerciseId).map { it.toDomain() }
        }.flowOn(ioDispatcher)

    override suspend fun insert(list: List<WorkoutSet>) = withContext(ioDispatcher) {
        dao.insert(list.toEntity())
        workoutDropSetRepository.insert(list.flatMap { it.dropSets })
    }

    override suspend fun insert(workoutSet: WorkoutSet) = withContext(ioDispatcher) {
        dao.insert(workoutSet.toEntity())
        workoutDropSetRepository.insert(workoutSet.dropSets)
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

fun WorkoutSet.toEntity(): WorkoutSetEntity = WorkoutSetEntity(
    id = id,
    exerciseId = exerciseId,
    setOrder = setOrder,
    repRange = repRange.toStringValue()
)

fun List<WorkoutSet>.toEntity(): List<WorkoutSetEntity> = this.map { it.toEntity() }