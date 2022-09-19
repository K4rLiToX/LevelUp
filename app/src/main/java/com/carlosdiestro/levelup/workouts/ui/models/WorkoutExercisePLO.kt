package com.carlosdiestro.levelup.workouts.ui.models

import androidx.recyclerview.widget.DiffUtil

data class WorkoutExercisePLO(
    val id: Int,
    val name: String,
    val isUnilateral: Boolean,
    val exerciseOrder: Int,
    val sets: List<WorkoutSetPLO> = mutableListOf()
) {

    class WorkoutExerciseDiffCallback : DiffUtil.ItemCallback<WorkoutExercisePLO>() {
        override fun areItemsTheSame(
            oldItem: WorkoutExercisePLO,
            newItem: WorkoutExercisePLO
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: WorkoutExercisePLO,
            newItem: WorkoutExercisePLO
        ): Boolean {
            return oldItem == newItem
        }
    }
}
