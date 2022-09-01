package com.carlosdiestro.levelup.exercise_library.ui.exercise_add

import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.core.ui.extensions.toTrimmedString
import com.carlosdiestro.levelup.core.ui.managers.viewBinding
import com.carlosdiestro.levelup.core.ui.resources.StringResource
import com.carlosdiestro.levelup.core.ui.resources.toText
import com.carlosdiestro.levelup.databinding.FragmentNewExerciseBinding
import com.carlosdiestro.levelup.exercise_library.domain.models.ExerciseCategory
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewExerciseFragment : Fragment(R.layout.fragment_new_exercise) {

    private val binding by viewBinding(FragmentNewExerciseBinding::bind)
    private val viewModel by viewModels<NewExerciseViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition = getSharedElementTransition()
        sharedElementReturnTransition = getSharedElementTransition()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
        collectUIState()
    }

    private fun setUpClickListeners() {
        binding.apply {
            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
            toolbar.menu.getItem(0).setOnMenuItemClickListener {
                submitNewExercise()
                true
            }
        }
    }

    private fun collectUIState() {
        launchAndCollect(viewModel.state) {
            handleNameInput(it.exerciseName, it.exerciseNameError)
            if(it.isSubmitSuccessful) {
                handleIsUnilateral()
                handleCategory()
                handleIsSubmitSuccessful()
            }

        }
    }

    private fun getSharedElementTransition(): Transition? {
        return TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
    }

    private fun submitNewExercise() {
        val exerciseName = binding.etName.text.toTrimmedString()
        val isUnilateral = binding.sUnilateral.isChecked
        val exerciseCategory = getCategoryChecked(binding.cgCategories)

        viewModel.onEvent(
            NewExerciseContract.NewExerciseEvent.SaveNewExercise(
                exerciseName,
                isUnilateral,
                exerciseCategory
            )
        )
    }

    private fun handleNameInput(exerciseName: String, exerciseNameError: StringResource?) {
        binding.apply {
            etName.setText(exerciseName)
            ilName.error = exerciseNameError?.toText(requireContext())
        }
    }

    private fun handleIsUnilateral() {
        binding.sUnilateral.isChecked = false
    }

    private fun handleCategory() {
        binding.cgCategories.check(R.id.cPush)
    }

    private fun handleIsSubmitSuccessful() {
        viewModel.onEvent(NewExerciseContract.NewExerciseEvent.ResetNewExerciseState)
    }

    private fun getCategoryChecked(group: ChipGroup): ExerciseCategory {
        return when (group.checkedChipId) {
            R.id.cPush -> ExerciseCategory.PUSH
            R.id.cPull -> ExerciseCategory.PULL
            R.id.cLegs -> ExerciseCategory.LEGS
            else -> ExerciseCategory.CORE
        }
    }
}