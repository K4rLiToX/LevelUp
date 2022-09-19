package com.carlosdiestro.levelup.workouts.ui.workout_on_going

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.carlosdiestro.levelup.workouts.ui.models.CompletedWorkoutExercisePLO

class OnGoingWorkoutFragmentAdapter(
    private val exercises: List<CompletedWorkoutExercisePLO>,
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = exercises.size

    override fun createFragment(position: Int): Fragment {
        return OnGoingWorkoutExerciseFragment().apply {
            arguments = bundleOf(OnGoingWorkoutExerciseFragment.EXERCISE to exercises[position])
        }
    }
}