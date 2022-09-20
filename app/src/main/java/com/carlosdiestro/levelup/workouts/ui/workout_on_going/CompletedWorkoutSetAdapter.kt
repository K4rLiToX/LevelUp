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
import com.carlosdiestro.levelup.databinding.ItemCompletedWorkoutSetBinding
import com.carlosdiestro.levelup.workouts.domain.models.RepRange
import com.carlosdiestro.levelup.workouts.domain.models.Repetition
import com.carlosdiestro.levelup.workouts.domain.models.Weight
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutSetPLO

class CompletedWorkoutSetAdapter(
    private val isUnilateral: Boolean,
    private val updateSet: (Double, Double, Int, Int, Int, Int, CompletedWorkoutSetPLO) -> Unit,
    private val openDialog: () -> Unit
) :
    ListAdapter<CompletedWorkoutSetPLO, CompletedWorkoutSetAdapter.ViewHolder>(
        CompletedWorkoutSetPLO.CompletedWorkoutSetDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCompletedWorkoutSetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(
            binding,
            isUnilateral,
            { rw, lw, rr, lr, rp, lp, pos ->
                updateSet(rw, lw, rr, lr, rp, lp, getItem(pos))
            },
            { openDialog() }
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: ItemCompletedWorkoutSetBinding,
        private val isUnilateral: Boolean,
        private val updateSet: (Double, Double, Int, Int, Int, Int, Int) -> Unit,
        private val openDialog: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                binding.apply {
                    btnCompleteSet.setOnClickListener { completeSet() }
                    tvSetHeader.setOnClickListener {
                        bindVisibilities(!binding.dividerHeader.isVisible)
                    }
                }
            }
        }

        private fun completeSet() {
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
                openDialog()
            }
        }

        fun bind(item: CompletedWorkoutSetPLO) {
            bindVisibilities(item.isCompleted)
            bindViews(item)
        }

        private fun bindVisibilities(isGone: Boolean) {
            binding.apply {
                dividerHeader.gone(isGone)
                tvWeight.gone(isGone)
                ilWeightLeft.gone(isGone)
                tvRepetitionsRight.gone(isGone)
                ilRepsRight.gone(isGone)
                tvPlusRight.gone(isGone)
                ilPartialsRight.gone(isGone)
                dividerCurrent.gone(isGone)
                tvPreviousHeader.gone(isGone)
                tvLastReps.gone(isGone)
                tvLastWeight.gone(isGone)
                btnCompleteSet.gone(isGone)

                ilWeightRight.gone(isGone || !isUnilateral)
                tvRepetitionsLeft.gone(isGone || !isUnilateral)
                ilRepsLeft.gone(isGone || !isUnilateral)
                tvPlusLeft.gone(isGone || !isUnilateral)
                ilPartialsLeft.gone(isGone || !isUnilateral)
            }
        }

        private fun bindViews(item: CompletedWorkoutSetPLO) {
            bindCommonViews(item.setOrder, item.repRange)
            bindLastWeights(item.lastWeight)
            bindLastReps(item.lastReps)
            bindRepetitionTitle()

            binding.apply {
                etWeightRight.setText(item.currentWeight.rightWeight.toString())
                etWeightLeft.setText(item.currentWeight.leftWeight.toString())
                etRepsRight.setText(item.currentReps.rightReps.toString())
                etRepsLeft.setText(item.currentReps.leftReps.toString())
                etPartialsRight.setText(item.currentReps.rightPartialReps.toString())
                etPartialsLeft.setText(item.currentReps.leftPartialReps.toString())
            }
        }

        private fun bindCommonViews(setOrder: Int, repRange: RepRange) {
            binding.apply {
                tvSetHeader.text =
                    StringResource.Set.toText(binding.root.context, setOrder.toString())
                tvRepRange.text = StringResource.RepRangeTitle.toText(
                    binding.root.context,
                    repRange.lower.toString(),
                    repRange.upper.toString()
                )
            }
        }

        private fun bindLastWeights(weights: Weight) {
            binding.apply {
                if (isUnilateral) {
                    tvLastWeight.text = StringResource.KgUnilateral.toText(
                        binding.root.context,
                        weights.rightWeight,
                        weights.leftWeight
                    )
                } else {
                    tvLastWeight.text =
                        StringResource.Kg.toText(binding.root.context, weights.leftWeight)
                }
            }
        }

        private fun bindLastReps(reps: Repetition) {
            binding.apply {
                if (isUnilateral) {
                    tvLastReps.text = StringResource.RepsUnilateral.toText(
                        binding.root.context,
                        reps.rightReps.toString(),
                        reps.rightPartialReps.toString(),
                        reps.leftReps.toString(),
                        reps.leftPartialReps.toString()
                    )
                } else {
                    tvLastReps.text = StringResource.Reps.toText(
                        binding.root.context,
                        reps.rightReps,
                        reps.rightPartialReps
                    )
                }
            }
        }

        private fun bindRepetitionTitle() {
            binding.tvRepetitionsRight.text =
                if (isUnilateral) StringResource.RightRepsTitle.toText(binding.root.context) else StringResource.RepsTitle.toText(
                    binding.root.context
                )
        }

        private fun allInputsCorrect(
            rw: String,
            lw: String,
            rr: String,
            lr: String,
            rp: String,
            lp: String
        ): Boolean {
            return if (isUnilateral) rw.isNotBlank() && lw.isNotBlank() &&
                    rr.isNotBlank() && lr.isNotBlank() &&
                    rp.isNotBlank() && lp.isNotBlank() &&
                    rw != "0" && lw != "0" &&
                    rr != "0" && lr != "0"
            else lw.isNotBlank() && rr.isNotBlank() && rp.isNotBlank() && lw != "0" && rr != "0"
        }

    }
}