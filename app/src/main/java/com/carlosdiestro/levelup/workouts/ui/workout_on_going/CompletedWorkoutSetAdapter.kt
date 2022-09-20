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
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutSetPLO

class CompletedWorkoutSetAdapter(
    private val updateSet: (Double, Int, Int, CompletedWorkoutSetPLO) -> Unit,
    private val showWarningDialog: () -> Unit
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
            { weight, reps, partials, pos ->
                updateSet(weight, reps, partials, getItem(pos))
            },
            { showWarningDialog() }
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: ItemCompletedWorkoutSetBinding,
        private val updateSet: (Double, Int, Int, Int) -> Unit,
        private val showWarningDialog: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnCompleteSet.setOnClickListener { finishWorkout() }
            binding.tvSetHeader.setOnClickListener {
                bindVisibilities(!binding.dividerHeader.isVisible)
            }
        }

        private fun finishWorkout() {
            val weight = binding.etWeight.text.toTrimmedString()
            val reps = binding.etReps.text.toTrimmedString()
            val partials = binding.etPartials.text.toTrimmedString()

            if (allInputsCorrect(weight, reps, partials)) {
                updateSet(
                    weight.toDouble(),
                    reps.toInt(),
                    partials.toInt(),
                    bindingAdapterPosition
                )
                bindVisibilities(true)
            } else showWarningDialog()
        }

        fun bind(item: CompletedWorkoutSetPLO) {
            bindVisibilities(item.isCompleted)
            bindTexts(item)
        }

        private fun bindTexts(item: CompletedWorkoutSetPLO) {
            binding.apply {
                tvSetHeader.text =
                    StringResource.Set.toText(binding.root.context, item.setOrder.toString())
                tvRepRange.text = StringResource.RepRangeTitle.toText(
                    binding.root.context,
                    item.repRange.lower.toString(),
                    item.repRange.upper.toString()
                )
                tvLastWeight.text =
                    StringResource.Kg.toText(binding.root.context, item.lastWeight.rightWeight)
                tvLastReps.text = StringResource.Reps.toText(
                    binding.root.context,
                    item.lastReps.rightReps,
                    item.lastReps.rightPartialReps
                )

                etWeight.setText(item.currentWeight.rightWeight.toString())
                etReps.setText(item.currentReps.rightReps.toString())
                etPartials.setText(item.currentReps.rightPartialReps.toString())
            }
        }

        private fun bindVisibilities(isGone: Boolean) {
            binding.apply {
                dividerHeader.gone(isGone)
                tvWeight.gone(isGone)
                ilWeight.gone(isGone)
                tvRepetitions.gone(isGone)
                ilReps.gone(isGone)
                tvPlus.gone(isGone)
                ilPartials.gone(isGone)
                dividerCurrent.gone(isGone)
                tvPreviousHeader.gone(isGone)
                tvLastReps.gone(isGone)
                tvLastWeight.gone(isGone)
                tvLastReps.gone(isGone)
                btnCompleteSet.gone(isGone)
            }
        }

        private fun allInputsCorrect(weight: String, reps: String, partials: String): Boolean =
            weight.isNotBlank() && reps.isNotBlank() && partials.isNotBlank() && weight != "0" && reps != "0"
    }
}