package com.carlosdiestro.levelup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.carlosdiestro.levelup.core.ui.extensions.slide
import com.carlosdiestro.levelup.databinding.ActivityMainBinding
import com.carlosdiestro.levelup.exercise_library.ui.ExerciseLibraryFragmentDirections
import com.carlosdiestro.levelup.workouts.ui.WorkoutsFragmentDirections
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
        setUpOnNavigationChangedListener()
        setUpClickListeners()
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

    private fun setUpOnNavigationChangedListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            setUpBottomNavigationVisibility(destination.id)
            setUpToolbar(destination)
        }
    }

    private fun setUpClickListeners() {
        binding.btnAdd.setOnClickListener {
            if (navController.currentDestination?.id == R.id.workoutsFragment) navController.navigate(
                WorkoutsFragmentDirections.toNewWorkoutFragment()
            )
            else navController.navigate(ExerciseLibraryFragmentDirections.toNewExerciseFragment())
        }
    }

    private fun setUpBottomNavigationVisibility(id: Int) {
        binding.bottomNavigation.apply {
            if (isMainSection(id)) {
                setUpFABVisibility(id)
                if (!this.isVisible) slide(true)
            } else {
                binding.btnAdd.hide()
                if (this.isVisible) slide(false)
            }
        }
    }

    private fun setUpFABVisibility(id: Int) {
        binding.btnAdd.apply {
            if (isFABSection(id)) show()
            else hide()
        }
    }

    private fun setUpToolbar(destination: NavDestination) {
        binding.mainToolbar.apply {
            title = destination.label.toString().uppercase()
            navigationIcon = if (isMainSection(destination.id)) null else ContextCompat.getDrawable(
                this@MainActivity,
                R.drawable.ic_back
            )
        }
    }

    private fun isMainSection(id: Int): Boolean = id in getMainSectionsIds()

    private fun isFABSection(id: Int): Boolean = id in getFABSectionsIds()

    private fun getMainSectionsIds(): List<Int> =
        listOf(R.id.workoutsFragment, R.id.exerciseLibraryFragment, R.id.bodyWeightProgressFragment)

    private fun getFABSectionsIds(): List<Int> =
        listOf(R.id.workoutsFragment, R.id.exerciseLibraryFragment)
}