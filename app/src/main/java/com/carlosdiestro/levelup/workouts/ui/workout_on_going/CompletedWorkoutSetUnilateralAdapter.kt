package com.carlosdiestro.levelup.workouts.ui.workout_on_going

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carlosdiestro.levelup.core.ui.extensions.gone
import com.carlosdiestro.levelup.core.ui.extensions.toTrimmedString
import com.carlosdiestro.levelup.core.ui.resources.StringResource
import com.carlosdiestro.levelup.core.ui.resources.toText
import com.carlosdiestro.levelup.databinding.ItemCompletedWorkoutSetUnilateralBinding
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutSetPLO

class CompletedWorkoutSetUnilateralAdapter(
    private val updateSet: (Double, Double, Int, Int, Int, Int, CompletedWorkoutSetPLO) -> Unit,
    private val showWarningDialog: () -> Unit
) :
    ListAdapter<CompletedWorkoutSetPLO, CompletedWorkoutSetUnilateralAdapter.ViewHolder>(
        CompletedWorkoutSetPLO.CompletedWorkoutSetDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCompletedWorkoutSetUnilateralBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(
            binding,
            { rw, lw, rr, lr, rp, lp, pos ->
                updateSet(rw, lw, rr, lr, rp, lp, getItem(pos))
            },
            {
                showWarningDialog()
            }
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: ItemCompletedWorkoutSetUnilateralBinding,
        private val updateSet: (Double, Double, Int, Int, Int, Int, Int) -> Unit,
        private val showWarningDialog: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnCompleteSet.setOnClickListener {
                val rightWeight = binding.etWeightRight.text.toTrimmedString()
                val leftWeight = binding.etWeightLeft.text.toTrimmedString()
                val rightReps = binding.etRepsRight.text.toTrimmedString()
                val leftReps = binding.etRepsLeft.text.toTrimmedString()
                val rightPartials = binding.etPartialsRight.text.toTrimmedString()
                val leftPartials = binding.etPartialsLeft.text.toTrimmedString()

                if (allInputsCorrect(
                        rightWeight,
                        leftWeight,
                        rightReps,
                        leftReps,
                        rightPartials,
                        leftPartials
                    )
                ) {
                    updateSet(
                        rightWeight.toDouble(),
                        leftWeight.toDouble(),
                        rightReps.toInt(),
                        leftReps.toInt(),
                        rightPartials.toInt(),
                        leftPartials.toInt(),
                        bindingAdapterPosition
                    )
                    bindVisibilities(true)
                } else {
                    showWarningDialog()
                }
            }
            binding.tvSetHeader.setOnClickListener {
                if (!binding.dividerHeader.isVisible) bindVisibilities(false)
                else bindVisibilities(true)
            }
        }

        fun bind(item: CompletedWorkoutSetPLO) {
            if (item.isCompleted) bindVisibilities(true)
            binding.apply {
                tvSetHeader.text =
                    StringResource.Set.toText(binding.root.context, item.setOrder.toString())
                tvRepRange.text = StringResource.RepRangeTitle.toText(
                    binding.root.context,
                    item.repRange.lower.toString(),
                    item.repRange.upper.toString()
                )
                tvLastWeight.text = StringResource.KgUnilateral.toText(
                    binding.root.context,
                    item.lastWeight.rightWeight,
                    item.lastWeight.leftWeight
                )
                tvLastReps.text = StringResource.RepsUnilateral.toText(
                    binding.root.context,
                    item.lastReps.rightReps.toString(),
                    item.lastReps.rightPartialReps.toString(),
                    item.lastReps.leftReps.toString(),
                    item.lastReps.leftPartialReps.toString()
                )
                etWeightRight.setText(item.currentWeight.rightWeight.toString())
                etWeightLeft.setText(item.currentWeight.leftWeight.toString())
                etRepsRight.setText(item.currentReps.rightReps.toString())
                etRepsLeft.setText(item.currentReps.leftReps.toString())
                etPartialsRight.setText(item.currentReps.rightPartialReps.toString())
                etPartialsLeft.setText(item.currentReps.leftPartialReps.toString())
            }
        }

        private fun bindVisibilities(isGone: Boolean) {
            binding.apply {
                dividerHeader.gone(isGone)
                tvWeight.gone(isGone)
                ilWeightRight.gone(isGone)
                ilWeightLeft.gone(isGone)
                tvRepetitionsRight.gone(isGone)
                ilRepsRight.gone(isGone)
                tvPlusRight.gone(isGone)
                ilPartialsRight.gone(isGone)
                tvRepetitionsLeft.gone(isGone)
                ilRepsLeft.gone(isGone)
                tvPlusLeft.gone(isGone)
                ilPartialsLeft.gone(isGone)
                dividerCurrent.gone(isGone)
                tvPreviousHeader.gone(isGone)
                tvLastReps.gone(isGone)
                tvLastWeight.gone(isGone)
                btnCompleteSet.gone(isGone)
            }
        }

        private fun allInputsCorrect(
            rw: String,
            lw: String,
            rr: String,
            lr: String,
            rp: String,
            lp: String
        ): Boolean =
            rw.isNotBlank() && lw.isNotBlank() &&
                    rr.isNotBlank() && lr.isNotBlank() &&
                    rp.isNotBlank() && lp.isNotBlank() &&
                    rw != "0" && lw != "0" &&
                    rr != "0" && lr != "0"
    }
}