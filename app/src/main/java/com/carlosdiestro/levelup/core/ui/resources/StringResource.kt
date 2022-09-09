package com.carlosdiestro.levelup.core.ui.resources

import android.content.Context
import androidx.annotation.StringRes
import com.carlosdiestro.levelup.R

sealed class StringResource(@StringRes val resId: Int) {
    object Workout : StringResource(R.string.section_workout)
    object ExerciseLibrary : StringResource(R.string.section_exercise_library)
    object BodyWeight : StringResource(R.string.section_body_weight)
    object Exercises : StringResource(R.string.title_exercise)
    object Progress : StringResource(R.string.title_progress)
    object NewWorkout : StringResource(R.string.title_new_workout)
    object EditWorkout : StringResource(R.string.title_edit_workout)
    object Set : StringResource(R.string.title_set)
    object DropSet : StringResource(R.string.title_set_drop)
    object NewWeight : StringResource(R.string.title_new_weight)
    object All : StringResource(R.string.exercise_category_all)
    object Push : StringResource(R.string.exercise_category_push)
    object Pull : StringResource(R.string.exercise_category_pull)
    object Legs : StringResource(R.string.exercise_category_legs)
    object Core : StringResource(R.string.exercise_category_core)
    object Today : StringResource(R.string.time_today)
    object Yesterday : StringResource(R.string.time_yesterday)
    object AddExercise : StringResource(R.string.action_add_exercise)
    object Add : StringResource(R.string.action_add)
    object NoteDown : StringResource(R.string.action_note_down)
    object Cancel : StringResource(R.string.action_cancel)
    object Save : StringResource(R.string.action_save)
    object Accept : StringResource(R.string.action_accept)
    object Ok : StringResource(R.string.action_ok)
    object Close : StringResource(R.string.action_close)
    object Title : StringResource(R.string.form_input_text_title)
    object Range : StringResource(R.string.form_input_text_range)
    object Weight : StringResource(R.string.form_input_text_weight)
    object Reps : StringResource(R.string.form_input_text_repetitions)
    object Partials : StringResource(R.string.form_input_text_repetitions_partials)
    object Kg : StringResource(R.string.placeholder_weight)
    object BlankInput : StringResource(R.string.error_blank_input)
    object ZeroValue : StringResource(R.string.error_weight_zero_value)
    object WeightExists : StringResource(R.string.error_weight_exists)
    object RepRange : StringResource(R.string.placeholder_range)
    object NoExercisesAdded : StringResource(R.string.error_no_exercises_added)
    object NoSetsAdded : StringResource(R.string.error_no_sets_added_to_exercise)
    object Warning : StringResource(R.string.title_warning)
    object ExerciseWithNumber : StringResource(R.string.placeholder_exercise_with_number)
    object OneVariablePlaceHolder : StringResource(R.string.placeholder_one_variable)
}

fun StringResource.toText(context: Context): String = context.getString(this.resId)
fun StringResource.toText(context: Context, vararg args: Any): String =
    context.getString(this.resId, *args)
