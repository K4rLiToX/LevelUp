package com.carlosdiestro.levelup.bodyweight_progress.ui.bodyweight_list

import com.carlosdiestro.levelup.bodyweight_progress.ui.models.BodyWeightPLO
import com.carlosdiestro.levelup.core.ui.resources.StringResource

sealed interface BodyWeightProgressEvent {
    class NoteDown(val weight: String) : BodyWeightProgressEvent
    class Update(val item: BodyWeightPLO) : BodyWeightProgressEvent
}

data class BodyWeightProgressState(
    val noData: Boolean = false,
    val bodyWeights: List<BodyWeightPLO> = emptyList(),
    val weight: String = "",
    val weightError: StringResource? = null
)