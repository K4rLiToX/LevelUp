package com.carlosdiestro.levelup.workouts.ui.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CompletedWorkoutExercisePLO(
    val workoutId: Int,
    val name: String,
    val isUnilateral: Boolean,
    val exerciseOrder: Int,
    val completedSets: List<CompletedWorkoutSetPLO> = mutableListOf()
) : Parcelable