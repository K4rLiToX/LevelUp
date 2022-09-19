package com.carlosdiestro.levelup.exercise_library.ui.exercise_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.core.ui.extensions.verticalLayoutManger
import com.carlosdiestro.levelup.core.ui.extensions.visible
import com.carlosdiestro.levelup.core.ui.managers.viewBinding
import com.carlosdiestro.levelup.databinding.FragmentExerciseCategoryBinding
import com.carlosdiestro.levelup.exercise_library.ui.models.ExercisePLO
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExerciseCategoryFragment : Fragment(R.layout.fragment_exercise_category) {

    private val binding by viewBinding(FragmentExerciseCategoryBinding::bind)
    private val viewModel by viewModels<ExerciseCategoryViewModel>()
    private lateinit var recyclerAdapter: ExerciseAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        collectUIState()
    }

    private fun setUpRecyclerView() {
        recyclerAdapter = ExerciseAdapter()
        binding.recyclerView.apply {
            verticalLayoutManger(requireContext())
            adapter = recyclerAdapter
        }
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
    }
}