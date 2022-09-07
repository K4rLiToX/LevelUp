package com.carlosdiestro.levelup.workouts.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
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
    private lateinit var recyclerAdapter: ExerciseAdapter

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
            cgCategories.setOnCheckedStateChangeListener { _, checkedIds ->
                if (checkedIds.isNotEmpty()) viewModel.onEvent(
                    ExerciseChooserEvent.OnFilterChanged(
                        getExerciseCategory(checkedIds[0])
                    )
                )
                else viewModel.onEvent(ExerciseChooserEvent.OnFilterChanged(ExerciseCategory.ALL))
            }
        }
    }

    private fun setUpRecyclerAdapter() {
        recyclerAdapter = ExerciseAdapter(true) {
            setFragmentResult(
                ITEM_CLICKED_KEY,
                Bundle().apply { putParcelable(ITEM_CLICKED_KEY, it) }
            )
            findNavController().popBackStack()
        }
    }

    private fun setUpRecyclerView() {
        binding.rvExercises.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
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