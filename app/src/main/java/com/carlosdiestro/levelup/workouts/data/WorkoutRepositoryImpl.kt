package com.carlosdiestro.levelup.workouts.data

import com.carlosdiestro.levelup.core.data.IoDispatcher
import com.carlosdiestro.levelup.workouts.domain.models.*
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutExerciseRepository
import com.carlosdiestro.levelup.workouts.domain.repositories.WorkoutRepository
import com.carlosdiestro.levelup.workouts.framework.WorkoutDAO
import com.carlosdiestro.levelup.workouts.framework.WorkoutEntity
import com.carlosdiestro.levelup.workouts.framework.middle_tables.ExerciseWithSets
import com.carlosdiestro.levelup.workouts.framework.middle_tables.WorkoutWithExercises
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val dao: WorkoutDAO,
    private val workoutExerciseRepository: WorkoutExerciseRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : WorkoutRepository {

    override fun getAll(): Flow<List<Workout>> =
        dao.getAll().map { it?.toDomain() ?: emptyList() }.flowOn(ioDispatcher)

    override suspend fun insert(workout: Workout) = withContext(ioDispatcher) {
        val workoutId = dao.insert(workout.toEntity()).toInt()
        workoutExerciseRepository.insert(workoutId, workout.exercises)
    }

    override suspend fun update(workout: Workout) = withContext(ioDispatcher) {
        dao.update(workout.toEntity())
        workoutExerciseRepository.update(workout.exercises)
    }

    override suspend fun delete(workout: Workout) = withContext(ioDispatcher) {
        dao.delete(workout.toEntity())
    }
}

fun WorkoutWithExercises.toDomain(): Workout = Workout(
    id = this.workout!!.id!!,
    name = this.workout.name,
    exercises = this.exercises.toDomain()
)

@JvmName("toDomainWorkoutWithExercises")
fun List<WorkoutWithExercises>.toDomain(): List<Workout> = this.map { it.toDomain() }

fun ExerciseWithSets.toDomain(): WorkoutExercise = WorkoutExercise(
    id = this.exercise!!.id!!,
    workoutId = this.exercise.workoutId,
    name = this.exercise.name,
    isUnilateral = this.exercise.isUnilateral,
    exerciseOrder = this.exercise.orderPosition,
    sets = this.sets.toDomain()
)

@JvmName("toDomainExerciseWithSets")
fun List<ExerciseWithSets>.toDomain(): List<WorkoutExercise> = this.map { it.toDomain() }

fun Workout.toEntity(): WorkoutEntity = WorkoutEntity(
    id = if(id == -1) null else id,
    name = name
)