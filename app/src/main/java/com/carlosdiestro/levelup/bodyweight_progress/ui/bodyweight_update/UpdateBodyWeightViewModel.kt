package com.carlosdiestro.levelup.bodyweight_progress.ui.bodyweight_update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosdiestro.levelup.bodyweight_progress.domain.usecases.ValidateNewWeightUseCase
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
    private val validateNewWeightUseCase: ValidateNewWeightUseCase
) : ViewModel() {

    private var _state: MutableStateFlow<UpdateBodyWeightState> =
        MutableStateFlow(UpdateBodyWeightState())
    val state = _state.asStateFlow()

    private val _callbackChannel: Channel<String> = Channel()
    val callbackChannel = _callbackChannel.receiveAsFlow()

    fun onEvent(event: UpdateBodyWeightEvent) {
        when (event) {
            is UpdateBodyWeightEvent.Save -> updateNewWeight(event.weight)
        }
    }

    private fun updateNewWeight(input: String) {
        viewModelScope.launch {
            val response = validateNewWeightUseCase(input, true)
            if (!response.isSuccessful) {
                updateBodyWeightFormState(input, response.errorMessage)
            } else {
                updateBodyWeightFormState()
                _callbackChannel.send(input)
            }
        }
    }

    private fun updateBodyWeightFormState(
        input: String = "",
        errorMessage: StringResource? = null
    ) {
        _state.update {
            it.copy(
                weight = input,
                weightError = errorMessage
            )
        }
    }
}