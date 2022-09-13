package com.carlosdiestro.levelup.workouts.ui.workout_details

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class WorkoutDetailsFragmentAdapter(
    private val workoutId: Int,
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = WORKOUT_DETAILS

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> WorkoutDetailsExercisesFragment().apply {
                arguments = bundleOf(WorkoutDetailsExercisesFragment.WORKOUT_ID_KEY to workoutId)
            }
            else -> WorkoutDetailsProgressFragment().apply {
                arguments = bundleOf(WorkoutDetailsProgressFragment.WORKOUT_ID_KEY to workoutId)
            }
        }
    }

    companion object {
        const val WORKOUT_DETAILS = 2
    }
}