package com.carlosdiestro.levelup.workouts.ui.workout_details

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutExercisePLO

class WorkoutDetailsProgressFragmentAdapter(
    private val exercises: List<Pair<String, List<CompletedWorkoutExercisePLO>>>,
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = exercises.size

    override fun createFragment(position: Int): Fragment {
        return WorkoutDetailsProgressExerciseFragment().apply {
            arguments =
                bundleOf(WorkoutDetailsProgressExerciseFragment.EXERCISES to exercises[position].second)
        }
    }
}