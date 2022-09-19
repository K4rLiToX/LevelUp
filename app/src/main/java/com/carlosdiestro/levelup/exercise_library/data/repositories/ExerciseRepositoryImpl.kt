package com.carlosdiestro.levelup.exercise_library.data.repositories

import com.carlosdiestro.levelup.core.data.IoDispatcher
import com.carlosdiestro.levelup.exercise_library.domain.models.Exercise
import com.carlosdiestro.levelup.exercise_library.domain.models.ExerciseCategory
import com.carlosdiestro.levelup.exercise_library.domain.models.toExerciseCategory
import com.carlosdiestro.levelup.exercise_library.domain.models.toValue
import com.carlosdiestro.levelup.exercise_library.domain.repositories.ExerciseRepository
import com.carlosdiestro.levelup.exercise_library.framework.ExerciseDao
import com.carlosdiestro.levelup.exercise_library.framework.ExerciseEntity
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

    override fun getAll(): Flow<List<Exercise>> =
        dao.getAll().map { it?.toDomain() ?: emptyList() }.flowOn(dispatcher)

    override fun getByGroup(group: ExerciseCategory): Flow<List<Exercise>> =
        dao.getByGroup(group.toValue()).map { it?.toDomain() ?: emptyList() }
}

fun Exercise.toEntity(): ExerciseEntity = ExerciseEntity(
    name = name,
    isUnilateral = isUnilateral,
    group = category.toValue(),
)

fun ExerciseEntity.toDomain(): Exercise = Exercise(
    id = id!!,
    name = name,
    isUnilateral = isUnilateral,
    category = group.toExerciseCategory()
)

fun List<ExerciseEntity>.toDomain(): List<Exercise> = this.map { it.toDomain() }