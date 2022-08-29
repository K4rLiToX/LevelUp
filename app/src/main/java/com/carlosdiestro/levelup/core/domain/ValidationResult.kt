package com.carlosdiestro.levelup.core.domain

import com.carlosdiestro.levelup.core.ui.StringValue

data class ValidationResult(
    val isSuccessful: Boolean,
    val errorMessage: StringValue? = null
)