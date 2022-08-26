package com.carlosdiestro.levelup.core.ui

import androidx.annotation.MainThread
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

interface NavigationManager {
    fun to(scope: CoroutineScope, routeDestination: NavDestination)
    fun back(scope: CoroutineScope)
    fun getEventCallback(): Flow<NavController.() -> Any>
}

private class NavigationManagerImpl : NavigationManager {
    private val navEventChannel = Channel<NavController.() -> Any>()
    private val navEventFlow = navEventChannel.receiveAsFlow()

    override fun to(scope: CoroutineScope, routeDestination: NavDestination) {
        withNavController(scope) { navigate(routeDestination.navDirections) }
    }

    override fun back(scope: CoroutineScope) {
        withNavController(scope) { navigateUp() }
    }

    override fun getEventCallback(): Flow<NavController.() -> Any> {
        return navEventFlow
    }

    private fun withNavController(scope: CoroutineScope, action: NavController.() -> Any) {
        scope.launch { navEventChannel.send(action) }
    }
}

sealed class NavDestination(val navDirections: NavDirections) {
    //TODO
    //Implement own destinations in the form of
    // class FragmentNameToFragmentName(safeArg1: Type, safeArg2: Type, ...) : RouteDestination(FragmentNameDirections.actionFragmentNameToFragmentName(safeArg1, safeArg2, ...))
}

@MainThread
fun navigations(): Lazy<NavigationManager> {
    return NavigationManagerLazy()
}

private class NavigationManagerLazy : Lazy<NavigationManager> {

    private var cached: NavigationManager? = null

    override val value: NavigationManager
        get() {
            return cached ?: NavigationManagerImpl().also { cached = it }
        }

    override fun isInitialized(): Boolean = cached != null
}