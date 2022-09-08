package com.carlosdiestro.levelup.workouts.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carlosdiestro.levelup.core.ui.extensions.visible
import com.carlosdiestro.levelup.databinding.ItemExerciseWithSetsBinding
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutSetPLO

class WorkoutExerciseAdapter(
    private val onAddSetClicked: (List<WorkoutSetPLO>, Int) -> Unit,
    private val onRemoveSetClicked: (WorkoutExercisePLO, WorkoutSetPLO) -> Unit,
    private val onUpdateSetClicked: (WorkoutExercisePLO, WorkoutSetPLO) -> Unit
) :
    ListAdapter<WorkoutExercisePLO, WorkoutExerciseAdapter.ViewHolder>(WorkoutExercisePLO.WorkoutExerciseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemExerciseWithSetsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(
            binding,
            {
                onAddSetClicked(getItem(it).sets, it)
            },
            { i, s ->
                onRemoveSetClicked(getItem(i), s)
            },
            { i, s ->
                onUpdateSetClicked(getItem(i), s)
            }
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: ItemExerciseWithSetsBinding,
        private val onAddSetClicked: (Int) -> Unit,
        private val onRemoveSetClicked: (Int, WorkoutSetPLO) -> Unit,
        private val onUpdateSetClicked: (Int, WorkoutSetPLO) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var recyclerAdapter: WorkoutSetAdapter

        fun bind(item: WorkoutExercisePLO) {
            setUpClickListeners()
            setUpRecyclerAdapter()
            setUpRecyclerView()
            bindViews(item)
        }

        private fun setUpClickListeners() {
            binding.apply {
                btnAddSet.setOnClickListener { onAddSetClicked(bindingAdapterPosition) }
            }
        }

        private fun setUpRecyclerAdapter() {
            recyclerAdapter = WorkoutSetAdapter(
                {
                    onRemoveSetClicked(bindingAdapterPosition, it)
                },
                {
                    onUpdateSetClicked(bindingAdapterPosition, it)
                }
            )
        }

        private fun setUpRecyclerView() {
            binding.rvSets.apply {
                layoutManager =
                    LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
                adapter = recyclerAdapter
            }
        }

        private fun bindViews(item: WorkoutExercisePLO) {
            recyclerAdapter.submitList(item.sets)
            binding.apply {
                tvName.text = item.name
                ivUnilateral.visible(item.isUnilateral)
            }
        }
    }
}