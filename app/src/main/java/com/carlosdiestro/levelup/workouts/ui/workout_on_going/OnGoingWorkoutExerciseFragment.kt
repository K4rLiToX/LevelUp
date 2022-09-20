package com.carlosdiestro.levelup.workouts.ui.workout_on_going

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.core.ui.extensions.setUp
import com.carlosdiestro.levelup.core.ui.extensions.showWarningDialog
import com.carlosdiestro.levelup.core.ui.managers.viewBinding
import com.carlosdiestro.levelup.core.ui.resources.StringResource
import com.carlosdiestro.levelup.databinding.FragmentOnGoingWorkoutExerciseBinding
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutSetPLO
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnGoingWorkoutExerciseFragment : Fragment(R.layout.fragment_on_going_workout_exercise) {

    private val binding by viewBinding(FragmentOnGoingWorkoutExerciseBinding::bind)
    private val viewModel by viewModels<OnGoingWorkoutExerciseViewModel>()
    private val onGoingWorkoutViewModel: OnGoingWorkoutViewModel by viewModels(ownerProducer = { requireParentFragment() })
    private lateinit var recyclerAdapterUnilateral: CompletedWorkoutSetAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        collectUIState()
    }

    private fun setUpRecyclerView() {
        val isUnilateral = viewModel.isUnilateral()
        recyclerAdapterUnilateral = CompletedWorkoutSetAdapter(
            isUnilateral,
            { rw, lw, rr, lr, rp, lp, set ->
                val newSet = set.copy(
                    currentWeight = set.currentWeight.copy(
                        rightWeight = if (isUnilateral) rw else 0.0,
                        leftWeight = lw
                    ),
                    currentReps = set.currentReps.copy(
                        rightReps = rr,
                        rightPartialReps = rp,
                        leftReps = if (isUnilateral) lr else 0,
                        leftPartialReps = if (isUnilateral) lp else 0
                    ),
                    isCompleted = true
                )
                onGoingWorkoutViewModel.onEvent(
                    OnGoingWorkoutEvent.UpdateCompletedWorkoutSet(
                        newSet,
                        viewModel.exerciseOrder
                    )
                )
            },
            { openWarningDialog() }
        )
        binding.recyclerView.setUp(recyclerAdapterUnilateral)
    }

    private fun openWarningDialog() {
        showWarningDialog(StringResource.CompleteSetError.resId)
    }

    private fun collectUIState() {
        launchAndCollect(viewModel.state) {
            handleSets(it.exercise.completedSets)
        }
    }

    private fun handleSets(sets: List<CompletedWorkoutSetPLO>) {
        recyclerAdapterUnilateral.submitList(sets)
    }

    companion object {
        const val EXERCISE = "exercise"
    }
}