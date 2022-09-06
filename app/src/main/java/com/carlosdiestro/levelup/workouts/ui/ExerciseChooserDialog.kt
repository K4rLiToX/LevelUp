package com.carlosdiestro.levelup.workouts.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.core.ui.extensions.visible
import com.carlosdiestro.levelup.databinding.DialogExerciseChooserBinding
import com.carlosdiestro.levelup.exercise_library.domain.models.ExerciseCategory
import com.carlosdiestro.levelup.exercise_library.ui.exercise_list.ExerciseAdapter
import com.carlosdiestro.levelup.exercise_library.ui.models.ExercisePLO
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExerciseChooserDialog(
    private val onItemClicked: (ExercisePLO) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: DialogExerciseChooserBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ExerciseChooserViewModel>()
    private lateinit var recyclerAdapter: ExerciseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogExerciseChooserBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
        setUpRecyclerAdapter()
        setUpRecyclerView()
        collectUIState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpClickListeners() {
        binding.apply {
            binding.btnCancel.setOnClickListener { dismiss() }
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
            onItemClicked(it)
            dismiss()
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
        const val TAG = "ExerciseChooserDialog"
    }
}