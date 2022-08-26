package com.carlosdiestro.levelup.bodyweight_progress.ui

import androidx.lifecycle.viewModelScope
import com.carlosdiestro.levelup.bodyweight_progress.domain.usecases.GetWeightListUseCase
import com.carlosdiestro.levelup.core.domain.Response
import com.carlosdiestro.levelup.core.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BodyWeightProgressViewModel @Inject constructor(
    private val getWeightListUseCase: GetWeightListUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<BodyWeightUIState> = MutableStateFlow(
        BodyWeightUIState()
    )
    val state = _state.asStateFlow()

    init {
        initBodyWeights()
    }

    private fun initBodyWeights() {
        viewModelScope.launch {
            getWeightListUseCase().collect { response ->
                when (response) {
                    is Response.Loading -> _state.update { it.copy(isLoading = true) }
                    is Response.Error -> _state.update {
                        it.copy(
                            isLoading = false,
                            noData = true,
                            weightList = emptyList()
                        )
                    }
                    is Response.Success -> _state.update {
                        it.copy(
                            isLoading = false,
                            noData = false,
                            weightList = response.data!!
                        )
                    }
                }
            }
        }
    }

    data class BodyWeightUIState(
        val isLoading: Boolean = false,
        val noData: Boolean = false,
        val weightList: List<BodyWeightPLO> = emptyList()
    )
}