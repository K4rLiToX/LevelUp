package com.carlosdiestro.levelup.bodyweight_progress.ui.bodyweight_update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosdiestro.levelup.bodyweight_progress.domain.usecases.ValidateBodyWeight
import com.carlosdiestro.levelup.core.ui.resources.StringResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateBodyWeightViewModel @Inject constructor(
    private val validateBodyWeight: ValidateBodyWeight
) : ViewModel() {

    private var _state: MutableStateFlow<UpdateBodyWeightState> =
        MutableStateFlow(UpdateBodyWeightState())
    val state = _state.asStateFlow()

    private val _callbackChannel: Channel<String> = Channel()
    val callbackChannel = _callbackChannel.receiveAsFlow()

    fun onEvent(event: UpdateBodyWeightEvent) {
        when (event) {
            is UpdateBodyWeightEvent.Save -> updateBodyWeight(event.weight)
        }
    }

    private fun updateBodyWeight(input: String) {
        viewModelScope.launch {
            val response = validateBodyWeight(input, true)
            if (!response.isSuccessful) {
                updateState(input, response.errorMessage)
            } else {
                updateState("", null)
                _callbackChannel.send(input)
            }
        }
    }

    private fun updateState(
        input: String,
        errorMessage: StringResource?
    ) {
        _state.update {
            it.copy(
                weight = input,
                weightError = errorMessage
            )
        }
    }
}