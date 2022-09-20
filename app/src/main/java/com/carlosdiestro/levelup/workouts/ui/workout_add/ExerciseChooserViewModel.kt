package com.carlosdiestro.levelup.workouts.ui.workout_add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosdiestro.levelup.exercise_library.ui.models.ExercisePLO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseChooserViewModel @Inject constructor() : ViewModel() {

    private val _channelCallback: Channel<ExerciseChooserResponse> = Channel()
    val channelCallback = _channelCallback.receiveAsFlow()

    private var exercise: ExercisePLO? = null

    fun setExercise(value: ExercisePLO) {
        exercise = value
        viewModelScope.launch {
            _channelCallback.send(ExerciseChooserResponse.ExerciseSelected)
        }
    }

    fun getExercise(): ExercisePLO? = exercise
}