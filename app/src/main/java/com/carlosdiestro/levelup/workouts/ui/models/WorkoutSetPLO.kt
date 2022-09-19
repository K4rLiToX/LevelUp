package com.carlosdiestro.levelup.workouts.ui.models

import androidx.recyclerview.widget.DiffUtil
import com.carlosdiestro.levelup.workouts.domain.models.RepRange

data class WorkoutSetPLO(
    val id: Int,
    val setOrder: Int,
    val repRange: RepRange
) {

    class WorkoutSetDiffCallback : DiffUtil.ItemCallback<WorkoutSetPLO>() {
        override fun areItemsTheSame(oldItem: WorkoutSetPLO, newItem: WorkoutSetPLO): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WorkoutSetPLO, newItem: WorkoutSetPLO): Boolean {
            return oldItem == newItem
        }
    }
}
