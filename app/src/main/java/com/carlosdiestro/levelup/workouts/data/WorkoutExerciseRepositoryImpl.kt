package com.carlosdiestro.levelup.workouts.data

import com.carlosdiestro.levelup.core.data.IoDispatcher
import com.carlosdiestro.levelup.core.ui.managers.TimeManager
import com.carlosdiestro.levelup.workouts.domain.models.*
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutExerciseRepository
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutSetRepository
import com.carlosdiestro.levelup.workouts.framework.*
import com.carlosdiestro.levelup.workouts.framework.middle_tables.CompletedWorkoutExercisesWithSets
import com.carlosdiestro.levelup.workouts.framework.middle_tables.ExerciseWithSets
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WorkoutExerciseRepositoryImpl @Inject constructor(
    private val dao: WorkoutExerciseDAO,
    private val completedWorkoutExerciseDAO: CompletedWorkoutExerciseDAO,
    private val workoutSetRepository: WorkoutSetRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : WorkoutExerciseRepository {

    override suspend fun getLastCompletedExercisesWithSets(workoutId: Int): List<CompletedWorkoutExercise> =
        withContext(ioDispatcher) {
            completedWorkoutExerciseDAO
                .getCompletedExercisesWithSets(workoutId)
                ?.groupBy { it.exercise?.date }
                ?.map { (_, value) -> value }
                ?.getOrNull(0)
                ?.map { it.toDomain() }
                ?: emptyList()
        }

    override fun getCompletedExercisesWithSets(workoutId: Int): Flow<List<CompletedWorkoutExercise>> =
        flow<List<CompletedWorkoutExercise>> {
            completedWorkoutExerciseDAO.getCompletedExercisesWithSetsFlow(workoutId)
                .map { it?.toDomain() ?: emptyList() }
        }.flowOn(ioDispatcher)

    override suspend fun insert(completedWorkoutExercise: CompletedWorkoutExercise) =
        withContext(ioDispatcher) {
            val exerciseId =
                completedWorkoutExerciseDAO.insert(completedWorkoutExercise.toEntity()).toInt()
            val newExercise =
                completedWorkoutExercise.copy(completedSets = completedWorkoutExercise.completedSets.map {
                    it.copy(exerciseId = exerciseId)
                })
            workoutSetRepository.insertCompletedSets(newExercise.completedSets)
        }

    override suspend fun insert(list: List<CompletedWorkoutExercise>) = withContext(ioDispatcher) {
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
            dao.getWorkoutExercisesWithSets(workoutId).map { it.toDomain() }
        }.flowOn(ioDispatcher)

    override suspend fun deleteById(id: Int) = withContext(ioDispatcher) {
        dao.deleteById(id)
    }

    override suspend fun insert(workoutId: Int, list: List<WorkoutExercise>) =
        withContext(ioDispatcher) {
            val exercisesIds = dao.insert(list.toEntityInsert(workoutId))
            val exercisesWithIds = list.mapIndexed { i, e -> Pair(exercisesIds[i].toInt(), e) }
            val newExerciseList = exercisesWithIds.map { (exerciseId, exercise) ->
                exercise.copy(sets = exercise.sets.map { it.copy(exerciseId = exerciseId) })
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

    override suspend fun update(workoutId: Int, newExercises: List<WorkoutExercise>) =
        withContext(ioDispatcher) {
            val formerExercises = dao.getWorkoutExercises(workoutId).first()
            val brandNewExercises = newExercises.filter { it.id == -1 }
            val existingExercises = (newExercises subtract brandNewExercises.toSet()).toList()

            insert(workoutId, brandNewExercises)
            formerExercises.forEach { fe ->
                val exists = existingExercises.find { it.id == fe.id } != null
                if (!exists) dao.delete(fe)
                else {
                    dao.update(existingExercises.toEntity())
                    workoutSetRepository.update(existingExercises.flatMap { it.sets })
                }
            }
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

fun CompletedWorkoutExercisesWithSets.toDomain(): CompletedWorkoutExercise =
    CompletedWorkoutExercise(
        id = this.exercise!!.id!!,
        workoutId = this.exercise.workoutId,
        date = TimeManager.toText(this.exercise.date),
        exerciseOrder = this.exercise.exerciseOrder,
        completedSets = this.sets.toDomain()
    )

@JvmName("toDomainCompletedWorkoutExercisesWithSets")
fun List<CompletedWorkoutExercisesWithSets>.toDomain(): List<CompletedWorkoutExercise> =
    this.map { it.toDomain() }

fun CompletedWorkoutSetEntity.toDomain(): CompletedWorkoutSet = CompletedWorkoutSet(
    id = id!!,
    exerciseId = exerciseId,
    setOrder = setOrder,
    date = TimeManager.toText(date),
    repetitions = repetitions.toRepetition(),
    weights = weights.toWeight(),
    status = status.toSetStatus()
)

@JvmName("toDomainCompletedWorkoutSet")
fun List<CompletedWorkoutSetEntity>.toDomain(): List<CompletedWorkoutSet> =
    this.map { it.toDomain() }

fun CompletedWorkoutExercise.toEntity(): CompletedWorkoutExerciseEntity =
    CompletedWorkoutExerciseEntity(
        workoutId = workoutId,
        exerciseOrder = exerciseOrder,
        date = TimeManager.toMillis(date)
    )

@JvmName("toEntityCompletedWorkoutExercise")
fun List<CompletedWorkoutExercise>.toEntity(): List<CompletedWorkoutExerciseEntity> =
    this.map { it.toEntity() }