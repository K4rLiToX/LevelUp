package com.carlosdiestro.levelup.workouts.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.carlosdiestro.levelup.MainActivity
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.core.ui.managers.viewBinding
import com.carlosdiestro.levelup.core.ui.resources.StringResource
import com.carlosdiestro.levelup.core.ui.resources.toText
import com.carlosdiestro.levelup.databinding.FragmentWorkoutDetailsBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkoutDetailsFragment : Fragment(R.layout.fragment_workout_details) {

    private val binding by viewBinding(FragmentWorkoutDetailsBinding::bind)
    private val viewModel by viewModels<WorkoutDetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewPagerWithTabLayout()
        collectUIState()
    }

    private fun setUpViewPagerWithTabLayout() {
        binding.apply {
            viewPager.adapter =
                WorkoutDetailsFragmentAdapter(viewModel.getWorkoutId(), this@WorkoutDetailsFragment)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = getTabTitle(position)
            }.attach()
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
        (requireActivity() as MainActivity).supportActionBar?.title = title.uppercase()
    }
}