package com.carlosdiestro.levelup.bodyweight_progress.ui

import com.carlosdiestro.levelup.core.ui.resources.StringResource

class UpdateBodyWeightContract {

    sealed class UpdateBodyWeightEvent {
        data class Save(val weight: String) : UpdateBodyWeightEvent()
    }

    data class UpdateBodyWeightState(
        val weight: String = "",
        val weightError: StringResource? = null
    )
}