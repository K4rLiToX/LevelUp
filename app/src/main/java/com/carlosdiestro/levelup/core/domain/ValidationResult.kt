package com.carlosdiestro.levelup.core.domain

import com.carlosdiestro.levelup.core.ui.resources.StringResource

data class ValidationResult(
    val isSuccessful: Boolean,
    val errorMessage: StringResource? = null
)