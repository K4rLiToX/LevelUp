package com.carlosdiestro.levelup.workouts.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carlosdiestro.levelup.core.ui.resources.StringResource
import com.carlosdiestro.levelup.core.ui.resources.toText
import com.carlosdiestro.levelup.databinding.ItemWorkoutBinding
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutPLO

class WorkoutAdapter(
    private val clickWorkout: (Int, String) -> Unit,
    private val openMenu: (Int, View) -> Unit
) : ListAdapter<WorkoutPLO, WorkoutAdapter.ViewHolder>(WorkoutPLO.WorkoutDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWorkoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(
            binding,
            {
                clickWorkout(getItem(it).id, getItem(it).name)
            },
            { pos, view ->
                openMenu(getItem(pos).id, view)
            }
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: ItemWorkoutBinding,
        private val clickWorkout: (Int) -> Unit,
        private val openMenu: (Int, View) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    clickWorkout(bindingAdapterPosition)
                }
                btnMore.setOnClickListener {
                    openMenu(bindingAdapterPosition, it)
                }
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