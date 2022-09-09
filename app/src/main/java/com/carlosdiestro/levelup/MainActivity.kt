package com.carlosdiestro.levelup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
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
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpNavigation()
        onNavigationChangedListener()
    }

    private fun setUpNavigation() {
        setSupportActionBar(binding.mainToolbar)
        appBarConfiguration = AppBarConfiguration(getMainSectionsIds().toSet())
        binding.apply {
            mainToolbar.setupWithNavController(
                navController,
                appBarConfiguration
            )
            bottomNavigation.setupWithNavController(navController)
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun onNavigationChangedListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            setUpBottomNavigationVisibility(destination.id)
            setUpToolbar(destination)
        }
    }

    private fun setUpBottomNavigationVisibility(id: Int) {
        if (isMainSection(id)) binding.bottomNavigation.visible()
        else binding.bottomNavigation.gone()
    }

    private fun setUpToolbar(destination: NavDestination) {
        binding.mainToolbar.apply {
            title = destination.label
            navigationIcon = if (isMainSection(destination.id)) null else ContextCompat.getDrawable(
                this@MainActivity,
                R.drawable.ic_back
            )
            isTitleCentered = isMainSection(destination.id)
        }
    }

    private fun isMainSection(id: Int): Boolean = id in getMainSectionsIds()


    private fun getMainSectionsIds(): List<Int> =
        listOf(R.id.workoutsFragment, R.id.exerciseLibraryFragment, R.id.bodyWeightProgressFragment)
}