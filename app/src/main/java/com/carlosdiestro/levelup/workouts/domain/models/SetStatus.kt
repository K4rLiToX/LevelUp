package com.carlosdiestro.levelup.workouts.domain.models

enum class SetStatus {
    LEVEL_UP,
    KEEP_WORKING,
    LEVEL_DOWN
}

fun SetStatus.toInt(): Int = when (this) {
    SetStatus.LEVEL_UP -> SetStatus.LEVEL_UP.ordinal
    SetStatus.KEEP_WORKING -> SetStatus.KEEP_WORKING.ordinal
    SetStatus.LEVEL_DOWN -> SetStatus.LEVEL_DOWN.ordinal
}

fun Int.toSetStatus(): SetStatus = when (this) {
    SetStatus.LEVEL_UP.ordinal -> SetStatus.LEVEL_UP
    SetStatus.KEEP_WORKING.ordinal -> SetStatus.KEEP_WORKING
    else -> SetStatus.LEVEL_DOWN
}