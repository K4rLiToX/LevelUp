package com.carlosdiestro.levelup.workouts.ui.models

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.parcelize.Parcelize

@Parcelize
data class CompletedWorkoutExercisePLO(
    val workoutId: Int,
    val name: String,
    val date: String = "",
    val isUnilateral: Boolean,
    val exerciseOrder: Int,
    val completedSets: List<CompletedWorkoutSetPLO> = mutableListOf()
) : Parcelable {

    class CompletedWorkoutExerciseDiffCallback :
        DiffUtil.ItemCallback<CompletedWorkoutExercisePLO>() {
        override fun areItemsTheSame(
            oldItem: CompletedWorkoutExercisePLO,
            newItem: CompletedWorkoutExercisePLO
        ): Boolean {
            return oldItem.exerciseOrder == newItem.exerciseOrder
        }

        override fun areContentsTheSame(
            oldItem: CompletedWorkoutExercisePLO,
            newItem: CompletedWorkoutExercisePLO
        ): Boolean {
            return oldItem == newItem
        }
    }
}