package com.carlosdiestro.levelup.bodyweight_progress.mappers

import com.carlosdiestro.levelup.bodyweight_progress.domain.models.BodyWeight
import com.carlosdiestro.levelup.bodyweight_progress.framework.BodyWeightEntity
import com.carlosdiestro.levelup.core.ui.managers.TimeManager

fun BodyWeightEntity.toDomain(): BodyWeight =
    BodyWeight(date = TimeManager.toText(date), weight = weight)

fun List<BodyWeightEntity>.toDomain(): List<BodyWeight> =
    this.map { it.toDomain() }

fun BodyWeight.toEntity(): BodyWeightEntity =
    BodyWeightEntity(date = TimeManager.toMillis(date), weight = weight)