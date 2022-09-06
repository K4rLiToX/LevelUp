package com.carlosdiestro.levelup.workouts.ui

import com.carlosdiestro.levelup.exercise_library.ui.models.ExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutSetPLO

sealed class NewWorkoutEvent {
    class OnExerciseClicked(val exercisePLO: ExercisePLO) : NewWorkoutEvent()
    class OnNewSetClicked(val newSet: WorkoutSetPLO, val exercisePosition: Int) : NewWorkoutEvent()
    class OnSetRemoved(val exercise: WorkoutExercisePLO, val set: WorkoutSetPLO) : NewWorkoutEvent()
}

data class NewWorkoutState(
    val noData: Boolean = true,
    val exerciseList: List<WorkoutExercisePLO> = emptyList()
)