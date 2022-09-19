package com.carlosdiestro.levelup.workouts.ui.workout_on_going

import android.os.Parcelable
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutSetPLO
import kotlinx.parcelize.Parcelize

@Parcelize
data class OnGoingWorkoutState(
    val workoutName: String = "",
    val completedExercises: List<CompletedWorkoutExercisePLO> = emptyList(),
    val currentExercise: CompletedWorkoutExercisePLO? = null
) : Parcelable

sealed interface OnGoingWorkoutEvent {
    class UpdateCompletedWorkoutSet(val set: CompletedWorkoutSetPLO, val exerciseOrder: Int) :
        OnGoingWorkoutEvent

    object FinishWorkout : OnGoingWorkoutEvent
}

sealed interface OnGoingWorkoutResponse {
    object ShowWarningDialog : OnGoingWorkoutResponse
    object NavigateBack : OnGoingWorkoutResponse
}