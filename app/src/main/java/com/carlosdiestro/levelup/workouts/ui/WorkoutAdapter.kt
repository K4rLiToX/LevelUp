package com.carlosdiestro.levelup.workouts.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carlosdiestro.levelup.core.ui.resources.StringResource
import com.carlosdiestro.levelup.core.ui.resources.toText
import com.carlosdiestro.levelup.databinding.ItemWorkoutBinding
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutPLO

class WorkoutAdapter(
    private val onItemClicked: (Int) -> Unit
) : ListAdapter<WorkoutPLO, WorkoutAdapter.ViewHolder>(WorkoutPLO.WorkoutDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWorkoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding) {
            onItemClicked(getItem(it).id)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: ItemWorkoutBinding,
        private val onItemClicked: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClicked(bindingAdapterPosition)
            }
        }

        fun bind(item: WorkoutPLO) {
            binding.apply {
                tvName.text = item.name
                tvNumberOfExercises.text = StringResource.ExerciseWithNumber.toText(
                    binding.root.context,
                    item.numberOfExercises
                )
            }
        }
    }
}