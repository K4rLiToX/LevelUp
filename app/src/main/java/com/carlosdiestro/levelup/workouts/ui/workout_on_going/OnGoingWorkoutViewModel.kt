package com.carlosdiestro.levelup.workouts.ui.workout_on_going

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.workouts.domain.usecases.AreAllExercisesCompletedUseCase
import com.carlosdiestro.levelup.workouts.domain.usecases.GetLastCompletedWorkoutUseCase
import com.carlosdiestro.levelup.workouts.domain.usecases.InsertCompletedWorkoutUseCase
import com.carlosdiestro.levelup.workouts.domain.usecases.UpdateCompletedWorkoutSetUseCase
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutSetPLO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnGoingWorkoutViewModel @Inject constructor(
    private val getLastCompletedWorkoutUseCase: GetLastCompletedWorkoutUseCase,
    private val updateCompletedWorkoutSetUseCase: UpdateCompletedWorkoutSetUseCase,
    private val areAllExercisesCompletedUseCase: AreAllExercisesCompletedUseCase,
    private val insertCompletedWorkoutUseCase: InsertCompletedWorkoutUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val UIStateKey = "ui_state_key"
    private val ExercisesKey = "exercises_key"

    private var _state: MutableStateFlow<OnGoingWorkoutState> =
        MutableStateFlow(savedStateHandle[UIStateKey] ?: OnGoingWorkoutState())
    val state = _state.asStateFlow()

    private val workoutId: Int = savedStateHandle.get<Int>("workoutId")!!
    private val workoutName: String = savedStateHandle.get<String>("workoutName")!!

    private var exercises: MutableList<CompletedWorkoutExercisePLO> =
        savedStateHandle[ExercisesKey] ?: mutableListOf()

    private val _eventChannel: Channel<OnGoingWorkoutResponse> = Channel()
    val eventChannel = _eventChannel.receiveAsFlow()

    init {
        fetchWorkout()
    }

    fun onEvent(event: OnGoingWorkoutEvent) {
        when (event) {
            is OnGoingWorkoutEvent.UpdateCompletedWorkoutSet -> updateSet(
                event.set,
                event.exerciseOrder
            )
            OnGoingWorkoutEvent.FinishWorkout -> finishWorkout()
        }
    }

    private fun fetchWorkout() {
        launchAndCollect(getLastCompletedWorkoutUseCase(workoutId)) { response ->
            _state.update {
                exercises = response.toMutableList()
                it.copy(
                    workoutName = workoutName,
                    completedExercises = response,
                    currentExercise = response[0]
                )
            }
        }
    }

    private fun updateSet(newSet: CompletedWorkoutSetPLO, exerciseOrder: Int) {
        launchAndCollect(
            updateCompletedWorkoutSetUseCase(
                newSet,
                exerciseOrder,
                exercises
            )
        ) { response ->
            _state.update {
                exercises = response.toMutableList()
                val newState = it.copy(
                    completedExercises = response,
                    currentExercise = exercises[exerciseOrder - 1]
                )
                savedStateHandle[UIStateKey] = newState
                savedStateHandle[ExercisesKey] = exercises
                newState
            }
        }
    }

    private fun finishWorkout() {
        viewModelScope.launch {
            areAllExercisesCompletedUseCase(exercises.toList()).collect { response ->
                if (response) {
                    _eventChannel.send(OnGoingWorkoutResponse.ShowWarningDialog)
                } else {
                    insertCompletedWorkoutUseCase(exercises.toList())
                    _eventChannel.send(OnGoingWorkoutResponse.NavigateBack)
                }
            }
        }
    }
}