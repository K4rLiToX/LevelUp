package com.carlosdiestro.levelup.exercise_library.ui.exercise_add

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.carlosdiestro.levelup.R
import com.carlosdiestro.levelup.core.ui.extensions.launchAndCollect
import com.carlosdiestro.levelup.core.ui.extensions.menuItemAsMaterialButton
import com.carlosdiestro.levelup.core.ui.extensions.setUpMenuProvider
import com.carlosdiestro.levelup.core.ui.extensions.toTrimmedString
import com.carlosdiestro.levelup.core.ui.managers.viewBinding
import com.carlosdiestro.levelup.core.ui.resources.StringResource
import com.carlosdiestro.levelup.core.ui.resources.toText
import com.carlosdiestro.levelup.databinding.FragmentNewExerciseBinding
import com.carlosdiestro.levelup.exercise_library.domain.models.Exercise
import com.carlosdiestro.levelup.exercise_library.domain.models.ExerciseCategory
import com.google.android.material.chip.ChipGroup
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewExerciseFragment : Fragment(R.layout.fragment_new_exercise), MenuProvider {

    private val binding by viewBinding(FragmentNewExerciseBinding::bind)
    private val viewModel by viewModels<NewExerciseViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMenu()
        collectUIState()
    }

    override fun onPrepareMenu(menu: Menu) {
        val text =
            if (viewModel.isUpdateMode)
                StringResource.Update.toText(requireContext())
            else
                StringResource.Save.toText(requireContext())
        menu.menuItemAsMaterialButton(
            R.id.action_save,
            text
        ) {
            submitExercise()
        }
    }


    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) =
        menuInflater.inflate(R.menu.menu_save, menu)


    override fun onMenuItemSelected(menuItem: MenuItem): Boolean = false

    private fun setUpMenu() = setUpMenuProvider(this)

    private fun collectUIState() {
        launchAndCollect(viewModel.state) {
            handleNameInput(it.exerciseName, it.exerciseNameError)
            if (viewModel.isUpdateMode) handleUnilateralAndCategory(it.isUnilateral, it.exerciseCategory)
            if (it.isSubmitSuccessful) reset()
        }
    }

    private fun submitExercise() {
        val exerciseName = binding.etName.text.toTrimmedString()
        val isUnilateral = binding.sUnilateral.isChecked
        val exerciseCategory = getCategoryChecked(binding.cgCategories)

        viewModel.onEvent(
            NewExerciseEvent.Save(
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

    private fun handleUnilateralAndCategory(isUnilateral: Boolean, category: ExerciseCategory) {
        binding.apply {
            sUnilateral.isChecked = isUnilateral
            cgCategories.check(getIdFromCategory(category))
        }
    }

    private fun reset() {
        binding.apply {
            sUnilateral.isChecked = false
            cgCategories.check(R.id.cPush)
        }
        viewModel.onEvent(NewExerciseEvent.ResetState)
    }

    private fun getCategoryChecked(group: ChipGroup): ExerciseCategory {
        return when (group.checkedChipId) {
            R.id.cPush -> ExerciseCategory.PUSH
            R.id.cPull -> ExerciseCategory.PULL
            R.id.cLegs -> ExerciseCategory.LEGS
            else -> ExerciseCategory.CORE
        }
    }

    private fun getIdFromCategory(category: ExerciseCategory): Int {
        return when(category) {
            ExerciseCategory.PUSH -> R.id.cPull
            ExerciseCategory.PULL -> R.id.cPull
            ExerciseCategory.LEGS -> R.id.cLegs
            ExerciseCategory.CORE -> R.id.cCore
            else -> -1
        }
    }
}