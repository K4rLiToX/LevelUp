package com.carlosdiestro.levelup.workouts.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Repetition(
    val rightReps: Int = 0,
    val leftReps: Int = 0,
    val rightPartialReps: Int = 0,
    val leftPartialReps: Int = 0
) : Parcelable

fun Repetition.toStringValue(): String = "$rightReps+$rightPartialReps-$leftReps+$leftPartialReps"
fun String.toRepetition(): Repetition {
    val rightRepsAndPartials = this.split("-")[0]
    val leftRepsAndPartials = this.split("-")[1]
    return Repetition(
        rightRepsAndPartials.split("+")[0].toInt(),
        leftRepsAndPartials.split("+")[0].toInt(),
        rightRepsAndPartials.split("+")[1].toInt(),
        leftRepsAndPartials.split("+")[1].toInt()
    )
}
