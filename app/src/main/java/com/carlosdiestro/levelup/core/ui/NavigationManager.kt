package com.carlosdiestro.levelup.core.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController

class NavigationManager(private val viewModel: BaseViewModel) :
    FragmentManager.FragmentLifecycleCallbacks() {

    sealed class NavCommand {
        data class To(val navDestination: NavDestination) : NavCommand()
        object Up : NavCommand()
        object Back : NavCommand()
        object Idle : NavCommand()
    }

    sealed class NavDestination(val navDirections: NavDirections) {
        //TODO
        //Implement own destinations in the form of
        // class FragmentNameToFragmentName(safeArg1: Type, safeArg2: Type, ...) : RouteDestination(FragmentNameDirections.actionFragmentNameToFragmentName(safeArg1, safeArg2, ...))
    }

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState)
        f.viewLifecycleOwner.launchAndCollect(viewModel.navEvent) { navCommand ->
            when (navCommand) {
                is NavCommand.To -> navigateTo(navCommand, f)
                is NavCommand.Up -> navigateUp(f)
                is NavCommand.Back -> navigateBack(f)
                is NavCommand.Idle -> Unit
            }
        }
    }

    fun register(fragmentManager: FragmentManager) {
        fragmentManager.registerFragmentLifecycleCallbacks(this, true)
    }

    private fun navigateTo(navCommand: NavCommand.To, fragment: Fragment) {
        findNavController(fragment).navigate(navCommand.navDestination.navDirections)
    }

    private fun navigateUp(fragment: Fragment) {
        findNavController(fragment).navigateUp()
    }

    private fun navigateBack(fragment: Fragment) {
        findNavController(fragment).popBackStack()
    }
}