package com.carlosdiestro.levelup.workouts.domain.models

data class RepRange(
    val lower: Int,
    val upper: Int
)

fun RepRange.toStringValue() = "$lower-$upper"
fun String.toRepRange(): RepRange = RepRange(
    this.split("-")[0].toInt(),
    this.split("-")[1].toInt()
)
