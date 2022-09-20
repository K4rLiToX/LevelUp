package com.carlosdiestro.levelup.workouts.ui.workout_add

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.core.ui.extensions.setUp
import com.carlosdiestro.levelup.core.ui.extensions.visible
import com.carlosdiestro.levelup.core.ui.managers.viewBinding
import com.carlosdiestro.levelup.databinding.FragmentExerciseChooserBinding
import com.carlosdiestro.levelup.exercise_library.domain.models.ExerciseCategory
import com.carlosdiestro.levelup.exercise_library.ui.exercise_list.ExerciseAdapter
import com.carlosdiestro.levelup.exercise_library.ui.models.ExercisePLO
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExerciseChooserFragment : Fragment(R.layout.fragment_exercise_chooser) {

    private val binding by viewBinding(FragmentExerciseChooserBinding::bind)
    private val viewModel by viewModels<ExerciseChooserViewModel>()
    private val recyclerAdapter: ExerciseAdapter by lazy {
        ExerciseAdapter {
            setFragmentResult(
                ITEM_CLICKED_KEY,
                bundleOf(ITEM_CLICKED_KEY to it)
            )
            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
        setUpRecyclerView()
        collectUIState()
    }

    private fun setUpClickListeners() {
        binding.apply {
            cgCategories.setOnCheckedStateChangeListener { _, checkedIds ->
                val category =
                    if (checkedIds.isNotEmpty()) getExerciseCategory(checkedIds[0]) else ExerciseCategory.ALL
                viewModel.onEvent(ExerciseChooserEvent.UpdateFilter(category))
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.setUp(recyclerAdapter)
    }

    private fun collectUIState() {
        launchAndCollect(viewModel.state) {
            handleNoData(it.noData)
            handleExerciseList(it.exercises)
        }
    }

    private fun handleNoData(noData: Boolean) {
        binding.lNoData.root.visible(noData)
    }

    private fun handleExerciseList(list: List<ExercisePLO>) {
        recyclerAdapter.submitList(list)
    }

    private fun getExerciseCategory(id: Int): ExerciseCategory = when (id) {
        R.id.cPush -> ExerciseCategory.PUSH
        R.id.cPull -> ExerciseCategory.PULL
        R.id.cLegs -> ExerciseCategory.LEGS
        else -> ExerciseCategory.CORE
    }

    companion object {
        const val ITEM_CLICKED_KEY = "item_clicked_key"
    }
}