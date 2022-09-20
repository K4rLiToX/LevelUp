package com.carlosdiestro.levelup.exercise_library.ui.exercise_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.core.ui.extensions.setUp
import com.carlosdiestro.levelup.core.ui.extensions.visible
import com.carlosdiestro.levelup.core.ui.managers.viewBinding
import com.carlosdiestro.levelup.databinding.FragmentExerciseCategoryBinding
import com.carlosdiestro.levelup.exercise_library.ui.models.ExercisePLO
import com.carlosdiestro.levelup.workouts.ui.workout_add.ExerciseChooserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExerciseCategoryFragment : Fragment(R.layout.fragment_exercise_category) {

    private val binding by viewBinding(FragmentExerciseCategoryBinding::bind)
    private val viewModel by viewModels<ExerciseCategoryViewModel>()
    private val exerciseChooserViewModel: ExerciseChooserViewModel by viewModels(ownerProducer = { requireParentFragment() })
    private val recyclerAdapter: ExerciseAdapter by lazy {
        ExerciseAdapter {
            if (viewModel.isSelectionModeEnable) {
                exerciseChooserViewModel.setExercise(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        collectUIState()
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

    companion object {
        const val EXERCISE_CATEGORY_KEY = "exercise_category_key"
        const val IS_SELECTION_MODE_KEY = "is_selection_mode_key"
    }
}