package com.carlosdiestro.levelup.bodyweight_progress.ui.bodyweight_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosdiestro.levelup.bodyweight_progress.domain.usecases.GetBodyWeightListUseCase
import com.carlosdiestro.levelup.bodyweight_progress.domain.usecases.InsertBodyWeightUseCase
import com.carlosdiestro.levelup.bodyweight_progress.domain.usecases.UpdateBodyWeightUseCase
import com.carlosdiestro.levelup.bodyweight_progress.domain.usecases.ValidateBodyWeight
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
    private val getBodyWeightListUseCase: GetBodyWeightListUseCase,
    private val insertBodyWeightUseCase: InsertBodyWeightUseCase,
    private val validateBodyWeight: ValidateBodyWeight,
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
            is BodyWeightProgressEvent.NoteDown -> submitBodyWeight(event.weight)
            is BodyWeightProgressEvent.Update -> updateBodyWeight(event.item)
        }
    }

    private fun fetchBodyWeights() {
        launchAndCollect(getBodyWeightListUseCase()) { response ->
            _state.update {
                it.copy(
                    noData = response.isEmpty(),
                    bodyWeights = response
                )
            }
        }
    }

    private fun submitBodyWeight(input: String) {
        viewModelScope.launch {
            val response = validateBodyWeight(input)
            if (!response.isSuccessful) {
                updateState(input, response.errorMessage)
            } else {
                insertBodyWeightUseCase(input.toDouble())
                updateState("", null)
            }
        }
    }

    private fun updateBodyWeight(item: BodyWeightPLO) {
        viewModelScope.launch {
            updateBodyWeightUseCase(item)
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