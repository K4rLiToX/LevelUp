package com.carlosdiestro.levelup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.carlosdiestro.levelup.core.ui.extensions.gone
import com.carlosdiestro.levelup.core.ui.extensions.visible
import com.carlosdiestro.levelup.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpBottomNavigationWithNavigationComponent()
        onNavigationChangedListener()
    }

    private fun setUpBottomNavigationWithNavigationComponent() {
        NavigationUI.setupWithNavController(
            binding.bottomNavigation,
            navController
        )
    }

    private fun onNavigationChangedListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (isMainSection(destination.id)) binding.bottomNavigation.visible()
            else binding.bottomNavigation.gone()
        }
    }

    private fun isMainSection(id: Int): Boolean {
        return id == R.id.workoutsFragment ||
                id == R.id.exerciseLibraryFragment ||
                id == R.id.bodyWeightProgressFragment
    }
}