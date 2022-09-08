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

    override suspend fun insert(workoutId: Int, list: List<WorkoutExercise>) =
        withContext(ioDispatcher) {
            val exercisesIds = dao.insert(list.toEntityInsert(workoutId))
            val exercisesWithIds = list.mapIndexed { i, e -> Pair(exercisesIds[i].toInt(), e) }
            val newExerciseList = exercisesWithIds.map { (workoutId, exercise) ->
                exercise.copy(sets = exercise.sets.map { it.copy(exerciseId = workoutId) })
            }
            workoutSetRepository.insert(newExerciseList.flatMap { it.sets })
        }

    override suspend fun insert(workoutId: Int, workoutExercise: WorkoutExercise) =
        withContext(ioDispatcher) {
            val exerciseId = dao.insert(workoutExercise.toEntityInsert(workoutId))
            val newExercise =
                workoutExercise.copy(sets = workoutExercise.sets.map { it.copy(exerciseId = exerciseId.toInt()) })
            workoutSetRepository.insert(newExercise.sets)
        }

    override suspend fun update(list: List<WorkoutExercise>) = withContext(ioDispatcher) {
        val newExercises = list.filter { it.id == -1}
        val formerExercises = (list subtract newExercises.toSet()).toList()

        dao.update(formerExercises.toEntity())
        workoutSetRepository.update(formerExercises.flatMap { it.sets })
        insert(formerExercises[0].workoutId, newExercises)
    }

    override suspend fun update(workoutExercise: WorkoutExercise) = withContext(ioDispatcher) {
        dao.update(workoutExercise.toEntity())
        workoutSetRepository.update(workoutExercise.sets)
    }

    override suspend fun delete(list: List<WorkoutExercise>) = withContext(ioDispatcher) {
        dao.delete(list.toEntity())
    }

    override suspend fun delete(workoutExercise: WorkoutExercise) = withContext(ioDispatcher) {
        dao.delete(workoutExercise.toEntity())
    }
}

fun WorkoutExercise.toEntityInsert(realWorkoutId: Int): WorkoutExerciseEntity =
    WorkoutExerciseEntity(
        workoutId = realWorkoutId,
        name = name,
        isUnilateral = isUnilateral,
        orderPosition = exerciseOrder
    )

fun WorkoutExercise.toEntity(): WorkoutExerciseEntity = WorkoutExerciseEntity(
    id = id,
    workoutId = workoutId,
    name = name,
    isUnilateral = isUnilateral,
    orderPosition = exerciseOrder
)

fun List<WorkoutExercise>.toEntityInsert(workoutId: Int): List<WorkoutExerciseEntity> =
    this.map { it.toEntityInsert(workoutId) }

fun List<WorkoutExercise>.toEntity(): List<WorkoutExerciseEntity> = this.map { it.toEntity() }