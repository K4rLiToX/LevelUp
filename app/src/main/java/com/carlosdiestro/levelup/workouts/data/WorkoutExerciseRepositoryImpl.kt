package com.carlosdiestro.levelup.workouts.data

import com.carlosdiestro.levelup.core.data.IoDispatcher
import com.carlosdiestro.levelup.workouts.domain.models.WorkoutExercise
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutExerciseRepository
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutSetRepository
import com.carlosdiestro.levelup.workouts.framework.WorkoutExerciseDAO
import com.carlosdiestro.levelup.workouts.framework.WorkoutExerciseEntity
import com.carlosdiestro.levelup.workouts.framework.middle_tables.ExerciseWithSets
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WorkoutExerciseRepositoryImpl @Inject constructor(
    private val dao: WorkoutExerciseDAO,
    private val workoutSetRepository: WorkoutSetRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : WorkoutExerciseRepository {

    override fun getWorkoutExercisesWithSets(workoutId: Int): Flow<List<ExerciseWithSets>> =
        flow<List<ExerciseWithSets>> {
            dao.getWorkoutExercisesWithSets(workoutId).map { it.toDomain() }
        }.flowOn(ioDispatcher)

    override suspend fun insert(list: List<WorkoutExercise>) = withContext(ioDispatcher) {
        dao.insert(list.toEntity())
        workoutSetRepository.insert(list.flatMap { it.sets })
    }

    override suspend fun insert(workoutExercise: WorkoutExercise) = withContext(ioDispatcher) {
        dao.insert(workoutExercise.toEntity())
        workoutSetRepository.insert(workoutExercise.sets)
    }

    override suspend fun update(list: List<WorkoutExercise>) = withContext(ioDispatcher) {
        dao.update(list.toEntity())
    }

    override suspend fun update(workoutExercise: WorkoutExercise) = withContext(ioDispatcher) {
        dao.update(workoutExercise.toEntity())
    }

    override suspend fun delete(list: List<WorkoutExercise>) = withContext(ioDispatcher) {
        dao.delete(list.toEntity())
    }

    override suspend fun delete(workoutExercise: WorkoutExercise) = withContext(ioDispatcher) {
        dao.delete(workoutExercise.toEntity())
    }
}

fun WorkoutExercise.toEntity(): WorkoutExerciseEntity = WorkoutExerciseEntity(
    id = id,
    workoutId = workoutId,
    name = name,
    isUnilateral = isUnilateral,
    orderPosition = orderPosition
)

fun List<WorkoutExercise>.toEntity(): List<WorkoutExerciseEntity> = this.map { it.toEntity() }