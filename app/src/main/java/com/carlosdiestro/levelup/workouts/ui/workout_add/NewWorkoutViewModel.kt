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
    private val addNewWorkoutExerciseUseCase: AddNewWorkoutExerciseUseCase,
    private val addSetToWorkoutExerciseUseCase: AddSetToWorkoutExerciseUseCase,
    private val removeSetFromExerciseUseCase: RemoveSetFromExerciseUseCase,
    private val blankStringValidatorUseCase: BlankStringValidatorUseCase,
    private val validateExercisesToAddUseCase: ValidateExercisesToAddUseCase,
    private val addNewWorkoutUseCase: AddNewWorkoutUseCase,
    private val getWorkoutUseCase: GetWorkoutUseCase,
    private val updateWorkoutUseCase: UpdateWorkoutUseCase,
    private val updateSetFromExerciseUseCase: UpdateSetFromExerciseUseCase,
    private val removeExerciseFromWorkoutUseCase: RemoveExerciseFromWorkoutUseCase,
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
            is NewWorkoutEvent.OnExerciseClicked -> addExerciseToWorkout(event.exercisePLO)
            is NewWorkoutEvent.OnNewSetClicked -> addNewSetToExercise(
                event.newSet,
                event.exercisePosition
            )
            is NewWorkoutEvent.OnSetRemoved -> removeSetFromExercise(
                event.exercise,
                event.set
            )
            is NewWorkoutEvent.AddNewWorkout -> submitOrUpdateWorkout(event.name)
            is NewWorkoutEvent.OnUpdateSetClicked -> updateSetFromExercise(
                event.exercise,
                event.set
            )
            is NewWorkoutEvent.OnRemoveExerciseClicked -> removeExerciseFromWorkout(event.id)
            is NewWorkoutEvent.EnableReplaceMode -> enableReplaceMode(event.id)
            is NewWorkoutEvent.ReplaceExercise -> replaceExercise(event.exercise)
        }
    }

    private fun addExerciseToWorkout(exercisePLO: ExercisePLO) {
        launchAndCollect(
            addNewWorkoutExerciseUseCase(
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

    private fun addNewSetToExercise(newSet: WorkoutSetPLO, position: Int) {
        launchAndCollect(
            addSetToWorkoutExerciseUseCase(
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

    private fun removeSetFromExercise(exercise: WorkoutExercisePLO, set: WorkoutSetPLO) {
        launchAndCollect(
            removeSetFromExerciseUseCase(
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

    private fun updateSetFromExercise(exercise: WorkoutExercisePLO, set: WorkoutSetPLO) {
        launchAndCollect(
            updateSetFromExerciseUseCase(
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

    private fun submitOrUpdateWorkout(name: String) {
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
            } else addNewWorkoutUseCase(name, exerciseList)
            channel.send(NewWorkoutEventResponse.PopBackStack)
        }
    }

    private fun removeExerciseFromWorkout(id: Int) {
        launchAndCollect(
            removeExerciseFromWorkoutUseCase(id, exerciseList.toList())
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