package com.carlosdiestro.levelup.core.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel : ViewModel() {
    private val _navEvent: MutableStateFlow<NavigationManager.NavCommand> = MutableStateFlow(NavigationManager.NavCommand.Idle)
    val navEvent = _navEvent.asSharedFlow()

    fun navigate(navCommand: NavigationManager.NavCommand) {
        _navEvent.update { navCommand }
    }
}