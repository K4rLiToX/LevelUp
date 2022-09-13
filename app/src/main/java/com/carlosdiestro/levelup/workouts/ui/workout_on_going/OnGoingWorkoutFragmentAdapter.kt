package com.carlosdiestro.levelup.workouts.ui.workout_on_going

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.carlosdiestro.levelup.workouts.ui.models.WorkoutExercisePLO

class OnGoingWorkoutFragmentAdapter(
    private val exercises: List<WorkoutExercisePLO>,
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = exercises.size

    override fun createFragment(position: Int): Fragment {
        TODO("Not yet implemented")
    }
}