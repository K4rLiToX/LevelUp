package com.carlosdiestro.levelup.workouts.ui.workout_on_going

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.core.ui.managers.viewBinding
import com.carlosdiestro.levelup.databinding.FragmentOnGoingWorkoutBinding
import com.google.android.material.tabs.TabLayoutMediator

class OnGoingWorkoutFragment : Fragment(R.layout.fragment_on_going_workout) {

    private val binding by viewBinding(FragmentOnGoingWorkoutBinding::bind)
    private val viewModel by viewModels<OnGoingWorkoutViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpTabLayoutWithViewPager()
    }

    private fun setUpTabLayoutWithViewPager() {
        binding.apply {
            viewPager.adapter = OnGoingWorkoutFragmentAdapter(emptyList(), this@OnGoingWorkoutFragment)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->

            }.attach()
        }
    }

    private fun getTabTitle(position: Int) = Unit
}