package com.carlosdiestro.levelup.bodyweight_progress.ui

import androidx.recyclerview.widget.DiffUtil
import com.carlosdiestro.levelup.core.ui.StringValue
import com.carlosdiestro.levelup.core.ui.TimeManager

data class BodyWeightPLO(
    val date: String,
    val weight: Double
) {
    val dateLabelId: Int
        get() {
            return when (date) {
                TimeManager.now() -> StringValue.Time.Today.resId
                TimeManager.yesterday() -> StringValue.Time.Yesterday.resId
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


