package com.carlosdiestro.levelup.bodyweight_progress.ui.bodyweight_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosdiestro.levelup.bodyweight_progress.domain.usecases.GetWeightListUseCase
import com.carlosdiestro.levelup.bodyweight_progress.domain.usecases.NoteDownBodyWeightUseCase
import com.carlosdiestro.levelup.bodyweight_progress.domain.usecases.UpdateBodyWeightUseCase
import com.carlosdiestro.levelup.bodyweight_progress.domain.usecases.ValidateNewWeightUseCase
import com.carlosdiestro.levelup.bodyweight_progress.ui.models.BodyWeightPLO
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.core.ui.resources.StringResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BodyWeightProgressViewModel @Inject constructor(
    private val getWeightListUseCase: GetWeightListUseCase,
    private val noteDownBodyWeightUseCase: NoteDownBodyWeightUseCase,
    private val validateNewWeightUseCase: ValidateNewWeightUseCase,
    private val updateBodyWeightUseCase: UpdateBodyWeightUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<BodyWeightProgressState> =
        MutableStateFlow(BodyWeightProgressState())
    val state = _state.asStateFlow()

    init {
        fetchBodyWeights()
    }

    fun onEvent(event: BodyWeightProgressEvent) {
        when (event) {
            is BodyWeightProgressEvent.NoteDown -> submitNewWeight(event.weight)
            is BodyWeightProgressEvent.Update -> updateBodyWeight(event.item)
        }
    }

    private fun fetchBodyWeights() {
        launchAndCollect(getWeightListUseCase()) { response ->
            _state.update {
                it.copy(
                    noData = response.isEmpty(),
                    bodyWeights = response
                )
            }
        }
    }

    private fun submitNewWeight(input: String) {
        viewModelScope.launch {
            val response = validateNewWeightUseCase(input)
            if (!response.isSuccessful) {
                updateBodyWeightFormState(input, response.errorMessage)
            } else {
                noteDownBodyWeightUseCase(input.toDouble())
                updateBodyWeightFormState()
            }
        }
    }

    private fun updateBodyWeight(item: BodyWeightPLO) {
        viewModelScope.launch {
            updateBodyWeightUseCase(item)
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