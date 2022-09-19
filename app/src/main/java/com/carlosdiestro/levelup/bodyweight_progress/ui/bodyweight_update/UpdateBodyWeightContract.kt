package com.carlosdiestro.levelup.bodyweight_progress.ui.bodyweight_update

import com.carlosdiestro.levelup.core.ui.resources.StringResource

sealed interface UpdateBodyWeightEvent {
    class Save(val weight: String) : UpdateBodyWeightEvent
}

data class UpdateBodyWeightState(
    val weight: String = "",
    val weightError: StringResource? = null
)