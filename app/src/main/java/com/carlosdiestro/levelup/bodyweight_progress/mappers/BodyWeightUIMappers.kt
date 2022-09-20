package com.carlosdiestro.levelup.bodyweight_progress.mappers

import com.carlosdiestro.levelup.bodyweight_progress.domain.models.BodyWeight
import com.carlosdiestro.levelup.bodyweight_progress.ui.models.BodyWeightPLO

fun BodyWeight.toPLO(): BodyWeightPLO =
    BodyWeightPLO(date = date, weight = weight)

fun List<BodyWeight>.toPLO(): List<BodyWeightPLO> =
    this.map { it.toPLO() }

fun BodyWeightPLO.toDomain(): BodyWeight =
    BodyWeight(date = date, weight = weight)