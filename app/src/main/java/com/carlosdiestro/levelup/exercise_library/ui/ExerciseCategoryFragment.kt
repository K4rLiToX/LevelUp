package com.carlosdiestro.levelup.exercise_library.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.core.ui.extensions.visible
import com.carlosdiestro.levelup.core.ui.managers.viewBinding
import com.carlosdiestro.levelup.databinding.FragmentExerciseCategoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExerciseCategoryFragment : Fragment(R.layout.fragment_exercise_category) {

    private val binding by viewBinding(FragmentExerciseCategoryBinding::bind)
    private val viewModel by viewModels<ExerciseCategoryViewModel>()
    private val recyclerAdapter: ExerciseAdapter by lazy {
        ExerciseAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectArguments()
        setUpRecyclerView()
        collectUIState()
        collectExercises()
    }

    private fun collectArguments() {
        arguments?.takeIf { it.containsKey(EXERCISE_CATEGORY_KEY) }?.apply {
            viewModel.setExerciseCategory(getInt(EXERCISE_CATEGORY_KEY))
        }
    }

    private fun setUpRecyclerView() {
        binding.rvExerciseCategory.apply {
            layoutManager = LinearLayoutManager(
                this@ExerciseCategoryFragment.requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = recyclerAdapter
        }
    }

    private fun collectUIState() {
        launchAndCollect(viewModel.state) {
            handleNoData(it.noData)
            handleExerciseList(it.exerciseList)
        }
    }

    private fun handleNoData(noData: Boolean) {
        binding.lNoData.root.visible(noData)
    }

    private fun handleExerciseList(list: List<ExercisePLO>) {
        recyclerAdapter.submitList(list)
    }

    private fun collectExercises() {
        viewModel.getExercises()
    }

    companion object {
        const val EXERCISE_CATEGORY_KEY = "exercise_category_key"
    }
}