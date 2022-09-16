package com.carlosdiestro.levelup.workouts.ui.workout_add

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosdiestro.levelup.MainActivity
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.core.ui.extensions.showWarningDialog
import com.carlosdiestro.levelup.core.ui.extensions.toTrimmedString
import com.carlosdiestro.levelup.core.ui.extensions.visible
import com.carlosdiestro.levelup.core.ui.managers.viewBinding
import com.carlosdiestro.levelup.core.ui.resources.StringResource
import com.carlosdiestro.levelup.core.ui.resources.toText
import com.carlosdiestro.levelup.databinding.FragmentNewWorkoutBinding
import com.carlosdiestro.levelup.exercise_library.ui.models.ExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutSetPLO
import com.google.android.material.button.MaterialButton
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewWorkoutFragment : Fragment(R.layout.fragment_new_workout), MenuProvider {

    private val binding by viewBinding(FragmentNewWorkoutBinding::bind)
    private val viewModel by viewModels<NewWorkoutViewModel>()
    private lateinit var recyclerAdapter: WorkoutExerciseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)

        setFragmentResultListener(
            ExerciseChooserFragment.ITEM_CLICKED_KEY
        ) { requestKey, bundle ->
            val result = bundle.getParcelable<ExercisePLO>(requestKey)
            result?.let {
                val isReplaceModeEnabled = viewModel.isReplaceModeEnabled()
                if (isReplaceModeEnabled) viewModel.onEvent(NewWorkoutEvent.ReplaceExercise(it))
                else viewModel.onEvent(NewWorkoutEvent.OnExerciseClicked(it))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMenu()
        setUpClickListeners()
        setUpRecyclerAdapter()
        setUpRecyclerView()
        collectUIState()
        collectChannelEvents()
    }

    override fun onPrepareMenu(menu: Menu) {
        ((menu.findItem(R.id.action_save).actionView) as MaterialButton).text =
            StringResource.Save.toText(requireContext())
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_save, menu)
        (menu.findItem(R.id.action_save).actionView as MaterialButton).setOnClickListener { submitNewWorkout() }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_save -> {
                submitNewWorkout()
                true
            }
            else -> false
        }
    }

    private fun setUpMenu() {
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setUpClickListeners() {
        binding.btnAddExercise.setOnClickListener { navigateToExerciseChooserFragment() }
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
                    showWarningDialog(it.resId)
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
        (requireActivity() as MainActivity).supportActionBar?.title =
            title.toText(requireContext()).uppercase()
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
                    R.id.action_replace -> {
                        viewModel.onEvent(NewWorkoutEvent.EnableReplaceMode(id))
                        navigateToExerciseChooserFragment()
                    }
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
}