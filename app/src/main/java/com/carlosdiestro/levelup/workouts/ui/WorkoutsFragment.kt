package com.carlosdiestro.levelup.workouts.ui

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.core.ui.extensions.verticalLayoutManger
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
        setUpRecyclerView()
        collectUIState()
    }

    private fun setUpRecyclerView() {
        recyclerAdapter = WorkoutAdapter(
            { id, name -> navigateToWorkoutDetails(id, name) },
            { id, view -> openMoreMenu(id, view) }
        )
        binding.rvWorkouts.apply {
            verticalLayoutManger(requireContext())
            adapter = recyclerAdapter
        }
    }

    private fun collectUIState() {
        launchAndCollect(viewModel.state) {
            handleNoData(it.noData)
            handleWorkoutList(it.workouts)
        }
    }

    private fun handleNoData(noData: Boolean) {
        binding.lNoData.root.visible(noData)
    }

    private fun handleWorkoutList(list: List<WorkoutPLO>) {
        recyclerAdapter.submitList(list)
    }

    private fun navigateToWorkoutDetails(id: Int, name: String) {
        findNavController().navigate(WorkoutsFragmentDirections.toWorkoutDetailsFragment(id, name))
    }

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

    private fun navigateToUpdateWorkout(id: Int) {
        findNavController().navigate(
            WorkoutsFragmentDirections.toNewWorkoutFragment(id)
        )
    }

    private fun deleteWorkout(id: Int) {
        viewModel.onEvent(WorkoutEvent.OnDeleteWorkout(id))
    }
}