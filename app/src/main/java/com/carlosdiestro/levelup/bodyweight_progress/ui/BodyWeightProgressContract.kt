package com.carlosdiestro.levelup.bodyweight_progress.ui

import com.carlosdiestro.levelup.bodyweight_progress.ui.models.BodyWeightPLO
import com.carlosdiestro.levelup.core.ui.StringValue

class BodyWeightProgressContract {

    sealed class BodyWeightProgressEvent {
        data class Submit(val weight: String) : BodyWeightProgressEvent()
    }

    data class BodyWeightProgressState(
        val noData: Boolean = false,
        val bodyWeightList: List<BodyWeightPLO> = emptyList(),
        val bodyWeightFormState: BodyWeightFormState = BodyWeightFormState()
    )

    data class BodyWeightFormState(
        val weight: String = "",
        val weightError: StringValue? = null
    )
}