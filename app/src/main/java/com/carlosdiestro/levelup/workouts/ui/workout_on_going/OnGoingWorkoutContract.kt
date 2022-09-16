package com.carlosdiestro.levelup.workouts.ui.workout_on_going

import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutSetPLO

data class OnGoingWorkoutState(
    val workoutName: String = "",
    val completedExercises: List<CompletedWorkoutExercisePLO> = emptyList(),
    val currentExercise: CompletedWorkoutExercisePLO? = null
)

sealed class OnGoingWorkoutEvent {
    class UpdateCompletedWorkoutSet(val set: CompletedWorkoutSetPLO, val exerciseOrder: Int) :
        OnGoingWorkoutEvent()

    object FinishWorkout : OnGoingWorkoutEvent()
}

sealed class OnGoingWorkoutResponse {
    object ShowWarningDialog : OnGoingWorkoutResponse()
    object NavigateBack : OnGoingWorkoutResponse()
}