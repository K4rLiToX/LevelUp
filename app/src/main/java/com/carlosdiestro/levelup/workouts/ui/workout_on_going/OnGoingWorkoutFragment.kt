package com.carlosdiestro.levelup.workouts.ui.workout_on_going

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.carlosdiestro.levelup.MainActivity
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.core.ui.extensions.showWarningDialog
import com.carlosdiestro.levelup.core.ui.managers.viewBinding
import com.carlosdiestro.levelup.core.ui.resources.StringResource
import com.carlosdiestro.levelup.core.ui.resources.toText
import com.carlosdiestro.levelup.databinding.FragmentOnGoingWorkoutBinding
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutExercisePLO
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnGoingWorkoutFragment : Fragment(R.layout.fragment_on_going_workout), MenuProvider {

    private val binding by viewBinding(FragmentOnGoingWorkoutBinding::bind)
    private val viewModel by viewModels<OnGoingWorkoutViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMenu()
        collectUIState()
        collectEvents()
    }

    override fun onPrepareMenu(menu: Menu) {
        (menu.findItem(R.id.action_finish).actionView as MaterialButton).text =
            StringResource.Finish.toText(requireContext())
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        if (menu.isEmpty()) {
            menuInflater.inflate(R.menu.menu_finish, menu)
            (menu.findItem(R.id.action_finish).actionView as MaterialButton).setOnClickListener {
                finishWorkout()
            }
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_finish -> {
                finishWorkout()
                true
            }
            else -> false
        }
    }

    private fun collectUIState() {
        launchAndCollect(viewModel.state) {
            handleWorkoutName(it.workoutName)
            handleExercises(it.completedExercises)
            handleCurrentExercise(it.currentExercise)
        }
    }

    private fun collectEvents() {
        launchAndCollect(viewModel.eventChannel) {
            when (it) {
                OnGoingWorkoutResponse.NavigateBack -> findNavController().popBackStack()
                OnGoingWorkoutResponse.ShowWarningDialog -> showWarningDialog(StringResource.CompleteExercisesError.resId)
            }
        }
    }

    private fun handleWorkoutName(name: String) {
        (requireActivity() as MainActivity).supportActionBar!!.title = name.uppercase()
    }

    private fun handleExercises(exercises: List<CompletedWorkoutExercisePLO>) {
        binding.apply {
            viewPager.adapter =
                OnGoingWorkoutFragmentAdapter(exercises, this@OnGoingWorkoutFragment)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = exercises[position].name
            }.attach()
        }
    }

    private fun handleCurrentExercise(exercise: CompletedWorkoutExercisePLO?) {
        exercise?.let {
            binding.viewPager.setCurrentItem(it.exerciseOrder - 1, false)
        }
    }

    private fun setUpMenu() {
        requireActivity().addMenuProvider(
            this@OnGoingWorkoutFragment,
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    private fun finishWorkout() {
        viewModel.onEvent(OnGoingWorkoutEvent.FinishWorkout)
    }
}