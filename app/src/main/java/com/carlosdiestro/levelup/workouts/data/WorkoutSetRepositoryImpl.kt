package com.carlosdiestro.levelup.workouts.data

import com.carlosdiestro.levelup.core.data.IoDispatcher
import com.carlosdiestro.levelup.core.ui.managers.TimeManager
import com.carlosdiestro.levelup.workouts.domain.models.*
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutSetRepository
import com.carlosdiestro.levelup.workouts.framework.CompletedWorkoutSetDao
import com.carlosdiestro.levelup.workouts.framework.CompletedWorkoutSetEntity
import com.carlosdiestro.levelup.workouts.framework.WorkoutSetDao
import com.carlosdiestro.levelup.workouts.framework.WorkoutSetEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WorkoutSetRepositoryImpl @Inject constructor(
    private val workoutSetDao: WorkoutSetDao,
    private val completedWorkoutSetDAO: CompletedWorkoutSetDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : WorkoutSetRepository {

    override fun getExerciseSets(exerciseId: Int): Flow<List<WorkoutSet>> =
        flow<List<WorkoutSet>> {
            workoutSetDao.getExerciseSets(exerciseId).map { it.toDomain() }
        }.flowOn(dispatcher)

    override suspend fun insert(list: List<WorkoutSet>) = withContext(dispatcher) {
        workoutSetDao.insert(list.toEntity())
    }

    override suspend fun insert(workoutSet: WorkoutSet) = withContext(dispatcher) {
        workoutSetDao.insert(workoutSet.toEntity())
    }

    override suspend fun insertCompletedSets(list: List<CompletedWorkoutSet>) =
        withContext(dispatcher) {
            completedWorkoutSetDAO.insert(list.toEntity())
        }

    override suspend fun update(list: List<WorkoutSet>) = withContext(dispatcher) {
        val exerciseAndSets = list.toEntity().groupBy { it.exerciseId }
        for ((exerciseId, newSets) in exerciseAndSets) {
            // Get the previous sets for the exercise
            val formerSets = workoutSetDao.getExerciseSets(exerciseId).first()

            if (newSets == formerSets) continue

            val deletedSets = mutableListOf<WorkoutSetEntity>()
            (formerSets zip newSets).forEach { (oldSet, newSet) ->
                val isSameId = isSameId(oldSet.id, newSet.id)
                val isSameRepRange = isSameRepRange(oldSet.repRange, newSet.repRange)
                val isInFormerList = isInFormerList(newSet.id, formerSets)
                val isInNewList = isInNewList(oldSet.id, newSets)

                if (isSameId) {
                    if (!isSameRepRange) workoutSetDao.update(newSet)
                } else {
                    if (!isInNewList) {
                        deletedSets.add(oldSet)
                        workoutSetDao.delete(oldSet)
                    }
                    if (!isInFormerList) workoutSetDao.insert(newSet)
                    else workoutSetDao.update(newSet)
                }
            }

            if (formerSets.size == newSets.size) continue

            if (formerSets.size < newSets.size) {
                workoutSetDao.insert(newSets.takeLast(newSets.size - formerSets.size))
            }
            if (formerSets.size > newSets.size) {
                val formerSetsAfterFirstDeletion =
                    (formerSets subtract deletedSets.toSet()).toList()
                formerSetsAfterFirstDeletion.forEach { fs ->
                    val exists = newSets.find { it.id == fs.id } != null
                    if (!exists) workoutSetDao.delete(fs)
                }
            }
        }
    }

    override suspend fun update(workoutSet: WorkoutSet) = withContext(dispatcher) {
        workoutSetDao.update(workoutSet.toEntity())
    }

    override suspend fun delete(list: List<WorkoutSet>) = withContext(dispatcher) {
        workoutSetDao.delete(list.toEntity())
    }

    override suspend fun delete(workoutSet: WorkoutSet) = withContext(dispatcher) {
        workoutSetDao.delete(workoutSet.toEntity())
    }

    private fun isSameId(oldId: Int?, newId: Int?): Boolean = oldId == newId

    private fun isSameRepRange(oldRepRange: String, newRepRange: String): Boolean =
        oldRepRange == newRepRange

    private fun isInFormerList(newId: Int?, formerList: List<WorkoutSetEntity>): Boolean =
        formerList.find { it.id == newId } != null

    private fun isInNewList(oldId: Int?, newList: List<WorkoutSetEntity>): Boolean =
        newList.find { it.id == oldId } != null
}

fun WorkoutSetEntity.toDomain(): WorkoutSet = WorkoutSet(
    id = id!!,
    exerciseId = exerciseId,
    setOrder = setOrder,
    repRange = repRange.toRepRange()
)

fun List<WorkoutSetEntity>.toDomain(): List<WorkoutSet> = this.map { it.toDomain() }

fun WorkoutSet.toEntity(): WorkoutSetEntity = WorkoutSetEntity(
    id = if (id == -1) null else id,
    exerciseId = exerciseId,
    setOrder = setOrder,
    repRange = repRange.toStringValue()
)

@JvmName("toEntityWorkoutSet")
fun List<WorkoutSet>.toEntity(): List<WorkoutSetEntity> = this.map { it.toEntity() }

fun CompletedWorkoutSet.toEntity(): CompletedWorkoutSetEntity = CompletedWorkoutSetEntity(
    exerciseId = exerciseId,
    setOrder = setOrder,
    date = TimeManager.toMillis(date),
    repetitions = repetitions.toStringValue(),
    weights = weights.toStringValue(),
    status = status.toInt()
)

@JvmName("toEntityCompletedWorkoutSet")
fun List<CompletedWorkoutSet>.toEntity(): List<CompletedWorkoutSetEntity> =
    this.map { it.toEntity() }