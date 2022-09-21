package com.carlosdiestro.levelup.exercise_library.data.repositories

import com.carlosdiestro.levelup.core.data.IoDispatcher
import com.carlosdiestro.levelup.exercise_library.domain.models.Exercise
import com.carlosdiestro.levelup.exercise_library.domain.models.ExerciseCategory
import com.carlosdiestro.levelup.exercise_library.domain.models.toValue
import com.carlosdiestro.levelup.exercise_library.domain.repositories.ExerciseRepository
import com.carlosdiestro.levelup.exercise_library.framework.ExerciseDao
import com.carlosdiestro.levelup.exercise_library.mappers.toDomain
import com.carlosdiestro.levelup.exercise_library.mappers.toEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExerciseRepositoryImpl @Inject constructor(
    private val dao: ExerciseDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ExerciseRepository {

    override suspend fun insert(exercise: Exercise) = withContext(dispatcher) {
        dao.insert(exercise.toEntity())
    }

    override suspend fun update(exercise: Exercise) = withContext(dispatcher) {
        dao.update(exercise.toEntity())
    }

    override suspend fun delete(exercise: Exercise) = withContext(dispatcher) {
        dao.delete(exercise.toEntity())
    }

    override suspend fun getById(id: Int): Exercise = withContext(dispatcher){
        dao.getById(id).toDomain()
    }

    override fun getAll(): Flow<List<Exercise>> =
        dao.getAll().map { it?.toDomain() ?: emptyList() }.flowOn(dispatcher)

    override fun getByGroup(group: ExerciseCategory): Flow<List<Exercise>> =
        dao.getByGroup(group.toValue()).map { it?.toDomain() ?: emptyList() }
}

