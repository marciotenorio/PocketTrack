package com.pockettrack.form

data class PersonFormUiState(
    val name: String = "",
    val age: String = "",
    val nameError: NameError? = null,
    val ageError: AgeError? = null,
    val submitAttempted: Boolean = false,
    val isSubmitted: Boolean = false,
)
