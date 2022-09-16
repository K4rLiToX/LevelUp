package com.carlosdiestro.levelup.workouts.ui.models

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.carlosdiestro.levelup.workouts.domain.models.RepRange
import com.carlosdiestro.levelup.workouts.domain.models.Repetition
import com.carlosdiestro.levelup.workouts.domain.models.SetStatus
import com.carlosdiestro.levelup.workouts.domain.models.Weight
import kotlinx.parcelize.Parcelize

@Parcelize
data class CompletedWorkoutSetPLO(
    val exerciseId: Int = -1,
    val repRange: RepRange,
    val setOrder: Int,
    val lastReps: Repetition,
    val lastWeight: Weight,
    val currentReps: Repetition,
    val currentWeight: Weight,
    val status: SetStatus,
    val isCompleted: Boolean = false
) : Parcelable {

    class CompletedWorkoutSetDiffCallback : DiffUtil.ItemCallback<CompletedWorkoutSetPLO>() {
        override fun areItemsTheSame(
            oldItem: CompletedWorkoutSetPLO,
            newItem: CompletedWorkoutSetPLO
        ): Boolean {
            return oldItem.setOrder == newItem.setOrder
        }

        override fun areContentsTheSame(
            oldItem: CompletedWorkoutSetPLO,
            newItem: CompletedWorkoutSetPLO
        ): Boolean {
            return oldItem == newItem
        }
    }
}
