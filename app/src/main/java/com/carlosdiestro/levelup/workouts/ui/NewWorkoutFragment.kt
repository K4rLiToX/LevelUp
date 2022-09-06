package com.carlosdiestro.levelup.workouts.ui

import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.core.ui.extensions.visible
import com.carlosdiestro.levelup.core.ui.managers.viewBinding
import com.carlosdiestro.levelup.databinding.FragmentNewWorkoutBinding
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutSetPLO
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewWorkoutFragment : Fragment(R.layout.fragment_new_workout) {

    private val binding by viewBinding(FragmentNewWorkoutBinding::bind)
    private val viewModel by viewModels<NewWorkoutViewModel>()
    private lateinit var recyclerAdapter: WorkoutExerciseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition = getSharedElementTransition()
        sharedElementReturnTransition = getSharedElementTransition()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
        setUpRecyclerAdapter()
        setUpRecyclerView()
        collectUIState()
    }

    private fun setUpClickListeners() {
        binding.apply {
            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
            toolbar.menu.getItem(0).setOnMenuItemClickListener {
                submitNewWorkout()
                true
            }
            btnAddExercise.setOnClickListener { openExerciseChooserDialog() }
        }
    }

    private fun setUpRecyclerAdapter() {
        recyclerAdapter = WorkoutExerciseAdapter(
            { sets, pos ->
                AddSetDialog {
                    val setToAdd = WorkoutSetPLO(
                        id = sets.size + 1,
                        setOrder = sets.size + 1,
                        repRange = it
                    )
                    viewModel.onEvent(NewWorkoutEvent.OnNewSetClicked(setToAdd, pos))
                }.show(requireActivity().supportFragmentManager, AddSetDialog.TAG)
            },
            { e, s ->
                viewModel.onEvent(NewWorkoutEvent.OnSetRemoved(e, s))
            }
        )
    }

    private fun setUpRecyclerView() {
        binding.rvExerciseWithSets.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = recyclerAdapter
        }
    }

    private fun collectUIState() {
        launchAndCollect(viewModel.state) {
            handleNoData(it.noData)
            handleWorkoutExerciseList(it.exerciseList)
        }
    }

    private fun handleNoData(noData: Boolean) {
        binding.lNoData.root.visible(noData)
    }

    private fun handleWorkoutExerciseList(list: List<WorkoutExercisePLO>) {
        recyclerAdapter.submitList(list)
    }

    private fun submitNewWorkout() = Unit

    private fun openExerciseChooserDialog() {
        ExerciseChooserDialog {
            viewModel.onEvent(NewWorkoutEvent.OnExerciseClicked(it))
        }.show(requireActivity().supportFragmentManager, ExerciseChooserDialog.TAG)
//        requireActivity().supportFragmentManager.beginTransaction().apply {
//            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//            add(
//                android.R.id.content,
//                ExerciseChooserDialog {
//                    viewModel.onEvent(NewWorkoutEvent.OnExerciseClicked(it))
//                }
//            )
//            addToBackStack(null)
//        }.also {
//            it.commit()
//        }
    }

    private fun getSharedElementTransition(): Transition? {
        return TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
    }
}