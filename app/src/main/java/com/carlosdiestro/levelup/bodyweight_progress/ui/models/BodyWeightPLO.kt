package com.carlosdiestro.levelup.bodyweight_progress.ui.models

import androidx.recyclerview.widget.DiffUtil
import com.carlosdiestro.levelup.core.ui.managers.TimeManager
import com.carlosdiestro.levelup.core.ui.resources.StringResource

data class BodyWeightPLO(
    val date: String,
    val weight: Double
) {
    val dateLabelId: Int
        get() {
            return when (date) {
                TimeManager.now() -> StringResource.Today.resId
                TimeManager.yesterday() -> StringResource.Yesterday.resId
                else -> -1
            }
        }

    class BodyWeightDiffCallback : DiffUtil.ItemCallback<BodyWeightPLO>() {
        override fun areItemsTheSame(oldItem: BodyWeightPLO, newItem: BodyWeightPLO): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: BodyWeightPLO, newItem: BodyWeightPLO): Boolean {
            return oldItem == newItem
        }
    }
}


