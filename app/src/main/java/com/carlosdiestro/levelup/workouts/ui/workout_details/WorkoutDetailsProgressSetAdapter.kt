package com.carlosdiestro.levelup.workouts.ui.workout_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carlosdiestro.levelup.core.ui.resources.StringResource
import com.carlosdiestro.levelup.core.ui.resources.toText
import com.carlosdiestro.levelup.databinding.ItemCompletedWorkoutSetProgressBinding
import com.carlosdiestro.levelup.workouts.domain.models.Repetition
import com.carlosdiestro.levelup.workouts.domain.models.Weight
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutSetPLO

class WorkoutDetailsProgressSetAdapter :
    ListAdapter<CompletedWorkoutSetPLO, WorkoutDetailsProgressSetAdapter.ViewHolder>(
        CompletedWorkoutSetPLO.CompletedWorkoutSetDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCompletedWorkoutSetProgressBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: ItemCompletedWorkoutSetProgressBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CompletedWorkoutSetPLO) {
            binding.apply {
                tvSetHeader.text = StringResource.Set.toText(binding.root.context, item.setOrder)
                bindWeight(item.lastWeight)
                bindReps(item.lastReps)
            }
        }

        private fun bindWeight(weight: Weight) {
            val (rw, lw) = weight
            binding.tvWeight.text =
                if (lw == 0.0) StringResource.Kg.toText(binding.root.context, rw.toString())
                else StringResource.KgUnilateral.toText(
                    binding.root.context,
                    rw.toString(),
                    lw.toString()
                )
        }

        private fun bindReps(reps: Repetition) {
            val (rr, lr, rp, lp) = reps
            binding.tvReps.text = if (lr == 0) StringResource.Reps.toText(
                binding.root.context,
                rr.toString(),
                rp.toString()
            )
            else StringResource.RepsUnilateral.toText(
                binding.root.context,
                rr.toString(),
                rp.toString(),
                lr.toString(),
                lp.toString()
            )
        }
    }
}