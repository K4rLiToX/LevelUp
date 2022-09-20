package com.carlosdiestro.levelup.workouts.ui.workout_add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.core.ui.resources.StringResource
import com.carlosdiestro.levelup.exercise_library.domain.usecases.BlankStringValidatorUseCase
import com.carlosdiestro.levelup.exercise_library.ui.models.ExercisePLO
import com.carlosdiestro.levelup.workouts.domain.usecases.*
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutPLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutSetPLO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewWorkoutViewModel @Inject constructor(
    private val insertWorkoutExerciseUseCase: InsertWorkoutExerciseUseCase,
    private val insertWorkoutSetInWorkoutExerciseUseCase: InsertWorkoutSetInWorkoutExerciseUseCase,
    private val deleteWorkoutSetFromWorkoutExerciseUseCase: DeleteWorkoutSetFromWorkoutExerciseUseCase,
    private val blankStringValidatorUseCase: BlankStringValidatorUseCase,
    private val validateExercisesToAddUseCase: ValidateWorkoutExerciseListUseCase,
    private val insertWorkoutUseCase: InsertWorkoutUseCase,
    private val getWorkoutUseCase: GetWorkoutUseCase,
    private val updateWorkoutUseCase: UpdateWorkoutUseCase,
    private val updateWorkoutSetFromWorkoutExerciseUseCase: UpdateWorkoutSetFromWorkoutExerciseUseCase,
    private val deleteWorkoutExerciseFromWorkoutUseCase: DeleteWorkoutExerciseFromWorkoutUseCase,
    private val replaceWorkoutExerciseUseCase: ReplaceWorkoutExerciseUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var exerciseList: MutableList<WorkoutExercisePLO> = mutableListOf()

    private var _state: MutableStateFlow<NewWorkoutState> = MutableStateFlow(NewWorkoutState())
    val state = _state.asStateFlow()

    private val channel: Channel<NewWorkoutEventResponse> = Channel()
    val eventChannel = channel.receiveAsFlow()

    private val workoutId: Int = savedStateHandle.get<Int>("workoutId")!!
    private var workoutToUpdate: WorkoutPLO? = null

    private var exerciseIdToReplace: Int = -1

    init {
        if (workoutId != -1) {
            fetchWorkout()
        }
    }

    fun onEvent(event: NewWorkoutEvent) {
        when (event) {
            is NewWorkoutEvent.ClickExercise -> insertExercise(event.exercisePLO)
            is NewWorkoutEvent.InsertSet -> insertSet(
                event.newSet,
                event.exercisePosition
            )
            is NewWorkoutEvent.RemoveSet -> deleteSet(
                event.exercise,
                event.set
            )
            is NewWorkoutEvent.InsertWorkout -> upsertWorkout(event.name)
            is NewWorkoutEvent.UpdateSet -> updateSet(
                event.exercise,
                event.set
            )
            is NewWorkoutEvent.DeleteExercise -> deleteExercise(event.id)
            is NewWorkoutEvent.EnableReplaceMode -> enableReplaceMode(event.id)
            is NewWorkoutEvent.ReplaceExercise -> replaceExercise(event.exercise)
        }
    }

    private fun insertExercise(exercisePLO: ExercisePLO) {
        launchAndCollect(
            insertWorkoutExerciseUseCase(
                exercisePLO,
                exerciseList.toList()
            )
        ) { response ->
            exerciseList = response.toMutableList()
            _state.update {
                it.copy(
                    noData = response.isEmpty(),
                    exercises = response
                )
            }
        }
    }

    private fun insertSet(newSet: WorkoutSetPLO, position: Int) {
        launchAndCollect(
            insertWorkoutSetInWorkoutExerciseUseCase(
                newSet,
                position,
                exerciseList.toList()
            )
        ) { response ->
            exerciseList = response.toMutableList()
            _state.update {
                it.copy(
                    exercises = response
                )
            }
        }
    }

    private fun deleteSet(exercise: WorkoutExercisePLO, set: WorkoutSetPLO) {
        launchAndCollect(
            deleteWorkoutSetFromWorkoutExerciseUseCase(
                exercise,
                set,
                exerciseList.toList()
            )
        ) { response ->
            exerciseList = response.toMutableList()
            _state.update {
                it.copy(
                    exercises = response
                )
            }
        }
    }

    private fun updateSet(exercise: WorkoutExercisePLO, set: WorkoutSetPLO) {
        launchAndCollect(
            updateWorkoutSetFromWorkoutExerciseUseCase(
                exercise,
                set,
                exerciseList.toList()
            )
        ) { response ->
            exerciseList = response.toMutableList()
            _state.update {
                it.copy(
                    exercises = response
                )
            }
        }
    }

    private fun upsertWorkout(name: String) {
        viewModelScope.launch {
            val isValidName = blankStringValidatorUseCase(name)
            val isExerciseListValid = validateExercisesToAddUseCase(exerciseList)
            val isNameChanged = name != workoutToUpdate?.name

            if (!isValidName.isSuccessful) {
                _state.update { it.copy(workoutNameError = isValidName.errorMessage) }
                return@launch
            } else {
                _state.update { it.copy(workoutNameError = null, workoutName = name) }
            }
            if (!isExerciseListValid.isSuccessful) {
                channel.send(NewWorkoutEventResponse.ShowWarningDialog(isExerciseListValid.errorMessage))
                return@launch
            }

            if (workoutId != -1) {
                updateWorkoutUseCase(
                    isNameChanged,
                    workoutToUpdate!!.copy(name = name),
                    exerciseList
                )
            } else insertWorkoutUseCase(name, exerciseList)
            channel.send(NewWorkoutEventResponse.PopBackStack)
        }
    }

    private fun deleteExercise(id: Int) {
        launchAndCollect(
            deleteWorkoutExerciseFromWorkoutUseCase(id, exerciseList.toList())
        ) { response ->
            exerciseList = response.toMutableList()
            _state.update {
                it.copy(exercises = response.toMutableList())
            }
        }
    }

    private fun enableReplaceMode(id: Int) {
        exerciseIdToReplace = id
    }

    private fun replaceExercise(exercise: ExercisePLO) {
        launchAndCollect(
            replaceWorkoutExerciseUseCase(
                exerciseIdToReplace,
                exercise,
                exerciseList.toList()
            )
        ) { response ->
            exerciseList = response.toMutableList()
            exerciseIdToReplace = -1
            _state.update {
                it.copy(
                    exercises = response
                )
            }
        }
    }

    fun isReplaceModeEnabled(): Boolean = exerciseIdToReplace != -1

    private fun fetchWorkout() {
        launchAndCollect(getWorkoutUseCase(workoutId)) { (workout, exercises) ->
            exerciseList = exercises.toMutableList()
            workoutToUpdate = workout
            _state.update {
                it.copy(
                    noData = exercises.isEmpty(),
                    exercises = exercises,
                    workoutName = workout.name,
                    toolbarTitle = StringResource.EditWorkout
                )
            }
        }
    }
}