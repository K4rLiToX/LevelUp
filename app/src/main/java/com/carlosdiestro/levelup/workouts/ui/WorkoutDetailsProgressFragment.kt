package com.carlosdiestro.levelup.workouts.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.core.ui.managers.viewBinding
import com.carlosdiestro.levelup.databinding.FragmentWorkoutDetailsProgressBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkoutDetailsProgressFragment : Fragment(R.layout.fragment_workout_details_progress) {

    private val binding by viewBinding(FragmentWorkoutDetailsProgressBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        const val WORKOUT_ID_KEY = "workout_id_key"
    }
}