package com.carlosdiestro.levelup.workouts.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.exercise_library.domain.usecases.BlankStringValidatorUseCase
import com.carlosdiestro.levelup.exercise_library.ui.models.ExercisePLO
import com.carlosdiestro.levelup.workouts.domain.usecases.*
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO
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
    private val addNewWorkoutUseCase: AddNewWorkoutUseCase
) : ViewModel() {

    private var exerciseList: MutableList<WorkoutExercisePLO> = mutableListOf()

    private var _state: MutableStateFlow<NewWorkoutState> = MutableStateFlow(NewWorkoutState())
    val state = _state.asStateFlow()

    private val channel: Channel<NewWorkoutEventResponse> = Channel()
    val eventChannel = channel.receiveAsFlow()

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
            is NewWorkoutEvent.AddNewWorkout -> submitNewWorkout(event.name)
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
                    exerciseList = response
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
                    exerciseList = response
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
                    exerciseList = response
                )
            }
        }
    }

    private fun submitNewWorkout(name: String) {
        viewModelScope.launch {
            val isValidName = blankStringValidatorUseCase(name)
            val isExerciseListValid = validateExercisesToAddUseCase(exerciseList)

            if (!isValidName.isSuccessful) {
                _state.update { it.copy(workoutNameError = isValidName.errorMessage) }
                return@launch
            }
            if (!isExerciseListValid.isSuccessful) {
                channel.send(NewWorkoutEventResponse.ShowWarningDialog(isExerciseListValid.errorMessage))
                return@launch
            }

            addNewWorkoutUseCase(name, exerciseList)
            channel.send(NewWorkoutEventResponse.PopBackStack)
        }
    }
}