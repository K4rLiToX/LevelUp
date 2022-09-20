package com.carlosdiestro.levelup.workouts.data

import com.carlosdiestro.levelup.core.data.IoDispatcher
import com.carlosdiestro.levelup.workouts.domain.models.Workout
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutExerciseRepository
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutRepository
import com.carlosdiestro.levelup.workouts.framework.WorkoutDao
import com.carlosdiestro.levelup.workouts.mappers.toDomain
import com.carlosdiestro.levelup.workouts.mappers.toEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val dao: WorkoutDao,
    private val workoutExerciseRepository: WorkoutExerciseRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : WorkoutRepository {

    override fun getAll(): Flow<List<Workout>> =
        dao.getAll().map { it?.toDomain() ?: emptyList() }.flowOn(dispatcher)

    override fun getById(id: Int): Flow<Workout> =
        dao.getById(id).map { it.toDomain() }.flowOn(dispatcher)

    override suspend fun insert(workout: Workout) = withContext(dispatcher) {
        val workoutId = dao.insert(workout.toEntity()).toInt()
        workoutExerciseRepository.insert(workoutId, workout.exercises)
    }

    override suspend fun update(workout: Workout) = withContext(dispatcher) {
        dao.update(workout.toEntity())
    }

    override suspend fun delete(workout: Workout) = withContext(dispatcher) {
        dao.delete(workout.toEntity())
    }
}