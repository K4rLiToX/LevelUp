package com.carlosdiestro.levelup.workouts.ui.models

import androidx.recyclerview.widget.DiffUtil

data class WorkoutPLO(
    val id: Int,
    val name: String,
    val numberOfExercises: String
) {

    class WorkoutDiffCallback : DiffUtil.ItemCallback<WorkoutPLO>() {
        override fun areItemsTheSame(oldItem: WorkoutPLO, newItem: WorkoutPLO): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WorkoutPLO, newItem: WorkoutPLO): Boolean {
            return oldItem == newItem
        }

    }
}