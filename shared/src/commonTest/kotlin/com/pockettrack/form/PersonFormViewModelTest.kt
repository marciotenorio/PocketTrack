package com.pockettrack.form

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PersonFormViewModelTest {
    @Test
    fun errorsAreHiddenBeforeFirstSubmit() {
        val viewModel = PersonFormViewModel()

        viewModel.onNameChange("Al")
        viewModel.onAgeChange("120")

        assertNull(viewModel.uiState.value.nameError)
        assertNull(viewModel.uiState.value.ageError)
    }

    @Test
    fun submitWithInvalidInputShowsErrors() {
        val viewModel = PersonFormViewModel()

        viewModel.onNameChange("Al")
        viewModel.onAgeChange("120")
        viewModel.onSubmit()

        val state = viewModel.uiState.value
        assertEquals(NameError.TooShort, state.nameError)
        assertEquals(AgeError.TooHigh, state.ageError)
        assertFalse(state.isSubmitted)
    }

    @Test
    fun errorsUpdateLiveAfterSubmitAttempt() {
        val viewModel = PersonFormViewModel()

        viewModel.onSubmit()
        viewModel.onNameChange("Ana")

        assertNull(viewModel.uiState.value.nameError)
        assertEquals(AgeError.Invalid, viewModel.uiState.value.ageError)
    }

    @Test
    fun submitWithValidInputSucceeds() {
        val viewModel = PersonFormViewModel()

        viewModel.onNameChange("Ana")
        viewModel.onAgeChange("30")
        viewModel.onSubmit()

        val state = viewModel.uiState.value
        assertNull(state.nameError)
        assertNull(state.ageError)
        assertTrue(state.isSubmitted)
    }
}
