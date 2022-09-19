package com.carlosdiestro.levelup.workouts.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Weight(
    val rightWeight: Double = 0.0,
    val leftWeight: Double = 0.0
) : Parcelable

fun Weight.toStringValue(): String = "$rightWeight-$leftWeight"
fun String.toWeight(): Weight = Weight(
    this.split("-")[0].toDouble(),
    this.split("-")[1].toDouble()
)
