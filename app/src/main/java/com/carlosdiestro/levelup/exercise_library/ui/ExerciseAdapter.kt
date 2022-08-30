package com.carlosdiestro.levelup.exercise_library.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carlosdiestro.levelup.core.ui.extensions.visible
import com.carlosdiestro.levelup.databinding.ItemExerciseBinding

class ExerciseAdapter(
    private val isSelectionMode: Boolean = false,
    private val onItemClicked: ((ExercisePLO) -> Unit)? = null
) :
    ListAdapter<ExercisePLO, ExerciseAdapter.ViewHolder>(ExercisePLO.ExerciseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(isSelectionMode, binding) {
            onItemClicked?.let { block -> block(getItem(it)) }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val isSelectionMode: Boolean,
        private val binding: ItemExerciseBinding,
        private val onItemClicked: ((Int) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (isSelectionMode) {
                    onItemClicked?.let { block ->
                        block(bindingAdapterPosition)
                        binding.root.isChecked = !getItem(bindingAdapterPosition).isSelected
                    }
                }
            }
        }

        fun bind(item: ExercisePLO) {
            binding.apply {
                tvName.text = item.name
                ivUnilateral.visible(item.isUnilateral)
                root.isChecked = item.isSelected
            }
        }
    }
}