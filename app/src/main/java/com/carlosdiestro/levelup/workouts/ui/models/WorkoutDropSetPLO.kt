package com.carlosdiestro.levelup.workouts.ui.models

import androidx.recyclerview.widget.DiffUtil
import com.carlosdiestro.levelup.workouts.domain.models.RepRange

data class WorkoutDropSetPLO(
    val dropSetOrder: Int,
    val repRange: RepRange
) {

    class WorkoutDropSetDiffCallback : DiffUtil.ItemCallback<WorkoutDropSetPLO>() {
        override fun areItemsTheSame(
            oldItem: WorkoutDropSetPLO,
            newItem: WorkoutDropSetPLO
        ): Boolean {
            return oldItem.dropSetOrder == newItem.dropSetOrder
        }

        override fun areContentsTheSame(
            oldItem: WorkoutDropSetPLO,
            newItem: WorkoutDropSetPLO
        ): Boolean {
            return oldItem == newItem
        }
    }
}
