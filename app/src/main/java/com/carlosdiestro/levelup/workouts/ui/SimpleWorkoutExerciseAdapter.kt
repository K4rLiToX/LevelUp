package com.carlosdiestro.levelup.workouts.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carlosdiestro.levelup.databinding.ItemSimpleWorkoutExerciseBinding
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO

class SimpleWorkoutExerciseAdapter :
    ListAdapter<WorkoutExercisePLO, SimpleWorkoutExerciseAdapter.ViewHolder>(WorkoutExercisePLO.WorkoutExerciseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSimpleWorkoutExerciseBinding.inflate(
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
        private val binding: ItemSimpleWorkoutExerciseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WorkoutExercisePLO) {
            binding.apply {
                tvExerciseName.text = item.name
                tvNumberOfSets.text = "${item.sets.size}"
            }
        }
    }
}