package com.carlosdiestro.levelup.workouts.ui

import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.core.ui.extensions.toTrimmedString
import com.carlosdiestro.levelup.core.ui.extensions.visible
import com.carlosdiestro.levelup.core.ui.managers.viewBinding
import com.carlosdiestro.levelup.core.ui.resources.StringResource
import com.carlosdiestro.levelup.core.ui.resources.toText
import com.carlosdiestro.levelup.databinding.FragmentNewWorkoutBinding
import com.carlosdiestro.levelup.exercise_library.ui.models.ExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutSetPLO
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewWorkoutFragment : Fragment(R.layout.fragment_new_workout) {

    private val binding by viewBinding(FragmentNewWorkoutBinding::bind)
    private val viewModel by viewModels<NewWorkoutViewModel>()
    private lateinit var recyclerAdapter: WorkoutExerciseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(
            ExerciseChooserFragment.ITEM_CLICKED_KEY
        ) { requestKey, bundle ->
            val result = bundle.getParcelable<ExercisePLO>(requestKey)
            result?.let { viewModel.onEvent(NewWorkoutEvent.OnExerciseClicked(it)) }
        }
    }

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
        collectChannelEvents()
    }

    private fun setUpClickListeners() {
        binding.apply {
            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
            toolbar.menu.getItem(0).setOnMenuItemClickListener {
                submitNewWorkout()
                true
            }
            btnAddExercise.setOnClickListener { navigateToExerciseChooserFragment() }
        }
    }

    private fun setUpRecyclerAdapter() {
        recyclerAdapter = WorkoutExerciseAdapter(
            { sets, pos ->
                AddSetDialog {
                    val setToAdd = WorkoutSetPLO(
                        id = -1,
                        setOrder = sets.size + 1,
                        repRange = it
                    )
                    viewModel.onEvent(NewWorkoutEvent.OnNewSetClicked(setToAdd, pos))
                }.show(requireActivity().supportFragmentManager, AddSetDialog.TAG)
            },
            { e, s ->
                viewModel.onEvent(NewWorkoutEvent.OnSetRemoved(e, s))
            },
            { e, s ->
                AddSetDialog(s.repRange) {
                    val newSet = s.copy(repRange = it)
                    viewModel.onEvent(NewWorkoutEvent.OnUpdateSetClicked(e, newSet))
                }.show(requireActivity().supportFragmentManager, AddSetDialog.TAG)

            },
            { id, view ->
                openMoreMenu(id, view)
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
            handleNameError(it.workoutNameError)
            handleWorkoutName(it.workoutName)
            handleToolbarTitle(it.toolbarTitle)
        }
    }

    private fun collectChannelEvents() {
        launchAndCollect(viewModel.eventChannel) { event ->
            when (event) {
                NewWorkoutEventResponse.PopBackStack -> findNavController().popBackStack()
                is NewWorkoutEventResponse.ShowWarningDialog -> event.message?.let {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(StringResource.Warning.resId)
                        .setMessage(it.resId)
                        .setNegativeButton(StringResource.Close.resId) { d, _ -> d.dismiss() }
                        .create()
                        .show()
                }
            }
        }
    }

    private fun handleNoData(noData: Boolean) {
        binding.lNoData.root.visible(noData)
    }

    private fun handleWorkoutExerciseList(list: List<WorkoutExercisePLO>) {
        recyclerAdapter.submitList(list)
    }

    private fun handleNameError(error: StringResource?) {
        binding.ilName.error = error?.toText(requireContext())
    }

    private fun handleWorkoutName(name: String) {
        binding.etName.setText(name)
    }

    private fun handleToolbarTitle(title: StringResource) {
        binding.toolbar.title = title.toText(requireContext())
    }

    private fun submitNewWorkout() {
        val workoutName = binding.etName.text.toTrimmedString()
        viewModel.onEvent(NewWorkoutEvent.AddNewWorkout(workoutName))
    }

    private fun navigateToExerciseChooserFragment() {
        findNavController().navigate(NewWorkoutFragmentDirections.toExerciseChooserFragment())
    }

    private fun openMoreMenu(id: Int, view: View) {
        PopupMenu(requireContext(), view).apply {
            menuInflater.inflate(R.menu.menu_workout_exercise_manager, this.menu)
            gravity = Gravity.END
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_replace -> navigateToExerciseChooserFragment()
                    else -> deleteExercise(id)
                }
                true
            }
        }.also {
            it.show()
        }
    }

    private fun deleteExercise(id: Int) {
        viewModel.onEvent(NewWorkoutEvent.OnRemoveExerciseClicked(id))
    }

    private fun getSharedElementTransition(): Transition? {
        return TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
    }
}