package com.carlosdiestro.levelup.workouts.ui.workout_add

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.core.ui.extensions.*
import com.carlosdiestro.levelup.core.ui.managers.viewBinding
import com.carlosdiestro.levelup.core.ui.resources.StringResource
import com.carlosdiestro.levelup.core.ui.resources.toText
import com.carlosdiestro.levelup.databinding.FragmentNewWorkoutBinding
import com.carlosdiestro.levelup.exercise_library.ui.models.ExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutSetPLO
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
            val result =
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) bundle.getParcelable(
                    requestKey
                ) else bundle.getParcelable(requestKey, ExercisePLO::class.java)
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

    override fun onPrepareMenu(menu: Menu) =
        menu.menuItemAsMaterialButton(
            R.id.action_save,
            StringResource.Save.toText(requireContext())
        ) {
            submitNewWorkout()
        }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) =
        menuInflater.inflate(R.menu.menu_save, menu)

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean = false

    private fun setUpMenu() = setUpMenuProvider(this)

    private fun setUpClickListeners() {
        binding.btnAddExercise.setOnClickListener { navigateToExerciseChooserFragment() }
    }

    private fun setUpRecyclerAdapter() {
        recyclerAdapter = WorkoutExerciseAdapter(
            { sets, pos -> addNewSet(sets, pos) },
            { e, s -> viewModel.onEvent(NewWorkoutEvent.OnSetRemoved(e, s)) },
            { exercise, set -> updateSet(exercise, set) },
            { id, view -> openMoreMenu(id, view) }
        )
    }

    private val addNewSet: (List<WorkoutSetPLO>, Int) -> Unit = { sets, pos ->
        AddSetDialog {
            val newSet = WorkoutSetPLO(
                id = -1,
                setOrder = sets.size + 1,
                repRange = it
            )
            viewModel.onEvent(NewWorkoutEvent.OnNewSetClicked(newSet, pos))
        }.show(requireActivity().supportFragmentManager, AddSetDialog.TAG)
    }

    private val updateSet: (WorkoutExercisePLO, WorkoutSetPLO) -> Unit = { exercise, set ->
        AddSetDialog(set.repRange) {
            val newSet = set.copy(repRange = it)
            viewModel.onEvent(NewWorkoutEvent.OnUpdateSetClicked(exercise, newSet))
        }.show(requireActivity().supportFragmentManager, AddSetDialog.TAG)
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.setUp(recyclerAdapter)
    }

    private fun collectUIState() {
        launchAndCollect(viewModel.state) {
            handleNoData(it.noData)
            handleWorkoutExerciseList(it.exercises)
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

    private fun handleToolbarTitle(title: StringResource) =
        setActionBarTitle(title.toText(requireContext()))

    private fun submitNewWorkout() {
        val workoutName = binding.etName.text.toTrimmedString()
        viewModel.onEvent(NewWorkoutEvent.AddNewWorkout(workoutName))
    }

    private fun navigateToExerciseChooserFragment() {
        findNavController().navigate(NewWorkoutFragmentDirections.toExerciseChooserFragment())
    }

    private fun openMoreMenu(id: Int, view: View) {
        showPopUpMenu(view, R.menu.menu_workout_exercise_manager) {
            when (it.itemId) {
                R.id.action_replace -> {
                    viewModel.onEvent(NewWorkoutEvent.EnableReplaceMode(id))
                    navigateToExerciseChooserFragment()
                }
                else -> viewModel.onEvent(NewWorkoutEvent.OnRemoveExerciseClicked(id))
            }
            true
        }
    }
}