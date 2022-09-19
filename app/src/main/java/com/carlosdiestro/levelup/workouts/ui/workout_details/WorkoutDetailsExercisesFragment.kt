package com.carlosdiestro.levelup.workouts.ui.workout_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.core.ui.extensions.verticalLayoutManger
import com.carlosdiestro.levelup.core.ui.managers.viewBinding
import com.carlosdiestro.levelup.databinding.FragmentWorkoutDetailsExercisesBinding
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkoutDetailsExercisesFragment : Fragment(R.layout.fragment_workout_details_exercises) {

    private val binding by viewBinding(FragmentWorkoutDetailsExercisesBinding::bind)
    private val viewModel by viewModels<WorkoutDetailsExercisesViewModel>()
    private lateinit var recyclerAdapter: SimpleWorkoutExerciseAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        collectUIState()
    }

    private fun setUpRecyclerView() {
        recyclerAdapter = SimpleWorkoutExerciseAdapter()
        binding.recyclerView.apply {
            verticalLayoutManger(requireContext())
            adapter = recyclerAdapter
        }
    }

    private fun collectUIState() {
        launchAndCollect(viewModel.state) {
            handleExerciseList(it.exercises)
        }
    }

    private fun handleExerciseList(list: List<WorkoutExercisePLO>) {
        recyclerAdapter.submitList(list)
    }

    companion object {
        const val WORKOUT_ID_KEY = "workout_id_key"
    }
}