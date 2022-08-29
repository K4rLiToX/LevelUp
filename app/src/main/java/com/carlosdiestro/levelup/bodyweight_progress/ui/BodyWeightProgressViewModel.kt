package com.carlosdiestro.levelup.bodyweight_progress.ui

import androidx.lifecycle.viewModelScope
import com.carlosdiestro.levelup.bodyweight_progress.domain.usecases.GetWeightListUseCase
import com.carlosdiestro.levelup.bodyweight_progress.domain.usecases.NoteDownBodyWeightUseCase
import com.carlosdiestro.levelup.bodyweight_progress.domain.usecases.ValidateNewWeightUseCase
import com.carlosdiestro.levelup.core.domain.Response
import com.carlosdiestro.levelup.core.ui.base.BaseViewModel
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
    private val validateNewWeightUseCase: ValidateNewWeightUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<BodyWeightProgressContract.BodyWeightProgressState> =
        MutableStateFlow(
            BodyWeightProgressContract.BodyWeightProgressState()
        )
    val state = _state.asStateFlow()

    init {
        fetchBodyWeights()
    }

    fun onEvent(event: BodyWeightProgressContract.BodyWeightProgressEvent) {
        when (event) {
            is BodyWeightProgressContract.BodyWeightProgressEvent.Submit -> submitNewWeight(event.weight)
        }
    }

    private fun fetchBodyWeights() {
        viewModelScope.launch {
            getWeightListUseCase().collect { response ->
                when (response) {
                    is Response.Loading -> Unit
                    is Response.Error -> Unit
                    is Response.Success -> _state.update {
                        it.copy(
                            noData = response.data!!.isEmpty(),
                            bodyWeightList = response.data
                        )
                    }
                }
            }
        }
    }

    private fun submitNewWeight(input: String) {
        viewModelScope.launch {
            validateNewWeightUseCase(input).collect { response ->
                if (!response.isSuccessful) {
                    _state.update {
                        it.copy(
                            bodyWeightFormState = it.bodyWeightFormState.copy(
                                weight = input,
                                weightError = response.errorMessage
                            )
                        )
                    }
                } else {
                    noteDownBodyWeightUseCase(input.toDouble())
                    _state.update {
                        it.copy(
                            bodyWeightFormState = it.bodyWeightFormState.copy(
                                weight = "",
                                weightError = null
                            )
                        )
                    }
                }
            }
        }
    }
}