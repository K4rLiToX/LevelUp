package com.carlosdiestro.levelup.workouts.ui.workout_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.core.ui.extensions.verticalLayoutManger
import com.carlosdiestro.levelup.core.ui.managers.viewBinding
import com.carlosdiestro.levelup.databinding.FragmentWorkoutDetailsProgressExerciseBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkoutDetailsProgressExerciseFragment :
    Fragment(R.layout.fragment_workout_details_progress_exercise) {

    private val binding by viewBinding(FragmentWorkoutDetailsProgressExerciseBinding::bind)
    private val viewModel by viewModels<WorkoutDetailsProgressExerciseViewModel>()
    private lateinit var recyclerAdapter: WorkoutDetailsProgressExerciseAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapterWithRecyclerView()
        collectUIState()
    }

    private fun setUpAdapterWithRecyclerView() {
        recyclerAdapter = WorkoutDetailsProgressExerciseAdapter()
        binding.recyclerView.apply {
            verticalLayoutManger(requireContext())
            adapter = recyclerAdapter
        }
    }

    private fun collectUIState() {
        launchAndCollect(viewModel.state) {
            recyclerAdapter.submitList(it.exercises)
        }
    }

    companion object {
        const val EXERCISES = "exercises"
    }
}