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
    private val onRemoveItemClicked: (WorkoutSetPLO) -> Unit,
    private val onUpdateItemClicked: (WorkoutSetPLO) -> Unit
) : ListAdapter<WorkoutSetPLO, WorkoutSetAdapter.ViewHolder>(WorkoutSetPLO.WorkoutSetDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(
            binding,
            {
                onRemoveItemClicked(getItem(it))
            },
            {
                onUpdateItemClicked(getItem(it))
            }
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: ItemSetBinding,
        private val onRemoveItemClicked: (Int) -> Unit,
        private val onUpdateItemClicked: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                btnRemoveSet.setOnClickListener {
                    onRemoveItemClicked(bindingAdapterPosition)
                }
                root.setOnClickListener {
                    onUpdateItemClicked(bindingAdapterPosition)
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