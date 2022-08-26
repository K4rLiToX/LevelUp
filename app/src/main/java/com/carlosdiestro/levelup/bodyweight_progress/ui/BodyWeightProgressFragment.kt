package com.carlosdiestro.levelup.bodyweight_progress.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosdiestro.levelup.core.ui.gone
import com.carlosdiestro.levelup.core.ui.launchAndCollect
import com.carlosdiestro.levelup.core.ui.visible
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
        binding.btnNoteDown.setOnClickListener { openDialog() }
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
            handleData(it.weightList)
        }
    }

    private fun handleData(list: List<BodyWeightPLO>) {
        if (list.isNotEmpty()) {
            recyclerAdapter.submitList(list)
            binding.lNoData.root.gone()
        } else {
            binding.lNoData.root.visible()
        }
    }

    private fun openDialog() = Unit
}