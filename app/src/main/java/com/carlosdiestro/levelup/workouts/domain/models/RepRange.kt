package com.carlosdiestro.levelup.workouts.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RepRange(
    val lower: Int,
    val upper: Int
) : Parcelable

fun RepRange.toStringValue() = "$lower-$upper"
fun String.toRepRange(): RepRange = RepRange(
    this.split("-")[0].toInt(),
    this.split("-")[1].toInt()
)
