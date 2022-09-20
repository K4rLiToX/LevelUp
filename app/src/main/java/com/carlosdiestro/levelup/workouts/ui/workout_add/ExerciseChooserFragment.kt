package com.carlosdiestro.levelup.workouts.ui.workout_add

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.core.ui.extensions.setUp
import com.carlosdiestro.levelup.core.ui.managers.viewBinding
import com.carlosdiestro.levelup.core.ui.resources.StringResource
import com.carlosdiestro.levelup.core.ui.resources.toText
import com.carlosdiestro.levelup.databinding.FragmentExerciseChooserBinding
import com.carlosdiestro.levelup.exercise_library.ui.ExerciseCategoryFragmentAdapter
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExerciseChooserFragment : Fragment(R.layout.fragment_exercise_chooser) {

    private val binding by viewBinding(FragmentExerciseChooserBinding::bind)
    private val viewModel by viewModels<ExerciseChooserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewPager()
        collectCallback()
    }

    private fun setUpViewPager() {
        binding.viewPager.setUp(
            ExerciseCategoryFragmentAdapter(true, this),
            binding.tabLayout
        ) { pos ->
            getTabText(pos)
        }
    }

    private fun collectCallback() {
        launchAndCollect(viewModel.channelCallback) { response ->
            when (response) {
                ExerciseChooserResponse.ExerciseSelected -> {
                    setFragmentResult(
                        ITEM_CLICKED_KEY,
                        bundleOf(ITEM_CLICKED_KEY to viewModel.getExercise()!!)
                    )
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun getTabText(position: Int): String {
        return when (position) {
            0 -> StringResource.All.toText(requireContext())
            1 -> StringResource.Push.toText(requireContext())
            2 -> StringResource.Pull.toText(requireContext())
            3 -> StringResource.Legs.toText(requireContext())
            else -> StringResource.Core.toText(requireContext())
        }
    }

    companion object {
        const val ITEM_CLICKED_KEY = "item_clicked_key"
    }
}