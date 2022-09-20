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
    private lateinit var recyclerAdapter: CompletedWorkoutSetAdapter
    private lateinit var recyclerAdapterUnilateral: CompletedWorkoutSetUnilateralAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        collectUIState()
    }

    private fun setUpRecyclerView() {
        if (viewModel.isUnilateral()) {
            recyclerAdapterUnilateral = CompletedWorkoutSetUnilateralAdapter(
                { rw, lw, rr, lr, rp, lp, set ->
                    val newSet = set.copy(
                        currentWeight = set.currentWeight.copy(rightWeight = rw, leftWeight = lw),
                        currentReps = set.currentReps.copy(
                            rightReps = rr,
                            rightPartialReps = rp,
                            leftReps = lr,
                            leftPartialReps = lp
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
        } else {
            recyclerAdapter = CompletedWorkoutSetAdapter(
                { weight, reps, partials, set ->
                    val newSet = set.copy(
                        currentWeight = set.currentWeight.copy(rightWeight = weight),
                        currentReps = set.currentReps.copy(
                            rightReps = reps,
                            rightPartialReps = partials
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
        }
        binding.recyclerView.setUp(if (viewModel.isUnilateral()) recyclerAdapterUnilateral else recyclerAdapter)
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
        if (viewModel.isUnilateral()) {
            recyclerAdapterUnilateral.submitList(sets)
        } else {
            recyclerAdapter.submitList(sets)
        }
    }

    companion object {
        const val EXERCISE = "exercise"
    }
}