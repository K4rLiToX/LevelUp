package com.carlosdiestro.levelup.exercise_library.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.core.ui.managers.viewBinding
import com.carlosdiestro.levelup.core.ui.resources.StringResource
import com.carlosdiestro.levelup.core.ui.resources.toText
import com.carlosdiestro.levelup.databinding.FragmentExerciseLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExerciseLibraryFragment : Fragment(R.layout.fragment_exercise_library) {

    private val binding by viewBinding(FragmentExerciseLibraryBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
        setUpViewPagerWithTabLayout()
    }

    private fun setUpClickListeners() {
        binding.btnAdd.setOnClickListener { navigateToAddExerciseFragment() }
    }

    private fun setUpViewPagerWithTabLayout() {
        binding.apply {
            vpExercises.adapter = ExerciseCategoryFragmentAdapter(this@ExerciseLibraryFragment)
            vpExercises.isUserInputEnabled = false
            TabLayoutMediator(tabExerciseGroup, vpExercises) { tab, position ->
                tab.text = getTabTitle(position)
            }.attach()
        }
    }

    private fun getTabTitle(position: Int): String {
        return when (position) {
            0 -> StringResource.All.toText(requireContext())
            1 -> StringResource.Push.toText(requireContext())
            2 -> StringResource.Pull.toText(requireContext())
            3 -> StringResource.Legs.toText(requireContext())
            else -> StringResource.Core.toText(requireContext())
        }
    }

    private fun navigateToAddExerciseFragment() {
        findNavController().navigate(
            ExerciseLibraryFragmentDirections.toNewExerciseFragment(),
            FragmentNavigatorExtras(
                binding.btnAdd to "transition_fab"
            )
        )
    }
}