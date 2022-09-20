package com.carlosdiestro.levelup.workouts.ui.workout_add

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carlosdiestro.levelup.core.ui.extensions.setUp
import com.carlosdiestro.levelup.core.ui.extensions.visible
import com.carlosdiestro.levelup.databinding.ItemExerciseWithSetsBinding
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutSetPLO

class WorkoutExerciseAdapter(
    private val insertSet: (List<WorkoutSetPLO>, Int) -> Unit,
    private val deleteSet: (WorkoutExercisePLO, WorkoutSetPLO) -> Unit,
    private val updateSet: (WorkoutExercisePLO, WorkoutSetPLO) -> Unit,
    private val openMenu: (Int, View) -> Unit
) :
    ListAdapter<WorkoutExercisePLO, WorkoutExerciseAdapter.ViewHolder>(WorkoutExercisePLO.WorkoutExerciseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemExerciseWithSetsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(
            binding,
            {
                insertSet(getItem(it).sets, it)
            },
            { i, s ->
                deleteSet(getItem(i), s)
            },
            { i, s ->
                updateSet(getItem(i), s)
            },
            { i, v ->
                openMenu(getItem(i).id, v)
            }
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: ItemExerciseWithSetsBinding,
        private val insertSet: (Int) -> Unit,
        private val deleteSet: (Int, WorkoutSetPLO) -> Unit,
        private val updateSet: (Int, WorkoutSetPLO) -> Unit,
        private val openMenu: (Int, View) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var recyclerAdapter: WorkoutSetAdapter

        init {
            binding.apply {
                btnAddSet.setOnClickListener { insertSet(bindingAdapterPosition) }
                btnMore.setOnClickListener { openMenu(bindingAdapterPosition, it) }
            }
        }

        fun bind(item: WorkoutExercisePLO) {
            setUpRecyclerView()
            bindViews(item)
        }

        private fun setUpRecyclerView() {
            recyclerAdapter = WorkoutSetAdapter(
                {
                    deleteSet(bindingAdapterPosition, it)
                },
                {
                    updateSet(bindingAdapterPosition, it)
                }
            )
            binding.recyclerView.setUp(recyclerAdapter)
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