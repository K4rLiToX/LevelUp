package com.carlosdiestro.levelup.workouts.ui.workout_add

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.core.ui.extensions.toTrimmedString
import com.carlosdiestro.levelup.core.ui.resources.StringResource
import com.carlosdiestro.levelup.databinding.DialogAddSetBinding
import com.carlosdiestro.levelup.workouts.domain.models.RepRange
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddSetDialog(
    private val repRange: RepRange? = null,
    private val onPositiveClicked: (RepRange) -> Unit
) : DialogFragment(R.layout.dialog_add_set) {

    private var _binding: DialogAddSetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAddSetBinding.inflate(layoutInflater)
        if (repRange != null) binding.apply {
            etLowerRange.setText(repRange.lower.toString())
            etUpperRange.setText(repRange.upper.toString())
        }
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("New Set")
            .setView(binding.root)
            .setPositiveButton(StringResource.Add.resId) { _, _ ->
                getRepRange()
                dismiss()
            }
            .setNegativeButton(StringResource.Cancel.resId) { _, _ -> dismiss() }
            .create()
    }

    private fun getRepRange() {
        binding.apply {
            val lower = etLowerRange.text.toTrimmedString()
            val upper = etUpperRange.text.toTrimmedString()

            if (lower != repRange?.lower.toString() || upper != repRange?.upper.toString())
                onPositiveClicked(
                    RepRange(lower.toInt(), upper.toInt())
                )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "AddSetDialog"
    }
}