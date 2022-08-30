package com.carlosdiestro.levelup.bodyweight_progress.ui.bodyweight_update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.carlosdiestro.levelup.bodyweight_progress.ui.models.BodyWeightPLO
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.core.ui.extensions.toTrimmedString
import com.carlosdiestro.levelup.databinding.DialogUpdateBodyWeightBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateBodyWeightDialog(
    private val item: BodyWeightPLO,
    private val onSaveClicked: (BodyWeightPLO) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: DialogUpdateBodyWeightBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<UpdateBodyWeightViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogUpdateBodyWeightBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
        setUpViews()
        collectUIState()
        collectCallbacks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpClickListeners() {
        binding.apply {
            btnSave.setOnClickListener { saveUpdatedBodyWeight() }
            btnCancel.setOnClickListener { dismiss() }
        }
    }

    private fun setUpViews() {
        binding.etUpdateBodyWeight.setText(item.weight.toString())
    }

    private fun collectUIState() {
        launchAndCollect(viewModel.state) {
            handleBodyWeightForm(it)
        }
    }

    private fun collectCallbacks() {
        launchAndCollect(viewModel.callbackChannel) {
            onSaveClicked(
                item.copy(
                    weight = it.toDouble(),
                    date = item.date,
                )
            )
            dismiss()
        }
    }

    private fun saveUpdatedBodyWeight() {
        val newBodyWeightText = binding.etUpdateBodyWeight.text.toTrimmedString()
        viewModel.onEvent(
            UpdateBodyWeightContract.UpdateBodyWeightEvent.Save(newBodyWeightText)
        )
    }

    private fun handleBodyWeightForm(response: UpdateBodyWeightContract.UpdateBodyWeightState) {
        binding.apply {
            etUpdateBodyWeight.setText(response.weight)
            ilUpdateBodyWeight.error = response.weightError?.let { getString(it.resId) }
        }
    }

    companion object {
        const val TAG = "UpdateBodyWeightDialog"
    }
}