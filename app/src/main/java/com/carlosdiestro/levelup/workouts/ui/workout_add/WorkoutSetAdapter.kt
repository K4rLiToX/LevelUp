package com.carlosdiestro.levelup.workouts.ui.workout_add

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carlosdiestro.levelup.core.ui.resources.StringResource
import com.carlosdiestro.levelup.core.ui.resources.toText
import com.carlosdiestro.levelup.databinding.ItemSetBinding
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutSetPLO

class WorkoutSetAdapter(
    private val deleteSet: (WorkoutSetPLO) -> Unit,
    private val updateSet: (WorkoutSetPLO) -> Unit
) : ListAdapter<WorkoutSetPLO, WorkoutSetAdapter.ViewHolder>(WorkoutSetPLO.WorkoutSetDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(
            binding,
            { deleteSet(getItem(it)) },
            { updateSet(getItem(it)) }
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: ItemSetBinding,
        private val deleteSet: (Int) -> Unit,
        private val updateSet: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                btnRemoveSet.setOnClickListener {
                    deleteSet(bindingAdapterPosition)
                }
                root.setOnClickListener {
                    updateSet(bindingAdapterPosition)
                }
            }
        }

        fun bind(item: WorkoutSetPLO) {
            binding.apply {
                tvSetNumber.text = StringResource.OneVariablePlaceHolder.toText(
                    binding.root.context,
                    (bindingAdapterPosition + 1).toString()
                )
                tvRepRange.text = StringResource.RepRange.toText(
                    root.context,
                    item.repRange.lower.toString(),
                    item.repRange.upper.toString()
                )
            }
        }
    }
}