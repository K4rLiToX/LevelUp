package com.carlosdiestro.levelup.exercise_library.ui.exercise_list

import com.carlosdiestro.levelup.exercise_library.ui.models.ExercisePLO

data class ExerciseCategoryState(
    val noData: Boolean = false,
    val exercises: List<ExercisePLO> = emptyList()
)