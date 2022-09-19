package com.carlosdiestro.levelup.workouts.ui.workout_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carlosdiestro.levelup.core.ui.extensions.verticalLayoutManger
import com.carlosdiestro.levelup.databinding.ItemCompletedWorkoutExerciseBinding
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutExercisePLO

class WorkoutDetailsProgressExerciseAdapter :
    ListAdapter<CompletedWorkoutExercisePLO, WorkoutDetailsProgressExerciseAdapter.ViewHolder>(
        CompletedWorkoutExercisePLO.CompletedWorkoutExerciseDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCompletedWorkoutExerciseBinding.inflate(
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
        private val binding: ItemCompletedWorkoutExerciseBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        private var recyclerAdapter: WorkoutDetailsProgressSetAdapter =
            WorkoutDetailsProgressSetAdapter()

        fun bind(item: CompletedWorkoutExercisePLO) {
            binding.apply {
                tvDate.text = item.date
                recyclerView.apply {
                    verticalLayoutManger(binding.root.context)
                    adapter = recyclerAdapter
                }
                recyclerAdapter.submitList(item.completedSets)
            }
        }
    }
}