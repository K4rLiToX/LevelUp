package com.carlosdiestro.levelup.bodyweight_progress.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosdiestro.levelup.core.ui.*
import com.carlosdiestro.levelup.databinding.FragmentBodyWeightProgressBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BodyWeightProgressFragment : Fragment() {

    private var _binding: FragmentBodyWeightProgressBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<BodyWeightProgressViewModel>()

    private val recyclerAdapter: BodyWeightAdapter by lazy { BodyWeightAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBodyWeightProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
        setUpRecyclerView()
        collectUIState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
            ilNewWeight.error = response.weightError?.let { getString(it.resId) }
        }
    }

    private fun submitNewWeight() {
        val newBodyWeightText = binding.etNewWeight.text.toTrimmedString()
        viewModel.onEvent(
            BodyWeightProgressContract.BodyWeightProgressEvent.Submit(
                newBodyWeightText
            )
        )
    }
}