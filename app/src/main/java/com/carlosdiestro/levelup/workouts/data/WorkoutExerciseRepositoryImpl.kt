package com.carlosdiestro.levelup.workouts.data

import com.carlosdiestro.levelup.core.data.IoDispatcher
import com.carlosdiestro.levelup.workouts.domain.models.CompletedWorkoutExercise
import com.carlosdiestro.levelup.workouts.domain.models.WorkoutExercise
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutExerciseRepository
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutSetRepository
import com.carlosdiestro.levelup.workouts.framework.CompletedWorkoutExerciseDao
import com.carlosdiestro.levelup.workouts.framework.WorkoutExerciseDao
import com.carlosdiestro.levelup.workouts.framework.middle_tables.ExerciseWithSets
import com.carlosdiestro.levelup.workouts.mappers.toDomain
import com.carlosdiestro.levelup.workouts.mappers.toEntity
import com.carlosdiestro.levelup.workouts.mappers.toEntityInsert
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WorkoutExerciseRepositoryImpl @Inject constructor(
    private val workoutExerciseDao: WorkoutExerciseDao,
    private val completedWorkoutExerciseDAO: CompletedWorkoutExerciseDao,
    private val workoutSetRepository: WorkoutSetRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : WorkoutExerciseRepository {

    override suspend fun getLastCompletedExercisesWithSets(workoutId: Int): List<CompletedWorkoutExercise> =
        withContext(dispatcher) {
            completedWorkoutExerciseDAO
                .getCompletedExercisesWithSets(workoutId)
                ?.groupBy { it.exercise?.date }
                ?.map { (_, value) -> value }
                ?.getOrNull(0)
                ?.map { it.toDomain() }
                ?: emptyList()
        }

    override fun getCompletedExercisesWithSets(workoutId: Int): Flow<List<Pair<Int, List<CompletedWorkoutExercise>>>> =
        completedWorkoutExerciseDAO
            .getCompletedExercisesWithSetsFlow(workoutId)
            .map { list ->
                list
                    ?.groupBy { it.exercise?.exerciseOrder }
                    ?.map { (key, value) -> Pair(key, value.sortedBy { it.exercise?.date }) }
                    ?.map { p -> Pair(p.first!!, p.second.toDomain()) }
                    ?: emptyList()
            }.flowOn(dispatcher)

    override suspend fun insert(completedWorkoutExercise: CompletedWorkoutExercise) =
        withContext(dispatcher) {
            val exerciseId =
                completedWorkoutExerciseDAO.insert(completedWorkoutExercise.toEntity()).toInt()
            val newExercise =
                completedWorkoutExercise.copy(completedSets = completedWorkoutExercise.completedSets.map {
                    it.copy(exerciseId = exerciseId)
                })
            workoutSetRepository.insertCompletedSets(newExercise.completedSets)
        }

    override suspend fun insert(list: List<CompletedWorkoutExercise>) = withContext(dispatcher) {
        val exercisesIds =
            completedWorkoutExerciseDAO.insert(list.toEntity()).map { it.toInt() }
        val exercisesWithIds = list.mapIndexed { i, e -> Pair(exercisesIds[i], e) }
        val newExerciseList = exercisesWithIds.map { (exerciseId, exercise) ->
            exercise.copy(completedSets = exercise.completedSets.map { it.copy(exerciseId = exerciseId) })
        }
        workoutSetRepository.insertCompletedSets(newExerciseList.flatMap { it.completedSets })
    }

    override fun getWorkoutExercisesWithSets(workoutId: Int): Flow<List<ExerciseWithSets>> =
        flow<List<ExerciseWithSets>> {
            workoutExerciseDao.getWorkoutExercisesWithSets(workoutId).map { it.toDomain() }
        }.flowOn(dispatcher)

    override suspend fun deleteById(id: Int) = withContext(dispatcher) {
        workoutExerciseDao.deleteById(id)
    }

    override suspend fun insert(workoutId: Int, list: List<WorkoutExercise>) =
        withContext(dispatcher) {
            val exercisesIds = workoutExerciseDao.insert(list.toEntityInsert(workoutId))
            val exercisesWithIds = list.mapIndexed { i, e -> Pair(exercisesIds[i].toInt(), e) }
            val newExerciseList = exercisesWithIds.map { (exerciseId, exercise) ->
                exercise.copy(sets = exercise.sets.map { it.copy(exerciseId = exerciseId) })
            }
            workoutSetRepository.insert(newExerciseList.flatMap { it.sets })
        }

    override suspend fun insert(workoutId: Int, workoutExercise: WorkoutExercise) =
        withContext(dispatcher) {
            val exerciseId = workoutExerciseDao.insert(workoutExercise.toEntityInsert(workoutId))
            val newExercise =
                workoutExercise.copy(sets = workoutExercise.sets.map { it.copy(exerciseId = exerciseId.toInt()) })
            workoutSetRepository.insert(newExercise.sets)
        }

    override suspend fun update(workoutId: Int, newExercises: List<WorkoutExercise>) =
        withContext(dispatcher) {
            val formerExercises = workoutExerciseDao.getWorkoutExercises(workoutId).first()
            val brandNewExercises = newExercises.filter { it.id == -1 }
            val existingExercises = (newExercises subtract brandNewExercises.toSet()).toList()

            insert(workoutId, brandNewExercises)
            formerExercises.forEach { fe ->
                val exists = existingExercises.find { it.id == fe.id } != null
                if (!exists) workoutExerciseDao.delete(fe)
                else {
                    workoutExerciseDao.update(existingExercises.toEntity())
                    workoutSetRepository.update(existingExercises.flatMap { it.sets })
                }
            }
        }

    override suspend fun update(workoutExercise: WorkoutExercise) = withContext(dispatcher) {
        workoutExerciseDao.update(workoutExercise.toEntity())
        workoutSetRepository.update(workoutExercise.sets)
    }

    override suspend fun delete(list: List<WorkoutExercise>) = withContext(dispatcher) {
        workoutExerciseDao.delete(list.toEntity())
    }

    override suspend fun delete(workoutExercise: WorkoutExercise) = withContext(dispatcher) {
        workoutExerciseDao.delete(workoutExercise.toEntity())
    }
}

