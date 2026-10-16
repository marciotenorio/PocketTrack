package com.pockettrack.form

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PersonFormViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PersonFormUiState())
    val uiState: StateFlow<PersonFormUiState> = _uiState.asStateFlow()

    // Errors are only shown live after the first submit attempt, so the user isn't
    // warned while still typing for the first time.
    fun onNameChange(name: String) {
        _uiState.update { state ->
            state.copy(
                name = name,
                nameError = if (state.submitAttempted) PersonFormValidator.validateName(name) else null,
                isSubmitted = false,
            )
        }
    }

    fun onAgeChange(age: String) {
        _uiState.update { state ->
            state.copy(
                age = age,
                ageError = if (state.submitAttempted) PersonFormValidator.validateAge(age) else null,
                isSubmitted = false,
            )
        }
    }

    fun onSubmit() {
        _uiState.update { state ->
            val nameError = PersonFormValidator.validateName(state.name)
            val ageError = PersonFormValidator.validateAge(state.age)
            state.copy(
                nameError = nameError,
                ageError = ageError,
                submitAttempted = true,
                isSubmitted = nameError == null && ageError == null,
            )
        }
    }
}
