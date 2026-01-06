package org.tues.tudy.data.model

data class AddTudyUiState(
    val isLoading: Boolean = false,
    val success: Boolean? = null,
    val error: String? = null
)
