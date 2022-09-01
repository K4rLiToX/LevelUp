package com.carlosdiestro.levelup.bodyweight_progress.ui.bodyweight_list

import com.carlosdiestro.levelup.bodyweight_progress.ui.models.BodyWeightPLO
import com.carlosdiestro.levelup.core.ui.resources.StringResource

sealed class BodyWeightProgressEvent {
    data class NoteDown(val weight: String) : BodyWeightProgressEvent()
    data class Update(val bodyWeightPLO: BodyWeightPLO) : BodyWeightProgressEvent()
}

data class BodyWeightProgressState(
    val noData: Boolean = false,
    val bodyWeightList: List<BodyWeightPLO> = emptyList(),
    val weight: String = "",
    val weightError: StringResource? = null
)