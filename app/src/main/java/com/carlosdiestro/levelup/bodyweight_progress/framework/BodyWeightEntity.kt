package com.carlosdiestro.levelup.bodyweight_progress.framework

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "body_weight_table")
data class BodyWeightEntity(
    @PrimaryKey
    val date: Long,
    val weight: Double
)
