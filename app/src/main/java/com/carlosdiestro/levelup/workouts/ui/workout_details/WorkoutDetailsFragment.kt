package com.carlosdiestro.levelup.workouts.ui.workout_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.core.ui.extensions.setActionBarTitle
import com.carlosdiestro.levelup.core.ui.extensions.setUp
import com.carlosdiestro.levelup.core.ui.managers.viewBinding
import com.carlosdiestro.levelup.core.ui.resources.StringResource
import com.carlosdiestro.levelup.core.ui.resources.toText
import com.carlosdiestro.levelup.databinding.FragmentWorkoutDetailsBinding
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkoutDetailsFragment : Fragment(R.layout.fragment_workout_details) {

    private val binding by viewBinding(FragmentWorkoutDetailsBinding::bind)
    private val viewModel by viewModels<WorkoutDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
        setUpViewPagerWithTabLayout()
        collectUIState()
    }

    private fun setUpClickListeners() {
        binding.btnStartWorkout.setOnClickListener { navigateToOnGoingWorkout() }
    }

    private fun navigateToOnGoingWorkout() {
        findNavController().navigate(
            WorkoutDetailsFragmentDirections.toOnGoingWorkoutFragment(
                viewModel.getWorkoutId(),
                viewModel.getWorkoutName()
            )
        )
    }

    private fun setUpViewPagerWithTabLayout() {
        binding.viewPager.setUp(
            WorkoutDetailsFragmentAdapter(
                viewModel.getWorkoutId(),
                this
            ),
            binding.tabLayout
        ) { pos ->
            getTabTitle(pos)
        }
    }

    private fun getTabTitle(position: Int): String {
        return when (position) {
            0 -> StringResource.Exercises.toText(requireContext())
            else -> StringResource.Progress.toText(requireContext())
        }
    }

    private fun collectUIState() {
        launchAndCollect(viewModel.state) {
            handleAppBarTitle(it.title)
        }
    }

    private fun handleAppBarTitle(title: String) {
        setActionBarTitle(title)
    }
}