package com.carlosdiestro.levelup.bodyweight_progress.ui.bodyweight_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.bodyweight_progress.ui.bodyweight_update.UpdateBodyWeightDialog
import com.carlosdiestro.levelup.bodyweight_progress.ui.models.BodyWeightPLO
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.core.ui.extensions.toTrimmedString
import com.carlosdiestro.levelup.core.ui.extensions.visible
import com.carlosdiestro.levelup.core.ui.managers.viewBinding
import com.carlosdiestro.levelup.core.ui.resources.toText
import com.carlosdiestro.levelup.databinding.FragmentBodyWeightProgressBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BodyWeightProgressFragment : Fragment(R.layout.fragment_body_weight_progress) {

    private val binding by viewBinding(FragmentBodyWeightProgressBinding::bind)
    private val viewModel by viewModels<BodyWeightProgressViewModel>()
    private val recyclerAdapter: BodyWeightAdapter by lazy {
        BodyWeightAdapter {
            openWeightDialogEditor(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
        setUpRecyclerView()
        collectUIState()
    }

    private fun setUpClickListeners() {
        binding.btnNoteDown.setOnClickListener { submitNewWeight() }
    }

    private fun setUpRecyclerView() {
        binding.rvBodyWeight.apply {
            layoutManager = LinearLayoutManager(
                this@BodyWeightProgressFragment.requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = recyclerAdapter
        }
    }

    private fun collectUIState() {
        launchAndCollect(viewModel.state) {
            handleNoData(it.noData)
            handleBodyWeightList(it.bodyWeightList)
            handleBodyWeightForm(it.bodyWeightFormState)
        }
    }

    private fun handleNoData(noData: Boolean) {
        binding.lNoData.root.visible(noData)
    }

    private fun handleBodyWeightList(list: List<BodyWeightPLO>) {
        recyclerAdapter.submitList(list)
    }

    private fun handleBodyWeightForm(response: BodyWeightProgressContract.BodyWeightFormState) {
        binding.apply {
            etNewWeight.setText(response.weight)
            ilNewWeight.error = response.weightError?.toText(requireContext())
        }
    }

    private fun submitNewWeight() {
        val newBodyWeightText = binding.etNewWeight.text.toTrimmedString()
        viewModel.onEvent(
            BodyWeightProgressContract.BodyWeightProgressEvent.NoteDown(
                newBodyWeightText
            )
        )
    }

    private fun openWeightDialogEditor(item: BodyWeightPLO) {
        UpdateBodyWeightDialog(item) {
            viewModel.onEvent(BodyWeightProgressContract.BodyWeightProgressEvent.Update(it))
        }.show(requireActivity().supportFragmentManager, UpdateBodyWeightDialog.TAG)
    }
}