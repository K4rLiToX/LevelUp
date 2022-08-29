package com.carlosdiestro.levelup.core.domain

import com.carlosdiestro.levelup.core.ui.resources.StringValue

data class ValidationResult(
    val isSuccessful: Boolean,
    val errorMessage: StringValue? = null
)