package com.carlosdiestro.levelup.workouts.ui

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.core.ui.extensions.visible
import com.carlosdiestro.levelup.core.ui.managers.viewBinding
import com.carlosdiestro.levelup.databinding.FragmentWorkoutsBinding
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutPLO
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkoutsFragment : Fragment(R.layout.fragment_workouts) {

    private val binding by viewBinding(FragmentWorkoutsBinding::bind)
    private val viewModel by viewModels<WorkoutsViewModel>()
    private lateinit var recyclerAdapter: WorkoutAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
        setUpRecyclerAdapter()
        setUpRecyclerView()
        collectUIState()
    }

    private fun setUpClickListeners() {
        binding.btnCreate.setOnClickListener { navigateToAddNewWorkout() }
    }

    private fun setUpRecyclerAdapter() {
        recyclerAdapter = WorkoutAdapter(
            { navigateToWorkoutDetails(it) },
            { id, view -> openMoreMenu(id, view) }
        )
    }

    private fun setUpRecyclerView() {
        binding.rvWorkouts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = recyclerAdapter
        }
    }

    private fun collectUIState() {
        launchAndCollect(viewModel.state) {
            handleNoData(it.noData)
            handleWorkoutList(it.workoutList)
        }
    }

    private fun handleNoData(noData: Boolean) {
        binding.lNoData.root.visible(noData)
    }

    private fun handleWorkoutList(list: List<WorkoutPLO>) {
        recyclerAdapter.submitList(list)
    }

    private fun navigateToWorkoutDetails(id: Int) = Unit

    private fun openMoreMenu(id: Int, view: View) {
        PopupMenu(requireContext(), view).apply {
            menuInflater.inflate(R.menu.menu_workout_manager, this.menu)
            gravity = Gravity.END
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_update -> navigateToUpdateWorkout(id)
                    else -> deleteWorkout(id)
                }
                true
            }
        }.also {
            it.show()
        }
    }

    private fun navigateToAddNewWorkout() {
        findNavController().navigate(
            WorkoutsFragmentDirections.toNewWorkoutFragment(),
            FragmentNavigatorExtras(
                binding.btnCreate to "fab_to_new_workout_transition"
            )
        )
    }

    private fun navigateToUpdateWorkout(id: Int) {
        findNavController().navigate(
            WorkoutsFragmentDirections.toNewWorkoutFragment(id)
        )
    }

    private fun deleteWorkout(id: Int) {
        viewModel.onEvent(WorkoutEvent.OnDeleteWorkout(id))
    }
}