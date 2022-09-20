package com.carlosdiestro.levelup.workouts.ui.workout_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.core.ui.extensions.gone
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.core.ui.extensions.setUp
import com.carlosdiestro.levelup.core.ui.managers.viewBinding
import com.carlosdiestro.levelup.databinding.FragmentWorkoutDetailsProgressBinding
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutExercisePLO
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkoutDetailsProgressFragment : Fragment(R.layout.fragment_workout_details_progress) {

    private val binding by viewBinding(FragmentWorkoutDetailsProgressBinding::bind)
    private val viewModel by viewModels<WorkoutDetailsProgressViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectUIState()
    }

    private fun collectUIState() {
        launchAndCollect(viewModel.state) {
            if (it.exercises.isNotEmpty()) {
                binding.apply {
                    tabLayout.gone(false)
                    viewPager.gone(false)
                    tabLayout.elevation = 0F
                }
                handleExercises(it.exercises)
            } else binding.apply { tabLayout.gone(); viewPager.gone() }
        }
    }

    private fun handleExercises(exercises: List<Pair<String, List<CompletedWorkoutExercisePLO>>>) {
        binding.viewPager.setUp(
            WorkoutDetailsProgressFragmentAdapter(
                exercises,
                this
            ),
            binding.tabLayout
        ) { pos ->
            exercises[pos].first
        }
    }

    companion object {
        const val WORKOUT_ID_KEY = "workout_id_key"
    }
}