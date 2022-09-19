package com.carlosdiestro.levelup.exercise_library.ui

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.carlosdiestro.levelup.exercise_library.domain.models.ExerciseCategory
import com.carlosdiestro.levelup.exercise_library.domain.models.toValue
import com.carlosdiestro.levelup.exercise_library.ui.exercise_list.ExerciseCategoryFragment

class ExerciseCategoryFragmentAdapter(
    fragment: Fragment,
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = EXERCISE_CATEGORIES

    override fun createFragment(position: Int): Fragment {
        return ExerciseCategoryFragment().apply {
            arguments = bundleOf(
                ExerciseCategoryFragment.EXERCISE_CATEGORY_KEY to getExerciseCategory(position)
            )
        }
    }

    private fun getExerciseCategory(position: Int): Int = when (position) {
        0 -> ExerciseCategory.ALL.toValue()
        1 -> ExerciseCategory.PUSH.toValue()
        2 -> ExerciseCategory.PULL.toValue()
        3 -> ExerciseCategory.LEGS.toValue()
        else -> ExerciseCategory.CORE.toValue()
    }

    companion object {
        const val EXERCISE_CATEGORIES = 5
    }
}