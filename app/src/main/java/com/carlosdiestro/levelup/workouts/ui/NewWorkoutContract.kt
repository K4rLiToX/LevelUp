package com.carlosdiestro.levelup.workouts.ui

import com.carlosdiestro.levelup.core.ui.resources.StringResource
import com.carlosdiestro.levelup.exercise_library.ui.models.ExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutSetPLO

sealed class NewWorkoutEvent {
    class OnExerciseClicked(val exercisePLO: ExercisePLO) : NewWorkoutEvent()
    class OnNewSetClicked(val newSet: WorkoutSetPLO, val exercisePosition: Int) : NewWorkoutEvent()
    class OnSetRemoved(val exercise: WorkoutExercisePLO, val set: WorkoutSetPLO) : NewWorkoutEvent()
    class AddNewWorkout(val name: String) : NewWorkoutEvent()
    class OnUpdateSetClicked(val exercise: WorkoutExercisePLO, val set: WorkoutSetPLO) :
        NewWorkoutEvent()

    class OnRemoveExerciseClicked(val id: Int) : NewWorkoutEvent()
    class EnableReplaceMode(val id: Int) : NewWorkoutEvent()
    class ReplaceExercise(val exercise: ExercisePLO) : NewWorkoutEvent()
}

sealed class NewWorkoutEventResponse {
    class ShowWarningDialog(val message: StringResource?) : NewWorkoutEventResponse()
    object PopBackStack : NewWorkoutEventResponse()
}

data class NewWorkoutState(
    val noData: Boolean = true,
    val workoutNameError: StringResource? = null,
    val workoutName: String = "",
    val toolbarTitle: StringResource = StringResource.NewWorkout,
    val exerciseList: List<WorkoutExercisePLO> = emptyList()
)