package com.carlosdiestro.levelup.workouts.ui.workout_add

import com.carlosdiestro.levelup.core.ui.resources.StringResource
import com.carlosdiestro.levelup.exercise_library.ui.models.ExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutSetPLO

sealed interface NewWorkoutEvent {
    class ClickExercise(val exercisePLO: ExercisePLO) : NewWorkoutEvent
    class InsertSet(val newSet: WorkoutSetPLO, val exercisePosition: Int) : NewWorkoutEvent
    class RemoveSet(val exercise: WorkoutExercisePLO, val set: WorkoutSetPLO) : NewWorkoutEvent
    class InsertWorkout(val name: String) : NewWorkoutEvent
    class UpdateSet(val exercise: WorkoutExercisePLO, val set: WorkoutSetPLO) :
        NewWorkoutEvent

    class DeleteExercise(val id: Int) : NewWorkoutEvent
    class EnableReplaceMode(val id: Int) : NewWorkoutEvent
    class ReplaceExercise(val exercise: ExercisePLO) : NewWorkoutEvent
}

sealed interface NewWorkoutEventResponse {
    class ShowWarningDialog(val message: StringResource?) : NewWorkoutEventResponse
    object PopBackStack : NewWorkoutEventResponse
}

data class NewWorkoutState(
    val noData: Boolean = true,
    val workoutNameError: StringResource? = null,
    val workoutName: String = "",
    val toolbarTitle: StringResource = StringResource.NewWorkout,
    val exercises: List<WorkoutExercisePLO> = emptyList()
)