package com.carlosdiestro.levelup.exercise_library.ui

import androidx.recyclerview.widget.DiffUtil
import com.carlosdiestro.levelup.exercise_library.domain.models.ExerciseCategory

data class ExercisePLO(
    val id: Int,
    val name: String,
    val isUnilateral: Boolean,
    val category: ExerciseCategory,
    val isSelected: Boolean = false
) {

    class ExerciseDiffCallback : DiffUtil.ItemCallback<ExercisePLO>() {
        override fun areItemsTheSame(oldItem: ExercisePLO, newItem: ExercisePLO): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ExercisePLO, newItem: ExercisePLO): Boolean {
            return oldItem == newItem
        }

    }
}
