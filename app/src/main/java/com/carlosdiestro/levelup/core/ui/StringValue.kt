package com.carlosdiestro.levelup.core.ui

import android.content.Context
import com.carlosdiestro.levelup.R

sealed class StringValue(val resId: Int) {
    sealed class Section(resId: Int) : StringValue(resId) {
        object Workout : Section(R.string.section_workout)
        object ExerciseAndProgress : Section(R.string.section_exercises_and_progress)
        object BodyWeight : Section(R.string.section_body_weight)
    }

    sealed class Title(resId: Int) : StringValue(resId) {
        object Exercise : Title(R.string.title_exercise)
        object Set : Title(R.string.title_set)
        object DropSet : Title(R.string.title_set_drop)
    }

    sealed class ExerciseCategory(resId: Int) : StringValue(resId) {
        object Push : ExerciseCategory(R.string.exercise_category_push)
        object Pull : ExerciseCategory(R.string.exercise_category_pull)
        object Legs : ExerciseCategory(R.string.exercise_category_legs)
        object Core : ExerciseCategory(R.string.exercise_category_core)
    }

    sealed class Time(resId: Int) : StringValue(resId) {
        object Today : Time(R.string.time_today)
        object Yesterday : Time(R.string.time_yesterday)
    }

    sealed class Action(resId: Int) : StringValue(resId) {
        object AddExercise : Action(R.string.action_add_exercise)
        object Add : Action(R.string.action_add)
        object NoteDown : Action(R.string.action_note_down)
    }

    sealed class FormInput(resId: Int) : StringValue(resId) {
        object Title : FormInput(R.string.form_input_text_title)
        object Range : FormInput(R.string.form_input_text_range)
        object Weight : FormInput(R.string.form_input_text_weight)
        object Reps : FormInput(R.string.form_input_text_repetitions)
        object Partials : FormInput(R.string.form_input_text_repetitions_partials)
    }

    sealed class Placeholder(resId: Int) : StringValue(resId) {
        object Kg : Placeholder(R.string.placeholder_weight)
    }
}

fun StringValue.toText(context: Context): String = context.getString(this.resId)
fun StringValue.toText(context: Context, value: Double): String = context.getString(this.resId, value)
